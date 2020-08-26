package logger;

import java.io.File;
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
            fileHandler = new FileHandler("D:\\logfile\\agentLog\\agent.log", true);
            logger = Logger.getLogger(LoggingController.class.getName());
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setEncoding("UTF-8");
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
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
}
