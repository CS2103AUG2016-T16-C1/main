package hard2do.taskmanager.commons.util;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


import java.text.SimpleDateFormat;
import java.util.Calendar;

//@@author A0141054W
public class InferDateUtilTest {
    private Calendar calendar;
    private String inferredDate;
    private final SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");

    @Test
    public void testInferDate_noDate_falseExpected() {
        String noDateString = "there is no date here";
        InferDateUtil idu = new InferDateUtil(noDateString);
        assertFalse(idu.findDate());
    }
    
    @Test
    public void testInferDate_tmr_correctInferDateExpected() {
        String tmrDateString = "there is date which is tmr";
        InferDateUtil idu = new InferDateUtil(tmrDateString);
        assertTrue(idu.findDate());
        calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        inferredDate = sdfDate.format(calendar.getTime());
        
        assertEquals(inferredDate, idu.getDate());
    }
    
    @Test
    public void testInferDate_nextWeek_correctInferDateExpected() {
        String nextWeekDateString = "there is date which is next week";
        InferDateUtil idu = new InferDateUtil(nextWeekDateString);
        assertTrue(idu.findDate());
        calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        inferredDate = sdfDate.format(calendar.getTime());
        
        assertEquals(inferredDate, idu.getDate());
    }  
}
