package collector.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logger.LoggingController;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class RunTimeCollector extends Thread {
    private RuntimeMXBean runtimeMXBean;
    private LoggingController loggingController;
    private LoggingController errorLoggingController;
    private Map<String, Object> hashMap;
    private Gson gson;

    public RunTimeCollector() {
    }

    public RunTimeCollector(ThreadGroup threadGroup, String threadName) {
        super(threadGroup, threadName);
        runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        loggingController = new LoggingController("D:\\logfile\\agentLog\\runtimelog\\runTimeCollectorInfo.log");
        errorLoggingController = new LoggingController("D:\\logfile\\agentLog\\runtimelog\\runTimeCollectorError.log");
        hashMap = new HashMap<>();
        gson = new GsonBuilder().create();
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
        loggingController.logging(Level.INFO, jsonStr);
    }

    @Override
    public void run() {
        while (true) {
            try {
                collectRuntimeInfo();
                printInfo();
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
                errorLoggingController.logging(Level.WARNING, e.toString() + " " + e.getStackTrace().toString());
                break;
            }
        }
    }
}
