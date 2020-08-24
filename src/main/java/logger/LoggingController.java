package logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingController {
    private Logger logger;
    private FileHandler fileHandler;

    public LoggingController(String path) {
        try {
            fileHandler = new FileHandler(path, true);
            logger = Logger.getLogger(LoggingController.class.getName());
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setEncoding("UTF-8");
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LoggingController(Logger logger, FileHandler fileHandler) {
        this.logger = logger;
        this.fileHandler = fileHandler;
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

    public void logging(Level level, String msg) {
        logger.log(level, msg);
    }
}
