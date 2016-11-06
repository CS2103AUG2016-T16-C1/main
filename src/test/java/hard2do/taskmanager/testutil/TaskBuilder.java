package hard2do.taskmanager.testutil;

import java.text.ParseException;

import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.model.tag.Tag;
import hard2do.taskmanager.model.task.*;

/**
 *
 */
// @@author A0139523E-reused
public class TaskBuilder {

	private TestTask task;

	public TaskBuilder() {
		this.task = new TestTask();
	}

	public TaskBuilder withContent(String content) throws IllegalValueException {
		this.task.setContent(new Content(content));
		return this;
	}

	public TaskBuilder withTags(String... tags) throws IllegalValueException {
		for (String tag : tags) {
			task.getTags().add(new Tag(tag));
		}
		return this;
	}

	public TaskBuilder withDate(String taskdate) throws IllegalValueException, ParseException {
		this.task.setDate(new TaskDate(taskdate));
		return this;
	}

	public TaskBuilder withEndDate(String enddate) throws IllegalValueException, ParseException {
		this.task.setEndDate(new TaskDate(enddate));
		return this;
	}

	public TaskBuilder withTime(String startTime) throws IllegalValueException, ParseException {
		this.task.setTime(new TaskTime(startTime));
		return this;
	}

	public TaskBuilder withEndTime(String endTime) throws IllegalValueException, ParseException {
		this.task.setEndTime(new TaskTime(endTime));
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
