package seedu.address.commons.util;



import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Infers time from content string if added Task does not specify any start time 
*/
//@@author A0135787N
public class InferTimeUtil {
	
	private String contentToInfer;
	private String inferredTime;
	private String startTime;
	private String endTime;
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
	    
	}
	
	/**
	 * Default Constructor
	 * 
	 */
	
	public InferTimeUtil(){


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
	 * finds a possible time period that is implied within the content and stores it.
	 * 
	 * @return true if found else false.
	 * 
	 */
	public boolean findTimeToTime(){
		Scanner vc = new Scanner(contentToInfer);
		String timeToTime = vc.findInLine(START_END_TIME_FORMAT);
		
		if (timeToTime != null){
			Matcher matcher = START_END_TIME_FORMAT.matcher(timeToTime);
			if(!matcher.matches()){
				vc.close();
				return false;
			}
			
			startTime = obtainTime(matcher.group("startTime"), matcher.group("smeridiem"));
			endTime = obtainTime(matcher.group("endTime"), matcher.group("emeridiem"));
			if(startTime != null && endTime != null ){
				vc.close();
				return true;
			}
		
		}
		vc.close();
		return false;
	}
	
	public String obtainTime(String numeral, String meridiem){
		int timePrefix = Integer.parseInt(numeral);
		meridiem = meridiem.toLowerCase();
		
		if(timePrefix > 1259 && numeral.length() == 4 && timePrefix/100 > 59 
				|| timePrefix > 12 && numeral.length() <= 2){
			return null;
			
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

		if(numeral.length() == 4){
			return numeral.substring(0, 2) + ":" + numeral.substring(2);

		}
		if(numeral.length() == 3){
			return numeral.substring(0, 1) + ":" + numeral.substring(1);
		}
		
		return null;
		
	}
	
	/**
	 * Getters to obtain times inferred from content.
	 * 
	 * @return null if there is no time. 
	 */
	
	public String getTime(){
		
	    return inferredTime;
	
	
	}
	public String getStartTime(){
		return startTime;
	}
	
	
	public String getEndTime(){
		return endTime;
	}
}
