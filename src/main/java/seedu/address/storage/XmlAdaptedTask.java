package seedu.address.storage;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@@author A0147989B
/**
 * JAXB-friendly version of the Task.
 */
public class XmlAdaptedTask {

    @XmlElement(required = true)
    private Content content;
    @XmlElement(required = true)
    private TaskDate date;
    @XmlElement(required = true)
    private TaskDate endDate;
    @XmlElement(required = true)
    private TaskTime time;
    @XmlElement(required = true)
    private TaskTime endTime;
    @XmlElement(required = true)
    private Integer duration;
    @XmlElement(required = true)
    private boolean done;
    @XmlElement(required = true)
    private boolean important;


    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedTask() {}


    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
        content = source.getContent();
        date = source.getDate();
        endDate = source.getEndDate();
        time = source.getTime();
        endTime = source.getEndTime();
        duration = source.getDuration();
        done = source.getDone();
        important = source.getImportant();
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted taskManager object into the model's TaskManager object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public ReadOnlyTask toModelType() throws IllegalValueException {
        final List<Tag> taskTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            taskTags.add(tag.toModelType());
        }
        final Content content = this.content;
        final TaskDate date = this.date;     
        final TaskDate endDate = this.endDate;
        final TaskTime time = this.time;
        final TaskTime endTime = this.endTime;
        final Integer duration = this.duration;
        final boolean done = this.done;
        final boolean important = this.important;
        final UniqueTagList tags = new UniqueTagList(taskTags);
        if (duration != null) return new RecurringTask(content, date, endDate, time, endTime, duration, done, important, tags);
        else {
            return new Task(content, date, endDate, time, endTime, done, important, tags);
        }
    }
}
