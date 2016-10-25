package ppg.experiment.wesnoth.chat;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class GzipEncoder extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf uncompressed,
            ByteBuf out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream zip = new GZIPOutputStream(baos, true);
        byte[] bytes = new byte[uncompressed.readableBytes()];
        uncompressed.readBytes(bytes);
        zip.write(bytes);
        zip.flush();
        zip.close();
        out.writeBytes(baos.toByteArray());
    }
}
