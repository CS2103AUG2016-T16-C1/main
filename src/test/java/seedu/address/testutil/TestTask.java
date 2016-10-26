package seedu.address.testutil;

import seedu.address.model.tag.Tag;
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
	    private boolean done;
	    private Integer duration;

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
	    if(this.getDate().enddateString == null) {
	          return getAsText();
	            }
	            else
	                return getAsText2();
	        }

	    public String getAddCommand() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("add " + this.getContent().value + " ");
	        sb.append("sd/" + this.getDate().dateString + " ");
	        sb.append("ed/" + this.getDate().enddateString + " ");
	        sb.append("st/" + this.getTime().timeString + " ");
	        sb.append("et/" + this.getTime().endtimeString + " ");
	        this.getTags().getInternalList().stream().forEach(s -> sb.append("#" + s.tagName + " "));
	        return sb.toString();
	    }

        @Override
        public boolean getDone() {
            return done;
        }

        @Override
        public boolean setDone() {
            if (!done) done = true;
            else return false;
            return true;
        }
        
        @Override
        public Integer getDuration() {
            return duration;
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
        
        /**
         * Replaces this task's tags with the tags in the argument tag list.
         */
        public void setTags(UniqueTagList replacement) {
            tags.setTags(replacement);
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }
        
        public boolean setUndone(){
        	if (done) done = false;
        	else return false;
        	return true;
        }
        

		@Override
		public boolean deleteTags(ArrayList<String> tagsToDel) throws DuplicateTagException, IllegalValueException {
			
			return false;
		}

        @Override
        public boolean getImportant() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean setImportant() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean setUnimportant() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean setNext() {
            // TODO Auto-generated method stub
            return false;
        }



	}

