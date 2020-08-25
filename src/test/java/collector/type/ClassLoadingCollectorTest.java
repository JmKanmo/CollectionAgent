package collector.type;

import logger.LoggingController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.management.ClassLoadingMXBean;
import java.util.Map;

import static org.junit.Assert.*;

public class ClassLoadingCollectorTest {
    @InjectMocks
    ClassLoadingCollector classLoadingCollector;

    @Mock
    ClassLoadingMXBean classLoadingMXBean;

    @Mock
    LoggingController loggingController;

    @Mock
    Map<String, Object> hashMap;

    @Test
    public void testThread() {
        Thread thread = new Thread(new ClassLoadingCollector());
        thread.start();
        assertEquals(thread.getState(), Thread.State.RUNNABLE);
        thread.interrupt();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    public void testConstructor() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(classLoadingCollector);
        assertNotNull(classLoadingMXBean);
        assertNotNull(hashMap);
        assertNotNull(loggingController);
    }

    @Test
    public void testPrintClassLoadingInfo() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(hashMap.put(Mockito.anyString(), Mockito.any())).thenReturn(null);
        Mockito.doNothing().when(loggingController).logging(Mockito.any(), Mockito.anyString());
        classLoadingCollector.printClassLoadingInfo();
        Mockito.verify(hashMap, Mockito.times(1)).put(Mockito.anyString(), Mockito.any());
        Mockito.verify(loggingController, Mockito.times(1)).logging(Mockito.any(), Mockito.anyString());
    }
}