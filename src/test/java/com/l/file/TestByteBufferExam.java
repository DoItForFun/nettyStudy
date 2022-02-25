package com.l.file;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author liam
 * @date 2022/2/24 14:25
 */
public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        byteBuffer.put("Hello,world\nI'm zhangsan\nHo".getBytes(StandardCharsets.UTF_8));
        split(byteBuffer);
        byteBuffer.put("w are you?\n".getBytes(StandardCharsets.UTF_8));
        split(byteBuffer);
    }
    private static void split(ByteBuffer byteBuffer){
        byteBuffer.flip();
        for (int i = 0; i < byteBuffer.limit(); i++) {
            byte b = byteBuffer.get(i);
            if(b == '\n'){
                int len = i + 1 - byteBuffer.position();
                ByteBuffer current = ByteBuffer.allocate(len);
                for (int j = 0; j < len; j++) {
                    byte b1 = byteBuffer.get();
                    current.put(b1);
                }
                current.flip();
                Common.read(current);
                current.clear();
            }
        }
        byteBuffer.compact();
    }
}
