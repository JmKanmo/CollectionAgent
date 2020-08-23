package collector.type;

import junit.framework.TestCase;
import org.junit.Test;

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
    public void testJMX() {
        HeapMemoryCollector heapMemoryCollector = new HeapMemoryCollector();
        assertNotNull(heapMemoryCollector.getMemoryMXBean());
        assertNotNull(heapMemoryCollector.getGarbageCollectorMXBeans());
        assertNotNull(heapMemoryCollector.getMemoryManagerMXBeans());
        assertNotNull(heapMemoryCollector.getMemoryPoolMXBeans());
    }
}