package com.mdata.thirdparty.dianli.lora.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * Created by administrator on 17/8/1.
 */
public class ConnectionListener implements ChannelFutureListener {


    public void operationComplete(ChannelFuture channelFuture) throws Exception {

        if (!channelFuture.isSuccess()) {

            System.out.println("Reconnect");

            final EventLoop loop = channelFuture.channel().eventLoop();

            loop.schedule(new Runnable() {


                public void run() {

                   Main.connectLora(loop);

                }

            }, 1L, TimeUnit.SECONDS);

        }

    }

}