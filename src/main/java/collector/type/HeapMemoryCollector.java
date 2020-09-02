package collector.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.AppConfiguration;
import logger.LoggingController;
import socket.SocketController;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class HeapMemoryCollector extends Thread {
    private MemoryMXBean memoryMXBean;
    private List<GarbageCollectorMXBean> garbageCollectorMXBeans;
    private Map<String, Object> hashMap;
    private Gson gson;
    private SocketController socketController;
    private AppConfiguration appConfiguration;

    public HeapMemoryCollector() {
    }

    public HeapMemoryCollector(ThreadGroup threadGroup, String threadName) {
        super(threadGroup, threadName);
        memoryMXBean = ManagementFactory.getMemoryMXBean();
        garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        hashMap = new HashMap<>();
        gson = new GsonBuilder().create();
        socketController = new SocketController();
        appConfiguration = AppConfiguration.getInstance();
    }

    public void collectMemoryUsage() {
        MemoryUsage memoryUsage = this.memoryMXBean.getHeapMemoryUsage();
        MemoryUsage memoryUnUsage = this.memoryMXBean.getNonHeapMemoryUsage();

        Map<String, Object> heapMemoryMap = new HashMap<>();
        Map<String, Object> nonHeapMemoryMap = new HashMap<>();

        heapMemoryMap.put("init", memoryUsage.getInit());
        heapMemoryMap.put("used", memoryUsage.getUsed());
        heapMemoryMap.put("commited", memoryUsage.getCommitted());
        heapMemoryMap.put("max", memoryUsage.getMax());

        nonHeapMemoryMap.put("init", memoryUnUsage.getInit());
        nonHeapMemoryMap.put("used", memoryUnUsage.getUsed());
        nonHeapMemoryMap.put("commited", memoryUnUsage.getCommitted());
        nonHeapMemoryMap.put("max", memoryUnUsage.getMax());

        hashMap.put("heapMemory", heapMemoryMap);
        hashMap.put("nonHeapMemory", nonHeapMemoryMap);
    }

    public void collectGarbageCollection() {
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
    }

    public void printInfo() {
        String jsonStr = gson.toJson(hashMap);
        LoggingController.logging(Level.INFO, jsonStr);
        socketController.sendData(getName() + "&" + jsonStr);
    }

    @Override
    public void run() {
        while (true) {
            try {
                collectMemoryUsage();
                collectGarbageCollection();
                printInfo();
                Thread.sleep(Long.parseLong(appConfiguration.getConfiguration("collectSleepTime")));
            } catch (Exception e) {
                LoggingController.errorLogging(e);
                break;
            }
        }
    }

    public void setGarbageCollectorMXBeans(List<GarbageCollectorMXBean> garbageCollectorMXBeans) {
        this.garbageCollectorMXBeans = garbageCollectorMXBeans;
    }
}
