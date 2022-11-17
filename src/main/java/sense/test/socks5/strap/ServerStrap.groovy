package sense.test.socks5.strap

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import sense.test.socks5.handler.ProxyType

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

    def init(int port, String protocol) {
        ServerBootstrap serverBootstrapTemplate = new ServerBootstrap()
        serverBootstrapTemplate.group(this.parentGroup, this.childGroup)
        serverBootstrapTemplate.channel(NioServerSocketChannel.class)

        def channelHandler = ProxyType.fromProtocol(protocol).channelHandler()
        serverBootstrapTemplate.childHandler(channelHandler)
        serverBootstrapTemplate.bind(port).sync().channel().closeFuture().await()
    }
}
