import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author liam
 * @date 2022/2/23 16:24
 */
public class TestScatteringWrites {
    public static void main(String[] args) {
        ByteBuffer b1 = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer b2 = StandardCharsets.UTF_8.encode("world");
        ByteBuffer b3 = StandardCharsets.UTF_8.encode("你好");
        try (FileChannel rw = new RandomAccessFile("world.txt", "rw").getChannel()) {
            rw.write(new ByteBuffer[]{b1, b2, b3});
        } catch (IOException e) {
        }
    }
}
