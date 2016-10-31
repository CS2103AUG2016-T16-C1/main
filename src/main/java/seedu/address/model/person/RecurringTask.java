package seedu.address.model.person;

import java.sql.Date;
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
 * Represents a recurring task in Hard2Do.
 * Guarantees: details are present and not null, field values are validated.
 */
public class RecurringTask implements ReadOnlyTask {

    private Content content;
    private TaskDate date;
    private TaskTime time;
    private Integer duration;
    private boolean done = false;
    private boolean important = false;

    private UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public RecurringTask(Content content, TaskDate date, TaskTime time, Integer duration, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(content, date, time, tags);
        this.content = content;
        this.date = date;
        this.time = time;
        this.tags = tags; 
        this.duration = duration;
    }
    
    /**
     * create a task with done status
     */
    public RecurringTask(Content content, TaskDate date, TaskTime time, Integer duration, boolean done, boolean important, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(content, date, time, tags);
        //System.out.println("creating R-task: "+content.value);
        this.content = content;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.done = done;
        this.important = important;
        this.tags = tags; 
    }

    /**
     * Copy constructor.
     */
    public RecurringTask(ReadOnlyTask source) {
        this(source.getContent(), 
                source.getDate(), 
                source.getTime(), 
                source.getDuration(), 
                source.getDone(), 
                source.getImportant(), 
                source.getTags());
        //System.out.println("backing R-task: "+content.value);
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
        /*if(!date.enddateString.isEmpty() && !time.endtimeString.isEmpty()) {
        return getAsText2();
        }
        else*/
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
    public TaskTime getTime() {
        return time;
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
