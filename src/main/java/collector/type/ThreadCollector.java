package collector.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logger.LoggingController;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ThreadCollector implements Runnable {
    private ThreadMXBean threadMXBean;
    private LoggingController loggingController;
    private Map<String, Object> hashMap;
    private Gson gson;

    public ThreadCollector() {
        threadMXBean = ManagementFactory.getThreadMXBean();
        loggingController = new LoggingController("D:\\logfile\\agentLog\\threadlog\\info.log");
        hashMap = new HashMap<>();
        gson = new GsonBuilder().create();
    }

    public void printInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("DaemonThreadCount", threadMXBean.getDaemonThreadCount());
        map.put("PeakThreadCount", threadMXBean.getPeakThreadCount());
        map.put("TotalStartedThreadCount", threadMXBean.getTotalStartedThreadCount());
        loggingController.logging(Level.INFO, map.toString());
        hashMap.put("overallInfo", map);
    }

    public void printDeadLockThreads() {
        long[] threadIds = threadMXBean.findDeadlockedThreads();
        printThreads("deadLockThread", threadIds);
    }

    public void printAllThreads() {
        long[] threadIds = threadMXBean.getAllThreadIds();
        printThreads("allThread", threadIds);
    }

    public void printThreads(String name, long[] ids) {
        if (ids == null || ids.length <= 0) {
            return;
        }
        List<Map<String, Object>> threadMapList = new ArrayList<>();

        for (long id : ids) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(id);

            if (threadInfo != null) {
                Map<String, Object> threadMap = new HashMap<>();
                threadMap.put("id", threadInfo.getThreadId());
                threadMap.put("name", threadInfo.getThreadName());
                threadMap.put("state", threadInfo.getThreadState());
                threadMap.put("waitCount", threadInfo.getWaitedCount());
                threadMap.put("lockName", threadInfo.getLockName());
                threadMapList.add(threadMap);
            }
        }
        hashMap.put(name, threadMapList);
        loggingController.logging(Level.INFO, threadMapList.toString());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public ThreadMXBean getThreadMXBean() {
        return threadMXBean;
    }

    public void setThreadMXBean(ThreadMXBean threadMXBean) {
        this.threadMXBean = threadMXBean;
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
