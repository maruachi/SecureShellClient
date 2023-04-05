import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class IoUtils {

    public static final int BUFFER_SIZE = 8192;
    public static final Charset ENCODING = StandardCharsets.UTF_8;

    public static BufferedReader toReader(InputStream inputStream) {
        BufferedInputStream bis = new BufferedInputStream(inputStream, BUFFER_SIZE);
        InputStreamReader isr = new InputStreamReader(bis, ENCODING);
        return new BufferedReader(isr, BUFFER_SIZE);
    }

    public static Writer toWriter(OutputStream outputStream) {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream, BUFFER_SIZE);
        return new OutputStreamWriter(bos, ENCODING);
    }

    public static void writeLine(Writer writer, String mesage) {
        try {
            writer.write(mesage);
            writer.write('\n');
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void transferAllByte(InputStream in, OutputStream out) {
        BufferedInputStream bis = new BufferedInputStream(in, BUFFER_SIZE);
        BufferedOutputStream bos = new BufferedOutputStream(out, BUFFER_SIZE);

        byte[] buffer = new byte[BUFFER_SIZE];
        while (true) {
            try {
                int len = bis.read(buffer);
                if (len == -1) {
                    bos.flush();
                    return;
                }
                bos.write(buffer,0,len);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
