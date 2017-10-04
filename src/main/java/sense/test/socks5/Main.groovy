package sense.test.socks5

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sense.test.socks5.strap.ServerStrap

/**
 * Created by sense on 2017/7/11.
 */
class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class)

    static void main(String[] args) {
        try {
            def cli = new CliBuilder()
            cli.usage = '指定服务启动所监听的端口。'
            cli.options
                    .addOption('p', 'port', true, '指定服务启动所监听的端口。（默认：1080）')
                    .addOption('h', 'help', false, '显示此帮助信息。')

            def opt = cli.parse(args)
            if (!opt || opt.h) {
                cli.usage()
                System.exit(1)
            }

            def port = (opt.p ? opt.p : '1080') as int

            logger.info('程序启动，监听端口：{}', port as String)
            new ServerStrap().init(port)
        } catch (Exception ex) {
            logger.error('程序启动出错。', ex)
            System.exit(2)
        }
    }
}
