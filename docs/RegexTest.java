import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexTest {
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern TASK_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("c/(?<content>[^/]+)"
                    + " d/(?<date>[^/]+)"
                    + " t/(?<time>[^/]+)"
                    + "(?<tagArguments>(?: #[^/]+)*)"); // variable number of tags

   public static void main( String args[] ) {
      // String to be scanned to find the pattern.
      String input = "add c/do this task manager d/20/10/2016 t/1300 #shaglife #wheregottime";


      // Now create matcher object.
      Matcher checkInitialInput = BASIC_COMMAND_FORMAT.matcher(input.trim());
      System.out.println("######### TEST BASIC_COMMAND_FORMAT ############");
      if (checkInitialInput.matches() {
         String commandWord = checkInitialInput.group("commandWord");
         String arguments = checkInitialInput.group("arguments");

         System.out.println("Found commandWord: " + commandWord );
         System.out.println("Found arguments: " + arguments );
         System.out.println("############## TEST PASSED ##################");
         System.out.println(" ");
         
         System.out.println("######### TEST TASK_DATA_ARGS_FORMAT ############");
         Matcher checkAddArgs = TASK_DATA_ARGS_FORMAT.matcher(arguments.trim());
         if(checkAddArgs.matches()){

           String content = checkAddArgs.group("content");
           String date = checkAddArgs.group("date");
           String time = checkAddArgs.group("time");
           System.out.println("Found content: " + content );
           System.out.println("Found date: " + date );
           System.out.println("Found time: " + time );
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
