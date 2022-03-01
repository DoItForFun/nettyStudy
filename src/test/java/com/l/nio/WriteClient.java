package com.l.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author liam
 * @date 2022/3/1 15:54
 */
public class WriteClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8080));

        int count = 0;
        while(true){
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
            count += socketChannel.read(byteBuffer);
            System.err.println(count);
            byteBuffer.clear();
        }
    }
}
