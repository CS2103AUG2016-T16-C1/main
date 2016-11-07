
# Test Script

> To test the full of Hard2Do TaskManger.

## Getting Started

  Ensure that you have downloaded the latest
  release of Hard2DoV0.5.jar.  

### Setting Up The Sample Data
  1. Run the .jar executable. If it is not your first time using the TaskManager, skip to `3`.
  >TaskManager will start.

  2. Add a floating task: <br>
  e.g `add something`
  >This will create a folder called data in the same directory of the .jar file, if it is the first time running the .jar.

  3. Download or copy the file SampleData.xml from the same directory as this document.

  4. Paste the file SampleData.xml into the folder named data.
  >There should already be an existing .xml called taskmanager.xml inside.

  5. On the TaskManager, type the command: <br>
  `load data/SampleData.xml`
  >You will be prompted to restart the TaskManager

  6. Close the TaskManager or type `exit` and re-run the .jar executable.
  >The 50 tasks from the SampleData.xml will be loaded.


## Adding Tasks
It is recommended to test adding tasks on an empty TaskManager.

** General ** <br>
To view the task details after adding a task, enter
`select` followed by the task index displayed. <br>

Adding a task without a time specified will cause the default time to be 12:00 AM.

### Add A Full Task With Deadlines
  1. Type `add` and press enter.
  > You will be prompted with the proper usage of the AddCommand and the relevant formats. <br>
  > Invalid formats will be blocked with error messages.

  2. Copy and paste the line displayed: <br>
  `add do this task manager sd/20-10-2016 ed/20-10-2016 st/13:00 et/16:00 #shaglife #wheregottime`
  > The TaskManager will indicate that you have sucessfullly added a task and show its details

  3. You can select the data via typing `select` and the task index.
  > The Task Details panel on the right side of the UI will show the relevant details of the task.

### Add A Floating Task
  1. Type `add something` and press enter.
  >A new floating task called something will be added


### Add A Task With Date And Time
  > Ensure that the date and time follow the format: <br>
  `dd-mm-yyyy` <br>

  > End date cannot be earlier than Start date.

  1. Enter `add event sd/12-11-2016 st/12:00` to add a new event with start date 12 NOV 2016 at 12:00

  2.  Enter `add overseas camp sd/10-11-2016 st/10:00 ed/12-11-2016 et/12:00` to add an event across 2 days

  3. Enter `add play game tmr` to add a task with the date of tommorrow.

  4. Enter `add play something at 1pm` to add a task with the time 13:00.

  5. Enter `add workout from 1130am to 12pm` to add a task with a time period

  6. Enter `add try this st/11:30 et/12:00` to add a task with a similar time period.

  7. Enter `add do this next week` to add a task with a date for next week.

  8. Enter `add do it this thursday` to add a task with the relevant date.

### Add Tags To A Task
>Duplciate tags will not be added in.

  1. Enter `addtag 1 CS2103` to add the tag to the first task.

  2. Enter `addtag 1 CS1231 CS1010` to add the tags to a task.

### Delete Tags From A Task
  1. Enter `deltag 1 CS2103` to delete the tag from the first task.

### Mark Task As Done
  1. Enter `done 1` to mark the first task as done. You can also check the checkbox.
  >The check box will become checked. To view the task again you need to use the list command.

### Mark Task As Not Done
>type `list done or list -d` to view all done tasks

  1. Enter  `notdone 1` to mark the first task as not done.
  > The task checkbox will become unchecked.

### Mark Task As Important

  1. Enter `important 1` to mark the first task as Important
  > The indicator will change from green to red.

### Mark Task As Unimportant

  1. Enter `unimportant 1` to mark the first task as Unimportant.
  > The indicator will change from red to green.

### List Commands

  1. Enter `list done` or `list -d`to show all done tasks.
  2. Enter `list undone` or `list -ud` to show all undone tasks.
  3. Enter `list all` or `list -a`to show all tasks.
  4. Enter `list` to show all undone tasks by default.
  5. Enter `list important` or `list -i` to show all important tasks.
  6. Enter `list unimportant` pr `list -ui` to show all unimportant tasks.

### Find Commands
> Recommended to use the Sample Data.

  1. Enter `find shop` to find all tasks which content contains shop as either a word or substring.
  2. Enter `findtag shaglife` to find all tasks with the tag shaglife.

### Delete Command

  1. Enter `delete 2` to delete the second task.

### Undo Command
>all previously saved states will be lost upon exiting the TaskManager.

  1. Enter `undo` to re-add the previously removed task.

  2. Enter `clear` the clear command to remove all tasks.

  3. Enter `undo` to undo the clear.

### Edit Command
>see user guide for relevant start and date values.

  1. Enter `edit 2 sd/12-10-2016` to change the date of the second task.

  2. Enter `edit 3 sd/12-10-2016`.

  3. Enter `edit 4 c/changed` to change the content to changed.

  4. Enter `edit 1 st/12:00` to change the start time to 12:00.

  5. You can test the undo command multiple times by typing `undo`.

  6. Use the date picker and time picker on the Task Detail panel on the right to edit the task via UI.

### Email Command

  1. Ensure you have a gmail account and there are a few (not a lot) unread email inside

  2. Hard2Do will open the browser to a page where you will give permission to Hard2Do. Just give the permission.

  2. Enter `email [EMAIL_ADDRESS]` to add all unread emails from that email address to Hard2Do.

### Add A Recurring Task

  1. Enter `add recur task sd/05-11-2016 r/4`: must ad a date earlier than today in order to test the next command.

### Next Command
  1. Enter `next [INDEX_OF_RECURRING_TASK]` to change the date of the task to the next 4 days later.


### Save Command
  1. Enter `save filepath/something.xml` to save to a new xml file.
