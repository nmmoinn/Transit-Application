package GroupR;


import com.sun.xml.internal.bind.v2.runtime.Coordinator;
import impl.org.controlsfx.tools.rectangle.CoordinatePosition;
import impl.org.controlsfx.tools.rectangle.CoordinatePositions;
import javafx.scene.control.Alert;

import java.io.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Stops class that stores a collection of the Stops to be displayed on the
 * MapView
 * @author goreckinj, hueblergw, jinq
 * @version 1.0
 * @created 05-Oct-2017 09:39:32
 */
public class Stops {

	/*******************************************************************************************
	 * DATA OBJECTS
	 *******************************************************************************************/
	private Collection<Observer> observers;
	private Map<String, Stop> stops;

	/*******************************************************************************************
	 * CONSTRUCTOR
	 *******************************************************************************************/
	public Stops() {
		observers = new LinkedList<>();
		stops = new TreeMap<String, Stop>();
	}


	/*******************************************************************************************
	 * LOAD FILE
	 *******************************************************************************************/

	/**
	 * Load file into data structure for stop
	 * @param file - file to be loaded
	 * @return - true if load successful, false if not
	 */
	public boolean loadFile(File file){

		//might wanna change this to hashmap or something, reuse stopAttributes[0] as key when adding

		String line;
		String stopAttributes[];
		double latitude;
		double longitude;

        if(file == null) {
            return false;
        }

        try {

			if(emptyFile(file)) {
				throw new IllegalArgumentException("stops.txt is an empty file");
			}

			Scanner fileIn = new Scanner(file);

			//make sure file format is correct, based on first line
			if(!properFileFormat(fileIn.nextLine())) {
				throw new IllegalArgumentException("Improper File Format for stops.txt");
			}

			//keep count and go through entire file
			for(int i = 2; fileIn.hasNextLine(); i++) {

				//get line
				line = fileIn.nextLine();

				//get rid of all spaces unless in parentheses
				line = line.replaceAll("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)","");

				//split line by commas, ignoring commas surrounded by quotes
				stopAttributes = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

				if(!properAttributeLength(stopAttributes)) {
					throw new IllegalArgumentException("Improper number of attributes at line " + i);
				}

                if(!validCoordinates(stopAttributes[3], stopAttributes[4])) {
                    throw new IllegalArgumentException("Improper latitude or longitude specified at line " + i);
                }

                latitude = Double.parseDouble(stopAttributes[3]);
				longitude = Double.parseDouble(stopAttributes[4]);

				stops.put(stopAttributes[0], new Stop(stopAttributes[0], stopAttributes[1], stopAttributes[2], latitude, longitude));
			}

			fileIn.close();

		}catch(IllegalArgumentException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("stops.txt is corrupt");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return false;

		}catch(FileNotFoundException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("File Not Found");
			alert.setContentText("The file stops.txt could not be found.");
			alert.showAndWait();
			return false;
		}

		return true;
	}


	/*******************************************************************************************
	 * EXPORT AND EXPORT CHECK
	 *******************************************************************************************/

    public boolean isReadyToExport() {

		Iterator<Stop> iter = stops.values().iterator();
		Stop stop;

		while(iter.hasNext()) {
			stop = iter.next();

			if(stop.getID() == null || stop.getName() == null || stop.getDescription() == null) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Stops data structure invalid");
				alert.setContentText("Problem with ID, Name, or Description for stop: " + stop.toString());
				alert.showAndWait();
				return false;
			}

			if(!validCoordinates(Double.toString(stop.getLatitude()), Double.toString(stop.getLongitude()))) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Stops data structure invalid");
				alert.setContentText("Problem with coordinates for stop: " + stop.toString());
				alert.showAndWait();
				return false;
			}
		}

		return true;
	}

	/**
	 * Exports the file to stops.txt
	 * @param directory
	 * @return
	 */
    public boolean exportFile(File directory) {

    	//if directory is null return false
		if(directory == null) {
			return false;
		}

		try {
			File stopsFile = new File(directory, "stops.txt");

			if(stopsFile.exists()) {
				stopsFile.createNewFile();
			}

			FileWriter out = new FileWriter(stopsFile);
			PrintWriter writer = new PrintWriter(out);

			writer.println("stop_id,stop_name,stop_desc,stop_lat,stop_lon");

			Iterator<Stop> iter = stops.values().iterator();
			Stop stop;

			while(iter.hasNext()) {

				stop = iter.next();

				writer.println(stop.getID() + "," + stop.getName() + "," + stop.getDescription() + "," + stop.getLatitude() + "," + stop.getLongitude());
			}

			writer.close();

		}catch(IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("stops.txt export failed");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return false;
		}

		return true;
	}



	/*******************************************************************************************
	 * GETTERS
	 *******************************************************************************************/
	public Map<String, Stop> getAllStops(){ return stops; }
	public Stop getStop(String id){
		return stops.get(id);
	}

	public Collection<String> getStopIdsWithName() {

		if(stops != null && stops.size() != 0) {

			Collection<String> stopIds = new LinkedList<String>();

			for (Stop stop : stops.values()) {
				stopIds.add(stop.getID() + ": " + stop.getName());
			}

			return stopIds;
		}

		return null;
	}

	/*******************************************************************************************
	 * SETTERS
	 *******************************************************************************************/

	public boolean setStopID(String oldID, String newID) {
		Stop stop = stops.get(oldID);

		if(stop != null) {
			stop.setID(newID);
			stops.put(newID, stops.remove(oldID));
			return true;
		}else {
			return false;
		}
	}

	public boolean setStopDescription(String ID, String newDescription) {
		Stop stop = stops.get(ID);

		if(stop != null) {
			stop.setDescription(newDescription);
			return true;
		}else {
			return false;
		}
	}

	public boolean setStopLatitude(String ID, double newLatitude) {
		Stop stop = stops.get(ID);

		if(stop != null) {
			stop.setLatitude(newLatitude);
			return true;
		}else {
			return false;
		}
	}

	public boolean setStopLongitude(String ID, double newLongitude) {
		Stop stop = stops.get(ID);

		if(stop != null) {
			stop.setLongitude(newLongitude);
			return true;
		}else {
			return false;
		}
	}

	public boolean setStopName(String ID, String newName) {
		Stop stop = stops.get(ID);

		if(stop != null) {
			stop.setName(newName);
			return true;
		}else {
			return false;
		}
	}

	/**
	 * Get all stop Id string in a collection
	 * @return
	 */
	public Collection<String> getStopIds() {

		if(stops != null && stops.size() != 0) {

			Collection<String> stopIds = new LinkedList<String>();

			for (Stop stop : stops.values()) {
				stopIds.add(stop.getID());
			}

			return stopIds;
		}

		return null;
	}

	/*******************************************************************************************
	 * VALIDITY CHECKERS
	 *******************************************************************************************/

	public boolean emptyFile(File file) {
		if(file.length() > 0) {
			return false;
		}

		return true;
	}

	public boolean properFileFormat(String description) {

		String properFileFormatDescription = "stop_id,stop_name,stop_desc,stop_lat,stop_lon";

		if(description.equals(properFileFormatDescription)) {
			return true;
		}

		return false;
	}

	public boolean properAttributeLength(String[] attributes) {

		int properNumberOfAttributes = 5;

		//if the line had more or less attributes than needed/wanted throw exception
		if(attributes.length == properNumberOfAttributes) {
			return true;
		}

		return false;
	}

	public boolean validCoordinates(String latitudeString, String longitudeString) {

		try {

			double latitude = Double.parseDouble(latitudeString);
			double longitude = Double.parseDouble(longitudeString);

			if(!(latitude <= 90 && latitude >= -90)) {
				return false;
			}

			//Checks if longitude is within accepted range
			if(!(longitude <= 180 && longitude >= -180)) {
				return false;
			}

		}catch (NumberFormatException e) {
			return false;
		}

		return true;
	}
}