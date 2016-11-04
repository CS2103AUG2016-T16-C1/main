# User Guide

 [2 About Hard2Do](#2-about-hard2do)<br>
 [3 Getting Started](#3-getting-started)<br>
 [3.1 Prerequisites](#31-prerequisites) <br>
 [3.2 Launch](#32-launch) <br>
 [3.3 Commands](#33-commands) <br>
 [4 Features](#4-features-)<br>
 [4.1 Adding tasks](#41-adding-tasks--add) <br>
 [4.2 Listing all tasks](#42-listing-all-tasks--list) <br>
 [4.3 Finding all tasks containing any keyword in their content](#43-finding-all-tasks-containing-any-keyword-in-their-content--find) <br>
 [4.4 Deleting a task](#44-deleting-a-task--delete) <br>
 [4.5 Select a task](#45-select-a-task--select) <br>
 [4.6 Edit a task](#46-edit-a-task--edit) <br>
 [4.7 Clearing all entries](#47-clearing-all-entries--clear) <br>
 [4.8 Exiting the program](#48-exiting-the-program--exit) <br>
 [4.9 Viewing help](#49-viewing-help--help) <br>
 [5 FAQ](#5-faq) <br>
 [6 Command Summary](#6-command-summary)<br>

## 2 About Hard2Do

  >Are you tired of having too many tasks on your mind and constantly being frustrated of not having an avenue to list them down? Fret not because Hard2Do is here to save you from all your daily migraines that will soon be a thing of the past.

  >Hard2Do is a personal scheduler that enables you to manage your daily tasks in an easy and flexible manner. It will keep your added tasks in order and remind you when the deadline is approaching. Unlike other task managers that you have encountered before, instead of being inconvenienced by the use of a mouse, all it takes is just a single line of command to get things done. No longer will you have to be burdened by the use of a mouse as everything can be done just on the keyboard itself! Furthermore, this is just the beginning of the multitude of functions that Hard2Do is able to perform.

  >Read on to find out more!

## 3 Getting Started

### 3.1 Prerequisites
1. *Java Version 1.8.0_60* <br>
   Before you can get started, ensure you have Java version `1.8.0_60` or later installed in your Computer. <br>
   >Having any Java 8 version is not enough. <br>
   This app will not work with earlier versions of Java 8.

2. *Hard2Name.exe* <br>
 Download the latest `Hard2Name.jar` from the [releases](../../../releases) tab.

### 3.2 Launch
Now you can finally begin your journey with `Hard2Do` Task Manager. <br>
1. Copy the file to the folder you want to use as the home folder for your `Hard2Do` list.<br>

2. Double-click the file to start the app. The GUI should appear in a few seconds. <br>

   > <img src="images/Ui.png" width="600">

### 3.3 Commands
4. Type the command in the command box and press <kbd>Enter</kbd> to execute it. <br>
   e.g. typing **`help`** and pressing <kbd>Enter</kbd> will open the help window.
5. Some example commands you can try:
   * **`list`** : lists all tasks to do in the future
   * **`add`**` go to tutorial sd/10-12-2016 st/12:00 #sadlife ` :
     adds a task of `Go to tutorial` to the `Hard2Do` Task Manager.
   * **`delete`**` 3` : deletes the 3rd task shown in the current list
   * **`exit`** : exits the app
   * **`edit`**` 3 c/do homework ` :
      updates the contents in the 3rd task shown in the current list
6. Refer to the [Features](#features) section below for details of each command.<br>


## 4 Features <br>
In the guide below, we will showcase how to execute the various features that `Hard2Do` has.
> **Command Format**
> * Words in `UPPER_CASE` are the parameters.
> * Items in `SQUARE_BRACKETS` are optional.
> * Items with `...` after them can have multiple instances.
> * Not all orders of parameters are fixed.

### 4.1 Adding tasks : `add`
*4.1.1 Adds a floating task to `Hard2Do`* <br>
Format: `add TASKDETAILS` <br>

Examples:
* `add Have a bbq`
* `add Do CS2103 tutorial` <br>

*4.1.2 Adds a task with date and time to `Hard2Do`* <br>
Format: `add TASKDETAILS sd/DD-MM-YYYY ed/DD-MM-YYYY st/HH:MM et/HH:MM #TAGNAME...` <br>
> * The `presence and order` of arguments after `TASKDETAILS` do not matter except end time and dates `cannot be added` without a start time or date.
> * The format of time is `24 hours` e.g. `19:15`
> * The days and months of dates added can be a single number. e.g. `sd/22-6-2016` or `ed/2-6-2016`
> * `sd/` takes in the start date, `ed/` takes in the end date, `st/` takes in the start time and `et/` takes in the end time
> * `#` takes in the tags assigned to that task and multiple tags can be added
> * Time inputted cannot be represented by a single digit e.g. ~~`9:30`~~

Examples:
* `add Go to CS tutorial sd/10-6-2016 st/12:00 #takethelaptop`
* `add Eat dinner sd/7-10-2016 st/19:00 et/20:00 #hungry #nomoney`
* `add Go for night cycling sd/1-7/2016 ed/2-7-2016 st/22:00 et/04:00 #tiring`
* `add Watch movie st/20:01 #gotmoney`
* `add Do homework sd/28-10-2016`

*4.1.3 Adds a task with date to `Hard2Do` using shortcuts* <br>
Format: `add TASKDETAILS KEYWORDS`
> * This command can be used in combination with `4.1.2` where the start date is replaced with the `KEYWORDS`
> * The available `KEYWORDS` are `tmr, tomorrow, next week, next wed, this wed`
> * `next week` adds to the task a date which is 7 days later
> * The keyword following `this` and `next` can be of any day of the week e.g. `this thurs, next mon`
> * The format of the day keyword can be in `short form` e.g. `wed, thurs`
> * `this DAYOFTHEWEEK` refers to the nearest instance of that day while `next DAYOFTHEWEEK` refers to the 2nd nearest instance of that day

Examples:
* `add meeting tmr #shag` <br>
  Adds a task with task details "meeting" for tomorrow's date
* `add dinner this wed` <br>
Adds a task with task details "dinner" for the nearest wednesday
* `add study for exam next week` <br>
Adds a task with task details "study for exam" for 7 days later from the current date
* `add wash clothes next fri` <br>
Adds a task with task details "wash clothes" for the next friday after the nearest one

*4.1.4 Adds a task with time to `Hard2Do` using shortcuts* <br>
Format: `add TASKDETAILS at STARTTIME` or `add TASKDETAILS from STARTTIME to ENDTIME`
> * This command can be used with `4.1.3` and `4.1.2` where the start time and end time are replaced with `STARTTIME` and `ENDTIME` respectively
> * `STARTIME` and `ENDTIME` has the format of `am` and `pm` e.g. `at 3pm` or `from 2pm to 3pm`


Examples:
* `add submit report tmr at 5pm #shag`
* `add dance lesson next wed from 5pm to 6pm #fun`

### 4.2 Adding tags : `addtag`
Adds tags to the specified task <br>
Format: `addtag INDEX TAGNAME...`
> * `INDEX` refers to the index number shown in the current listing<br>
> * The index **must be a positive integer** 1, 2, 3, ...
> * Multiple tags can be added

Examples:
* `list` <br>
`addtag 1 CS2103` <br>
Adds tag "CS2103" to the 1st task in the list
* `list` <br>
`addtag 2 shag tough tired` <br>
Adds tags "shag", "tough" and "tired" to the 2nd task in the list

### 4.3 Listing tasks : `list`
*4.3.1 List all undone tasks* <br>
Shows a list of all undone tasks added.<br>
Format: `list`

*4.3.2 List `all` tasks added* <br>
Shows the entire list of tasks that are added <br>
Format: `list all` or `list -a`
> * This command shows all added task, regardless of priority or status

*4.3.3 List all tasks that are marked as `done`* <br>
Shows a list of all tasks that are marked as done <br>
Format: `list undone` or `list -ud`

*4.3.4 List all tasks that are marked as `important`* <br>
Shows a list of all tasks that are marked as important <br>
Format: `list important` or `list -i`

*4.3.5 List all tasks that are marked as `unimportant`* <br>
Shows a list of all tasks that are marked as unimportant <br>
Format: `list unimportant` or `list -ui`

### 4.4 Finding a task: `find`
Finds tasks whose names contain any of the given keywords.<br>
Format: `find KEYWORD [MORE_KEYWORDS]`

> * The search is case insensitive. e.g `cs` will match `CS`
> * The order of the keywords does not matter. e.g. `CS tut` will match `tut CS`
> * Only the content of the task is searched.
> * Partial words will also be matched e.g. `CS` will match `CSS`
> * Persons matching at least one keyword will be returned (i.e. `OR` search).
    e.g. `CS` will match `CS tutorial`

Examples:
* `find tutorial`<br>
  Returns `Go to CS TUTORIAL`
* `find CS EE CEG`<br>
  Returns ANY tasks having content `CS`, `EE`, or `CEG`
* `find lab` <br>
  Returns `label cup` and `go to lab`

### 4.5 Finding a tag: `findtag`
Finds all tasks with the specified tag given <br>
Format: findtag TAGNAME
> * This command is only able to find `one` tag
> * The search is `case insensitive`
> * Only full words will be matched e.g. `CS21` will not match `CS2103`

Examples:
* `findtag CS2103` <br>
Returns ANY tasks which have the tag "CSS2103"
* `findtag homework` <br>
Returns ANY tasks which have the tag "homework"

### 4.6 Deleting a task : `delete`
Deletes the specified task from `Hard2Do`. This command is irreversible once `Hard2Do` is closed.<br>
Format: `delete INDEX`

> * Deletes the task at the specified `INDEX` <br>
> * The index refers to the index number shown in the current listing<br>
> * The index **must be a positive integer** 1, 2, 3, ...
> * Task deleted can be restored by using the `undo` command as long as it is within the same usage

Examples:
* `list`<br>
  `delete 2`<br>
  Deletes the 2nd task in `Hard2Do`.
* `find tutorial`<br>
  `delete 1`<br>
  Deletes the 1st task in the results of the `find` command.

### 4.7 Deleting tags from task : `deltag`
Deletes tags from the specific task <br>
Format: `deltag INDEX TAGNAMES...`
> * The `INDEX` refers to the index number shown in the current listing<br>
> * The index **must be a positive integer** 1, 2, 3, ...
> * This command is `case insensitive`
> * Multiple tags can be deleted

Examples:
* `list` <br>
  `deltag 1 cs2103` <br>
  Deletes tag "CS2103" from the 1st task
* `list` <br>
  `deltag 3 everything something nothing` <br>
  Deletes tags "everything", "something" and "nothing" from the 3rd task

### 4.8 Undo an action : `undo`
Undo the previous action <br>
Format: `undo`
> * Only commands that change the state of `Hard2Do` can be undone

### 4.9 Selecting a task : `select`
Selects the task identified by the index number used in the last task listing.<br>
Format: `select INDEX`

> * Selects the task and loads the details of the task at the specified `INDEX` <br>
> * The `INDEX` refers to the index number shown in the current listing<br>
> * The index **must be a positive integer** 1, 2, 3, ...

Examples:
* `list`<br>
  `select 2`<br>
  Selects and shows the details of the 2nd task in the to-do list.
* `find CS` <br>
  `select 1`<br>
  Selects and shows the details of the 1st task in the results of the `find` command.

### 4.10 Editing a task : `edit`
Edits the task identified by the index number used in the last task listing.<br>
Format: `edit INDEX c/TASKDETAILS sd/DD-MM-YYYY ed/DD-MM-YYYY st/HH:MM et/HH:MM`

> * Edits the current selected task and updates the edited details <br>
> * Tags can not be edited using this command<br>
> * The `presence and order` of arguments after `TASKDETAILS` do not matter <br>
> * The `INDEX` refers to the index number shown in the current listing <br>
> * The index **must be a positive integer** 1, 2, 3, ...
> * Repetition of detail type is not allowed

Examples:
* `list`<br>
  `edit 2 st/18:00`<br>
  Updates the start time of the 2nd task to 18:00.
* `list` <br>
  `edit 3 sd/7-10-2016 st/1800 c/update details` <br>
  Updates the start date, start time and task detail of the 3rd task.

### 4.11 Mark task as important : `important`
Sets the specified task of the current task listing to important priority <br>
Format: `important INDEX`
> * Indicator next to the task name turns from `green` to `red`
> * The `INDEX` refers to the index number shown in the current listing <br>
> * The index **must be a positive integer** 1, 2, 3, ...

Examples:
* `important 2` <br>
Changes the priority of the 2nd task to important
* `list` <br>
`important 3` <br>
Changes the priority of the 3rd task in the current task listing to important

### 4.12 Mark task as unimportant : `unimportant`
Sets the specified task of the current task listing to unimportant priority <br>
Format: `unimportant INDEX`
> * Indicator next to the task name turns from `red` to `green`
> * The `INDEX` refers to the index number shown in the current listing <br>
> * The index **must be a positive integer** 1, 2, 3, ...

Examples:
* `unimportant 1` <br>
Changes the priority of the 1st task to unimportant
* `list` <br>
`unimportant 3` <br>
Changes the priority of the 3rd task in the current task listing to unimportant

### 4.13 Mark task as done
Changes the state of a task to done when it is completed <br>
Format: `done INDEX`
> * The checkbox next to the task name becomes ticked
> * The `INDEX` refers to the index number shown in the current listing <br>
> * The index **must be a positive integer** 1, 2, 3, ...

Examples:
* `done 1` <br>
Marks the 1st task as done
* `list` <br>
`done 3` <br>
Marks the 3rd task in the current task listing as done

### 4.14 Mark task as undone
Changes the state of a task to undone <br>
Format: `undone INDEX`
> * The checkbox next to the task name becomes unticked
> * The `INDEX` refers to the index number shown in the current listing <br>
> * The index **must be a positive integer** 1, 2, 3, ...

Examples:
* `undone 1` <br>
Marks the 1st task as undone
* `list` <br>
`undone 2`
Marks the 2nd task in the current task listing as undone

### 4.15 Clearing all entries : `clear`
Clears all entries in `Hard2Do`. <br>
Format: `clear`  
> * This command can still be undone if `Hard2Do` has not been closed

### 4.16 Load tasks : `load`
Loads a previous saved state of `Hard2Do`
Format: `load FILELOCATION`
> * `Hard2Do` has to be restarted after the command is used
> * `FILELOCATION` includes the `filename` e.g. `data/taskmanager.xml`

Examples:
* `load data/taskmanager.xml` <br>
* `load data/taskmanager2.xml`

### 4.17 Exiting the program : `exit`
Exits and closes `Hard2Do`<br>
Format: `exit`  

### 4.18 Viewing help : `help`
Opens up the link to our user guide where the format of commands can be referred from. <br>
Format: `help`

> * The proper format of commands will also be shown if you enter an incorrect command e.g. `abcd`


#### Saving the data
Hard2Do list data are saved in the hard disk automatically after any command that changes the data.<br>
There is no need to save manually.

## 5 FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with
       the file that contains the data of your previous Hard2Do list folder.

## 6 Command Summary

Command | Format  
-------- | :--------
Add | `add CONTENT sd/DD-MM-YYYY ed/DD-MM-YYYY st/HH-MM et/HH-MM r/NUMBER #TAGS`
Clear | `clear`
Delete | `delete INDEX`
Edit | `edit INDEX c/CONTENT sd/DD-MM-YYYY ed/DD-MM-YYYY st/HH-MM et/HH-MM #TAGS`
Find | `find KEYWORD [MORE_KEYWORDS]`
FindTag | `findtag TAGNAME`
AddTag | `addtag INDEX TAGNAME`
DeleteTag | `deltag INDEX TAGNAME`
Undo | `undo`
List | `list`
List All | `list all` or `list -a`
List Done | `list done` or `list -d`
List Important | `list important` or `list -i`
List Unimportant | `list unimportant` or `list -ui`
Done | `done INDEX`
Undone | `undone INDEX`
Important | `important INDEX`
Unimportant | `unimportant INDEX`
Next | `next INDEX`
Load | `load FILENAME`
Help | `help`
Select | `select INDEX`
Exit | `exit`
