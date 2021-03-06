package config;

import logger.ErrorLoggingController;
import logger.LoggingController;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class ConfigChangeListener implements Runnable {
    private String configFileName;
    /*
    window: D:/OJT_projects/watch/config.properties
    linux: /home/junmokang/scriptBox/resources/config.properties
    * */
    private String fullFilePath = "D:/OJT_projects/watch/config.properties";

    @Override
    public void run() {
        try {
            register(fullFilePath);
        } catch (IOException e) {
            ErrorLoggingController.errorLogging(e);
        }
    }

    private void register(String filePath) throws IOException {
        final int lastIndex = filePath.lastIndexOf("/");
        String dirPath = filePath.substring(0, lastIndex + 1);
        String fileName = filePath.substring(lastIndex + 1, filePath.length());
        this.configFileName = fileName;

        configurationChanged(filePath);
        startWatcher(dirPath, fileName);
    }

    private void startWatcher(String dirPath, String fileName) throws IOException {
        final WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(dirPath);

        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        while (true) {
            try {
                WatchKey watchKey = watchService.take();
                List<WatchEvent<?>> watchEventList = watchKey.pollEvents();

                for (WatchEvent<?> event : watchEventList) {
                    String configFilename = event.context().toString();

                    if (configFilename.equals(this.configFileName)) {
                        configurationChanged(dirPath + fileName);
                    }
                }
                boolean reset = watchKey.reset();

                if (!reset) {
                    break;
                }
            } catch (Exception e) {
                ErrorLoggingController.errorLogging(e);
            }
        }
        watchService.close();
    }

    public void configurationChanged(final String file) throws IOException {
        AppConfiguration.getInstance().initialize(file);
    }
}
