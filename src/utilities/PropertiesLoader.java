package utilities;

import java.io.*;
import java.util.Properties;

/**
 * Created by Anca on 3/24/2017.
 */
public class PropertiesLoader {

    public static void changeXMLLocationFolder(String newLocation){
        Properties prop = loadFromProperties();
        OutputStream output = null;

        try {
            output = new FileOutputStream("config.properties");

            // Set the properties value

            prop.setProperty("xmlLocationFolder", newLocation);

            // Save properties to project root folder
            prop.store(output, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties loadFromProperties(){
        InputStream input = null;
        Properties prop = new Properties();

        try {
            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return prop;
    }

    public static String loadXMLLocationFolder(){
        Properties prop = loadFromProperties();

        return prop.getProperty("xmlLocationFolder");
    }
}