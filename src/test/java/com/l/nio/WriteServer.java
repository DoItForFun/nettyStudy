package com.l.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author liam
 * @date 2022/3/1 14:59
 */
public class WriteServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        SelectionKey register = ssc.register(selector, 0, null);
        register.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        while(true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                iterator.remove();
                if(next.isAcceptable()){
                    ServerSocketChannel sc = (ServerSocketChannel) next.channel();
                    SocketChannel accept = sc.accept();
                    accept.configureBlocking(false);
                    SelectionKey sk = accept.register(selector, 0, null);
                    sk.interestOps(SelectionKey.OP_READ);

                    // 向客户端发送大量的数据
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < 3000000; i++) {
                        stringBuilder.append("a");
                    }
                    ByteBuffer encode = StandardCharsets.UTF_8.encode(stringBuilder.toString());
                    // 返回实际写入的字节数判断是否有剩余内容
                    int write = accept.write(encode);
                    if(encode.hasRemaining()){
                        // 关注可写事件
                        sk.interestOps(sk.interestOps() + SelectionKey.OP_WRITE);
                        // 未写完的事件挂到key上
                        sk.attach(encode);
                    }
                }
                else if(next.isWritable()){
                    SocketChannel channel = (SocketChannel) next.channel();
                    ByteBuffer attachment = (ByteBuffer) next.attachment();
                    int write = channel.write(attachment);
                    System.err.println(write);
                    if(!attachment.hasRemaining()){
                        next.attach(null);
                        next.interestOps(next.interestOps() - SelectionKey.OP_WRITE);
                    }
                }
            }
        }
    }
}
