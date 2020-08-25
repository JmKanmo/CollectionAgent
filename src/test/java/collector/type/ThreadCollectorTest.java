package collector.type;

import junit.framework.TestCase;
import logger.LoggingController;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Map;

public class ThreadCollectorTest extends TestCase {
    @InjectMocks
    ThreadCollector threadCollector;

    @Mock
    LoggingController loggingController;

    @Mock
    ThreadMXBean threadMXBean;

    @Mock
    Map<String, Object> hashMap;

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
        MockitoAnnotations.initMocks(this);
        assertNotNull(threadCollector);
        assertNotNull(hashMap);
        assertNotNull(threadMXBean);
        assertNotNull(loggingController);
    }

    @Test
    public void testPrintInfo() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(hashMap.put(Mockito.anyString(), Mockito.any())).thenReturn(null);
        Mockito.doNothing().when(loggingController).logging(Mockito.any(), Mockito.anyString());
        threadCollector.printInfo();
        Mockito.verify(hashMap, Mockito.times(1)).put(Mockito.anyString(), Mockito.any());
        Mockito.verify(loggingController, Mockito.times(1)).logging(Mockito.any(), Mockito.anyString());
    }

    @Test
    public void testPrintDeadLockThreads() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(hashMap.put(Mockito.anyString(), Mockito.any())).thenReturn(null);
        Mockito.doNothing().when(loggingController).logging(Mockito.any(), Mockito.anyString());
        Mockito.when(threadMXBean.findDeadlockedThreads()).thenReturn(new long[]{3});
        threadCollector.printDeadLockThreads();
        Mockito.verify(hashMap, Mockito.times(1)).put(Mockito.anyString(), Mockito.any());
        Mockito.verify(loggingController, Mockito.times(1)).logging(Mockito.any(), Mockito.anyString());
    }

    @Test
    public void testPrintAllThreads() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(hashMap.put(Mockito.anyString(), Mockito.any())).thenReturn(null);
        Mockito.doNothing().when(loggingController).logging(Mockito.any(), Mockito.anyString());
        Mockito.when(threadMXBean.getAllThreadIds()).thenReturn(new long[]{3});
        threadCollector.printAllThreads();
        Mockito.verify(hashMap, Mockito.times(1)).put(Mockito.anyString(), Mockito.any());
        Mockito.verify(loggingController, Mockito.times(1)).logging(Mockito.any(), Mockito.anyString());
    }

    @Test
    public void testPrintThreads() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(hashMap.put(Mockito.anyString(), Mockito.any())).thenReturn(null);
        Mockito.doNothing().when(loggingController).logging(Mockito.any(), Mockito.anyString());
        threadCollector.printThreads(Mockito.anyString(), new long[]{0});
        Mockito.verify(hashMap, Mockito.times(1)).put(Mockito.anyString(), Mockito.any());
        Mockito.verify(loggingController, Mockito.times(1)).logging(Mockito.any(), Mockito.anyString());
    }

    @Test
    public void testPrintThread() {
        MockitoAnnotations.initMocks(this);
        ThreadCollector threadCollector = new ThreadCollector();
        threadCollector.printInfo();
    }
}