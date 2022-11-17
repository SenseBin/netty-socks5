package sense.test.socks5.handler

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.socks.SocksCmdRequestDecoder
import io.netty.handler.codec.socks.SocksInitRequestDecoder
import io.netty.handler.codec.socks.SocksMessageEncoder
import sense.test.socks5.constant.ChannelAttrKey

import javax.naming.OperationNotSupportedException
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicLong

enum ProxyType {
    SOCKS5("socks5"){
        @Override
        ChannelHandler channelHandler() {
            EventLoopGroup childConnectGroup = new NioEventLoopGroup(0, new ThreadFactory() {
                private AtomicLong atomicLong = new AtomicLong()

                @Override
                Thread newThread(Runnable r) {
                    return new Thread(r, "childConnectGroup-" + atomicLong.getAndIncrement())
                }
            })

            Bootstrap childConnectBootstrap = new Bootstrap().with {
                it.group(childConnectGroup).channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                            }
                        }).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
            }

            Bootstrap childUdpConnectBootstrap = new Bootstrap().with {
                it.group(childConnectGroup).channel(NioDatagramChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                            }
                        }).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
            }

            return new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    def channel = ch.pipeline()
                            .addLast(new SocksMessageEncoder())
                            .addLast(new SocksInitRequestDecoder())
                            .addLast(new InitHandler())
                            .addLast(new SocksCmdRequestDecoder())
                            .addLast(new CmdHandler())
                            .channel()
                    channel.attr(ChannelAttrKey.BOOTSTRAP).set(childConnectBootstrap)
                    channel.attr(ChannelAttrKey.BOOTSTRAP_UDP).set(childUdpConnectBootstrap)
                }
            }
        }
    },
    HTTPS("https"){
        @Override
        ChannelHandler channelHandler() {
            throw new OperationNotSupportedException("current does not support https!");
        }
    },
    HTTP("http"){
        @Override
        ChannelHandler channelHandler() {
            throw new OperationNotSupportedException("current does not support https!");
        }
    };

    private String proxyType;

    ProxyType(String proxyType) {
        this.proxyType = proxyType
    }

    String getProxyType() {
        return proxyType;
    }

    static ProxyType fromProtocol(String protocol) {
        switch (protocol) {
            case "http": return HTTP;
            case "https": return HTTPS;
            default: return SOCKS5;
        }
    }

    abstract ChannelHandler channelHandler();
}