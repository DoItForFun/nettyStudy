import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author liam
 * @date 2022/2/24 19:28
 */
public class TestFileChannelTransferTo {
    public static void main(String[] args) throws FileNotFoundException {
        try (FileChannel from = new FileInputStream("data.txt").getChannel();
             FileChannel to = new FileOutputStream("dataTo.txt").getChannel()
        ) {
            // 底层会利用操作系统零拷贝进行优化
            long size = from.size();
            for(long left = size; left > 0;){
                // 多次传输
                left -= from.transferTo(size - left, left, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
