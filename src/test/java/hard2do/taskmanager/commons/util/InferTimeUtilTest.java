package hard2do.taskmanager.commons.util;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class InferTimeUtilTest {
    
    @Test
    public void testInferTime_noTime_falseExpected() {
        String noTimeString = "there is no time here";
        InferTimeUtil itu = new InferTimeUtil(noTimeString);
        assertFalse(itu.findTime());
    }
    
    @Test
    public void testInferTime_atTime_matchesStringExpected() {
        String atTimeString = "there is time here at 1am";
        InferTimeUtil itu = new InferTimeUtil(atTimeString);
        assertTrue(itu.findTime());
        
        
    }

}
