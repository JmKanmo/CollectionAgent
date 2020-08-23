package collector.type;

import java.lang.management.*;
import java.util.List;

public class HeapMemoryCollector implements Runnable {
    private MemoryMXBean memoryMXBean;
    private List<GarbageCollectorMXBean> garbageCollectorMXBeans;
    private List<MemoryManagerMXBean> memoryManagerMXBeans;
    private List<MemoryPoolMXBean> memoryPoolMXBeans;

    public HeapMemoryCollector() {
        memoryMXBean = ManagementFactory.getMemoryMXBean();
        garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        memoryManagerMXBeans = ManagementFactory.getMemoryManagerMXBeans();
        memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
    }

    public MemoryMXBean getMemoryMXBean() {
        return memoryMXBean;
    }

    public List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
        return garbageCollectorMXBeans;
    }

    public List<MemoryManagerMXBean> getMemoryManagerMXBeans() {
        return memoryManagerMXBeans;
    }

    public List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
        return memoryPoolMXBeans;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
