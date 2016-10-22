package seedu.address.model.person;

import java.util.ArrayList;
import java.util.Objects;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;

/**
 * Represents a task in Hard2Do.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private Content content;
    private TaskDate date;
    private TaskTime time;
    private boolean done = false;

    private UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Task(Content content, TaskDate date, TaskTime time, /*boolean done, */UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(content, date, time, tags);
        this.content = content;
        this.date = date;
        this.time = time;
        this.tags = tags; 
    }
    
    /**
     * create a task with done status
     */
    public Task(Content content, TaskDate date, TaskTime time, boolean done, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(content, date, time, tags);
        this.content = content;
        this.date = date;
        this.time = time;
        this.done = done;
        this.tags = tags; 
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getContent(), source.getDate(), source.getTime(), source.getTags());
        if (source.getDone()) setDone();
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
        return getAsText();
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
    public boolean setDone() {
        if (!done) done = true;
        else return false;
        return true;
    }
    
    @Override
    public boolean getDone() {
        return done;
    }
}
