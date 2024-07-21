package com.kjr21362.codec;

import com.kjr21362.common.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/*
  4 bytes       1 byte          1 byte                1 byte         4 bytes        1 byte            4 bytes
magic number | version | serialization algorithm | message type | request id | placeholder byte | message length | message content |
 */

@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out)
        throws Exception {
        // 4 bytes magic number
        out.writeBytes(new byte[] {1, 2, 3, 4});
        // 1 byte version
        out.writeByte(1);
        // 1 byte serialization algorithm
        out.writeByte(0);
        // 1 byte message type
        out.writeByte(message.getMessageType());
        // 4 bytes request id
        out.writeInt(message.getSequenceId());
        // 1 byte for alignment to 16 bytes
        out.writeByte(0xff);
        // 4 bytes message length
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(message);
        byte[] bytes = bos.toByteArray();
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out)
        throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializationType = in.readByte();
        byte messageType = in.readByte();
        int requestId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        if (serializationType == 0) {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Message message = (Message) ois.readObject();

            log.debug("{}", message);
            out.add(message);
        }

    }
}
