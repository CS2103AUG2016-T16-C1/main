package seedu.address.testutil;

import java.text.ParseException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskManager;
import seedu.address.model.person.*;

/**
 *
 */
//@@author A0139523E
public class TypicalTestTasks {

    public static TestTask homework, study, exam, activities, tuition, family, groceries, flight, appointment;

    public TypicalTestTasks(){
        try {
            homework =  new TaskBuilder().withContent("Do Homework").withDate("24-08-2016", "25-08-2016").withTime("13:00", "15:00").withDuration(0).withTags("shag").build();
            study = new TaskBuilder().withContent("Study").withDate("28-08-2016", "29-08-2016").withTime("14:00", "15:00").withDuration(0).build();
            exam = new TaskBuilder().withContent("Have Exam").withDate("20-05-2016", "21-05-2016").withTime("10:00", "11:00").withDuration(0).withTags("shaglife", "tired").build();
            activities = new TaskBuilder().withContent("Hall Activities and Study section").withDate("15-06-2016", "16-06-2016").withTime("19:00" ,"23:00").withDuration(0).withTags("fun").build();
            tuition = new TaskBuilder().withContent("Teach Tuition").withDate("21-03-2016", "22-03-2016").withTime("14:00", "15:00").withDuration(0).withTags("money").build();
            family = new TaskBuilder().withContent("Family Event").withDate("02-11-2016", "03-11-2016").withTime("12:00", "13:00").withDuration(0).withTags("love").build();
            groceries = new TaskBuilder().withContent("Get Groceries").withDate("01-12-2016", "02-12-2016").withTime("09:00", "10:00").withDuration(0).withTags("food").build();

            //Manually added
            flight = new TaskBuilder().withContent("Catch Flight").withDate("01-02-2016", "02-02-2016").withTime("13:00", "15:00").withDuration(0).withTags("littleindia").build();
            appointment = new TaskBuilder().withContent("Have Appointment").withDate("12-01-2016", "13-01-2016").withTime("01:00", "03:00").withDuration(0).withTags("early").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible illegal value";
        } catch (ParseException e) {
			e.printStackTrace();
			assert false : "not possible parser exception";
		}
    }

    public static void loadTaskManagerWithSampleData(TaskManager tm) {

        try {
            tm.addTask(new Task(homework));
            tm.addTask(new Task(study));
            tm.addTask(new Task(exam));
            tm.addTask(new Task(activities));
            tm.addTask(new Task(tuition));
            tm.addTask(new Task(family));
            tm.addTask(new Task(groceries));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "not possible";
        }
    }

    public TestTask[] getTypicalTasks() {
        return new TestTask[]{homework, study, exam, activities, tuition, family, groceries};
    }

    public TaskManager getTypicalTaskManager(){
        TaskManager tm = new TaskManager();
        loadTaskManagerWithSampleData(tm);
        return tm;
    }
}
