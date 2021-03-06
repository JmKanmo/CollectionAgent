package collector.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.AppConfiguration;
import logger.ErrorLoggingController;
import logger.LoggingController;
import socket.SocketController;

import java.io.IOException;
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
    private AppConfiguration appConfiguration;

    public ClassLoadingCollector() {
    }

    public ClassLoadingCollector(ThreadGroup threadGroup, String threadName) {
        super(threadGroup, threadName);
        classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        hashMap = new HashMap<>();
        gson = new GsonBuilder().create();
        socketController = new SocketController();
        appConfiguration = AppConfiguration.getInstance();
    }

    public void collectClassLoadingInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("loadingClassCount", classLoadingMXBean.getLoadedClassCount());
        map.put("totalLoadedClassCount", classLoadingMXBean.getTotalLoadedClassCount());
        map.put("unloadedClassCount", classLoadingMXBean.getUnloadedClassCount());
        hashMap.put("classLoadingInfo", map);
    }

    public void printClassLoadingInfo() throws IOException {
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
            } catch (Exception e) {
                ErrorLoggingController.errorLogging(e);
            } finally {
                try {
                    Thread.sleep(Long.parseLong(appConfiguration.getConfiguration("collectSleepTime")));
                } catch (InterruptedException e) {
                    ErrorLoggingController.errorLogging(e);
                }
            }
        }
    }
}
