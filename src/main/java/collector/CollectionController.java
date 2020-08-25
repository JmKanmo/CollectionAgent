package collector;

import collector.type.ClassLoadingCollector;
import collector.type.HeapMemoryCollector;
import collector.type.RunTimeCollector;
import collector.type.ThreadCollector;
import logger.LoggingController;

import java.util.logging.Level;

public class CollectionController {
    private ThreadGroup threadGroup;
    private LoggingController errorLoggingController;

    public CollectionController() {
        try {
            threadGroup = new ThreadGroup("collector-threads");
            errorLoggingController = new LoggingController("D:\\logfile\\agentLog\\controller\\collectionControllerError.log");
        } catch (Exception e) {
            errorLoggingController.logging(Level.WARNING, e.toString());
        }
    }

    public void start() {
        ClassLoadingCollector classLoadingCollector = new ClassLoadingCollector(threadGroup, "classLoadingCollector");
        HeapMemoryCollector heapMemoryCollector = new HeapMemoryCollector(threadGroup, "heapMemoryCollector");
        RunTimeCollector runTimeCollector = new RunTimeCollector(threadGroup, "runTimeCollector");
        ThreadCollector threadCollector = new ThreadCollector(threadGroup, "threadCollector");

        classLoadingCollector.start();
        heapMemoryCollector.start();
        runTimeCollector.start();
        threadCollector.start();
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}
