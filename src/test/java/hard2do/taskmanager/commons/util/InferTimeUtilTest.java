package hard2do.taskmanager.commons.util;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class InferTimeUtilTest {
    
    @Test
    public void testInferTime_noTime_falseExpected() {
        String noTimeString = "there is no time here";
        InferTimeUtil idu = new InferTimeUtil(noTimeString);
        assertFalse(idu.findTime());
    }
    
    @Test
    public void testInferTime_fromTime_matchesStringExpected() {
        String fromTimeString = "there is time here from 1am to 2pm";
        InferTimeUtil idu = new InferTimeUtil(fromTimeString);
        assertTrue(idu.findTime());
    }

}
