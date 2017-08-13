package com.mdata.thirdparty.dianli.lora.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * Created by administrator on 17/8/1.
 */
public class ReconnectHandler extends ChannelInboundHandlerAdapter {



    @Override

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        final EventLoop eventLoop = ctx.channel().eventLoop();

        eventLoop.schedule(new Runnable() {


            public void run() {

                Main.connectLora( eventLoop);

            }

        }, 1L, TimeUnit.SECONDS);

        super.channelInactive(ctx);

    }

}
