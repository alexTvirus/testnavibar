package cc.xpcas.nettysocks.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.SocksMessage;
import io.netty.handler.codec.socksx.SocksVersion;
import io.netty.handler.codec.socksx.v5.*;

@ChannelHandler.Sharable
public class Socks5InitialRequestHandler extends SimpleChannelInboundHandler<SocksMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(Socks5InitialRequestHandler.class);

    private final boolean auth;

    private final Socks5AuthMethod authMethod;

    public Socks5InitialRequestHandler(boolean auth) {
        this.auth = auth;
        authMethod = auth ? Socks5AuthMethod.PASSWORD : Socks5AuthMethod.NO_AUTH;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SocksMessage msg) throws Exception {
        ChannelPipeline pipeline = ctx.pipeline();
        switch (msg.version()) {
            case SOCKS4a:
//                pipeline.remove(Socks4InitialRequestDecoder.class.getName());
                pipeline.remove(this);

                if (msg.decoderResult().isFailure()) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("message decode failed");
                    }
                    ctx.fireChannelRead(msg);
                } else {
                    if (msg.version().equals(SocksVersion.SOCKS4a)) {
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("socks4 init with ");
                        }
                        ctx.fireChannelRead(msg);
                    } else {
                        if (LOG.isWarnEnabled()) {
                            SocksVersion version = msg.version();
                            LOG.warn(String.format("unsupported version: %s(%d)", version.name(), version.byteValue()));
                        }
                        ctx.fireChannelRead(msg);
                    }
                }
                break;
            case SOCKS5:
                pipeline.remove(Socks5InitialRequestDecoder.class.getName());
                pipeline.remove(this);

                if (msg.decoderResult().isFailure()) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("message decode failed");
                    }
                    ctx.fireChannelRead(msg);
                } else {
                    if (msg.version().equals(SocksVersion.SOCKS5)) {
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("socks5 init with " + ((DefaultSocks5InitialRequest) msg).authMethods());
                        }
                        Socks5InitialResponse response = new DefaultSocks5InitialResponse(authMethod);
                        ctx.writeAndFlush(response);
                    } else {
                        if (LOG.isWarnEnabled()) {
                            SocksVersion version = msg.version();
                            LOG.warn(String.format("unsupported version: %s(%d)", version.name(), version.byteValue()));
                        }
                        ctx.fireChannelRead(msg);
                    }
                }
                break;
            case UNKNOWN:
        }

    }
}
