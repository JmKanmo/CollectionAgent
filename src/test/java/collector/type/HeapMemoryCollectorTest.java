package collector.type;

import junit.framework.TestCase;
import logger.LoggingController;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.List;

public class HeapMemoryCollectorTest extends TestCase {
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
    public void testConstructor() {
        HeapMemoryCollector heapMemoryCollector = new HeapMemoryCollector();

        assertNotNull(heapMemoryCollector.getMemoryMXBean());
        assertNotNull(heapMemoryCollector.getGarbageCollectorMXBeans());
        assertNotNull(heapMemoryCollector.getMemoryPoolMXBeans());
        assertNotNull(heapMemoryCollector.getLoggingController());
    }

    @Test
    public void testPrintMemory() {
        HeapMemoryCollector heapMemoryCollector = new HeapMemoryCollector();
        MemoryMXBean memoryMXBean = Mockito.mock(MemoryMXBean.class);
        LoggingController loggingController = Mockito.mock(LoggingController.class);

        Mockito.when(memoryMXBean.getHeapMemoryUsage()).thenReturn(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
        Mockito.when(memoryMXBean.getNonHeapMemoryUsage()).thenReturn(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage());
        Mockito.doNothing().when(loggingController).logging(Mockito.any(), Mockito.anyString());

        heapMemoryCollector.setMemoryMXBean(memoryMXBean);
        heapMemoryCollector.setLoggingController(loggingController);
        heapMemoryCollector.printMemory();

        Mockito.verify(memoryMXBean, Mockito.times(1)).getHeapMemoryUsage();
        Mockito.verify(memoryMXBean, Mockito.times(1)).getNonHeapMemoryUsage();
        Mockito.verify(loggingController, Mockito.times(1)).logging(Mockito.any(), Mockito.anyString());
        assertEquals(heapMemoryCollector.getStringBuilder().toString(), "");
    }

    @Test
    public void testPrintMemoryPool() {
        HeapMemoryCollector heapMemoryCollector = new HeapMemoryCollector();
        List<MemoryPoolMXBean> memoryPoolMXBeans = new ArrayList<>();
        MemoryPoolMXBean memoryPoolMXBean = Mockito.mock(MemoryPoolMXBean.class);
        MemoryPoolMXBean tempMemoryBean = ManagementFactory.getMemoryPoolMXBeans().get(0);
        LoggingController loggingController = Mockito.mock(LoggingController.class);

        Mockito.when(memoryPoolMXBean.getCollectionUsage()).thenReturn(tempMemoryBean.getCollectionUsage());
        Mockito.when(memoryPoolMXBean.getUsageThreshold()).thenReturn(tempMemoryBean.getUsageThreshold());
        Mockito.when(memoryPoolMXBean.getUsageThresholdCount()).thenReturn(tempMemoryBean.getUsageThresholdCount());
        Mockito.when(memoryPoolMXBean.getMemoryManagerNames()).thenReturn(tempMemoryBean.getMemoryManagerNames());
        Mockito.when(memoryPoolMXBean.getUsage()).thenReturn(tempMemoryBean.getUsage());
        Mockito.when(memoryPoolMXBean.getName()).thenReturn(tempMemoryBean.getName());
        Mockito.when(memoryPoolMXBean.getPeakUsage()).thenReturn(tempMemoryBean.getPeakUsage());
        Mockito.when(memoryPoolMXBean.getType()).thenReturn(tempMemoryBean.getType());

        Mockito.doNothing().when(loggingController).logging(Mockito.any(), Mockito.anyString());

        memoryPoolMXBeans.add(memoryPoolMXBean);
        heapMemoryCollector.setMemoryPoolMXBeans(memoryPoolMXBeans);
        heapMemoryCollector.setLoggingController(loggingController);
        heapMemoryCollector.printMemoryPool();

        Mockito.verify(memoryPoolMXBean, Mockito.times(1)).getCollectionUsage();
        Mockito.verify(memoryPoolMXBean, Mockito.times(1)).getUsageThreshold();
        Mockito.verify(memoryPoolMXBean, Mockito.times(1)).getUsageThresholdCount();
        Mockito.verify(memoryPoolMXBean, Mockito.times(1)).getMemoryManagerNames();
        Mockito.verify(memoryPoolMXBean, Mockito.times(1)).getUsage();
        Mockito.verify(memoryPoolMXBean, Mockito.times(1)).getName();
        Mockito.verify(memoryPoolMXBean, Mockito.times(1)).getType();
        Mockito.verify(memoryPoolMXBean, Mockito.times(1)).getPeakUsage();
        Mockito.verify(memoryPoolMXBean, Mockito.times(1)).getType();
        Mockito.verify(loggingController, Mockito.times(1)).logging(Mockito.any(), Mockito.anyString());
        assertEquals(heapMemoryCollector.getStringBuilder().toString(), "");
    }

    @Test
    public void testPrintGarbageCollector() {
        HeapMemoryCollector heapMemoryCollector = new HeapMemoryCollector();
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = new ArrayList<>();
        GarbageCollectorMXBean garbageCollectorMXBean = Mockito.mock(GarbageCollectorMXBean.class);
        GarbageCollectorMXBean tempGC = ManagementFactory.getGarbageCollectorMXBeans().get(0);
        LoggingController loggingController = Mockito.mock(LoggingController.class);

        Mockito.when(garbageCollectorMXBean.getCollectionCount()).thenReturn(tempGC.getCollectionCount());
        Mockito.when(garbageCollectorMXBean.getCollectionTime()).thenReturn(tempGC.getCollectionTime());
        Mockito.when(garbageCollectorMXBean.getMemoryPoolNames()).thenReturn(tempGC.getMemoryPoolNames());
        Mockito.when(garbageCollectorMXBean.getName()).thenReturn(tempGC.getName());

        Mockito.doNothing().when(loggingController).logging(Mockito.any(), Mockito.anyString());

        garbageCollectorMXBeans.add(garbageCollectorMXBean);
        heapMemoryCollector.setGarbageCollectorMXBeans(garbageCollectorMXBeans);
        heapMemoryCollector.setLoggingController(loggingController);
        heapMemoryCollector.printGarbageCollector();

        Mockito.verify(garbageCollectorMXBean, Mockito.times(1)).getCollectionCount();
        Mockito.verify(garbageCollectorMXBean, Mockito.times(1)).getCollectionTime();
        Mockito.verify(garbageCollectorMXBean, Mockito.times(1)).getMemoryPoolNames();
        Mockito.verify(garbageCollectorMXBean, Mockito.times(1)).getName();
        Mockito.verify(loggingController, Mockito.times(1)).logging(Mockito.any(), Mockito.anyString());
        assertEquals(heapMemoryCollector.getStringBuilder().toString(), "");
    }

    @Test
    public void testStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hello");
        stringBuilder.setLength(0);
        assertEquals(stringBuilder.toString(), "");
    }
}