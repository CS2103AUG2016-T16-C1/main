package hard2do.taskmanager.model;

import javafx.collections.transformation.FilteredList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import hard2do.taskmanager.commons.core.ComponentManager;
import hard2do.taskmanager.commons.core.LogsCenter;
import hard2do.taskmanager.commons.core.UnmodifiableObservableList;
import hard2do.taskmanager.commons.events.model.TaskManagerChangedEvent;
import hard2do.taskmanager.commons.exceptions.IllegalValueException;
import hard2do.taskmanager.commons.util.StringUtil;
import hard2do.taskmanager.model.History.StateNotFoundException;
import hard2do.taskmanager.model.tag.Tag;
import hard2do.taskmanager.model.task.ReadOnlyTask;
import hard2do.taskmanager.model.task.Task;
import hard2do.taskmanager.model.task.UniqueTaskList;
import hard2do.taskmanager.model.task.UniqueTaskList.DuplicateTaskException;
import hard2do.taskmanager.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Represents the in-memory model of the task manager data.
 * All changes to any model should be synchronized.
 */
//@@author A0141054W-reused
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private TaskManager taskManager;
    private FilteredList<ReadOnlyTask> filteredTasks;

    /**
     * Initializes a ModelManager with the given TaskManager
     * TaskManager and its variables should not be null
     */
    public ModelManager(TaskManager src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with task manager: " + src + " and user prefs " + userPrefs);

        taskManager = new TaskManager(src);
        filteredTasks = new FilteredList<>(taskManager.getTasks());
    }

    public ModelManager() {
        this(new TaskManager(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskManager initialData, UserPrefs userPrefs) {
        taskManager = new TaskManager(initialData);
        filteredTasks = new FilteredList<>(taskManager.getTasks());
    }

    @Override
    public void resetData(ReadOnlyTaskManager newData) throws IllegalValueException, ParseException {
    	try {
			taskManager.save("clear");
		} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
        taskManager.resetData(newData);
        indicateTaskManagerChanged();
    }

    @Override
    public ReadOnlyTaskManager getTaskManager() {
        return taskManager;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskManagerChanged() {
        raise(new TaskManagerChangedEvent(taskManager));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
    	try {
			taskManager.save("delete");
    	} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
    	taskManager.removeTask(target);
        indicateTaskManagerChanged();
    }

    @Override
    public synchronized void addTask(ReadOnlyTask task) throws UniqueTaskList.DuplicateTaskException {
    	try {
			taskManager.save("add");
    	} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
    	taskManager.addTask(task);
    	updateFilteredListToShowUndone();
        indicateTaskManagerChanged();
    }

    @Override
    public synchronized void editTask(ReadOnlyTask target, String newDate, String newEndDate, 
    		String newTime, String newEndTime, String newContent)
    		throws TaskNotFoundException, ParseException, IllegalValueException {
    	try {
			taskManager.save("edit");
    	} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
    	taskManager.editTask(target, newDate, newEndDate, newTime, newEndTime, newContent);
        indicateTaskManagerChanged();
    }

    @Override
    public synchronized void addTags(ReadOnlyTask target, ArrayList<String> newTags)
    		throws TaskNotFoundException, ParseException, IllegalValueException {
    	try {
			taskManager.save("addTag");
    	} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
    	taskManager.addTags(target, newTags);

        updateFilteredListToShowUndone();
    	indicateTaskManagerChanged();
    }

    @Override
    public synchronized void deleteTags(ReadOnlyTask target, ArrayList<String> tagsToDelete)
    		throws TaskNotFoundException, ParseException, IllegalValueException {
    	assert !(tagsToDelete.size() == 0);
    	try {
			taskManager.save("delTag");
    	} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
    	taskManager.deleteTags(target, tagsToDelete);

        updateFilteredListToShowUndone();
    	indicateTaskManagerChanged();
    }
    
    
    //@@author A0147989B 
    @Override
    public synchronized void nextTask(ReadOnlyTask target) throws TaskNotFoundException {
        try {
            taskManager.save("next");
        } catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
        taskManager.fetchNextDate(target);
        updateFilteredListToShowUndone();
        indicateTaskManagerChanged();

    }
    
    @Override
    public synchronized void doneTask(ReadOnlyTask target) throws TaskNotFoundException {
    	try {
			taskManager.save("done");
    	} catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
        taskManager.markTaskAsDone(target);
        logger.info("successfully mark as done"+target.getDone());
        updateFilteredListToShowUndone();
        indicateTaskManagerChanged();
    }
    
    @Override
    public synchronized void undoneTask(ReadOnlyTask target) throws TaskNotFoundException {
        try {
            taskManager.save("undone");
        } catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
        taskManager.markTaskAsUndone(target);
        logger.info("successfully mark as undone"+target.getDone());
        updateFilteredListToShowUndone();
        indicateTaskManagerChanged();
    }
    
    @Override
    public synchronized void importantTask(ReadOnlyTask target) throws TaskNotFoundException {
        try {
            taskManager.save("important");
        } catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
        taskManager.markTaskAsImportant(target);
        logger.info("successfully mark as important"+target.getImportant());
        updateFilteredListToShowUndone();
        indicateTaskManagerChanged();
    }
    
    @Override
    public synchronized void unimportantTask(ReadOnlyTask target) throws TaskNotFoundException {
        try {
            taskManager.save("unimportant");
        } catch (IllegalValueException e) {
			assert false : "State must always be valid.";
		} catch (ParseException e) {
			assert false : "Values have already been validated.";
		}
        taskManager.markTaskAsUnimportant(target);
        logger.info("successfully mark as unimportant"+target.getImportant());
        updateFilteredListToShowUndone();
        indicateTaskManagerChanged();
    }
    //@@author
    
    @Override
    public synchronized void save(String commandType) throws IllegalValueException, ParseException{
    	taskManager.save(commandType);
    }
    @Override
    public synchronized void undo() throws StateNotFoundException{
    	taskManager.undo();
    	updateFilteredListToShowUndone();
    	indicateTaskManagerChanged();
    }

    //=========== Filtered Person List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }

    public void updateFilteredListToShowDone() {
        filteredTasks.setPredicate(null);
        filteredTasks.setPredicate((ReadOnlyTask t) -> t.getDone());
    }

    public void updateFilteredListToShowUndone() {
        filteredTasks.setPredicate(null);
        filteredTasks.setPredicate((ReadOnlyTask t) -> !t.getDone());
    }
    
    public void updateFilteredListToShowImportant() {
    	filteredTasks.setPredicate(null);
    	filteredTasks.setPredicate((ReadOnlyTask t) -> t.getImportant());
    }
    
    public void updateFilteredListToShowUnimportant(){
    	filteredTasks.setPredicate(null);
    	filteredTasks.setPredicate((ReadOnlyTask t) -> !t.getImportant());
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords){
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }

    public void updateFilteredTaskList(String toFind){

    	filteredTasks.setPredicate((ReadOnlyTask t) -> 
    		new Scanner(t.getContent().value.toLowerCase()).findInLine(toFind) != null);
    	
    	Comparator<ReadOnlyTask> byEditDistance = new Comparator<ReadOnlyTask>() {
    	    public int compare(ReadOnlyTask left, ReadOnlyTask right) {
    	        if (left.getContent().value.length() > right.getContent().value.length()) {
    	            return -1;
    	        } else {
    	            return 1;
    	        }
    	    }
    	};

    	filteredTasks.sorted(byEditDistance);
    }

    public void updateFilteredTaskList(Tag tagToFind){
    	filteredTasks.setPredicate((ReadOnlyTask t) -> t.getTags().hasTag(tagToFind));

    }

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getContent().value, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }
    
	@Override
	public History getHistory() {
		return taskManager.getHistory();
	}
}
