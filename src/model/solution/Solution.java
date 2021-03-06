package model.solution;

import model.event.Events;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "solution")
public class Solution implements Serializable {
    private String id;
    private String description;
    private Events events;
    private Report report;

    public Solution(){

    }

    public Solution(String id, String description, Events events, Report report) {
        this.id = id;
        this.description = description;
        this.events = events;
        this.report = report;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }
}
