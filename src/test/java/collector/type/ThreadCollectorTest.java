package collector.type;

import junit.framework.TestCase;
import logger.LoggingController;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ThreadCollectorTest extends TestCase {
    @InjectMocks
    ThreadCollector threadCollector;

    @Mock
    LoggingController loggingController;

    @Mock
    ThreadMXBean threadMXBean;

    @Test
    public void testThread() {
        Thread thread = new Thread(new ThreadCollector());
        thread.start();
        assertEquals(thread.getState(), Thread.State.RUNNABLE);
        thread.interrupt();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    public void testConstructor() {
        ThreadCollector threadCollector = new ThreadCollector();

        assertNotNull(threadCollector.getLoggingController());
        assertNotNull(threadCollector.getStringBuilder());
        assertNotNull(threadCollector.getThreadMXBean());
    }

    @Test
    public void testStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hello");
        stringBuilder.setLength(0);
        assertEquals(stringBuilder.toString(), "");
    }

    @Test
    public void testPrintThread() {
        MockitoAnnotations.initMocks(this);
        ThreadMXBean tempThreadMXBean = ManagementFactory.getThreadMXBean();

        Mockito.when(threadMXBean.getAllThreadIds()).thenReturn(tempThreadMXBean.getAllThreadIds());
        Mockito.when(threadMXBean.getThreadInfo(Mockito.anyLong())).thenReturn(null);

        threadCollector.setThreadMXBean(threadMXBean);
        threadCollector.printThreadInfo();

        Mockito.verify(threadMXBean, Mockito.times(1)).getAllThreadIds();
        Mockito.verify(threadMXBean, Mockito.times(tempThreadMXBean.getAllThreadIds().length)).getThreadInfo(Mockito.anyLong());
    }
}