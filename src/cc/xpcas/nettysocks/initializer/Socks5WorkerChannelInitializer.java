package cc.xpcas.nettysocks.initializer;

import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.xpcas.nettysocks.authenticator.BasicAuthenticator;
import cc.xpcas.nettysocks.config.Address;
import cc.xpcas.nettysocks.config.SocksProperties;
import cc.xpcas.nettysocks.handler.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.socksx.SocksMessage;
import io.netty.handler.codec.socksx.SocksVersion;
import io.netty.handler.codec.socksx.v4.Socks4ServerEncoder;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.handler.codec.socksx.v4.*;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author xp
 */
public class Socks5WorkerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOG = LoggerFactory.getLogger(Socks5WorkerChannelInitializer.class);

    private final SocksProperties socksProperties;

    private Socks5InitialRequestHandler socks5InitialRequestHandler;

    private Socks5PasswordAuthRequestHandler socks5PasswordAuthRequestHandler;

    private Socks5CommandRequestHandler socks5CommandRequestHandler;

    public Socks5WorkerChannelInitializer(SocksProperties socksProperties, EventLoopGroup forwarders, int rowid) {
        this.socksProperties = socksProperties;

        // 先初始化 shared handlers
        socks5InitialRequestHandler = new Socks5InitialRequestHandler(socksProperties.isAuth());
        if (socksProperties.isAuth()) {
            BasicAuthenticator authenticator = new BasicAuthenticator();
            authenticator.batchSet(socksProperties.getAuthMap());
            socks5PasswordAuthRequestHandler = new Socks5PasswordAuthRequestHandler(authenticator);
        } else {
            socks5PasswordAuthRequestHandler = null;
        }
        socks5CommandRequestHandler = new Socks5CommandRequestHandler(forwarders, socksProperties.getUpstream(), rowid);
    }

    @Override
    protected void initChannel(io.netty.channel.socket.SocketChannel channel) throws Exception {

        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addFirst(new CaptureInputHandler());
        // check ip
        pipeline.addFirst(new FilterHandler());

        // 连接管理
        pipeline.addLast(ConnectionManageHandler.NAME, new ConnectionManageHandler(3000));

        // 空闲超时 Idle timeout
        pipeline.addLast(new IdleStateHandler(10, 10, 0));
        pipeline.addLast(new IdleStateEventHandler());

        // 读写超时 Read and write timeout
        pipeline.addLast(new ReadTimeoutHandler(socksProperties.getReadTimeoutMillis(), TimeUnit.MILLISECONDS));
        pipeline.addLast(new WriteTimeoutHandler(socksProperties.getWriteTimeoutMillis(), TimeUnit.MILLISECONDS));

// netty log
        //pipeline.addLast(new LoggingHandler());
        // 负责将输出的 Socks5Message 转为 ByteBuf ma hoa kieu nao
        pipeline.addLast(Socks5ServerEncoder.DEFAULT);
        // init
		// giai ma kieu nao
        pipeline.addLast(Socks5InitialRequestDecoder.class.getName(), new Socks5InitialRequestDecoder());
		//  xu ly request
        pipeline.addLast(Socks5InitialRequestHandler.class.getName(), socks5InitialRequestHandler);

        // auth
		// xac thuc user pass
        if (socks5PasswordAuthRequestHandler != null) {
            pipeline.addLast(Socks5PasswordAuthRequestDecoder.class.getName(), new Socks5PasswordAuthRequestDecoder());
            pipeline.addLast(Socks5PasswordAuthRequestHandler.class.getName(), socks5PasswordAuthRequestHandler);
        }

        // connection
		// giai ma request
        pipeline.addLast(Socks5CommandRequestDecoder.class.getName(), new Socks5CommandRequestDecoder());
		// xy ly request
        pipeline.addLast(Socks5CommandRequestHandler.class.getName(), socks5CommandRequestHandler);
    }
}
