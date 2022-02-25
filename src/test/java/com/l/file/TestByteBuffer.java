package com.l.file;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author liam
 * @date 2022/2/21 16:44
 */
@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(5);
            int len;
            while ((len = channel.read(byteBuffer)) > 0) {
                log.debug("读取到的字节 {}", len);
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get();
                    log.debug("实际字节 {}", (char) b);
                }
                byteBuffer.clear();
            }
        } catch (IOException ignored) {
        }
    }
}
