package main;

import collector.CollectionController;
import config.ConfigChangeListener;

import java.lang.instrument.Instrumentation;

public class Main {
    public static void premain(String args, Instrumentation instrumentation) {
        CollectionController collectionController = new CollectionController();
        collectionController.setDaemon(true);
        collectionController.start();

        Thread configListenerThread = new Thread(new ConfigChangeListener());
        configListenerThread.setDaemon(true);
        configListenerThread.start();
    }
}
