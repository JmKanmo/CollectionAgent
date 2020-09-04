package config;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AppConfigurationTest {
    @Test
    public void testInitialize() {
        boolean ret = false;
        try {
            AppConfiguration.getInstance().initialize("D:/watch/config.properties");
            ret = true;
        } catch (IOException e) {
            e.printStackTrace();
            ret = false;
        } finally {
            assertEquals(ret, true);
        }
    }

    @Test
    public void testGetConfiguration() {
        try {
            AppConfiguration.getInstance().initialize("D:/watch/config.properties");
            String collectSleepTime = AppConfiguration.getInstance().getConfiguration("collectSleepTime");
            assertEquals(collectSleepTime,"30000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}