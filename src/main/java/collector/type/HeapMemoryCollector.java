package collector.type;

import logger.LoggingController;

import java.lang.management.*;
import java.util.List;
import java.util.logging.Level;

public class HeapMemoryCollector implements Runnable {
    private MemoryMXBean memoryMXBean;
    private List<GarbageCollectorMXBean> garbageCollectorMXBeans;
    private List<MemoryPoolMXBean> memoryPoolMXBeans;
    private LoggingController loggingController;
    private StringBuilder stringBuilder;

    public HeapMemoryCollector() {
        stringBuilder = new StringBuilder();
        memoryMXBean = ManagementFactory.getMemoryMXBean();
        garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        loggingController = new LoggingController("D:\\logfile\\agentLog\\heapLog\\info.log");
    }

    public void printMemory() {
        MemoryUsage memoryUsage = this.memoryMXBean.getHeapMemoryUsage();
        MemoryUsage memoryUnUsage = this.memoryMXBean.getNonHeapMemoryUsage();

        stringBuilder.append("[HeapMemory]\n");
        stringBuilder.append(" Init: " + memoryUsage.getInit());
        stringBuilder.append(" Used: " + memoryUsage.getUsed());
        stringBuilder.append(" Commited: " + memoryUsage.getCommitted());
        stringBuilder.append(" Max: " + memoryUsage.getMax());

        stringBuilder.append("\n[NonHeapMemory]\n");
        stringBuilder.append(" Init: " + memoryUsage.getInit());
        stringBuilder.append(" Used: " + memoryUsage.getUsed());
        stringBuilder.append(" Commited: " + memoryUsage.getCommitted());
        stringBuilder.append(" Max: " + memoryUsage.getMax());

        loggingController.logging(Level.INFO, stringBuilder.toString());
        stringBuilder.setLength(0);
    }

    public void printMemoryPool() {
        stringBuilder.append("[Heap Memory Pool ]");

        for (MemoryPoolMXBean memoryPoolMXBean : this.memoryPoolMXBeans) {
            stringBuilder.append("\nName: " + memoryPoolMXBean.getName());
            stringBuilder.append("\nUsage: " + memoryPoolMXBean.getUsage());
            stringBuilder.append("\nCollection Usage: " + memoryPoolMXBean.getCollectionUsage());
            stringBuilder.append("\nType: " + memoryPoolMXBean.getType());
            stringBuilder.append("\nPeak Usage: " + memoryPoolMXBean.getPeakUsage());
            stringBuilder.append("\nUsage Threshold: " + memoryPoolMXBean.getUsageThreshold());
            stringBuilder.append("\nUsage ThresholdCount: " + memoryPoolMXBean.getUsageThresholdCount());

            stringBuilder.append("\n[Memory ManagerNames]\n");
            String[] memoryManagerNames = memoryPoolMXBean.getMemoryManagerNames();

            for (String managerName : memoryManagerNames) {
                stringBuilder.append("\nmanagerName: " + managerName);
            }
        }
        loggingController.logging(Level.INFO, stringBuilder.toString());
        stringBuilder.setLength(0);
    }

    public void printGarbageCollector() {
        stringBuilder.append("[Garbage Collector]");

        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
            stringBuilder.append("\nName: " + garbageCollectorMXBean.getName());
            stringBuilder.append("\nCollection Count: " + garbageCollectorMXBean.getCollectionCount());
            stringBuilder.append("\nCollection Time: " + garbageCollectorMXBean.getCollectionTime());
            stringBuilder.append("\nMemory Pools: ");

            String[] memoryPools = garbageCollectorMXBean.getMemoryPoolNames();

            for (String memoryPool : memoryPools) {
                stringBuilder.append("\nName: " + memoryPool);
            }
            stringBuilder.append("\n");
        }
        loggingController.logging(Level.INFO, stringBuilder.toString());
        stringBuilder.setLength(0);
    }

    @Override
    public void run() {
        while (true) {
            try {
                printMemory();
                printMemoryPool();
                printGarbageCollector();
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void setMemoryMXBean(MemoryMXBean memoryMXBean) {
        this.memoryMXBean = memoryMXBean;
    }

    public void setGarbageCollectorMXBeans(List<GarbageCollectorMXBean> garbageCollectorMXBeans) {
        this.garbageCollectorMXBeans = garbageCollectorMXBeans;
    }

    public void setMemoryPoolMXBeans(List<MemoryPoolMXBean> memoryPoolMXBeans) {
        this.memoryPoolMXBeans = memoryPoolMXBeans;
    }

    public void setLoggingController(LoggingController loggingController) {
        this.loggingController = loggingController;
    }

    public MemoryMXBean getMemoryMXBean() {
        return memoryMXBean;
    }

    public List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
        return garbageCollectorMXBeans;
    }

    public List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
        return memoryPoolMXBeans;
    }

    public LoggingController getLoggingController() {
        return loggingController;
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }
}
