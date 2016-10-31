package seedu.address.model.person;

import java.util.ArrayList;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;

/**
 * A read-only immutable interface for a task in Hard2Do.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
//@@author A0139523E
public interface ReadOnlyTask {

    Content getContent();
    TaskDate getDate();
    TaskTime getTime();
    //@@author A0147989B
    Integer getDuration();
    boolean setNext();
    boolean getDone();
    boolean setDone();
    boolean setUndone();
    boolean getImportant();
    boolean setImportant();
    boolean setUnimportant();
    //@@author
    boolean addTags(ArrayList<String> tagsToAdd) throws DuplicateTagException, IllegalValueException;
    boolean deleteTags(ArrayList<String> tagsToDel) throws DuplicateTagException, IllegalValueException;
    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the task's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getContent().equals(this.getContent())); // state checks here onwards
                //&& other.getDate().equals(this.getDate())
                //&& other.getTime().equals(this.getTime()));
    }

    /**
     * Formats the task as text, showing all task details.
     */
    /*
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getContent())
                .append(" ")
                .append(getDate())
                .append(" ")
                .append(getTime())
                .append(" ")
                .append("repeat per "+getDuration()+" days ")
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
    
    default String getAsText2() {
    	final StringBuilder builder = new StringBuilder();
    	builder.append(getContent())
    			.append(" ")
    			.append(getDate().dateString)
    			.append(" ")
    			.append(getTime().timeString)
    			.append(" - ")
    			.append(getDate().enddateString)
    			.append(" ")
    			.append(getTime().endtimeString)
    			.append(" ")
    			.append("repeat per "+getDuration()+" days ")
    			.append(" Tags: ");
    	getTags().forEach(builder::append);
    	return builder.toString();
    }*/
    
    default String getAsText0() {
        final StringBuilder builder = new StringBuilder();
        if (!this.getDate().enddateString.isEmpty() && !this.getTime().endtimeString.isEmpty())
            builder.append(getContent())
                   .append(" ")
                   .append(getDate().dateString)
                   .append(" ")
                   .append(getTime().timeString)
                   .append(" - ")
                   .append(getDate().enddateString)
                   .append(" ")
                   .append(getTime().endtimeString)
                   .append(" ");
        else         
            builder.append(getContent())
                   .append(" ")
                   .append(getDate())
                   .append(" ")
                   .append(getTime())
                   .append(" ");
                   
        if (getDuration() != null) builder .append("repeat per "+getDuration()+" days ");
        builder.append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
    
    /**
     * Returns a string representation of this task's tags
     */
    default String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        final String separator = ", ";
        getTags().forEach(tag -> buffer.append(tag).append(separator));
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }
    void setTags(UniqueTagList uniqueTagList);
	
	
}
