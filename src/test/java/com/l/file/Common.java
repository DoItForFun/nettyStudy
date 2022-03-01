package com.l.file;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author liam
 * @date 2022/2/23 15:57
 */
public class Common {
    public static void read(ByteBuffer byteBuffer) {
        while (byteBuffer.hasRemaining()) {
            String string = StandardCharsets.UTF_8.decode(byteBuffer).toString();
            System.err.println(string);
        }
    }
}
