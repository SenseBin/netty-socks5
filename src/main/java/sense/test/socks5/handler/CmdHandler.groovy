package sense.test.socks5.handler

import io.netty.channel.Channel
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.socks.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sense.test.socks5.constant.ChannelAttrKey

/**
 * Created by sense on 2017/10/2.
 */
class CmdHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(CmdHandler.class)

    @Override
    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (isCmdRequest(msg)) {
            handleCmdRequest(ctx, (SocksCmdRequest) msg)
            return
        }
        ctx.close()
    }

    protected boolean isCmdRequest(Object msg) {
        boolean pass = msg instanceof SocksCmdRequest
        if (!pass) {
            return pass
        }
        SocksCmdRequest socksCmdRequest = (SocksCmdRequest) msg
        pass = pass && socksCmdRequest.protocolVersion() == SocksProtocolVersion.SOCKS5
        pass
    }

    protected void handleCmdRequest(ChannelHandlerContext ctx, SocksCmdRequest request) {
        switch (request.cmdType()) {
            case SocksCmdType.CONNECT:
                connect(ctx, request)
                break
            default:
                ctx.close()
        }
    }

    protected void connect(ChannelHandlerContext ctx, SocksCmdRequest request) {
        InetSocketAddress clientAddress = ctx.channel().remoteAddress() as InetSocketAddress
        logger.debug('以connect模式进行代理，[{}:{} -> {}:{}]',
                clientAddress.hostString, clientAddress.port, request.host(), request.port())

        Channel channel = null
        try {
            channel = ctx.channel().attr(ChannelAttrKey.BOOTSTRAP).get().connect(request.host(), request.port()).sync().channel()
            InetSocketAddress socketAddress = channel.remoteAddress() as InetSocketAddress
            boolean ipv4 = socketAddress.address instanceof Inet4Address
            SocksAddressType addressType = ipv4 ? SocksAddressType.IPv4 : SocksAddressType.IPv6
            SocksCmdResponse cmdResponse = new SocksCmdResponse(SocksCmdStatus.SUCCESS, addressType, socketAddress.address.getHostAddress(), socketAddress.port)

            ctx.pipeline().addFirst(new ConnectHandler(channel))
            channel.pipeline().addLast(new ChildConnectHandler(ctx.channel()))
            ctx.writeAndFlush(cmdResponse)
            ctx.pipeline().remove(this)
        } catch (Exception e) {
            logger.warn('代理connect请求失败', e)
            try {
                channel?.close()
            } catch (ignore) {
            }
            try {
                ctx.writeAndFlush(new SocksCmdResponse(SocksCmdStatus.FAILURE, SocksAddressType.UNKNOWN)).sync().channel().close()
            } catch (ignore) {
            }
        }
    }
}
