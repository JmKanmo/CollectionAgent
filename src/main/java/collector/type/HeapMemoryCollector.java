package collector.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logger.LoggingController;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class HeapMemoryCollector implements Runnable {
    private MemoryMXBean memoryMXBean;
    private List<GarbageCollectorMXBean> garbageCollectorMXBeans;
    private LoggingController loggingController;
    private Map<String, Object> hashMap;
    private Gson gson;

    public HeapMemoryCollector() {
        memoryMXBean = ManagementFactory.getMemoryMXBean();
        garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        loggingController = new LoggingController("D:\\logfile\\agentLog\\heapLog\\info.log");
        hashMap = new HashMap<>();
        gson = new GsonBuilder().create();
    }

    public void printMemory() {
        MemoryUsage memoryUsage = this.memoryMXBean.getHeapMemoryUsage();
        MemoryUsage memoryUnUsage = this.memoryMXBean.getNonHeapMemoryUsage();

        Map<String, Object> heapMemoryMap = new HashMap<>();

        heapMemoryMap.put("init", memoryUsage.getInit());
        heapMemoryMap.put("used", memoryUsage.getUsed());
        heapMemoryMap.put("commited", memoryUsage.getCommitted());
        heapMemoryMap.put("max", memoryUsage.getMax());

        Map<String, Object> nonHeapMemoryMap = new HashMap<>();

        nonHeapMemoryMap.put("init", memoryUnUsage.getInit());
        nonHeapMemoryMap.put("used", memoryUnUsage.getUsed());
        nonHeapMemoryMap.put("commited", memoryUnUsage.getCommitted());
        nonHeapMemoryMap.put("max", memoryUnUsage.getMax());

        hashMap.put("heapMemory", heapMemoryMap);
        hashMap.put("nonHeapMemory", nonHeapMemoryMap);

        loggingController.logging(Level.INFO, heapMemoryMap.toString());
        loggingController.logging(Level.INFO, nonHeapMemoryMap.toString());
    }

    public void printGarbageCollector() {
        List<Map<String, Object>> gcMapList = new ArrayList<>();

        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
            Map<String, Object> gcMap = new HashMap<>();
            gcMap.put("name", garbageCollectorMXBean.getName());
            gcMap.put("collectionCount", garbageCollectorMXBean.getCollectionCount());
            gcMap.put("collectionTime", garbageCollectorMXBean.getCollectionTime());
            String[] memoryPools = garbageCollectorMXBean.getMemoryPoolNames();
            gcMap.put("memoryPools", memoryPools);
            gcMapList.add(gcMap);
        }
        hashMap.put("garbageCollection", gcMapList);
        loggingController.logging(Level.INFO, gcMapList.toString());
    }

    @Override
    public void run() {
        while (true) {
            try {
                printMemory();
                printGarbageCollector();
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public MemoryMXBean getMemoryMXBean() {
        return memoryMXBean;
    }

    public void setMemoryMXBean(MemoryMXBean memoryMXBean) {
        this.memoryMXBean = memoryMXBean;
    }

    public List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
        return garbageCollectorMXBeans;
    }

    public void setGarbageCollectorMXBeans(List<GarbageCollectorMXBean> garbageCollectorMXBeans) {
        this.garbageCollectorMXBeans = garbageCollectorMXBeans;
    }

    public LoggingController getLoggingController() {
        return loggingController;
    }

    public void setLoggingController(LoggingController loggingController) {
        this.loggingController = loggingController;
    }

    public Map<String, Object> getHashMap() {
        return hashMap;
    }

    public void setHashMap(Map<String, Object> hashMap) {
        this.hashMap = hashMap;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
