package logger;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.logging.Level;

public class LoggingControllerTest extends TestCase {
    @Test
    public void testLogging() {
        assertNotNull(LoggingController.getFileHandler());
        assertNotNull(LoggingController.getLogger());
        LoggingController.logging(Level.INFO, "hello guys");
    }
}