package sense.test.socks5.handler

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.socks.SocksAuthScheme
import io.netty.handler.codec.socks.SocksInitRequest
import io.netty.handler.codec.socks.SocksInitResponse
import io.netty.handler.codec.socks.SocksProtocolVersion
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by sense on 2017/10/2.
 */
class InitHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(InitHandler.class)

    @Override
    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (isSocks5Request(msg)) {
            logger.debug('收到代理请求，{}', ctx.channel())

            ctx.writeAndFlush(buildResponse())
            ctx.pipeline().remove(this)
        } else {
            ctx.close()
        }
    }

    protected boolean isSocks5Request(Object msg) {
        boolean pass = msg instanceof SocksInitRequest
        if (!pass) {
            return pass
        }
        SocksInitRequest socksInitRequest = (SocksInitRequest) msg
        pass = pass && socksInitRequest.protocolVersion() == SocksProtocolVersion.SOCKS5
        pass = pass && socksInitRequest.authSchemes().contains(SocksAuthScheme.NO_AUTH)
        pass
    }

    protected SocksInitResponse buildResponse() {
        return new SocksInitResponse(SocksAuthScheme.NO_AUTH)
    }
}
