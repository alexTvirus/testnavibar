/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xpcas.nettysocks.handler;

import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@ChannelHandler.Sharable
public class CaptureInputHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object buf1)
            throws Exception {
        ByteBuf buf = (ByteBuf) buf1;

//        System.out.println("cap:"+buf.toString(Charset.forName("UTF-8")));
//        String x = buf.getClass().getName();
        byte[] data = getArrayByteFromByteBuf(buf);
        int[] y =getArrayInt(data);
        String x = getIpPort(y);
        // nếu data chứa domain thì ko cho kết nối
//        if (checkIsPresentDomain(data, "avfay")
//                ||checkIsPresentDomain(data, "binomo")
//                ||checkIsPresentDomain(data, "salsapo")
//                        ||checkIsPresentDomain(data, "vnfbs")
//                ||checkIsPresentDomain(data, "monad")
//                ||checkIsPresentDomain(data, "puss")
//                ||checkIsPresentDomain(data, "malay")
//                ||checkIsPresentDomain(data, "8live")
//                ||checkIsPresentDomain(data, "88")) { 
//            ctx.close();
//        }

        ctx.fireChannelRead(buf);
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // Once session is secured, send a greeting and register the channel to the global channel
        // list so the channel received the messages from others.

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    // chuyển ByteBuf về bytearray để xử lý
    public byte[] getArrayByteFromByteBuf(ByteBuf buf) {
        byte[] bytes;
        int offset;
        int length = buf.readableBytes();

        if (buf.hasArray()) {
            bytes = buf.array();
            offset = buf.arrayOffset();
        } else {
            bytes = new byte[length];
            buf.getBytes(buf.readerIndex(), bytes);
            offset = 0;
        }
        return bytes;
    }

    // chuyển byte[] về int[] để giúp đọc được dữ liệu
    public int[] getArrayInt(byte[] data) {
        int[] iarray = new int[data.length];
        int i = 0;
        for (byte b : data) {
            iarray[i++] = b & 0xff;
        }
        return iarray;
    }

    // kiểm tra xem trong data có chứa domain ko
    private boolean checkIsPresentDomain(byte[] data, String domain) {
        String check = new String(data, StandardCharsets.UTF_8);
        if (check.contains(domain)) {
            return true;
        } else {
            return false;
        }
    }

    // get ra ip từ data
    private String getIpPort(int[] data) {
        //kiểm tra ip đích , dùng để chặn kết nối
        if (data.length == 10 && (data[9] == 80 || data[9] == 187)) {
            System.out.println("ip: " + data[4] + "." + data[5] + "." + data[6] + "." + data[7] + ":" + data[9]);
            return data[4] + "." + data[5] + "." + data[6] + "." + data[7] + ":" + (data[9] == 187 ? "443" : data[9]);
        } else {
            return "";
        }

    }
}
