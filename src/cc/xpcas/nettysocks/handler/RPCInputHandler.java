/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xpcas.nettysocks.handler;

import cc.xpcas.nettysocks.upstream.Upstream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 *
 * @author Alex
 */
public class RPCInputHandler extends SimpleChannelInboundHandler<ByteBuf> {

    PipedInputStream in;
    PipedOutputStream out;
    private Upstream<SocketChannel> upstream;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        in = new PipedInputStream();
        out = new PipedOutputStream(in);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        out.close(); // This sends EOF to the other pipe
    }

    // This method is called messageReceived(ChannelHandlerContext, I) in 5.0.
    @Override
    protected void channelRead0(ChannelHandlerContext chc, ByteBuf buf) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(new ByteBufInputStream(buf), "UTF-8"));
        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }

}
