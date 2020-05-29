package cc.xpcas.nettysocks.handler;

import cc.xpcas.nettysocks.utils.NettyUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xp
 */
public class ChannelHandlerContextForwardingHandler extends ChannelForwardingHandler {

    private final ChannelHandlerContext destinationChannelHandlerContext;
    private int rowid;

    public ChannelHandlerContextForwardingHandler(ChannelHandlerContext destinationChannelHandlerContext, boolean isReadLocalWriteRemote, int rowid) {
        super(destinationChannelHandlerContext.channel(), isReadLocalWriteRemote, rowid);
        this.destinationChannelHandlerContext = destinationChannelHandlerContext;
        this.rowid = rowid;
    }

    @Override
    protected ChannelFuture doWriteAndFlush(Object msg) {
        return destinationChannelHandlerContext.writeAndFlush(msg);
    }

    @Override
    protected void closeAfterFlush() {
        NettyUtils.closeAfterFlush(destinationChannelHandlerContext);
    }
}
