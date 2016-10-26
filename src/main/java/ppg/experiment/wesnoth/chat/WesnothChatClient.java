package ppg.experiment.wesnoth.chat;

import java.net.InetSocketAddress;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import ppg.experiment.wesnoth.chat.codec.GzipDecoder;
import ppg.experiment.wesnoth.chat.parser.Tokenizer;

public class WesnothChatClient implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(WesnothChatClient.class);

    private Channel channel;

    private VersionRequestHandler versionRequestHandler;

    private MustLoginRequestHandler mustLoginRequestHandler;

    public WesnothChatClient(VersionRequestHandler versionRequestHandler,
            MustLoginRequestHandler mustLoginRequestHandler) {
        this.versionRequestHandler = versionRequestHandler;
        this.mustLoginRequestHandler = mustLoginRequestHandler;
    }

    private void start() throws InterruptedException {
        LOGGER.info("Starting server.");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("localhost", 15000))
                    .handler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch)
                                throws Exception {
                            ch.pipeline().addLast(new ConnectionInitializer());
                            ch.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(
                                            40 * 1024 * 1024, 0, 4, 0, 4));
                            ch.pipeline().addLast(new GzipDecoder());

                            LinkedList<MessageHandler> messageHandlers = new LinkedList<>();
                            messageHandlers.add(versionRequestHandler);
                            messageHandlers.add(mustLoginRequestHandler);
                            messageHandlers.add(new IgnoreMessageHandler());

                            ch.pipeline()
                                    .addLast(new MessageBus(new Tokenizer(),
                                            messageHandlers,
                                            new WMLMessageBuilderFactory()));
                        }
                    });
            ChannelFuture f = b.connect().sync();
            this.channel = f.channel();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    @Override
    public void run() {
        try {
            start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
