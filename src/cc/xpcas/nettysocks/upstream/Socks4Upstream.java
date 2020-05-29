package cc.xpcas.nettysocks.upstream;

import java.net.*;

import cc.xpcas.nettysocks.config.Address;
import cc.xpcas.nettysocks.utils.Utils;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;

/**
 * @author xp
 */
public class Socks4Upstream extends Upstream<SocketChannel> {

    String user = "";
    String pass = "";

    public Socks4Upstream(final Address address, String user, String pass) {
        setAddress(address);
        this.user = user;
        this.pass = pass;
    }

    @Override
    public void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        Address upstreamAddress = getAddress();
        SocketAddress address = new InetSocketAddress(upstreamAddress.getHost(), upstreamAddress.getPort());
        pipeline.addFirst(HANDLER_NAME, new Socks4ProxyHandler(address));
//         pipeline.addFirst(HANDLER_NAME, new Socks4ProxyHandler(address));
    }
}
