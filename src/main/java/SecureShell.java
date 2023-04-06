import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.net.Socket;

public class SecureShell implements Closeable {
    private final Socket socket;

    public SecureShell(Socket socket) {
        this.socket = socket;
    }


    public void run() {

        // responseHandlerThread에서 조건을 받아서 호출한 프로그램을 종료시키는 방법?
//        Thread responseHandlerThread = new Thread(() -> {
//            try {
//                BufferedReader serverReader = IoUtils.toReader(socket.getInputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        responseHandlerThread.start();

        try {
            IoUtils.transferLineByLine(System.in, socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
