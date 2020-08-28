package collector.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logger.LoggingController;
import socket.SocketController;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ClassLoadingCollector extends Thread {
    private ClassLoadingMXBean classLoadingMXBean;
    private Map<String, Object> hashMap;
    private Gson gson;
    private SocketController socketController;

    public ClassLoadingCollector() {
    }

    public ClassLoadingCollector(ThreadGroup threadGroup, String threadName) {
        super(threadGroup, threadName);
        classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        hashMap = new HashMap<>();
        gson = new GsonBuilder().create();
        socketController = new SocketController();
    }

    public void collectClassLoadingInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("loadingClassCount", classLoadingMXBean.getLoadedClassCount());
        map.put("totalLoadedClassCount", classLoadingMXBean.getTotalLoadedClassCount());
        map.put("unloadedClassCount", classLoadingMXBean.getUnloadedClassCount());
        hashMap.put("classLoadingInfo", map);
    }

    public void printClassLoadingInfo() {
        String jsonStr = gson.toJson(hashMap);
        LoggingController.logging(Level.INFO, jsonStr);
        socketController.sendData(getName() + "&" + jsonStr);
    }

    @Override
    public void run() {
        while (true) {
            try {
                collectClassLoadingInfo();
                printClassLoadingInfo();
                Thread.sleep(10000);
            } catch (Exception e) {
                LoggingController.errorLogging(e);
                break;
            }
        }
    }
}
