import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.net.Socket;

public class SecureShellClient {

    public static final int PORT = 7777;

    public static void main(String[] args) {
        SecureShellClient secureShellClient = new SecureShellClient();
        secureShellClient.run();
    }

    public void run() {
        BufferedReader clientReader = IoUtils.toReader(System.in);

        while (true) {
            try {
                String sshLine = clientReader.readLine();
                String[] sshLineElement = sshLine.split("[ ]+");
                int numElement = sshLineElement.length;

                if (!(numElement == 4 && "ssh".equals(sshLineElement[0]))) {
                    continue;
                }

                String ip = sshLineElement[1];
                String username = sshLineElement[2];
                String password = sshLineElement[3];

                final Socket socket = new Socket(ip, PORT);

                Writer serverWriter = IoUtils.toWriter(socket.getOutputStream());
                Writer clientWriter = IoUtils.toWriter(System.out);
                BufferedReader serverReader = IoUtils.toReader(socket.getInputStream());

                IoUtils.writeLine(serverWriter, username + " " + password);

                Response response = Response.create(serverReader.readLine());

                if (Response.FAIL == response) {
                    String failMessage = clientReader.readLine();
                    IoUtils.writeLine(clientWriter, failMessage);
                    socket.close();
                    continue;
                }

                if (Response.OK == response) {
                    Thread receiveHandlerThread = new Thread(() -> {
                        try {
                            IoUtils.transferAllByte(socket.getInputStream(), System.out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    receiveHandlerThread.start();

                    try (SecureShell secureShell = new SecureShell(socket);) {
                        secureShell.run();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
