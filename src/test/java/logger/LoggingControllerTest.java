package logger;

import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingControllerTest extends TestCase {
    @Test
    public void testLogging() {
        LoggingController loggingController = new LoggingController("D:\\logfile\\agentLog\\heapLog\\info.log");
        assertNotNull(loggingController.getFileHandler());
        assertNotNull(loggingController.getLogger());

        Logger logger = Mockito.mock(Logger.class);
        loggingController.setLogger(logger);

        Mockito.doNothing().when(logger).log(Mockito.any(), Mockito.anyString());
        loggingController.logging(Level.INFO, "hello guys");
        Mockito.verify(logger, Mockito.times(1)).log(Mockito.any(), Mockito.anyString());
    }
}