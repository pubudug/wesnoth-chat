package ppg.experiment.wesnoth.chat;

import java.net.InetSocketAddress;
import java.util.LinkedList;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;

public class Client implements Runnable {
    private Channel channel;

    public Client() {

    }

    private void start() throws InterruptedException {
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
                            ch.pipeline().addLast(ZlibCodecFactory
                                    .newZlibDecoder(ZlibWrapper.GZIP));
                            ch.pipeline().addLast(
                                    new MessageBus(new LinkedList<>()));
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

    public static void main(String[] args) {
        new Client().run();
    }

}
