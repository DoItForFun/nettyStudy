package com.l.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author liam
 * @date 2022/2/25 18:51
 */
@Slf4j
public class ServerSelector {
    public static void main(String[] args) throws IOException {
        // 创建selector,管理多个channel
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        // 建立selector 和 channel的联系  （注册）
        // 注册方法返回selectionKey。将来事件发生后通过它可以知道事件和channel
        // accept  会在有连接请求时触发
        // connect 客户端，连接建立后触发
        // read  可读事件
        // write 可写事件
        SelectionKey serverKey = serverSocketChannel.register(selector, 0, null);
        // 监听连接事件
        serverKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("registerKey :{}", serverKey);
        serverSocketChannel.bind(new InetSocketAddress(8888));
        while(true){
            /* select 方法。无事件发生时，线程阻塞。有事件发生，线程向下运行 */
            selector.select();
            // 处理事件。selectedKeys内部包含了所有发生的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                log.debug("iteratorKey :{}", key);
                if (key.isAcceptable()) {
                    ServerSocketChannel sc = (ServerSocketChannel) key.channel();
                    SocketChannel accept = sc.accept();
                    accept.configureBlocking(false);
                    // 可以使用ByteBuffer作为附件传入socketChannel中
                    ByteBuffer allocate = ByteBuffer.allocate(16);
                    SelectionKey register = accept.register(selector, 0, allocate);
                    register.interestOps(SelectionKey.OP_READ);
                }
                else if(key.isReadable()){
                    try{
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        // 获取socketChannel中的attachment
                        ByteBuffer attachment = (ByteBuffer) key.attachment();
                        int read = socketChannel.read(attachment);
                        if(read == -1){
                            key.cancel();
                        }else{
                            split(attachment);
                            if(attachment.position() == attachment.limit()){
                                ByteBuffer byteBuffer = ByteBuffer.allocate(attachment.capacity() * 2);
                                attachment.flip();
                                byteBuffer.put(attachment);
                                key.attach(byteBuffer);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            }
        }
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
                while(current.hasRemaining()){
                    System.err.println(StandardCharsets.UTF_8.decode(current));
                }
                current.compact();
            }
        }
        byteBuffer.compact();
    }
}
