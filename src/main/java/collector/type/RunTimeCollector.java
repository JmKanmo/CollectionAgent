package collector.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.AppConfiguration;
import logger.LoggingController;
import socket.SocketController;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class RunTimeCollector extends Thread {
    private RuntimeMXBean runtimeMXBean;
    private Map<String, Object> hashMap;
    private Gson gson;
    private SocketController socketController;
    private AppConfiguration appConfiguration;

    public RunTimeCollector() {
    }

    public RunTimeCollector(ThreadGroup threadGroup, String threadName) {
        super(threadGroup, threadName);
        runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        hashMap = new HashMap<>();
        gson = new GsonBuilder().create();
        socketController = new SocketController();
        appConfiguration = AppConfiguration.getInstance();
    }

    public void collectRuntimeInfo() {
        Map<String, Object> runTimeMap = new HashMap<>();
        runTimeMap.put("startTime", runtimeMXBean.getStartTime());
        runTimeMap.put("upTime", runtimeMXBean.getUptime());
        runTimeMap.put("vmversion", runtimeMXBean.getVmVersion());
        runTimeMap.put("vmName", runtimeMXBean.getVmName());
        runTimeMap.put("vmVendor", runtimeMXBean.getVmVendor());
        runTimeMap.put("name", runtimeMXBean.getName());
        hashMap.put("runtimeInfo", runTimeMap);
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
                collectRuntimeInfo();
                printInfo();
                Thread.sleep(Long.parseLong(appConfiguration.getConfiguration("collectSleepTime")));
            } catch (Exception e) {
                LoggingController.errorLogging(e);
                break;
            }
        }
    }
}
