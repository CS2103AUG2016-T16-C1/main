package hard2do.taskmanager.model.task;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;

import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.model.tag.UniqueTagList;
import hard2do.taskmanager.model.tag.UniqueTagList.DuplicateTagException;

/**
 * A read-only immutable interface for a task in Hard2Do.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
//@@author A0139523E
public interface ReadOnlyTask {

    Content getContent();
    TaskDate getDate();
    TaskDate getEndDate();
    TaskTime getTime();
    TaskTime getEndTime();
    //@@author A0147989B
    Integer getDuration();
    boolean setNext();
    boolean getDone();
    boolean setDone();
    boolean setUndone();
    boolean getImportant();
    boolean setImportant();
    boolean setUnimportant();
    boolean setDate(String newDate) throws IllegalValueException, ParseException;
    boolean setEndDate(String newEndDate) throws IllegalValueException, ParseException;
    boolean setContent(String newContent) throws IllegalValueException;
    boolean setTime(String newTime) throws IllegalValueException, ParseException;
    boolean setEndTime(String newEndTime) throws IllegalValueException, ParseException;
    //@@author
    boolean addTags(ArrayList<String> tagsToAdd) throws DuplicateTagException, IllegalValueException;
    boolean deleteTags(ArrayList<String> tagsToDel) throws DuplicateTagException, IllegalValueException;
    void setTags(UniqueTagList uniqueTagList);
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
    default String getAsText0() {
        final StringBuilder builder = new StringBuilder();
        if (this.getEndDate() != null)
            builder.append(getContent())
                   .append(" ")
                   .append(getDate().dateString)
                   .append(" ")
                   .append(getTime().timeString)
                   .append(" - ")
                   .append(getEndDate().dateString)
                   .append(" ")
                   .append(getEndTime().timeString)
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
    
	
	
	
}
