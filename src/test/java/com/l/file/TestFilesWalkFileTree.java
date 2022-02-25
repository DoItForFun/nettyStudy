package com.l.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liam
 * @date 2022/2/25 14:17
 */
public class TestFilesWalkFileTree {
    public static void main(String[] args) throws IOException {
        Files.walkFileTree(Paths.get("/Users/liam/code/study/nettyStudy/netty/testA"), new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

    public static void m2(String[] args) throws IOException {
        Files.walkFileTree(Paths.get("/Users/liam/code/study/nettyStudy/netty"), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if(file.toString().endsWith(".txt")){
                    System.err.println(file);
                }
                return super.visitFile(file, attrs);
            }
        });
    }
    public static void m1(String[] args) throws IOException {
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        // 访问者模式
        Files.walkFileTree(Paths.get("/Users/liam/code/study/nettyStudy/netty"), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.err.println("=======>" + dir);
                fileCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.err.println(file);
                dirCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        System.err.println("dir count: " + dirCount);
        System.err.println("file count: " + fileCount);
    }
}
