package collector;

import collector.type.ClassLoadingCollector;
import collector.type.HeapMemoryCollector;
import collector.type.RunTimeCollector;
import collector.type.ThreadCollector;
import logger.ErrorLoggingController;
import logger.LoggingController;


public class CollectionController extends Thread {
    private ThreadGroup threadGroup;

    public CollectionController() {
        try {
            threadGroup = new ThreadGroup("collector-threads");
        } catch (Exception e) {
            ErrorLoggingController.errorLogging(e);
        }
    }

    public ThreadGroup retThreadGroup() {
        return threadGroup;
    }

    @Override
    public void run() {
        ClassLoadingCollector classLoadingCollector = new ClassLoadingCollector(threadGroup, "classLoadingCollector");
        HeapMemoryCollector heapMemoryCollector = new HeapMemoryCollector(threadGroup, "heapMemoryCollector");
        RunTimeCollector runTimeCollector = new RunTimeCollector(threadGroup, "runTimeCollector");
        ThreadCollector threadCollector = new ThreadCollector(threadGroup, "threadCollector");

        classLoadingCollector.start();
        heapMemoryCollector.start();
        runTimeCollector.start();
        threadCollector.start();
    }
}
