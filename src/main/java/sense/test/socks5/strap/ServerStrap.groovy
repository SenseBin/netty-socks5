package sense.test.socks5.strap

import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.socks.SocksCmdRequestDecoder
import io.netty.handler.codec.socks.SocksInitRequestDecoder
import io.netty.handler.codec.socks.SocksMessageEncoder
import sense.test.socks5.constant.ChannelAttrKey
import sense.test.socks5.handler.CmdHandler
import sense.test.socks5.handler.InitHandler

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by sense on 2017/7/11.
 */
class ServerStrap {
    private EventLoopGroup parentGroup = new NioEventLoopGroup(0, new ThreadFactory() {
        private AtomicLong atomicLong = new AtomicLong()

        @Override
        Thread newThread(Runnable r) {
            return new Thread(r, "parentGroup-" + atomicLong.getAndIncrement())
        }
    })
    private EventLoopGroup childGroup = new NioEventLoopGroup(0, new ThreadFactory() {
        private AtomicLong atomicLong = new AtomicLong()

        @Override
        Thread newThread(Runnable r) {
            return new Thread(r, "childGroup-" + atomicLong.getAndIncrement())
        }
    })
    private Bootstrap childConnectBootstrap = new Bootstrap().with {
        EventLoopGroup group = new NioEventLoopGroup(0, new ThreadFactory() {
            private AtomicLong atomicLong = new AtomicLong()

            @Override
            Thread newThread(Runnable r) {
                return new Thread(r, "childConnectGroup-" + atomicLong.getAndIncrement())
            }
        })
        it.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
            }
        }).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
    }

    def init(int port) {
        ServerBootstrap serverBootstrapTemplate = new ServerBootstrap()
        serverBootstrapTemplate.group(this.parentGroup, this.childGroup)
        serverBootstrapTemplate.channel(NioServerSocketChannel.class)
        serverBootstrapTemplate.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new SocksMessageEncoder())
                        .addLast(new SocksInitRequestDecoder())
                        .addLast(new InitHandler())
                        .addLast(new SocksCmdRequestDecoder())
                        .addLast(new CmdHandler())
                        .channel()
                        .attr(ChannelAttrKey.BOOTSTRAP).set(childConnectBootstrap)
            }
        })
        serverBootstrapTemplate.bind(port).sync().channel().closeFuture().await()
    }
}
