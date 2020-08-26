package collector.type;

import logger.LoggingController;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.management.RuntimeMXBean;
import java.util.Map;

import static org.junit.Assert.*;

public class RunTimeCollectorTest {
    @InjectMocks
    RunTimeCollector runTimeCollector = new RunTimeCollector(null,"runTimeCollector");

    @Mock
    RuntimeMXBean runtimeMXBean;

    @Mock
    Map<String, Object> hashMap;

    @Test
    public void testThread() {
        Thread thread = new Thread(new RunTimeCollector());
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
    public void testInitiation() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(runTimeCollector);
        assertNotNull(hashMap);
        assertNotNull(runtimeMXBean);
        assertEquals(runTimeCollector.getName(),"runTimeCollector");
    }

    @Test
    public void testCollectRuntimeInfo() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(hashMap.put(Mockito.anyString(), Mockito.any())).thenReturn(null);
        runTimeCollector.collectRuntimeInfo();
        Mockito.verify(hashMap, Mockito.times(1)).put(Mockito.anyString(), Mockito.any());
    }
}