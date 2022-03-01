package com.l.nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author liam
 * @date 2022/2/25 15:53
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("localhost", 8080));
        channel.write(StandardCharsets.UTF_8.encode("1234567890abcdef"));
        System.in.read();
    }
}
