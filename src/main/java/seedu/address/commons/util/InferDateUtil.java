package seedu.address.commons.util;

//@@author A0135787N



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class InferDateUtil {
private String contentToInfer;
private Calendar calendar;
private String inferredDate;
private final SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
private Date now;
private int currentDay;
private int inferredDay;
private HashMap<String, Integer> days;

public InferDateUtil(String content){
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
    days.put("wed", 3);
    days.put("thur", 4);
    days.put("fri", 5);
    days.put("sat", 6);
    days.put("sun", 7);
    
    
}


public InferDateUtil(){
    calendar = Calendar.getInstance();
    now = new Date();
    currentDay = calendar.get(Calendar.DAY_OF_WEEK);
}

public boolean hasDate(){
	
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
	return false;
}
public String inferDate(){

    return inferredDate;


}
}