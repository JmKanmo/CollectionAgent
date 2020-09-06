package socket;

import logger.ErrorLoggingController;
import logger.LoggingController;

import java.io.BufferedOutputStream;
import java.io.IOException;
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
            ErrorLoggingController.errorLogging(e);
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
                    } catch (IOException e) {
                        ErrorLoggingController.errorLogging(e);
                    }
                }
                LoggingController.logging(Level.INFO, "Socket is closed?" + socket.isClosed());
            }
        });
    }

    public void sendData(String jsonData) throws IOException {
        try {
            if (socket.isClosed()) {
                connect();
            } else {
                BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
                byte[] bytes = jsonData.getBytes("UTF-8");
                outputStream.write(bytes);
                outputStream.flush();
            }
        } catch (Exception e) {
            if (socket.isClosed() != true) {
                socket.close();
            }
            throw e;
        }
    }
}
