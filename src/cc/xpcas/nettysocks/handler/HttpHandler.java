/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xpcas.nettysocks.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Alex
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Connected!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
            FullHttpRequest msg) throws Exception {
        // Check for invalid http data:
        if (msg.getDecoderResult() != DecoderResult.SUCCESS) {
            ctx.close();
            return;
        }

        System.out.println("Recieved request!");
        System.out.println("HTTP Method: " + msg.getMethod());
        System.out.println("HTTP Version: " + msg.getProtocolVersion());
        System.out.println("URI: " + msg.getUri());
        System.out.println("Headers: " + msg.headers());
        System.out.println("Trailing headers: " + msg.trailingHeaders());

        ByteBuf data = msg.content();
        System.out.println("POST/PUT length: " + data.readableBytes());
        System.out.println("POST/PUT as string: ");
        System.out.println("-- DATA --");
        System.out.println(data.toString(StandardCharsets.UTF_8));
        System.out.println("-- DATA END --");

        // Send response back so the browser won't timeout
        ByteBuf responseBytes = ctx.alloc().buffer();
        responseBytes.writeBytes("Hello World".getBytes());

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, responseBytes);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE,
                "text/plain");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
                response.content().readableBytes());

        response.headers().set(HttpHeaders.Names.CONNECTION,
                HttpHeaders.Values.KEEP_ALIVE);
        ctx.write(response);
    }
}
