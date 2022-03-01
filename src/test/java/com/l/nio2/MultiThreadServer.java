package com.l.nio2;

import com.l.file.Common;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author liam
 * @date 2022/3/1 16:54
 */
@Slf4j
public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        Worker worker = new Worker("work-0");

        while(true){
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey sk = iterator.next();
                iterator.remove();
                if (sk.isAcceptable()) {
                    ssc = (ServerSocketChannel) sk.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.debug("connect.....{}", sc.getRemoteAddress());
                    log.debug("before register.....{}", sc.getRemoteAddress());
                    worker.register(sc);
                    log.debug("after register.....{}", sc.getRemoteAddress());
                }
            }
        }
    }

    static class Worker implements Runnable{
        private Thread thread;
        private Selector selector;
        private String name;
        private volatile boolean start = false;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        Worker(String name){
            this.name = name;
        }

        public void register(SocketChannel sc) throws IOException {
            if(!start){
                thread = new Thread(this, name);
                thread.start();
                selector = Selector.open();
                start = true;
            }
            queue.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ, null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            selector.wakeup();
        }

        @Override
        public void run() {
            while(true){
                try{
                    selector.select();
                    Runnable task = queue.poll();
                    if(task != null){
                        task.run();
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        iterator.remove();
                        if (next.isReadable()) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
                            SocketChannel socketChannel = (SocketChannel) next.channel();
                            log.debug("read....{}", socketChannel.getRemoteAddress());
                            socketChannel.read(byteBuffer);
                            byteBuffer.flip();
                            Common.read(byteBuffer);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
