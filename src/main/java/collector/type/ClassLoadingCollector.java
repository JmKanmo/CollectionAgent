package collector.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logger.LoggingController;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ClassLoadingCollector implements Runnable {
    private ClassLoadingMXBean classLoadingMXBean;
    private LoggingController loggingController;
    private Map<String, Object> hashMap;
    private Gson gson;

    public ClassLoadingCollector() {
        classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        loggingController = new LoggingController("D:\\logfile\\agentLog\\class\\info.log");
        hashMap = new HashMap<>();
        gson = new GsonBuilder().create();
    }

    public void printClassLoadingInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("loadingClassCount", classLoadingMXBean.getLoadedClassCount());
        map.put("totalLoadedClassCount", classLoadingMXBean.getTotalLoadedClassCount());
        map.put("unloadedClassCount", classLoadingMXBean.getUnloadedClassCount());
        hashMap.put("classLoadingInfo", map);
        loggingController.logging(Level.INFO, map.toString());
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

    public ClassLoadingMXBean getClassLoadingMXBean() {
        return classLoadingMXBean;
    }

    public void setClassLoadingMXBean(ClassLoadingMXBean classLoadingMXBean) {
        this.classLoadingMXBean = classLoadingMXBean;
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
