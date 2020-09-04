package collector.type;

import junit.framework.TestCase;
import logger.LoggingController;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;

public class ThreadCollectorTest extends TestCase {
    @InjectMocks
    ThreadCollector threadCollector = new ThreadCollector(null,"threadCollector");

    @Mock
    ThreadMXBean threadMXBean;

    @Mock
    Map<String, Object> hashMap;

    @Test
    public void testInitiation() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(threadCollector);
        assertNotNull(hashMap);
        assertNotNull(threadMXBean);
        assertEquals(threadCollector.getName(),"threadCollector");
    }

    @Test
    public void testCollectThreadInfo() {
        MockitoAnnotations.initMocks(this);
        long threadInfoId = ManagementFactory.getThreadMXBean().getAllThreadIds()[0];
        ThreadInfo tempThreadInfo = ManagementFactory.getThreadMXBean().getThreadInfo(threadInfoId);
        Mockito.when(threadMXBean.getThreadInfo(Mockito.anyLong())).thenReturn(tempThreadInfo);
        Mockito.when(hashMap.put(Mockito.anyString(), Mockito.any())).thenReturn(null);
        threadCollector.collectThreadInfo(Mockito.anyString(), new long[]{25,88});
        Mockito.verify(hashMap, Mockito.times(1)).put(Mockito.anyString(), Mockito.any());
    }
}