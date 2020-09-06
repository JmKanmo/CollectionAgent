package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ErrorLoggingController {
    private static Logger errorLogger;
    private static FileHandler errorFileHandler;

    static {
        try {
            /*
            [ Error Logging ]
            window: D:\OJT_projects\logfile\agentLog\error.log
            linux: /home/junmokang/scriptBox/agentlog/error.log
            * */
            errorFileHandler = new FileHandler("D:\\OJT_projects\\logfile\\agentLog\\error.log", true);
            errorLogger = Logger.getLogger(ErrorLoggingController.class.getName());
            errorFileHandler.setFormatter(new SimpleFormatter());
            errorFileHandler.setEncoding("UTF-8");
            errorLogger.addHandler(errorFileHandler);
        } catch (IOException e) {
            errorLogging(e);
        }
    }

    public static void errorLogging(Exception e) {
        StackTraceElement[] stacktrace = e.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ERROR:" + e.getMessage() + "\n");

        for (int i = 0; i < 10 && i < stacktrace.length; i++) {
            stringBuilder.append("Index " + i)
                    .append(" of stack trace")
                    .append(", array conatins = ")
                    .append(stacktrace[i].toString())
                    .append("\n");
        }
        errorLogger.log(Level.WARNING, stringBuilder.toString());
    }
}
