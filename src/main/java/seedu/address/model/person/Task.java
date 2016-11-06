package seedu.address.model.person;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;

//@@author A0147989B
/**
 * Represents a task in Hard2Do.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private Content content;
    private TaskDate date;
    private TaskDate endDate;
    private TaskTime time;
    private TaskTime endTime;
    private Integer duration;
    private boolean done = false;
    private boolean important = false;

    private UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Task(Content content, TaskDate date, TaskTime time, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(content, date, time, tags);
        this.content = content;
        this.date = date;
        this.time = time;
        this.tags = tags; 
    }
    
    /**
     * create a task with done status
     */
    public Task(Content content, TaskDate date, TaskTime time, boolean done, boolean important, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(content, date, time, tags);
        this.content = content;
        this.date = date;
        this.time = time;
        this.done = done;
        this.important = important;
        this.tags = tags; 
    }
    
    public Task(Content content, TaskDate date, TaskDate endDate, TaskTime time, TaskTime endTime, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(content, date, endDate, time, endTime, tags);
        this.content = content;
        this.date = date;
        this.endDate = endDate;
        this.time = time;
        this.endTime = endTime;
        this.tags = tags; 
    }
    
    public Task(Content content, TaskDate date, TaskDate endDate, TaskTime time, TaskTime endTime, boolean done, boolean important, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(content, date, endDate, time, endTime, tags);
        this.content = content;
        this.date = date;
        this.endDate = endDate;
        this.time = time;
        this.endTime = endTime;
        this.done = done;
        this.important = important;
        this.tags = tags; 
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getContent(), 
                source.getDate(), 
                source.getEndDate(),
                source.getTime(), 
                source.getEndTime(),
                source.getDone(), 
                source.getImportant(), 
                source.getTags());
    }
    
    public boolean isEvent(Task taskToCheck) {
    	if(taskToCheck.getEndDate() == null)
    		return false;
    	else
    		return true;
    }
    
    @Override
    public boolean addTags(ArrayList<String> tagsToAdd) throws DuplicateTagException, IllegalValueException {
    	UniqueTagList newList = new UniqueTagList();
    	for(String t : tagsToAdd){
    		newList.add(new Tag(t));
    	}
    	newList.mergeFrom(tags);
        setTags(newList);
        return true;
    }
    
    @Override
    public boolean deleteTags(ArrayList<String> tagsToDelete) 
    		throws DuplicateTagException, IllegalValueException {
    	UniqueTagList newList = new UniqueTagList(tags);
    	for(String t : tagsToDelete){
    		newList.remove(new Tag(t));
    	}
        setTags(newList);
        return true;
    }

    @Override
    public UniqueTagList getTags() {
        return tags;
    }

    /**
     * Replaces this task's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(content, date, time, tags);
    }

    @Override
    public String toString() {

		return getAsText0();
    }

    @Override
    public Content getContent() {
        return content;
    }

    @Override
    public TaskDate getDate() {
        return date;
    }
    
    @Override
    public TaskDate getEndDate() {
    	return endDate;
    			
    }

    @Override
    public TaskTime getTime() {
        return time;
    }

    @Override
    public TaskTime getEndTime() {
    	return endTime;
    }
    @Override
    public boolean setDate(String date) throws IllegalValueException, ParseException{
    	this.date = new TaskDate(date);
    	return true;
    }
    @Override
    public boolean setEndDate(String newDate) throws IllegalValueException, ParseException{
    	this.endDate = new TaskDate(newDate);
		return true;
    }
    @Override
    public boolean setTime(String time) throws IllegalValueException, ParseException{
    	this.time = new TaskTime(time);
    	return true;
    }
    @Override
    public boolean setEndTime(String time) throws IllegalValueException, ParseException{
    	this.endTime = new TaskTime(time);
    	return true;
    }
    @Override
    public boolean setContent(String newContent) throws IllegalValueException{
    	this.content = new Content(newContent);
    	return true;
    }
    
    @Override
    public Integer getDuration() {
        return duration;
    }
    
    @Override
    public boolean setNext() {
        if (duration != null){
            while (date.getValue().before(Date.valueOf(LocalDate.now()))){
                date.getValue().setDate(date.getValue().getDate()+duration);
            };
            System.out.println("nexting");
        }
        return true;
    }
    
    @Override
    public boolean setDone() {
        if (!done) done = true;
        return true;
    }
    
    @Override
    public boolean setUndone() {
        if (done) done = false;
        else return false;
        return true;
    }
    
    @Override
    public boolean getDone() {
        return done;
    }
    
    @Override
    public boolean setImportant() {
        if (!important) important = true;
        return true;
    }
    
    @Override
    public boolean setUnimportant() {
        if (important) important = false;
        else return false;
        return true;
    }
    
    @Override
    public boolean getImportant() {
        return important;
    }
    
}
