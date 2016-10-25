package ppg.experiment.wesnoth.chat;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.GZIPInputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class GzipDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
            List<Object> out) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        GZIPInputStream gzis = new GZIPInputStream(
                new ByteArrayInputStream(bytes));
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader bufferedReader = new BufferedReader(reader);

        StringBuilder message = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            message.append(line);
        }
        out.add(message.toString());
    }

}
