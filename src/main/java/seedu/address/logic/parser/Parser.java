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
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_EMAIL_FORMAT;

/**
 * Parses user input.
 */
// @@author A0139523E
public class Parser {

	/**
	 * Used for initial separation of command word and args.
	 */
	private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

	private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

	private static final Pattern KEYWORDS_ARGS_FORMAT = Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); 

	private static final Pattern EDIT_TASK_ARGS_FORMAT = Pattern.compile("(?<index>[0-9]+) (?<taskDetails>.+)");

	private static final Pattern ADD_TAGS_FORMAT = Pattern.compile("(?<index>\\S+)(?<tagsToAdd>[^#/%]+)");

	private static final Pattern DELETE_TAGS_FORMAT = Pattern.compile("(?<index>\\S+)(?<tagsToDelete>[^#/%]+)");

	private static final Pattern FIND_TAG_FORMAT = Pattern.compile("(?<tagname>[\\p{Alnum}]+)");
	// @@author A0141054W
	public static final Pattern VALID_EMAIL_ADDRESS_FORMAT = Pattern
			.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	// @@author

	private static final ArrayList<String> listKeywords = new ArrayList<>(
			Arrays.asList("all", "done", "undone", "important", "unimportant", "-a", "-d", "-ud", "-ui", "-i"));

	public Parser() {
	}

	/**
	 * Parses user input into command for execution.
	 *
	 * @param userInput
	 *            full user input string
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
		switch (commandWord.toLowerCase()) {

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

		// @@author A0147989B
		case HelpCommand.COMMAND_WORD:
			return new HelpCommand();

		case DoneCommand.COMMAND_WORD:
			return prepareDone(arguments);

		case UndoneCommand.COMMAND_WORD:
			return prepareUndone(arguments);

		case ImportantCommand.COMMAND_WORD:
			return prepareImportant(arguments);

		case UnimportantCommand.COMMAND_WORD:
			return prepareUnimportant(arguments);

		case NextCommand.COMMAND_WORD:
			return prepareNext(arguments);
		// @@author

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

		case SaveCommand.COMMAND_WORD:
			return prepareSave(arguments);

		// @@author A0141054W

		case EmailCommand.COMMAND_WORD:
			return prepareEmail(arguments);

		default:
			return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
		}
	}

	/**
	 * Parses arguments in the context of the edit task command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 * @throws ParseException
	 */
	// @@author A0135787N
	private Command prepareEdit(String args) throws ParseException {
		final Matcher matcher = EDIT_TASK_ARGS_FORMAT.matcher(args.trim());
		// Validate arg string format
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
		}
		try {
			return new EditCommand(matcher.group("index"), matcher.group("taskDetails"));

		} catch (IllegalValueException ive) {
			return new IncorrectCommand(ive.getMessage());
		}
	}

	/**
	 * Parses arguments in the context of the add task command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 * @throws ParseException
	 */
	private Command prepareAdd(String args) throws ParseException {

		Scanner validator = new Scanner(args);
		// Validate arg string format: String cannot be empty or null
		if (args == null || args.isEmpty()) {
			validator.close();
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

		} else {
			// Validate arg string format: String starts with content and not
			// date/time/tag
			String startOfLine = validator.next();

			if (startOfLine.startsWith("sd/") || startOfLine.startsWith("st/") || startOfLine.startsWith("#")
					|| startOfLine.startsWith("et/") || startOfLine.startsWith("ed/") || startOfLine.startsWith("r/")) {
				validator.close();
				return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
			}

		}
		validator.close();

		// Obtain content of Task from String args
		Scanner scanContent = new Scanner(args);
		StringBuilder content = new StringBuilder();
		while (scanContent.hasNext()) {
			String check = scanContent.next();
			if (check.startsWith("sd/") || check.startsWith("st/") || check.startsWith("#") || check.startsWith("et/")
					|| check.startsWith("ed/") || check.startsWith("r/"))
				break;
			else
				content.append(" " + check);
		}
		scanContent.close();

		// Obtain set of tags if any from String args
		Set<String> setTags = new HashSet<String>();
		Scanner scanTags = new Scanner(args);
		if (scanTags.findInLine("#") != null) {
			setTags.add(scanTags.next());
			while (scanTags.findInLine("#") != null) {
				setTags.add(scanTags.next());
			}

		} else {
			setTags = Collections.emptySet();
		}
		scanTags.close();

		// Obtain date if any from String args
		String dateString = null;
		Scanner scanDate = new Scanner(args);
		if (scanDate.findInLine("sd/") != null) {
			dateString = scanDate.next();
		}
		scanDate.close();

		// Obtain time if any from String args
		String timeString = null;
		Scanner scanTime = new Scanner(args);
		if (scanTime.findInLine("st/") != null) {
			timeString = scanTime.next();
		}
		scanTime.close();

		// Obtain endTime if any from String args
		String endTimeString = null;
		Scanner scanEndTime = new Scanner(args);
		if (scanEndTime.findInLine("et/") != null) {
			endTimeString = scanEndTime.next();
		}

		scanEndTime.close();

		// Obtain endDate if any from String args
		String endDateString = null;
		Scanner scanEndDate = new Scanner(args);
		if (scanEndDate.findInLine("ed/") != null) {
			endDateString = scanEndDate.next();
		}
		scanEndDate.close();

		// Obtain duration if any from String args
		Integer duration = null;
		Scanner scanDuration = new Scanner(args);
		if (scanDuration.findInLine("r/") != null) {
			duration = scanDuration.nextInt();
		}
		scanDuration.close();

		try {
			return new AddCommand(content.toString().trim(), dateString, endDateString, timeString, endTimeString,
					duration, setTags);

		} catch (IllegalValueException ive) {
			return new IncorrectCommand(ive.getMessage());
		}
	}

	// @@author A0141054W
	/**
	 * Parses arguments in the context of the list task command.
	 *
	 * @param done,
	 *            undone, all
	 * @return the prepared command
	 * @throws ParseException
	 */
	private Command prepareList(String args) throws ParseException {
		if (!listKeywords.contains(args.trim().toLowerCase())) {
			return new IncorrectCommand(
					String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_LIST_RESTRICTION));
		} else
			return new ListCommand(args.trim());
	}

	/**
	 * Parses arguments in the context of the email command.
	 *
	 * @param args
	 *            should be an email address
	 * @return the prepared command
	 * @throws ParseException
	 */
	private Command prepareEmail(String arg) throws ParseException {
		String email = arg.trim();
		if (email.compareTo("") == 0) {
			return new EmailCommand("");
		}
		final Matcher matcher = VALID_EMAIL_ADDRESS_FORMAT.matcher(email);
		if (!matcher.matches()) {
			return new IncorrectCommand(MESSAGE_INVALID_EMAIL_FORMAT);
		} else {
			return new EmailCommand(email);
		}
	}

	/*
	 * Parses arguments in the context of the save command.
	 * 
	 * @param args should be the file path
	 * 
	 * @return the prepared command
	 * 
	 * @throws ParseException
	 */
	private Command prepareSave(String args) throws ParseException {
		File file = new File(args.trim());
		if (file.isDirectory()) {
			return new IncorrectCommand(
					String.format(MESSAGE_INVALID_COMMAND_FORMAT, SaveCommand.MESSAGE_DIRECTORY_FILEPATH));
		} else {
			return new SaveCommand(args.trim());
		}
	}

	/**
	 * Parses arguments in the context of the load command.
	 *
	 * @param args
	 *            should be the file path
	 * @return the prepared command
	 * @throws ParseException
	 */
	private Command prepareLoad(String args) throws ParseException {
		File file = new File(args.trim());
		if (file.isDirectory()) {
			return new IncorrectCommand(
					String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_DIRECTORY_FILEPATH));
		} else if (!file.exists()) {
			return new IncorrectCommand(
					String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_INVALID_FILEPATH));
		} else
			return new LoadCommand(args.trim());
	}

	/**
	 * Parses arguments in the context of the add tag command.
	 *
	 * @param args
	 *            should be a valid tag
	 * @return the prepared command
	 * @throws ParseException
	 */
	private Command prepareAddTags(String args) throws ParseException {
		Matcher matcher = ADD_TAGS_FORMAT.matcher(args.trim());
		// Validate arg string format
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE));
		}

		try {
			return new AddTagCommand(matcher.group("index"), matcher.group("tagsToAdd"));

		} catch (IllegalValueException ive) {
			return new IncorrectCommand(ive.getMessage());
		}
	}

	/**
	 * Parses arguments in the context of the delete tag command.
	 *
	 * @param args
	 *            should be a valid tag
	 * @return the prepared command
	 * @throws ParseException
	 */
	private Command prepareDeleteTags(String args) throws ParseException {
		Matcher matcher = DELETE_TAGS_FORMAT.matcher(args.trim());
		// Validate arg string format
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTagCommand.MESSAGE_USAGE));
		}

		try {
			return new DeleteTagCommand(matcher.group("index"), matcher.group("tagsToDelete"));

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
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareDelete(String args) {

		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
		}

		return new DeleteCommand(index.get());
	}

	/**
	 * Parses arguments in the context of the done person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	// @@author A0147989B
	private Command prepareNext(String args) {

		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
		}

		return new NextCommand(index.get());
	}

	/**
	 * Parses arguments in the context of the done person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareDone(String args) {

		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
		}

		return new DoneCommand(index.get());
	}

	/**
	 * Parses arguments in the context of the undone person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareUndone(String args) {

		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoneCommand.MESSAGE_USAGE));
		}

		return new UndoneCommand(index.get());
	}

	/**
	 * Parses arguments in the context of the important person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareImportant(String args) {

		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportantCommand.MESSAGE_USAGE));
		}

		return new ImportantCommand(index.get());
	}

	/**
	 * Parses arguments in the context of the unimportant person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareUnimportant(String args) {

		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(
					String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnimportantCommand.MESSAGE_USAGE));
		}

		return new UnimportantCommand(index.get());
	}
	// @@author

	/**
	 * Parses arguments in the context of the select task command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	// @@author A0141054W
	private Command prepareSelect(String args) {
		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
		}

		return new SelectCommand(index.get());
	}

	/**
	 * Returns the specified index in the {@code command} IF a positive unsigned
	 * integer is given as the index. Returns an {@code Optional.empty()}
	 * otherwise.
	 */
	private Optional<Integer> parseIndex(String command) {
		final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
		if (!matcher.matches()) {
			return Optional.empty();
		}

		String index = matcher.group("targetIndex");
		if (!StringUtil.isUnsignedInteger(index)) {
			return Optional.empty();
		}
		return Optional.of(Integer.parseInt(index));

	}

	/**
	 * Parses arguments in the context of the find task command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	// @@author A0135787N
	private Command prepareFind(String args) {
		final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
		}
		final String keywords = matcher.group("keywords");
		// keywords delimited by whitespace
		// final String[] keywords = matcher.group("keywords").split("\\s+");
		// final Set<String> keywordSet = new
		// HashSet<>(Arrays.asList(keywords));
		return new FindCommand(keywords);
	}

	/**
	 * Parses arguments in the context of the findtag task command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 * @throws IllegalValueException
	 */
	private Command prepareFindTag(String args) {
		final Matcher matcher = FIND_TAG_FORMAT.matcher(args.trim());
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
		}
		final String keywords = matcher.group("tagname");
		try {
			return new FindTagCommand(keywords);
		} catch (IllegalValueException e) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
		}
	}
}