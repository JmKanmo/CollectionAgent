package collector.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import junit.framework.TestCase;
import logger.LoggingController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static org.junit.Assert.assertEquals;

public class HeapMemoryCollectorTest extends TestCase {
    @InjectMocks
    HeapMemoryCollector heapMemoryCollector = new HeapMemoryCollector(null, "heapMemoryCollector");

    @Mock
    MemoryMXBean memoryMXBean;

    @Mock
    List<GarbageCollectorMXBean> garbageCollectorMXBeans;

    @Mock
    Map<String, Object> hashMap;

    @Test
    public void testThread() {
        Thread thread = new Thread(new HeapMemoryCollector());
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
        assertNotNull(heapMemoryCollector);
        assertNotNull(memoryMXBean);
        assertNotNull(garbageCollectorMXBeans);
        assertEquals(heapMemoryCollector.getName(), "heapMemoryCollector");
    }

    @Test
    public void testCollectMemoryUsage() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(memoryMXBean.getHeapMemoryUsage()).thenReturn(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
        Mockito.when(memoryMXBean.getNonHeapMemoryUsage()).thenReturn(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage());
        Mockito.when(hashMap.put(Mockito.anyString(), Mockito.any())).thenReturn(null);

        heapMemoryCollector.collectMemoryUsage();

        Mockito.verify(memoryMXBean, Mockito.times(1)).getHeapMemoryUsage();
        Mockito.verify(memoryMXBean, Mockito.times(1)).getNonHeapMemoryUsage();
        Mockito.verify(hashMap, Mockito.times(2)).put(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void testCollectGarbageCollection() {
        MockitoAnnotations.initMocks(this);
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        heapMemoryCollector.setGarbageCollectorMXBeans(garbageCollectorMXBeans);
        Mockito.when(hashMap.put(Mockito.anyString(), Mockito.any())).thenReturn(null);
        heapMemoryCollector.setGarbageCollectorMXBeans(garbageCollectorMXBeans);
        heapMemoryCollector.collectGarbageCollection();
        Mockito.verify(hashMap, Mockito.times(1)).put(Mockito.anyString(), Mockito.any());
    }
}