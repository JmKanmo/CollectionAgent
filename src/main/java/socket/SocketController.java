package socket;

import logger.LoggingController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;

public class SocketController {
    private Socket socket;

    public SocketController() {
        try {
            autoShutdown();
            connect();
        } catch (Exception e) {
            LoggingController.errorLogging(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void connect() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 5001));
    }

    public void autoShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        LoggingController.errorLogging(e);
                    }
                }
                LoggingController.logging(Level.INFO, "Socket is closed?" + socket.isClosed());
            }
        });
    }

    public void sendData(String jsonData) {
        try {
            if (socket.isClosed()) {
                connect();
                return;
            } else {
                OutputStream outputStream = socket.getOutputStream();
                byte[] bytes = jsonData.getBytes("UTF-8");
                outputStream.write(bytes);
                outputStream.flush();
            }
        } catch (Exception e) {
            LoggingController.errorLogging(e);
            try {
                if (socket.isClosed() != true) {
                    socket.close();
                }
            } catch (IOException ioException) {
                LoggingController.errorLogging(ioException);
            }
        }
    }
}
