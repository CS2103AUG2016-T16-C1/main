package seedu.address.commons.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Infers time from content string if added Task does not specify any start date 
*/
//@@author A0135787N
public class InferTimeUtil {
	
	private String contentToInfer;
	private Calendar calendar;
	private String inferredTime;
	private Date timeNow;
	private final SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");
	private static final Pattern TIME_FORMAT =
    		Pattern.compile("at (?<time>[0-9]+)(?<meridiem>[amp]+)?");
	
	private static final Pattern START_END_TIME_FORMAT =
			Pattern.compile("from (?<startTime>[0-9]+)(?<smeridiem>[amp]+)? to "
					+ "(?<endTime>[0-9]+)(?<emeridiem>[amp]+)?");
	
	/**
	 * Constructor that takes in content to infer.
	 * 
	 * @param content
	 */
	
	public InferTimeUtil(String content){
		assert content != null;
		
	    contentToInfer = content;
	    calendar = Calendar.getInstance();
	    timeNow = new Date();
	    
	    
	}
	
	/**
	 * Default Constructor
	 * 
	 */
	
	public InferTimeUtil(){
	    calendar = Calendar.getInstance();
	    timeNow = new Date();

	}
	
	/**
	 * finds a possible time that is implied within the content and stores it.
	 * 
	 * @return true if found else false.
	 * 
	 */
	
	public boolean findTime(){
		Scanner sc = new Scanner(contentToInfer);
		String atTime = sc.findInLine(TIME_FORMAT);
		if (atTime != null){
			Matcher matcher = TIME_FORMAT.matcher(atTime);
			if(!matcher.matches()){
				sc.close();
				return false;
			}
			String numeral = matcher.group("time");
			String meridiem = matcher.group("meridiem").toLowerCase();
			int timePrefix = Integer.parseInt(numeral);
			
			if(timePrefix > 1259 && numeral.length() == 4 && timePrefix/100 > 59 
					|| timePrefix > 12 && numeral.length() <= 2){
				sc.close();
				return false;
				
			}if(numeral.length() < 3){
				timePrefix = timePrefix * 100;
				numeral = numeral + "00";
				
			}if(meridiem.equals("pm")){
				if(timePrefix < 1200){
					timePrefix += 1200;
					numeral = Integer.toString(timePrefix);
				}
				
			}if(meridiem.equals("am")){
				if(timePrefix >= 1200){
					timePrefix -= 1200;
					numeral = "00" + Integer.toString(timePrefix);
				}
			}
			sc.close();
			if(numeral.length() == 4){
				inferredTime = numeral.substring(0, 2) + ":" + numeral.substring(2);
				return true;
			}
			if(numeral.length() == 3){
				inferredTime = numeral.substring(0, 1) + ":" + numeral.substring(1);
				return true;
			}
			
		}
		sc.close();
		return false;
	}
	
	/**
	 * Getter to obtain time inferred from content.
	 * 
	 * @return null if there is no date. 
	 */
	public String getTime(){
		
	    return inferredTime;
	
	
	}
}
