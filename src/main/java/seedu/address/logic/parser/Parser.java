package seedu.address.logic.parser;

import seedu.address.logic.commands.*;
import seedu.address.commons.util.StringUtil;
import seedu.address.commons.exceptions.IllegalValueException;

import java.io.File;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

            		
    private static final Pattern EDIT_TASK_ARGS_FORMAT = 
    		Pattern.compile("(?<index>\\S+)(?<taskDetails>.+)");
    
    private static final Pattern ADD_TAGS_FORMAT =
    		Pattern.compile("(?<index>\\S+)(?<tagsToAdd>[^#/%]+)");
    
    private static final Pattern DELETE_TAGS_FORMAT =
    		Pattern.compile("(?<index>\\S+)(?<tagsToDelete>[^#/%]+)");
    
    private static final Pattern FIND_TAG_FORMAT =
    		Pattern.compile("(?<tagname>[\\p{Alnum}]+)");

    public Parser() {}

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException 
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);
            
        case FindTagCommand.COMMAND_WORD:
        	return prepareFindTag(arguments);

        case ListCommand.COMMAND_WORD:
            return prepareList(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
            
        case DoneCommand.COMMAND_WORD:
            return prepareDone(arguments);

        case UndoneCommand.COMMAND_WORD:
            return prepareUndone(arguments);
            
        case EditCommand.COMMAND_WORD:
        	return prepareEdit(arguments);
        
        case AddTagCommand.COMMAND_WORD:
        	return prepareAddTags(arguments);
        
        case DeleteTagCommand.COMMAND_WORD:
        	return prepareDeleteTags(arguments);
        
        case UndoCommand.COMMAND_WORD:
        	return new UndoCommand();
        	
        case LoadCommand.COMMAND_WORD:
            return prepareLoad(arguments);
        

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }
    /**
     * Parses arguments in the context of the edit person command.
     *
     * @param args full command args string
     * @return the prepared command
     * @throws ParseException 
     */
    private Command prepareEdit(String args) throws ParseException{
        final Matcher matcher = EDIT_TASK_ARGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        try {
            return new EditCommand( matcher.group("index"),
                    matcher.group("taskDetails"));
            
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
    /**
     * Parses arguments in the context of the add person command.
     *
     * @param args full command args string
     * @return the prepared command
     * @throws ParseException 
     */
    private Command prepareAdd(String args) throws ParseException{
    	
    	Scanner validator = new Scanner(args);
    	// Validate arg string format: String cannot be empty or null
        if (args == null || args.isEmpty()) {
        	validator.close();
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
            
        }else{
        //Validate arg string format: String starts with content and not date/time/tag
        	String startOfLine = validator.next();
        	
        	if(startOfLine.startsWith("d/") || startOfLine.startsWith("st/") 
        			|| startOfLine.startsWith("#") || startOfLine.startsWith("et/")){
        		validator.close();
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        	}
        	
        }
        validator.close();
        
        //Obtain content of Task from String args
    	Scanner scanContent = new Scanner(args);
    	StringBuilder content = new StringBuilder();
		while(scanContent.hasNext()){
			String check = scanContent.next();
			if(check.startsWith("d/") || check.startsWith("st/") || check.startsWith("#") || check.startsWith("et/"))
				break;
			else
				content.append(" " + check);
		}
		scanContent.close();
		
		//Obtain set of tags if any from String args
		Set<String> setTags = new HashSet<String>();
		Scanner scanTags = new Scanner(args);
		if(scanTags.findInLine("#") != null){
			setTags.add(scanTags.next());
			while(scanTags.findInLine("#") != null){
				setTags.add(scanTags.next());
			}
			
		}else{
			setTags = Collections.emptySet();
		}
		scanTags.close();
		
		//Obtain date if any from String args
		String dateString = null;
		Scanner scanDate = new Scanner(args);
		if(scanDate.findInLine("d/") != null){
			dateString = scanDate.next();
		}
		scanDate.close();
		
		//Obtain time if any from String args
		String timeString = null;
		Scanner scanTime = new Scanner(args);
		if(scanTime.findInLine("st/") != null){
			timeString = scanTime.next();
		}
		scanTime.close();
		
		//Obtain endTime if any from String args
		String endString = null;
		Scanner scanEnd = new Scanner(args);
		if(scanEnd.findInLine("et/") != null){
			endString = scanEnd.next();
		}
		scanEnd.close();
        
        try {
            return new AddCommand( content.toString().trim(), dateString, timeString, endString, setTags);
            
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
    
    private Command prepareList(String args) throws ParseException{
        if (args.trim().compareTo("done") != 0 && args.trim().compareTo("undone") != 0 && args.trim().compareTo("all") != 0 && args.trim().compareTo("") != 0) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_LIST_RESTRICTION));
        }
        else 
            return new ListCommand(args.trim());
    }
    
    private Command prepareLoad(String args) throws ParseException{
        File file = new File(args.trim());
        if (file.isDirectory()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_DIRECTORY_FILEPATH));
        }
        else if (!file.exists()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_INVALID_FILEPATH));
        }
        else
            return new LoadCommand(args.trim());
    }
    
    private Command prepareAddTags(String args) throws ParseException{
        Matcher matcher = ADD_TAGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE));
        }
        
        try {
            return new AddTagCommand( matcher.group("index"),
                    matcher.group("tagsToAdd"));
            
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
    
    private Command prepareDeleteTags(String args) throws ParseException{
        Matcher matcher = DELETE_TAGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTagCommand.MESSAGE_USAGE));
        }
        
        try {
            return new DeleteTagCommand( matcher.group("index"),
                    matcher.group("tagsToDelete"));
            
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Extracts the new task's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" #", "").split(" #"));
        return new HashSet<>(tagStrings);
    }

    /**
     * Parses arguments in the context of the delete person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());
    }
    
    /**
     * Parses arguments in the context of the delete person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDone(String args) {

        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
        }

        return new DoneCommand(index.get());
    }
    
    /**
     * Parses arguments in the context of the delete person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareUndone(String args) {

        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoneCommand.MESSAGE_USAGE));
        }

        return new UndoneCommand(index.get());
    }

    /**
     * Parses arguments in the context of the select task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    /**
     * Parses arguments in the context of the find task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareFind(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }
        final String keywords = matcher.group("keywords");
        // keywords delimited by whitespace
        //final String[] keywords = matcher.group("keywords").split("\\s+");
        //final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywords);
    }
    
    /**
     * Parses arguments in the context of the findtag task command.
     *
     * @param args full command args string
     * @return the prepared command
     * @throws IllegalValueException 
     */
    private Command prepareFindTag(String args){
        final Matcher matcher = FIND_TAG_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindTagCommand.MESSAGE_USAGE));
        }
        final String keywords = matcher.group("tagname");
        try {
			return new FindTagCommand(keywords);
		} catch (IllegalValueException e) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, 
					FindTagCommand.MESSAGE_USAGE));
		}
    }
}