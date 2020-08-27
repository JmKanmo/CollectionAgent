package socket;

import logger.LoggingController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketController {
    private Socket socket;

    public void connect() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 5001));
        } catch (Exception e) {
            LoggingController.errorLogging(e);
        }
    }

    public void close() {
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                LoggingController.errorLogging(e);
            }
        }
    }

    public void sendData(String jsonData) {

    }
}
