package seedu.address.commons.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/*
 * Infers date from content string if added Task does not specify any start date 
*/
//@@author A0135787N
public class InferDateUtil {
	
	private String contentToInfer;
	private Calendar calendar;
	private String inferredDate;
	private final SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
	private Date now;
	private int currentDay;
	private int inferredDay;
	private HashMap<String, Integer> days;
	
	/**
	 * Constructor that obtains today's date and day of the week.
	 * Passes in content to infer.
	 * 
	 * @param content
	 */
	
	public InferDateUtil(String content){
		assert content != null;
		
	    contentToInfer = content;
	    calendar = Calendar.getInstance();
	    currentDay = calendar.get(Calendar.DAY_OF_WEEK);
	    now = new Date();
	    days = new HashMap<String, Integer>();
	    days.put("monday", 1);
	    days.put("tuesday", 2);
	    days.put("wednesday", 3);
	    days.put("thursday", 4);
	    days.put("friday", 5);
	    days.put("saturday", 6);
	    days.put("sunday", 7);
	    days.put("mon", 1);
	    days.put("tue", 2);
	    days.put("tues", 2);
	    days.put("wed", 3);
	    days.put("thur", 4);
	    days.put("thurs", 4);
	    days.put("fri", 5);
	    days.put("sat", 6);
	    days.put("sun", 7);
	    
	    
	}
	
	/**
	 * Default Constructor
	 * 
	 * initalizes today's date and the day of the week only
	 */
	
	public InferDateUtil(){
	    calendar = Calendar.getInstance();
	    now = new Date();
	    currentDay = calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * finds a possible date that is implied within the content and stores it.
	 * 
	 * @return true if found else false.
	 * 
	 */
	
	public boolean findDate(){
		
		if(new Scanner(contentToInfer).findInLine("tmr") != null
				|| new Scanner(contentToInfer).findInLine("tommorrow") != null){
			calendar.setTime(now);
		    calendar.add(Calendar.DAY_OF_YEAR, 1);
		    inferredDate = sdfDate.format(calendar.getTime());
		 
		    return true;
		}
		Scanner sc = new Scanner(contentToInfer);
		if(sc.findInLine("this") != null){
			if(sc.hasNext()){
				String check = sc.next().toLowerCase();
				
				if(check.equals("this")){
					check = sc.next().toLowerCase();
				}
				
				if(days.containsKey(check)){
					inferredDay = days.get(check);
					
					if(inferredDay - currentDay <= 0){
						calendar.add(Calendar.DAY_OF_YEAR, inferredDay - currentDay + 7);
						inferredDate = sdfDate.format(calendar.getTime());
						sc.close();
						return true;
					}else{
						calendar.add(Calendar.DAY_OF_YEAR, inferredDay - currentDay );
						inferredDate = sdfDate.format(calendar.getTime());
						sc.close();
						return true;
					}
					
				}while(sc.hasNext()){
					if(sc.next().equals("this") && sc.hasNext()){
						check = sc.next().toLowerCase();
						
						if(days.containsKey(check)){
							inferredDay = days.get(check);
							
							if(inferredDay - currentDay <= 0){
								calendar.add(Calendar.DAY_OF_YEAR, inferredDay - currentDay + 7);
								inferredDate = sdfDate.format(calendar.getTime());
								sc.close();
								return true;
								
							}else{
								calendar.add(Calendar.DAY_OF_YEAR, inferredDay - currentDay );
								inferredDate = sdfDate.format(calendar.getTime());
								sc.close();
								return true;
							}
						}
					
					
					}
				}
			} 
				
		}
		sc.close();
		Scanner vc = new Scanner(contentToInfer);
		if(vc.findInLine("next") != null){
			if(vc.hasNext()){
				String check = vc.next().toLowerCase();
				
				if(check.equals("week")){
					calendar.add(Calendar.DAY_OF_YEAR, 7);
					inferredDate = sdfDate.format(calendar.getTime());
					vc.close();
					return true;
				}
				if(days.containsKey(check)){
					inferredDay = days.get(check);
					
					if(inferredDay - currentDay <= 0){
						calendar.add(Calendar.DAY_OF_YEAR, inferredDay - currentDay + 14);
						inferredDate = sdfDate.format(calendar.getTime());
						vc.close();
						return true;
						
					}else{
						calendar.add(Calendar.DAY_OF_YEAR, inferredDay - currentDay + 7);
						inferredDate = sdfDate.format(calendar.getTime());
						vc.close();
						return true;
					}
					
				}
			}
		}
		vc.close();
		return false;
	}
	
	/**
	 * Getter to obtain date inferred from content.
	 * 
	 * @return null if there is no date. 
	 */
	public String getDate(){
		
	    return inferredDate;
	
	
	}
}