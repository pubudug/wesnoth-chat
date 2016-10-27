package ppg.experiment.wesnoth.chat;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import ppg.experiment.wesnoth.chat.codec.GzipDecoder;
import ppg.experiment.wesnoth.chat.handler.GameListDiffMessageHandler;
import ppg.experiment.wesnoth.chat.handler.IgnoreMessageHandler;
import ppg.experiment.wesnoth.chat.handler.MessageHandler;
import ppg.experiment.wesnoth.chat.handler.MessageMessageHandler;
import ppg.experiment.wesnoth.chat.handler.MustLoginRequestHandler;
import ppg.experiment.wesnoth.chat.handler.UserMessageHandler;
import ppg.experiment.wesnoth.chat.handler.VersionRequestHandler;
import ppg.experiment.wesnoth.chat.handler.WhisperMessageHandler;
import ppg.experiment.wesnoth.chat.parser.Tokenizer;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public class WesnothChatClient implements Runnable {

    private static final Logger LOGGER = LogManager
            .getLogger(WesnothChatClient.class);

    private Channel channel;

    private VersionRequestHandler versionRequestHandler;

    private MustLoginRequestHandler mustLoginRequestHandler;

    private UserMessageHandler userMessageHandler;

    private GameListDiffMessageHandler gameListDiffMessageHandler;

    private WhisperMessageHandler whisperMessageHandler;

    private MessageMessageHandler messageMessageHandler;

    private String host;

    private int port;

    private ErrorHandler errorHandler;

    public WesnothChatClient(String host, int port,
            VersionRequestHandler versionRequestHandler,
            MustLoginRequestHandler mustLoginRequestHandler,
            UserMessageHandler userMessageHandler,
            GameListDiffMessageHandler gameListDiffMessageHandler,
            WhisperMessageHandler whisperMessageHandler,
            MessageMessageHandler messageMessageHandler,
            ErrorHandler errorHandler) {
        this.host = host;
        this.port = port;
        this.versionRequestHandler = versionRequestHandler;
        this.mustLoginRequestHandler = mustLoginRequestHandler;
        this.userMessageHandler = userMessageHandler;
        this.gameListDiffMessageHandler = gameListDiffMessageHandler;
        this.whisperMessageHandler = whisperMessageHandler;
        this.messageMessageHandler = messageMessageHandler;
        this.errorHandler = errorHandler;
    }

    private void start() throws InterruptedException {
        LOGGER.info("Starting server.");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
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
                            messageHandlers.add(userMessageHandler);
                            messageHandlers.add(gameListDiffMessageHandler);
                            messageHandlers.add(whisperMessageHandler);
                            messageHandlers.add(messageMessageHandler);
                            messageHandlers.add(errorHandler);
                            messageHandlers.add(getRedirectMessageHandler());
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

    private MessageHandler getRedirectMessageHandler() {
        return new MessageHandler() {

            @Override
            public boolean handles(WMLMessage message) {
                return "[redirect]".equals(message.getNode());
            }

            @Override
            public void handle(WMLMessage msg, Channel c) {
                host = msg.getAttribute("host");
                port = Integer.parseInt(msg.getAttribute("port"));
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            start();
                        } catch (InterruptedException e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    }
                }).start();
                c.close();

            }
        };
    }

    public void sendMessage(String message) {
        message = message.replace("\"", "\"\"");
        String string = "[message]\nmessage=\"" + message + "\"\n[/message]";
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = channel.alloc().buffer(bytes.length);
        buf.writeBytes(bytes);
        channel.writeAndFlush(buf);
    }

    public void whisper(String to, String message) {
        message = message.replace("\"", "\"\"");
        String string = "[whisper]\nmessage=\"" + message + "\"\nreceiver=\""
                + to + "\"\n[/whisper]";
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = channel.alloc().buffer(bytes.length);
        buf.writeBytes(bytes);
        channel.writeAndFlush(buf);
    }
}
