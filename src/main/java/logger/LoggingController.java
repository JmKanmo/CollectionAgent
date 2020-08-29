package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingController {
    private static Logger logger;
    private static FileHandler fileHandler;

    static {
        try {
            /*
            window: D:\logfile\agentLog\agent.log
            linux: /home/junmokang/scriptBox/agentlog/agent.log
            * */
            fileHandler = new FileHandler("D:\\logfile\\agentLog\\agent.log", true);
            logger = Logger.getLogger(LoggingController.class.getName());
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setEncoding("UTF-8");
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            errorLogging(e);
        }
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public static void logging(Level level, String msg) {
        logger.log(level, msg + "\n");
    }

    public static void errorLogging(Exception e) {
        e.printStackTrace();
        StackTraceElement[] stacktrace = e.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ERROR:" + e.toString() + "\n");

        for (int i = 0; i < stacktrace.length; i++) {
            stringBuilder.append("Index " + i
                    + " of stack trace"
                    + ", array conatins = "
                    + stacktrace[i].toString() + "\n");
        }
        LoggingController.logging(Level.WARNING, stringBuilder.toString());
    }
}
