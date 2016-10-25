package seedu.address.model;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.util.StringUtil;
import seedu.address.commons.events.model.TaskManagerChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.core.ComponentManager;
import seedu.address.model.History.StateNotFoundException;
import seedu.address.model.person.ReadOnlyTask;
import seedu.address.model.person.Task;
import seedu.address.model.person.UniqueTaskList;
import seedu.address.model.person.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.person.UniqueTaskList.TaskNotFoundException;
import seedu.address.model.tag.Tag;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the task manager data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private TaskManager taskManager;
    private FilteredList<Task> filteredTasks;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	taskManager.removeTask(target);
        indicateTaskManagerChanged();
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
    	try {
			taskManager.save("add");
		} catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	taskManager.addTask(task);
    	updateFilteredListToShowUndone();
        indicateTaskManagerChanged();
    }

    @Override
    public synchronized void editTask(int targetIndex, String newDate, String newTime, String newEndTime, String newContent)
    		throws TaskNotFoundException, ParseException {
    	try {
			taskManager.save("edit");
		} catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	taskManager.editTask(targetIndex, newDate, newTime, newEndTime, newContent);
        updateFilteredListToShowUndone();
        indicateTaskManagerChanged();

    }

    @Override
    public synchronized void addTags(ReadOnlyTask target, ArrayList<String> newTags)
    		throws TaskNotFoundException, ParseException, IllegalValueException {
    	try {
			taskManager.save("addTag");
		} catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			taskManager.save("deleteTag");
		} catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	taskManager.deleteTags(target, tagsToDelete);

        updateFilteredListToShowDone();
    	indicateTaskManagerChanged();
    }
    
    @Override
    public synchronized void doneTask(ReadOnlyTask target) throws TaskNotFoundException {
    	try {
			taskManager.save("done");
		} catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        taskManager.markTaskAsUndone(target);
        logger.info("successfully mark as undone"+target.getDone());
        updateFilteredListToShowUndone();
        indicateTaskManagerChanged();
    }
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
        filteredTasks.setPredicate((Task t) -> t.getDone());
    }

    public void updateFilteredListToShowUndone() {
        filteredTasks.setPredicate(null);
        filteredTasks.setPredicate((Task t) -> !t.getDone());
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords){
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }

    public void updateFilteredTaskList(String toFind){

    	filteredTasks.setPredicate((Task t) -> new Scanner(t.getContent().value).findInLine(toFind) != null);
    	Comparator<Task> byEditDistance = new Comparator<Task>() {
    	    public int compare(Task left, Task right) {
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
    	filteredTasks.setPredicate((Task t) -> t.getTags().hasTag(tagToFind));

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
    //TODO
    //Check in future: find command
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
