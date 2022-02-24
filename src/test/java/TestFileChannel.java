import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author liam
 * @date 2022/2/24 17:14
 */
public class TestFileChannel {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("world.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer allocate = ByteBuffer.allocate(10);
        channel.read(allocate);
        allocate.flip();
        Common.read(allocate);
        allocate.clear();
        channel.close();
    }
}
