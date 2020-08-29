package socket;

import logger.LoggingController;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

import static org.junit.Assert.*;

public class SocketControllerTest {
    private ServerSocket serverSocket;
    private SocketController socketController;
    private Socket serverAcceptSocket;

    @Test
    public void testConnect() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serverSocket = new ServerSocket(5001);
                        serverAcceptSocket = serverSocket.accept();

                        int readCount = serverAcceptSocket.getInputStream().read(new byte[100]);
                        System.out.println(readCount);
                        Thread.sleep(1000);

                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            socketController = new SocketController();
            socketController.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(socketController.getSocket().isConnected(), true);
    }
}