package seedu.address.testutil;

import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;

import java.util.ArrayList;

import seedu.address.commons.exceptions.IllegalValueException;
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
	        sb.append("add " + this.getContent().value + " ");
	        sb.append("d/" + this.getDate().dateString + " ");
	        sb.append("t/" + this.getTime().timeString + " ");
	        this.getTags().getInternalList().stream().forEach(s -> sb.append("#" + s.tagName + " "));
	        return sb.toString();
	    }

        @Override
        public boolean getDone() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean setDone() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void addTags(ArrayList<String> tagsToAdd) throws DuplicateTagException, IllegalValueException {
            // TODO Auto-generated method stub
            
        }
	}

