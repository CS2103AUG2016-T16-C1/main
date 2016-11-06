package hard2do.taskmanager.model.person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import hard2do.taskmanager.commons.exceptions.DuplicateDataException;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.commons.util.CollectionUtil;
import hard2do.taskmanager.model.tag.UniqueTagList.DuplicateTagException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of tasks that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
//@@author A0141054W-reused
public class UniqueTaskList implements Iterable<ReadOnlyTask> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateTaskException extends DuplicateDataException {
        protected DuplicateTaskException() {
            super("Operation would result in duplicate tasks");
        }
    }

    /**
     * Signals that an operation targeting a specified person in the list would fail because
     * there is no such matching person in the list.
     */
    public static class TaskNotFoundException extends Exception {}
    
    
    private ObservableList<ReadOnlyTask> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty TaskList.
     */
    public UniqueTaskList() {}

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicateTaskException if the task to add is a duplicate of an existing task in the list.
     */
    public void add(ReadOnlyTask toAdd) throws DuplicateTaskException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalList.add(toAdd);
    }
    /**
     * Edits a task in the list.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     * @throws ParseException 
     * @throws IllegalValueException 
     */
    public boolean edit(ReadOnlyTask taskToEdit, String newDate, String newEndDate, String newTime, String newEndTime, String newContent) 
    		throws TaskNotFoundException, ParseException, IllegalValueException {
    	assert taskToEdit != null;
        
        if(newDate != null){
        	taskToEdit.setDate(newDate);
        }
        
        if(newEndDate != null) {
        	taskToEdit.setEndDate(newEndDate);
        }
        
        if(newTime != null){
        	taskToEdit.setTime(newTime);
        }
        
        if(newEndTime != null){
        	taskToEdit.setEndTime(newEndTime);
        }
        
        if(newContent != null)
        	taskToEdit.setContent(newContent);
        return true;

    }

    /**
     * Removes the equivalent task from the list.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        assert toRemove != null;
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }
    
    /**
     * Add tags to the equivalent task.
     * 
     * @throws TaskNotFoundException if no such task could be found in the list.
     * @throws IllegalValueException if tags are not alphanumerical.
     * @throws DuplicateTagException if task already has similar tag.
     */
    
    public boolean addTags(ReadOnlyTask target, ArrayList<String> tagsToAdd) 
    		throws DuplicateTagException, IllegalValueException, TaskNotFoundException{
    	assert target != null;
    	final boolean tagsAddedToTask = target.addTags(tagsToAdd);
    	if (!tagsAddedToTask) {
            throw new TaskNotFoundException();
        }
        return tagsAddedToTask;
    	
    }
    
    /**
     * Delete tags from the equivalent task.
     * 
     * @throws TaskNotFoundException if no such task could be found in the list.
     * @throws IllegalValueException if tags are not alphanumerical.
     * @throws DuplicateTagException if task already has similar tag.
     */
    
    public boolean deleteTags(ReadOnlyTask target, ArrayList<String> tagsToDel) 
    		throws DuplicateTagException, IllegalValueException, TaskNotFoundException{
    	assert target != null;
    	final boolean tagsDeletedFromTask = target.deleteTags(tagsToDel);
    	if (!tagsDeletedFromTask) {
            throw new TaskNotFoundException();
        }
        return tagsDeletedFromTask;
    	
    }
    
    
    /**
     * Fetch the next date of the task.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean next(ReadOnlyTask toNext) throws TaskNotFoundException {
        assert toNext != null;
        final boolean taskFoundAndMarked = toNext.setNext();
        if (!taskFoundAndMarked) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndMarked;
    }
    
    
    /**
     * Mark the equivalent task as done.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean done(ReadOnlyTask toDone) throws TaskNotFoundException {
        assert toDone != null;
        final boolean taskFoundAndMarked = toDone.setDone();
        if (!taskFoundAndMarked) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndMarked;
    }
    
    /**
     * Mark the equivalent task as undone.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean undone(ReadOnlyTask toUnDone) throws TaskNotFoundException {
        assert toUnDone != null;
        final boolean taskFoundAndMarked = toUnDone.setUndone();
        if (!taskFoundAndMarked) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndMarked;
    }
    
    /**
     * Mark the equivalent task as important.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean important(ReadOnlyTask toImportant) throws TaskNotFoundException {
        assert toImportant != null;
        final boolean taskFoundAndMarked = toImportant.setImportant();
        if (!taskFoundAndMarked) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndMarked;
    }
    
    /**
     * Mark the equivalent task as unimportant.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean unimportant(ReadOnlyTask toUnimportant) throws TaskNotFoundException {
        assert toUnimportant != null;
        final boolean taskFoundAndMarked = toUnimportant.setUnimportant();
        if (!taskFoundAndMarked) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndMarked;
    }
    
    
   
    public ObservableList<ReadOnlyTask> getInternalList() {
        return internalList;
    }

    @Override
    public Iterator<ReadOnlyTask> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
