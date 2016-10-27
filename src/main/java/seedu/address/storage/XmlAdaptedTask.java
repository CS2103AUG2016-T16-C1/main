package seedu.address.storage;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * JAXB-friendly version of the Task.
 */
public class XmlAdaptedTask {

    //@@author A0147989B
    @XmlElement(required = true)
    private Content content;
    @XmlElement(required = true)
    private TaskDate date;
    @XmlElement(required = true)
    private TaskTime time;
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
        time = source.getTime();
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
    public Task toModelType() throws IllegalValueException {
        final List<Tag> taskTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            taskTags.add(tag.toModelType());
        }
        final Content content = this.content;
        final TaskDate date = this.date;     
        final TaskTime time = this.time;
        final Integer duration = this.duration;
        final boolean done = this.done;
        final boolean important = this.important;
        final UniqueTagList tags = new UniqueTagList(taskTags);
        return new Task(content, date, time, duration, done, important, tags);
    }
}
