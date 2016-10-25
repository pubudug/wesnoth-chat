package ppg.experiment.wesnoth.chat;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ppg.experiment.wesnoth.chat.parser.Tokenizer;
import ppg.experiment.wesnoth.chat.wml.WMLMessage;

public class MessageBus extends ChannelInboundHandlerAdapter {

    private Tokenizer tokenizer;

    private List<MessageHandler> messageHandlers;

    private WMLMessageBuilderFactory wmlMessageBuilderFactory;

    public MessageBus(Tokenizer tokenizer, List<MessageHandler> messageHandlers,
            WMLMessageBuilderFactory wmlMessageBuilderFactory) {
        super();
        this.tokenizer = tokenizer;
        this.messageHandlers = messageHandlers;
        this.wmlMessageBuilderFactory = wmlMessageBuilderFactory;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        String b = (String) msg;

        WMLMessageBuilder builder = wmlMessageBuilderFactory
                .createNewWMLMessageBuilder();
        tokenizer.tokenize(b, builder);
        WMLMessage message = builder.getWMLMessage();
        messageHandlers.stream().filter(h -> h.handles(message)).findFirst()
                .orElse(new MissingMessageHandler())
                .handle(message, ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
