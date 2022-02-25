package com.l.file;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author liam
 * @date 2022/2/23 15:36
 */
public class TestByteBufferString {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byte[] bytes = "hello".getBytes(StandardCharsets.UTF_8);
        byteBuffer.put(bytes);
        byteBuffer.flip();

        ByteBuffer world = StandardCharsets.UTF_8.encode("world");

        ByteBuffer wrap = ByteBuffer.wrap("!!!".getBytes(StandardCharsets.UTF_8));

        world.flip();
        String wordStr = StandardCharsets.UTF_8.decode(world).toString();
        System.err.println(wordStr);
    }


}
