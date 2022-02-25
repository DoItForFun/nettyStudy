package com.l.file;

import java.nio.ByteBuffer;

/**
 * @author liam
 * @date 2022/2/23 15:57
 */
public class Common {
    public static void read(ByteBuffer byteBuffer) {
        while (byteBuffer.hasRemaining()) {
            byte b = byteBuffer.get();
            System.err.println((char) b);
        }
    }
}
