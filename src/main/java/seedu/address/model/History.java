package seedu.address.model;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.person.Content;
import seedu.address.model.person.ReadOnlyTask;
import seedu.address.model.person.RecurringTask;
import seedu.address.model.person.Task;
import seedu.address.model.person.TaskDate;
import seedu.address.model.person.TaskTime;
import seedu.address.model.person.UniqueTaskList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;


import java.text.ParseException;
import java.util.*;

/**
 * A list of states that records previous states of the task manager before changes.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
//@@author A0135787N

public class History {
	
	
	public static class StateNotFoundException extends Exception {} 
	
	
	private Stack<List<ReadOnlyTask>> taskStates;
	private Stack<Collection<Tag>> tagStates;
	private Stack <String> messages;
	private List<ReadOnlyTask> tasksState;
	private Collection<Tag> tagsState;
	
	private String message;

	/**

	 * Constructor that initialises History class whenever Hard2Do is started
	 */
	
	History(){
		taskStates =  new Stack <List<ReadOnlyTask>>(); 
		messages = new Stack<String>();
		tagStates = new Stack <Collection<Tag>>();
		tagsState = FXCollections.observableArrayList();
	}
	
	/*
	 * method to save and store the existing state of the TaskManger before any overwrite operations
	 */
	public void save(ObservableList<ReadOnlyTask> stateToSave, ObservableList<Tag> tagsToSave, String commandType) 
			throws IllegalValueException, ParseException{
		
		if (stateToSave.isEmpty())
			return;
		
		ObservableList<ReadOnlyTask> newState = FXCollections.observableArrayList();
		
		//Create deep copy of tasks
		for(ReadOnlyTask t : stateToSave){
			
			Set<Tag> tagSet = new HashSet<>();
	        for (Tag tag : t.getTags().toSet()) {
	            tagSet.add(new Tag(tag.tagName));
	        }
	        

	        TaskDate td = new TaskDate(t.getDate());
	        TaskDate ed = new TaskDate(t.getEndDate());
	        TaskTime tt = new TaskTime(t.getTime());
	        TaskTime et = new TaskTime(t.getEndTime());
	        
	        Integer duration = null;
	        
	        //@@author A0147989B
			if (t.getDuration() != null) 
			    newState.add( new RecurringTask( new Content(t.getContent().value), 
					td,
					tt,
					duration,
					new UniqueTagList(tagSet))
					);
			else { //@@author
	        	newState.add( new Task( new Content(t.getContent().value),
	        			td,
	        			ed,
	        			tt,
	        			et,
	        			new UniqueTagList(tagSet))
	        			);
			}

			if(t.getDone())
				newState.get(newState.size() - 1).setDone();
			
	        if(t.getImportant())
	        	newState.get(newState.size() - 1).setImportant();
		}
		//Store the current state of the TaskManger into Stacks
		taskStates.push(newState);
		messages.push(commandType);
		
		if(!tagsToSave.isEmpty()){
			Collection <Tag> newTags = FXCollections.observableArrayList();
			for(Tag g : tagsToSave){
				newTags.add(new Tag(g.tagName));
			}
			tagStates.push(newTags);
		}
	}
	/**
	 * Sets the state before last change
	 */
	public void undo() {
		
		if(taskStates.isEmpty())
			return;
		
		tasksState = taskStates.pop();
		message = messages.pop();
		if(tagsState.isEmpty())
			return;
		tagsState = tagStates.pop();
	}
	
	//Operations to retrieve previous state of Tasks and Tags
	
	public List<ReadOnlyTask> getPreviousTasks(){
		return tasksState;
	}
	
	public Collection<Tag> getPreviousTags(){
		return tagsState;
	}
	
	public boolean isEmpty(){
		return taskStates.isEmpty();
	}
	public String getMessage(){
		return message;
	}
}
