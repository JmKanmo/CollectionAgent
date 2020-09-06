package logger;

import org.junit.Test;

import java.util.logging.Level;

import static org.junit.Assert.*;

public class ErrorLoggingControllerTest {

    @Test
    public void errorLogging() {
        ErrorLoggingController.errorLogging(new NullPointerException());
    }
}