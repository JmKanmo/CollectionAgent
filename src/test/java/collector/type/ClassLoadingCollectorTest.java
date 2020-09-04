package collector.type;

import logger.LoggingController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;

import java.lang.management.ClassLoadingMXBean;
import java.util.Map;
import java.util.logging.Level;

import static org.junit.Assert.*;

public class ClassLoadingCollectorTest {
    @InjectMocks
    ClassLoadingCollector classLoadingCollector = new ClassLoadingCollector(null, "Collector");

    @Mock
    ClassLoadingMXBean classLoadingMXBean;

    @Mock
    Map<String, Object> hashMap;

    @Test
    public void testInitiation() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(classLoadingCollector);
        assertNotNull(classLoadingMXBean);
        assertNotNull(hashMap);
        assertEquals(classLoadingCollector.getName(), "Collector");
    }

    @Test
    public void testCollectClassLoadingInfo() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(hashMap.put(Mockito.anyString(), Mockito.any())).thenReturn(null);
        classLoadingCollector.collectClassLoadingInfo();
        Mockito.verify(hashMap, Mockito.times(1)).put(Mockito.anyString(), Mockito.any());
    }
}