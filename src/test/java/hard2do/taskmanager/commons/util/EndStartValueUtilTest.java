package hard2do.taskmanager.commons.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import hard2do.taskmanager.commons.exceptions.IllegalValueException;

//@@author A0139523E
public class EndStartValueUtilTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
	@Test
	public void testEndStartTime_sameTime_errorMessageExpected() throws IllegalValueException {
		thrown.expect(IllegalValueException.class);
		thrown.expectMessage(EndStartValuesUtil.MESSAGE_SAME_TIME_CONSTRAINT);
		EndStartValuesUtil.timeRangeValid("13:00", "13:00");
	}	
	
	@Test
	public void testEndStartTime_invalidFormat_errorMessageExpected() throws IllegalValueException {
		thrown.expect(IllegalValueException.class);
		thrown.expectMessage(EndStartValuesUtil.MESSAGE_TIME_FORMAT);
		EndStartValuesUtil.timeRangeValid("13-00", "16:00");
	}
	
	@Test
	public void testEndStartTime_invalidTime_errorMessageExpected() throws IllegalValueException {
		thrown.expect(IllegalValueException.class);
		thrown.expectMessage(EndStartValuesUtil.MESSAGE_ENDTIME_CONSTRAINT);
		EndStartValuesUtil.timeRangeValid("15:00", "12:00");
		
		thrown.expect(IllegalValueException.class);
		thrown.expectMessage(EndStartValuesUtil.MESSAGE_ENDTIME_CONSTRAINT);
		EndStartValuesUtil.timeRangeValid("16:50", "16:40");
		}
}
