package compute;

import model.Metadata;
import model.Timetable;
import model.constraint.Constraint;
import model.constraint.Constraints;
import model.event.Event;
import model.event.Events;
import model.resource.Resource;
import model.resource.ResourceTypes;
import model.resource.Resources;
import model.solution.Solution;
import model.time.Time;
import model.time.Times;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Anca on 1/18/2017.
 */
public class DataLoad {
    private static String loadPath = new String("D:\\Dropbox\\Licenta\\CodeHour (github)\\CodeHour\\CodeHourXMLs\\");

    public static Timetable loadDataFromXML(){
        Timetable timetable = new Timetable();
        try{
            //Loading timetable
            File file = new File(loadPath + "timetable.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Timetable.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            timetable= (Timetable) jaxbUnmarshaller.unmarshal(file);
            List<Event> eventList = timetable.getEvents().getEvents();
//            System.out.println("Event list:");
//            for(Event e: eventList) {
//                System.out.println(e.getId());
//                for (Resource r : e.getResources().getResources()){
//                    System.out.println(r.getId()+" "+ r.getName()+" "+r.getResourceType());
//                }
//            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return timetable;
    }

    public static void loadSolvedTimetableToXML(Timetable timetable){
        try {
            File timetableFile = new File(loadPath + "solvedTimetable.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Timetable.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(timetable, timetableFile);
//            marshaller.marshal(events, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static Timetable loadDataToXML() {
        Metadata metadata = new Metadata("Example Timetable", "Anca Ad", new Date(), "");

        List<String> weekdays = Arrays.asList("monday", "tuesday", "wednesday","thursday", "friday");
        List<Time> timeList = new ArrayList<>();

        for(int day = 0; day<5; day++) {
            for (int hour = 0; hour < 6; hour++) {
                Time time = new Time(new StringBuilder().append(String.valueOf(hour+1)).append("_").append(weekdays.get(day).substring(0, 3)).toString(), weekdays.get(day));
                timeList.add(time);
            }
        }

        Times times = new Times();
        times.setTimes(timeList);

        List<String> rtList = Arrays.asList("teacher", "studyGroup", "classroom");
        ResourceTypes resourceTypes = new ResourceTypes(rtList);

        List<Resource> resourceList = Arrays.asList(
                //Teachers
                new Resource("dpop", "Dan Pop", rtList.get(0)),
                new Resource("catnis", "Catalina Ilas", rtList.get(0)),
                new Resource("vhariuc", "Virgil Hariuc", rtList.get(0)),
                new Resource("asava", "Ana Savascu", rtList.get(0)),
                new Resource("doniciuc", "Daniel Oniciuc", rtList.get(0)),
                //StudyGroups
                new Resource("9A", "", rtList.get(1)),
                new Resource("9B", "", rtList.get(1)),
                new Resource("10A", "", rtList.get(1)),
                new Resource("10B", "", rtList.get(1)),
                new Resource("11A", "", rtList.get(1)),
                new Resource("12A", "", rtList.get(1)),
                //Classrooms
                new Resource("101", "", rtList.get(2)),
                new Resource("102", "", rtList.get(2)),
                new Resource("103", "", rtList.get(2)),
                new Resource("104", "", rtList.get(2)),
                new Resource("201", "", rtList.get(2)),
                new Resource("202", "", rtList.get(2))

        );

        Resources resources = new Resources(resourceList);

        List<Event> eventList = Arrays.asList(
                new Event("ev1", 1, null, new Resources(Arrays.asList(resourceList.get(0), resourceList.get(5), resourceList.get(11)))),
                new Event("ev2", 1, null, new Resources(Arrays.asList(resourceList.get(0), resourceList.get(5), resourceList.get(11)))),
                new Event("ev3", 1, null, new Resources(Arrays.asList(resourceList.get(1), resourceList.get(6), resourceList.get(12)))),
                new Event("ev4", 1, null, new Resources(Arrays.asList(resourceList.get(1), resourceList.get(7), resourceList.get(13)))),
                new Event("ev5", 1, null, new Resources(Arrays.asList(resourceList.get(2), resourceList.get(8), resourceList.get(14)))),
                new Event("ev6", 1, null, new Resources(Arrays.asList(resourceList.get(3), resourceList.get(9), resourceList.get(15)))),
                new Event("ev7", 1, null, new Resources(Arrays.asList(resourceList.get(4), resourceList.get(10), resourceList.get(16)))),
                new Event("ev8", 1, null, new Resources(Arrays.asList(resourceList.get(4), resourceList.get(6), resourceList.get(12))))
        );

        Events events = new Events(eventList);

        List<Constraint> constraintList = Arrays.asList(
                new Constraint("assignResourceConstraint", true, 1, events),
                new Constraint("assignTimeConstraint", true, 1, events)
        );

        Constraints constraints = new Constraints(constraintList);

        Timetable timetable = new Timetable("exampleTimetable", metadata, times, resourceTypes, resources, events, constraints);

        try {
            File timetableFile = new File(loadPath + "timetable.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Timetable.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(timetable, timetableFile);
//            marshaller.marshal(events, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return timetable;
    }
}