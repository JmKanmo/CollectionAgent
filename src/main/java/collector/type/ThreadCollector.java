package collector.type;

import logger.LoggingController;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ThreadCollector implements Runnable {
    private ThreadMXBean threadMXBean;
    private LoggingController loggingController;
    private StringBuilder stringBuilder;

    public ThreadCollector() {
        threadMXBean = ManagementFactory.getThreadMXBean();
        loggingController = new LoggingController("D:\\logfile\\agentLog\\threadlog\\info.log");
        stringBuilder = new StringBuilder();
    }

    public void printThreadInfo() {
        long[] threadIds = threadMXBean.getAllThreadIds();

        for (long id : threadIds) {
            threadMXBean.getThreadInfo(id);
            // 코드 마저 작성 
        }
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

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public void setStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }
}
