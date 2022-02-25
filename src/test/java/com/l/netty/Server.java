package com.l.netty;

import com.l.file.Common;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liam
 * @date 2022/2/25 15:11
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        // 预设缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        // 阻塞模式
        // 使用nio
        // 创建服务器
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false); // 非阻塞模式
        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8888));
        // 连接集合
        List<SocketChannel> channelList = new ArrayList<>();
        while(true){
            // 建立连接accept
//            log.debug("connect.....");
            SocketChannel accept = serverSocketChannel.accept(); // 阻塞方法，线程停止运行
            if(accept != null){
                log.debug("connected.....{}", accept);
                accept.configureBlocking(false); // read设置非阻塞。如果channel.read没有读到数据，返回0
                channelList.add(accept);
            }
            // 接收数据
            for (SocketChannel socketChannel : channelList) {
//                log.debug("before read....{}", socketChannel);
                int read = socketChannel.read(byteBuffer); // 阻塞方法，线程停止运行
                if(read > 0){
                    byteBuffer.flip();
                    Common.read(byteBuffer);
                    byteBuffer.clear();
                    log.debug("after read....{}", socketChannel);
                }
            }
        }
    }
}
