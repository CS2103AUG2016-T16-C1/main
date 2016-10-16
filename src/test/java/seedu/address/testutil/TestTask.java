package seedu.address.testutil;

import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.person.*;

/**
 * A mutable task object. For testing only.
 */
	public class TestTask implements ReadOnlyTask {

	    private Content content;
	    private TaskDate taskdate;
	    private TaskTime tasktime;

	    private UniqueTagList tags;

	    public TestTask() {
	        tags = new UniqueTagList();
	    }

	    public void setContent(Content content) {
	        this.content = content;
	    }

	    public void setDate(TaskDate taskdate) {
	        this.taskdate = taskdate;
	    }

	    public void setTime(TaskTime tasktime) {
	        this.tasktime = tasktime;
	    }

	    @Override
	    public Content getContent() {
	        return content;
	    }

	    @Override
	    public TaskDate getDate() {
	        return taskdate;
	    }

	    @Override
	    public TaskTime getTime() {
	        return tasktime;
	    }

	    @Override
	    public UniqueTagList getTags() {
	        return tags;
	    }

	    @Override
	    public String toString() {
	        return getAsText();
	    }

	    public String getAddCommand() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("add " + this.getContent() + " ");
	        sb.append("d/" + this.getDate() + " ");
	        sb.append("t/" + this.getTime() + " ");
	        this.getTags().getInternalList().stream().forEach(s -> sb.append("#" + s.tagName + " "));
	        return sb.toString();
	    }
	}

}
