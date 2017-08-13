package com.mdata.thirdparty.dianli.lora.client;

import com.mdata.thirdparty.dianli.frontend.web.services.sensor.SensorService;
import com.mdata.thirdparty.dianli.lora.client.codec.LoraDecoder;
import com.mdata.thirdparty.dianli.lora.client.codec.PayloadDecoder;
import com.mdata.thirdparty.dianli.lora.client.codec.StringJsonDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by administrator on 17/7/23.
 */
public class Main  {
    final static  String host = "122.225.88.90";
    final static int port = Integer.parseInt("30002");
    final static SensorService holder=null;
    public static  void connectLora(EventLoopGroup workerGroup){
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new StringDecoder()).addLast(new StringJsonDecoder()).addLast(new LoraDecoder()).addLast(new PayloadDecoder());
                    ch.pipeline().addLast(new StringEncoder()).addLast(new LoraHandler(holder)).addLast(new ReconnectHandler());
                }
            });

            // Start the client.
           b.connect(host, port).addListener(new ConnectionListener()).sync();

        }catch ( Exception e){
            e.printStackTrace();
        }
        finally {
            //  workerGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args)  throws  Exception{

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        connectLora(workerGroup);

    }
}
