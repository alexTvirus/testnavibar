package cc.xpcas.nettysocks.handler;

import View.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.xpcas.nettysocks.upstream.Upstream;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.socksx.SocksMessage;
import io.netty.handler.codec.socksx.v4.DefaultSocks4CommandRequest;
import io.netty.handler.codec.socksx.v4.DefaultSocks4CommandResponse;
import io.netty.handler.codec.socksx.v4.Socks4CommandRequest;
import io.netty.handler.codec.socksx.v4.Socks4CommandResponse;
import io.netty.handler.codec.socksx.v4.Socks4CommandStatus;
import io.netty.handler.codec.socksx.v4.Socks4CommandType;
import io.netty.handler.codec.socksx.v5.*;

@ChannelHandler.Sharable
public class Socks5CommandRequestHandler extends SimpleChannelInboundHandler<SocksMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(Socks5CommandRequestHandler.class);

    private final EventLoopGroup forwarders;
    private int rowid;
    private final Upstream<SocketChannel> upstream;

    public Socks5CommandRequestHandler(EventLoopGroup forwarders, Upstream<SocketChannel> upstream, int rowid) {
        this.forwarders = forwarders;
        this.upstream = upstream;
        this.rowid = rowid;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, SocksMessage msg) throws Exception {
        ChannelPipeline pipeline = ctx.pipeline();
        switch (msg.version()) {
            case SOCKS4a:
//                pipeline.remove(Socks5CommandRequestDecoder.class.getName());
                pipeline.remove(this);
                if (LOG.isDebugEnabled()) {
                    Channel channel = ctx.channel();
                    LOG.debug(String.format("%s %s %s:%d",
                            channel.remoteAddress(),
                            ((DefaultSocks4CommandRequest) msg).type(),
                            ((DefaultSocks4CommandRequest) msg).dstAddr(), ((DefaultSocks4CommandRequest) msg).dstPort()));
                }

                if (((DefaultSocks4CommandRequest) msg).type().equals(Socks4CommandType.CONNECT)) {
//                    View.jframe.setStatus(rowid, "waiting");
                    handleConnectSock4(ctx, ((DefaultSocks4CommandRequest) msg));
                } else {
                    //TODO handle other command type
                    ctx.close();
                }
                break;
            case SOCKS5:
                pipeline.remove(Socks5CommandRequestDecoder.class.getName());
                pipeline.remove(this);
                if (LOG.isDebugEnabled()) {
                    Channel channel = ctx.channel();
                    LOG.debug(String.format("%s %s %s:%d",
                            channel.remoteAddress(),
                            ((DefaultSocks5CommandRequest) msg).type(),
                            ((DefaultSocks5CommandRequest) msg).dstAddr(), ((DefaultSocks5CommandRequest) msg).dstPort()));
                }

                if (((DefaultSocks5CommandRequest) msg).type().equals(Socks5CommandType.CONNECT)) {
//                    View.jframe.setStatus(rowid, "waiting");
                    handleConnectSock5(ctx, ((DefaultSocks5CommandRequest) msg));
                } else {
                    //TODO handle other command type
                    ctx.close();
                }
                break;
            case UNKNOWN:
                ctx.close();
                break;
        }

    }

    private void handleConnectSock5(final ChannelHandlerContext client, Socks5CommandRequest msg) {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(forwarders)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        upstream.initChannel(channel);
                    }
                });

        ChannelFuture forwarderConnectFuture = bootstrap.connect(msg.dstAddr(), msg.dstPort());

        forwarderConnectFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("upstream connected");
                    View.jframe.setStatus(rowid, "upstream connected");
                }
                ChannelPipeline upstreamPipeline = future.channel().pipeline();
                upstreamPipeline.addLast("from-upstream", new ChannelHandlerContextForwardingHandler(client, false, rowid));

                ChannelPipeline clientPipeline = client.pipeline();
                clientPipeline.addLast("to-upstream", new ChannelForwardingHandler(future.channel(), true, rowid));

                client.writeAndFlush(socks5CommandResponse(msg, true));
            } else {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("upstream disconnected", future.cause());
                }
//                if (future.cause().getMessage().contains("Connection refused: no further information:")) {
//                    View.jframe.setStatus(rowid, "disconnect");
//                }

                client.writeAndFlush(socks5CommandResponse(msg, false)).addListener(ChannelFutureListener.CLOSE);
            }
        });
    }

    private void handleConnectSock4(final ChannelHandlerContext client, Socks4CommandRequest msg) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(forwarders)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        upstream.initChannel(channel);
                    }
                });

        ChannelFuture forwarderConnectFuture = bootstrap.connect(msg.dstAddr(), msg.dstPort());

        forwarderConnectFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                if (LOG.isTraceEnabled()) {

                    LOG.trace("upstream connected");
                }
                ChannelPipeline upstreamPipeline = future.channel().pipeline();
                upstreamPipeline.addLast("from-upstream", new ChannelHandlerContextForwardingHandler(client, false, rowid));

                ChannelPipeline clientPipeline = client.pipeline();
                clientPipeline.addLast("to-upstream", new ChannelForwardingHandler(future.channel(), true, rowid));

                client.writeAndFlush(socks4CommandResponse(msg, true));
            } else {
                if (LOG.isWarnEnabled()) {
//                    View.jframe.setStatus(ServiceIndexListServer.getRowTableId(rowid), "disconnect");
                    LOG.warn("upstream disconnected", future.cause());
                }

                client.writeAndFlush(socks4CommandResponse(msg, false)).addListener(ChannelFutureListener.CLOSE);
            }
        });
    }

    private Socks5CommandResponse socks5CommandResponse(Socks5CommandRequest request, boolean success) {
        Socks5CommandStatus status = success ? Socks5CommandStatus.SUCCESS : Socks5CommandStatus.FAILURE;
        // bug: 不能使用 DOMAIN 会导致消息无法发出
        Socks5AddressType addressType = Socks5AddressType.IPv4;
        return new DefaultSocks5CommandResponse(status, addressType);
    }

    private Socks4CommandResponse socks4CommandResponse(Socks4CommandRequest request, boolean success) {
        Socks4CommandStatus status = success ? Socks4CommandStatus.SUCCESS : Socks4CommandStatus.REJECTED_OR_FAILED;
        // bug: 不能使用 DOMAIN 会导致消息无法发出
        Socks5AddressType addressType = Socks5AddressType.IPv4;
        return new DefaultSocks4CommandResponse(status);
    }
}
