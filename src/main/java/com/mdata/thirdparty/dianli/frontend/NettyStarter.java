package com.mdata.thirdparty.dianli.frontend;

import com.mdata.thirdparty.dianli.frontend.web.services.sensor.SensorService;
import com.mdata.thirdparty.dianli.lora.client.ConnectionListener;
import com.mdata.thirdparty.dianli.lora.client.LoraHandler;
import com.mdata.thirdparty.dianli.lora.client.ReconnectHandler;
import com.mdata.thirdparty.dianli.lora.client.codec.LoraDecoder;
import com.mdata.thirdparty.dianli.lora.client.codec.PayloadDecoder;
import com.mdata.thirdparty.dianli.lora.client.codec.StringJsonDecoder;
import com.mdata.thirdparty.dianli.lora.client.heartbeat.ConnectionWatchdog;
import com.mdata.thirdparty.dianli.lora.client.heartbeat.ConnectorIdleStateTrigger;
import com.mdata.thirdparty.dianli.lora.client.heartbeat.HeartBeatClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by administrator on 17/8/13.
 */
@Service
public class NettyStarter implements InitializingBean {
    @Value("${lora_host}")
    private String host;
    @Value("${lora_port}")

    private int port;
    @Autowired
    private SensorService  sensorService;
    protected final HashedWheelTimer timer = new HashedWheelTimer();

    private Bootstrap boot;

    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();

    public void connect(String host,int port) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();

        boot = new Bootstrap();
        boot.group(group).channel(NioSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO));

        final ConnectionWatchdog watchdog = new ConnectionWatchdog(boot, timer, port,host, true) {

            public ChannelHandler[] handlers() {
                return new ChannelHandler[] {
                        this,
                        new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
                        idleStateTrigger,
                        new StringDecoder(),
                        new StringJsonDecoder(),
                        new LoraDecoder(),
                        new PayloadDecoder(),
                        new StringEncoder(),
                        new LoraHandler(sensorService),
                        new HeartBeatClientHandler()
                };
            }
        };

        ChannelFuture future;
        //进行连接
        try {
            synchronized (boot) {
                boot.handler(new ChannelInitializer<Channel>() {

                    //初始化channel
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(watchdog.handlers());
                    }
                });

                future = boot.connect(host,port);
            }

            // 以下代码在synchronized同步块外面是安全的
            future.sync();
        } catch (Throwable t) {
            throw new Exception("connects to  fails", t);
        }
    }
    @Override
    public void afterPropertiesSet() throws Exception {

        connect(host,port);
    }
}
