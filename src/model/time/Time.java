package model.time;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Anca on 3/9/2017.
 */
@XmlRootElement(name = "time")
public class Time implements Serializable{
    private int id;
    private String name;
    private String day;

    public Time(){

    }

    public Time(int id, String name, String day) {
        this.id = id;
        this.name = name;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    @XmlAttribute
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
