package cc.xpcas.nettysocks.upstream;

import PackageChongHack.StringConstant;
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
public class Socks5Upstream extends Upstream<SocketChannel> {

    public Socks5Upstream(final Address address) {
        setAddress(address);
    }

    @Override
    public void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        Address upstreamAddress = getAddress();
        SocketAddress address = new InetSocketAddress(upstreamAddress.getHost(), upstreamAddress.getPort());
        if (StringConstant.SOCKS.equals(getTypeProtocol())) {
            if (Utils.isBlank(getUser())) {
                pipeline.addFirst(HANDLER_NAME, new Socks5ProxyHandler(address));
            } else {
                pipeline.addFirst(HANDLER_NAME, new Socks5ProxyHandler(address, getUser(), getPass()));
            }
        } else {
            if (Utils.isBlank(getUser())) {
                pipeline.addFirst(HANDLER_NAME, new HttpProxyHandler(address));
            } else {
                pipeline.addFirst(HANDLER_NAME, new HttpProxyHandler(address, getUser(), getPass()));
            }

        }

//         pipeline.addFirst(HANDLER_NAME, new Socks4ProxyHandler(address));
    }
}
