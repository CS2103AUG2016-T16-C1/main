package seedu.address.testutil;

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

    public TaskBuilder withDate(String taskdate) throws IllegalValueException {
        this.task.setDate(new TaskDate(taskdate));
        return this;
    }

    public TaskBuilder withTime(String tasktime) throws IllegalValueException {
        this.task.setTime(new TaskTime(tasktime));
        return this;
    }

    public TestTask build() {
        return this.task;
    }

}
