package sense.test.socks5.handler

import io.netty.channel.Channel
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext

/**
 * Created by sense on 2017/10/3.
 */
class ChildConnectHandler extends ChannelDuplexHandler {
    private Channel channel

    ChildConnectHandler(Channel channel) {
        this.channel = channel
    }

    @Override
    void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close()
        channel.close()
    }

    @Override
    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        channel.writeAndFlush(msg)
    }
}
