package model.event;

import com.sun.org.apache.xpath.internal.operations.Bool;
import model.resource.Resources;
import model.time.Time;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Anca on 1/17/2017.
 */
@XmlRootElement(name = "event")
public class Event implements Serializable{
    private String id;
    private Time time;
    private Resources resources;
    private String description;
    private Boolean toAdd;

    public Event(){

    }

    public Event(String id, Time time, Resources resources, String description) {
        this.id = id;
        this.time = time;
        this.resources = resources;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getToAdd() {
        return toAdd;
    }

    public void setToAdd(Boolean toAdd) {
        this.toAdd = toAdd;
    }
}
