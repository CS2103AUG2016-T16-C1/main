import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class RegexTest {
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern TASK_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<content>[^/#]+) (?<otherData>.+)?"); // variable number of tags

    private static final Pattern EDIT_TASK_ARGS_FORMAT =
            Pattern.compile("(?<index>\\S+)(?<taskDetails>.*)");
    private static final Pattern ADD_TAGS_FORMAT =
            Pattern.compile("(?<index>\\S+)(?<tagsToAdd>.+)");
   public static void main( String args[] ) {


      // String to be scanned to find the pattern.


      String input = "add do this task manager #test d/20-10-2016 t/13:00 #shaglife #wheregottime d/what";
      String input2 = "add";
      String editInput = "2 d/12-20-2012 c/do something";
      String addTagsInput = "1 sometag ok";
      Matcher test = ADD_TAGS_FORMAT.matcher(addTagsInput.trim());
      if(test.matches()){
        System.out.println("Matches");
        System.out.println(test.group("index"));
        System.out.println(test.group("tagsToAdd"));
      }
      Scanner sc = new Scanner(input);
      String setTags = "";
      if(sc.findInLine("#") != null){

			  setTags = setTags + sc.next();
        while(sc.findInLine("#") != null){
				  setTags = setTags + " " + sc.next();
			}

		  }
      System.out.println("output is : " + setTags);

      System.out.println("########## BEGIN REGEX TEST ###############");
      System.out.println("Input: " + input);
      System.out.println(" ");
      // Now create matcher object.
      Matcher checkInitialInput = BASIC_COMMAND_FORMAT.matcher(input2.trim());
      System.out.println("######### TEST BASIC_COMMAND_FORMAT ############");
      if (checkInitialInput.matches()) {
         String commandWord = checkInitialInput.group("commandWord");
         String arguments = checkInitialInput.group("arguments");

         System.out.println("Found commandWord: " + commandWord );
         System.out.println("Found arguments: " + arguments + "end" );
         System.out.println("############## TEST PASSED ##################");
         System.out.println(" ");
        //  Matcher checkEditArgs = EDIT_TASK_ARGS_FORMAT.matcher(arguments.trim());
        //  System.out.print("index: " + checkEditArgs.group("index"));
         //System.out.print("arguments: " + checkEditArgs.group("taskDetails") );

         System.out.println("######### TEST TASK_DATA_ARGS_FORMAT ############");
         Matcher checkAddArgs = TASK_DATA_ARGS_FORMAT.matcher(arguments.trim());
         System.out.println("Testing matcher groups...");
         //System.out.println("Group 1: " + checkAddArgs.group(1));
         //System.out.println("Group 2: " + checkAddArgs.group(2));

         if(checkAddArgs.matches()){

           String content = checkAddArgs.group("content");
           String otherData = checkAddArgs.group("otherData");
          //  String date = checkAddArgs.group("date");
          //  String time = checkAddArgs.group("time");
          //  String tags = checkAddArgs.group("tagArguments");
           System.out.println("Found content: " + content );
           System.out.println("Found Arguments: " + otherData);
          //  System.out.println("Found date: " + date );
          //  System.out.println("Found time: " + time );
          //  System.out.println("Found Tags: " + tags);
           System.out.println("############## TEST PASSED ##################");




         }else {
           System.out.println("Invalid input or regex at TASK_DATA_ARGS_FORMAT");
           return;
         }

      }else {
         System.out.println("NO MATCH AT INITIAL INPUT");
         return;
      }
   }
}
