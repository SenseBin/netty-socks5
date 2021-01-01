package sense.test.socks5.constant

import io.netty.bootstrap.Bootstrap
import io.netty.util.AttributeKey

/**
 * Created by sense on 2017/10/3.
 */
interface ChannelAttrKey {
    AttributeKey<Bootstrap> BOOTSTRAP = new AttributeKey('bootstrap')
    AttributeKey<Bootstrap> BOOTSTRAP_UDP = new AttributeKey('bootstrap_udp')
}
