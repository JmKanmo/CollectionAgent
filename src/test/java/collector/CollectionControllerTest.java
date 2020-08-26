package collector;

import junit.framework.TestCase;
import org.junit.Test;

public class CollectionControllerTest extends TestCase {
    @Test
    public void testCollectionController() {
        CollectionController collectionController = new CollectionController();
        collectionController.start();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ThreadGroup threadGroup = collectionController.retThreadGroup();
        assertEquals(threadGroup.activeCount(), 3);
        threadGroup.interrupt();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(threadGroup.activeCount(), 0);
    }
}