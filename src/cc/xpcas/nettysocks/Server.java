package cc.xpcas.nettysocks;

import Enity.ProxyEntity;
import PackageChongHack.StringConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.xpcas.nettysocks.config.Address;
import cc.xpcas.nettysocks.config.SocksProperties;
import cc.xpcas.nettysocks.initializer.Socks4WorkerChannelInitializer;
import cc.xpcas.nettysocks.initializer.Socks5WorkerChannelInitializer;
import cc.xpcas.nettysocks.upstream.Socks5Upstream;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author xp
 */
public class Server {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);
    public SocksProperties socksProperties;
    public EventLoopGroup acceptors = null;
    public EventLoopGroup workers = null;
    public ChannelFuture future = null;
    public String ip = "";
    public int port;
    public int rowid;
    public String StatusConnect = "";
    public ProxyEntity proxy;

    public Server() {
    }

    public Server(SocksProperties socksProperties, int rowid) {
        this.socksProperties = socksProperties;
        this.rowid = rowid;
    }

    public void setSocksProperties(SocksProperties socksProperties) {
        this.socksProperties = socksProperties;
    }

    public void start() throws InterruptedException {
        acceptors = new NioEventLoopGroup(socksProperties.getAcceptors());
        workers = new NioEventLoopGroup();
        EventLoopGroup forwarders = new NioEventLoopGroup();
        ChannelInitializer<SocketChannel> chanel = null;

        if (MainController.SocksType.equals(StringConstant.NUMBER5)) {
            chanel = new Socks5WorkerChannelInitializer(socksProperties, forwarders, this.rowid);
        } else if (MainController.SocksType.equals(StringConstant.NUMBER4)) {
            chanel = new Socks4WorkerChannelInitializer(socksProperties, forwarders, this.rowid);
        } else {
            chanel = new Socks5WorkerChannelInitializer(socksProperties, forwarders, this.rowid);
        }
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(acceptors, workers)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, socksProperties.getBacklog())
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, socksProperties.getConnectTimeoutMillis())
                    .childHandler(chanel);

            Address address = socksProperties.getListen();
            ip = address.getHost();
            port = address.getPort();
            future = bootstrap.bind(address.getHost(), address.getPort()).sync();
            future.channel().closeFuture().sync();
        } finally {
            forwarders.shutdownGracefully();
            workers.shutdownGracefully();
            acceptors.shutdownGracefully();
        }
    }

//    public static void directMain(int port) throws InterruptedException {
//        new Thread(() -> {
//            try {
//                SocksProperties socksProperties = new SocksProperties();
//
//                socksProperties.setListen(new Address(StringConstant.LOCAL_HOST, port));
//                socksProperties.setUpstream(new DirectUpstream());
//                socksProperties.setAuth(false);
//
//                Server server = new Server(socksProperties);
//                server.start();
//            } catch (InterruptedException e) {
//                LOG.info(port + " interrupted");
//            }
//        }).start();
//    }
    public Server socks5Main(Address address, ProxyEntity proxyEntity, int LocalPort, int rowid) throws InterruptedException {
        Server server = null;
        SocksProperties socksProperties = new SocksProperties();
        socksProperties.setListen(new Address(StringConstant.LOCAL_HOST, LocalPort));
        socksProperties.setUpstream(new Socks5Upstream(address));
        socksProperties.getUpstream().setTypeProtocol(StringConstant.SOCKS);
        socksProperties.getUpstream().setUser(proxyEntity.getUsername());
        socksProperties.getUpstream().setPass(proxyEntity.getPassword());
        socksProperties.setAuth(false);

        server = new Server(socksProperties, rowid);
        return server;
    }

//    public Server socks4Main(Address address, ProxyEntity proxyEntity, int LocalPort, int rowid) throws InterruptedException {
//        Server server = null;
//        SocksProperties socksProperties = new SocksProperties();
//        socksProperties.setListen(new Address(StringConstant.LOCAL_HOST, LocalPort));
//        socksProperties.setUpstream(new Socks5Upstream(address, proxyEntity.getUsername(), proxyEntity.getPassword()));
//        socksProperties.setAuth(false);
//        server = new Server(socksProperties, rowid);
//        return server;
//    }
    public Server sockHTTP(Address address, ProxyEntity proxyEntity, int LocalPort, int rowid) throws InterruptedException {
        Server server = null;
        SocksProperties socksProperties = new SocksProperties();
        socksProperties.setListen(new Address(StringConstant.LOCAL_HOST, LocalPort));
        socksProperties.setUpstream(new Socks5Upstream(address));
        socksProperties.getUpstream().setTypeProtocol(StringConstant.HTTP);
        socksProperties.getUpstream().setUser(proxyEntity.getUsername());
        socksProperties.getUpstream().setPass(proxyEntity.getPassword());
        socksProperties.setAuth(false);
        server = new Server(socksProperties, rowid);
        return server;
    }

//    public static void ssocksMain(int port) throws InterruptedException {
//        new Thread(() -> {
//            try {
//                SocksProperties socksProperties = new SocksProperties();
//
//                socksProperties.setListen(new Address(StringConstant.LOCAL_HOST, port));
//                socksProperties.setUpstream(new SSocksUpstream(
//                        new Address("::1", 8388),
//                        AESCFBCipher.AES_256_CFB, "HelloWorld."
//                ));
//                socksProperties.setAuth(false);
//
//                Server server = new Server(socksProperties);
//                server.start();
//            } catch (InterruptedException e) {
//                LOG.info(port + " interrupted");
//            }
//        }).start();
//    }
    public void stop(EventLoopGroup bossGroup, EventLoopGroup workerGroup, ChannelFuture future) {
        try {
            // shutdown EventLoopGroup
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
            future.channel().closeFuture().sync();   // close port

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
