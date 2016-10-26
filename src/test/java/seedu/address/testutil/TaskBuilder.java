package seedu.address.testutil;

import java.text.ParseException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.person.*;

/**
 *
 */
public class TaskBuilder {

    private TestTask task;

    public TaskBuilder() {
        this.task = new TestTask();
    }

    public TaskBuilder withContent(String content) throws IllegalValueException {
        this.task.setContent(new Content(content));
        return this;
    }

    public TaskBuilder withTags(String ... tags) throws IllegalValueException {
        for (String tag: tags) {
            task.getTags().add(new Tag(tag));
        }
        return this;
    }

    public TaskBuilder withDate(String taskdate, String enddate) throws IllegalValueException, ParseException {
        this.task.setDate(new TaskDate(taskdate, enddate));
        return this;
    }

    public TaskBuilder withTime(String startTime, String endTime) throws IllegalValueException, ParseException {
        this.task.setTime(new TaskTime(startTime, endTime));
        return this;
    }
    
    public TaskBuilder withDuration(Integer duration) throws IllegalValueException, ParseException {
    	this.task.setDuration(duration);
		return this;
    }
    
    public TaskBuilder withDone(boolean done) throws IllegalValueException, ParseException {
    	this.task.setDone(done);
    	return this;
    }
    
    public TestTask build() {
        return this.task;
    }

}
