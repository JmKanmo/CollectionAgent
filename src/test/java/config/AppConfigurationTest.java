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
            String username = AppConfiguration.getInstance().getConfiguration("job");
            String password = AppConfiguration.getInstance().getConfiguration("favorites");
            assertEquals(username, "exem");
            assertEquals(password, "programming");
            String dbName = AppConfiguration.getInstance().getConfigurationWithDefaultValue("dbname", "postgresl");
            assertEquals(dbName, "postgresl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}