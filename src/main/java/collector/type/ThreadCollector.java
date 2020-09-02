package collector.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.AppConfiguration;
import logger.LoggingController;
import socket.SocketController;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ThreadCollector extends Thread {
    private ThreadMXBean threadMXBean;
    private Map<String, Object> hashMap;
    private Gson gson;
    private SocketController socketController;
    private AppConfiguration appConfiguration;

    public ThreadCollector() {
    }

    public ThreadCollector(ThreadGroup threadGroup, String threadName) {
        super(threadGroup, threadName);
        threadMXBean = ManagementFactory.getThreadMXBean();
        hashMap = new HashMap<>();
        gson = new GsonBuilder().create();
        socketController = new SocketController();
        appConfiguration = AppConfiguration.getInstance();
    }

    public void collectOverallInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("DaemonThreadCount", threadMXBean.getDaemonThreadCount());
        map.put("PeakThreadCount", threadMXBean.getPeakThreadCount());
        map.put("TotalStartedThreadCount", threadMXBean.getTotalStartedThreadCount());
        hashMap.put("overallInfo", map);
    }

    public void collectDeadLockThreads() {
        long[] threadIds = threadMXBean.findDeadlockedThreads();
        collectThreadInfo("deadLockThread", threadIds);
    }

    public void collectAllThreads() {
        long[] threadIds = threadMXBean.getAllThreadIds();
        collectThreadInfo("allThread", threadIds);
    }

    public void collectThreadInfo(String name, long[] ids) {
        List<Map<String, Object>> threadMapList = new ArrayList<>();
        if (ids == null) return;

        for (long id : ids) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(id);
            Map<String, Object> threadMap = new HashMap<>();
            threadMap.put("id", threadInfo.getThreadId());
            threadMap.put("name", threadInfo.getThreadName());
            threadMap.put("state", threadInfo.getThreadState());
            threadMap.put("waitCount", threadInfo.getWaitedCount());
            threadMap.put("lockName", threadInfo.getLockName());
            threadMapList.add(threadMap);
        }
        hashMap.put(name, threadMapList);
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
                collectOverallInfo();
                collectDeadLockThreads();
                collectAllThreads();
                printInfo();
                Thread.sleep(Long.parseLong(appConfiguration.getConfiguration("collectSleepTime")));
            } catch (Exception e) {
                LoggingController.errorLogging(e);
                break;
            }
        }
    }
}
