package config;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigChangeListenerTest {
    @Test
    public void testStartStop() throws InterruptedException {
        Thread thread = new Thread(new ConfigChangeListener());
        thread.start();
        Thread.sleep(100);
    }
}