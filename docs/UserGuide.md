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
Adds a task to the to-do list <br>
Format: `add TASKDETAILS sd/DD-MM-YYYY ed/DD-MM-YYYY st/HH:MM et/HH:MM #TAGNAME...` <br>
> * The `presence and order` of arguments after `TASKDETAILS` do not matter except end time and dates `cannot be added` without a start time or date.
> * The format of time is `24 hours` e.g. `19:15`
> * The days and months of dates added can be a single number. e.g. `sd/22-6-2016` or `ed/2-6-2016`
> * `sd/` takes in the start date, `ed/` takes in the end date, `st/` takes in the start time and `et/` takes in the end time
> * Time inputted cannot be represented by a single digit e.g. ~~`9:30`~~

Examples:
* `add Go to CS tutorial sd/10-6-2016 st/12:00 #takethelaptop`
* `add Eat dinner sd/7-10-2016 st/19:00 et/20:00 #hungry #nomoney`
* `add Go for night cycling sd/1-7/2016 ed/2-7-2016 st/22:00 et/04:00 #tiring`
* `add Watch movie st/20:01 #gotmoney`
* `add Do homework sd/28-10-2016`
* `add Have a bbq`

### 4.2 Listing all tasks : `list`
Shows a list of all tasks in the future.<br>
Format: `list`

### 4.3 Finding all tasks containing any keyword in their content : `find`
Finds tasks whose names contain any of the given keywords.<br>
Format: `find KEYWORD [MORE_KEYWORDS]`

> * The search is case sensitive. e.g `cs` will not match `CS`
> * The order of the keywords does not matter. e.g. `CS tut` will match `tut CS`
> * Only the content of the task is searched.
> * Only full words will be matched e.g. `CS` will not match `CSS`
> * Persons matching at least one keyword will be returned (i.e. `OR` search).
    e.g. `CS` will match `CS tutorial`

Examples:
* `find tutorial`<br>
  Returns `Go to CS tutorial` but not `tutorial`
* `find CS EE CEG`<br>
  Returns ANY tasks having content `CS`, `EE`, or `CEG`

### 4.4 Deleting a task : `delete`
Deletes the specified task from the address book. Irreversible.<br>
Format: `delete INDEX`

> Deletes the task at the specified `INDEX`.
  The index refers to the index number shown in the current listing.<br>
  The index **must be a positive integer** 1, 2, 3, ...

Examples:
* `list`<br>
  `delete 2`<br>
  Deletes the 2nd task in the address book.
* `find tutorial`<br>
  `delete 1`<br>
  Deletes the 1st task in the results of the `find` command.

### 4.5 Select a task : `select`
Selects the task identified by the index number used in the last task listing.<br>
Format: `select INDEX`

> Selects the task and loads the detail of the task at the specified `INDEX`.
  The index refers to the index number shown in the current listing.<br>
  The index **must be a positive integer** 1, 2, 3, ...

Examples:
* `list`<br>
  `select 2`<br>
  Selects the 2nd task in the to-do list.
* `find CS` <br>
  `select 1`<br>
  Selects the 1st task in the results of the `find` command.

### 4.6 Edit a task : `edit`
Edits the current selected task.<br>
Format: `edit DETAIL/NEW_DETAIL_INFO [OTHER_DETAILS/OTHER_NEW_DETAILS]`

> Edits the current selected task and updates the relevant `DETAIL`. <br>
  The detail refers to a specific detail of the selected task.<br>
  The detail **must be a valid detail** d/, t/, c/ . <br>
  Repetition of detail type is not allowed..

Examples:
* `list`<br>
  `select 2` <br>
  `edit /t1800`<br>
  Updates the time in the task selected by the `select` command to 1800.
* `list` <br>
  `select 3`<br>
  `edit d/2016.10.07 t/1800 c/update details`
  Updates all details in the task selected by the `select` command.

### 4.7 Clearing all entries : `clear`
Clears all entries from the address book.<br>
Format: `clear`  

### 4.8 Exiting the program : `exit`
Exits the program.<br>
Format: `exit`  

### 4.9 Viewing help : `help`
Format: `help`

> Help is also shown if you enter an incorrect command e.g. `abcd`


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
AddTag | `addtag TAGNAME`
DeleteTag | `deltag TAGNAME`
Undo | `undo`
List | `list`
List By Progress | `list done/undone`
Done | `done INDEX`
Undone | `undone INDEX`
Important | `important INDEX`
Unimportant | `unimportant INDEX`
Load | `load /FILENAME`
Help | `help`
Select | `select INDEX`
Exit | `exit`
