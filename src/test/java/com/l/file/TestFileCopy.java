package com.l.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author liam
 * @date 2022/2/25 14:56
 */
public class TestFileCopy {
    public static void main(String[] args) throws IOException {
        String source = "/Users/liam/code/study/nettyStudy/netty/copyBase";
        String target = "/Users/liam/code/study/nettyStudy/netty/copy";
        Files.walk(Paths.get(source)).forEach(path -> {
            String targetName = path.toString().replace(source, target);
            try {
                if(Files.isDirectory(path)){
                    Files.createDirectory(Paths.get(targetName));
                }
                else if(Files.isRegularFile(path)){
                    Files.copy(path, Paths.get(targetName));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }
}
