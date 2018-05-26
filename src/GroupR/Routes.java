package GroupR;

import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.*;


/**
 * Routes class that stores a collection of the routes to be displayed on the
 * MapView
 * @author goreckinj, hueblergw, jinq
 * @version 1.0
 * @created 05-Oct-2017 09:39:32
 */
public class Routes {

	/*******************************************************************************************
	 * DATA OBJECTS
	 *******************************************************************************************/
	private Map<String, Route> routes;


	/*******************************************************************************************
	 * CONSTRUCTOR
	 *******************************************************************************************/
	public Routes(){
		routes = new TreeMap<>();
	}


	/*******************************************************************************************
	 * LOAD FILE
	 *******************************************************************************************/

	/**
	 * Load file into data structure for stop
	 * @param file - file to be loaded
	 * @return - true if load successful, false if not
	 */
	public boolean loadFile(File file) {
		//might wanna change this to hashmap or something, reuse stopAttributes[0] as key when adding

		String line;
		String fileFormatDescription = "route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color";
		String routeAttributes[];

		try(Scanner fileIn = new Scanner(file)) {

			//make sure file has stuff in it
			if(checkEmptyFile(file)) {
				line = fileIn.nextLine();
			}else {
				throw new IllegalArgumentException("routes.txt is an empty file");
			}

			//make sure file format is correct, based on first line
			if(!checkFormat(fileFormatDescription)) {
				throw new IllegalArgumentException("Improper File Header Format for routes.txt");
			}

			//keep count and go through entire file
			for(int i = 2; fileIn.hasNextLine(); i++) {

				//get line
				line = fileIn.nextLine();

				//get rid of all spaces unless in parentheses
				line = line.replaceAll("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)","");

				//split line by commas, ignoring commas surrounded by quotes
				routeAttributes = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

				//if the line had more or less attributes than needed/wanted throw exception
				if(!checkAttributeLength(routeAttributes.length)) {
					throw new IllegalArgumentException("Improper number of attributes at line " + i);
				}

				//if color is empty set to black
				if(routeAttributes[7].equals("")) {
					routeAttributes[7] = "FFFFFF";
				}
				//ensure the color contains only 6 hex characters
				if(!checkColor(routeAttributes[7])) {
					throw new IllegalArgumentException("Improper color format at line " + i);
				}

				//add the new route
				routes.put(routeAttributes[0], new Route(routeAttributes[0], routeAttributes[2], routeAttributes[3], routeAttributes[4], Color.web(routeAttributes[7])));
			}

			fileIn.close(); //using try with actually automatically creates the finally block for the file, of which closes it as well.

		}catch(IllegalArgumentException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("routes.txt is corrupt");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return false;

		}catch(FileNotFoundException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("File Not Found");
			alert.setContentText("The file routes.txt could not be found.");
			alert.showAndWait();
			return false;
		}

		return true;
	}

	/*******************************************************************************************
	 * EXPORT AND EXPORT CHECK
	 *******************************************************************************************/

	/**
	 * Exports routes.txt files to directory
	 * @param directory
	 * @return boolean
	 * @author hueblergw
	 */
	public boolean exportFiles(File directory) {
		if(directory == null) {
			return false;
		}

		try {
			File routesFile = new File(directory + "\\routes.txt");

			if(routesFile.exists()) {
				routesFile.createNewFile();
			}

			FileWriter out = new FileWriter(routesFile);
			PrintWriter writer = new PrintWriter(out);

			writer.println("route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color");

			Iterator<Route> iter = routes.values().iterator();
			Route route;

			while(iter.hasNext()) {

				route = iter.next();

				writer.println(route.getID() + ",," + route.getShortName() + "," + route.getLongName() + "," + route.getDescription() + ",,," + route.getColor().toString().substring(2,8).toUpperCase() + ',');
			}

			writer.close();

		}catch(IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("routes.txt export failed");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return false;
		}

		return true;
	}

	public boolean isReadyToExport() {

		Iterator<Route> iter = routes.values().iterator();
		Route route;

		while(iter.hasNext()) {
			route = iter.next();

			if(route.getID() == null || route.getShortName() == null || route.getDescription() == null || route.getLongName() == null || route.getColor() == null) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Routes data structure invalid");
				alert.setContentText("Problem with ID, Shor tName, Long Name, or Description for route: " + route.toString());
				alert.showAndWait();
				return false;
			}

			if(!checkColor(route.getColor().toString().substring(2,8).toUpperCase())) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Routes data structure invalid");
				alert.setContentText("Invalid color for route: " + route.toString());
				alert.showAndWait();
				return false;
			}

		}

		return true;
	}

	/*******************************************************************************************
	 * GETTERS AND SETTERS
	 *******************************************************************************************/

	/**
	 * Gets all the routes. Returns a collection of routes
	 */
	public Map<String, Route> getAllRoutes(){
		return routes;
	}

	/**
	 * Gets the specified route. Returns the route
	 * 
	 * @param id
	 */
	public Route getRoute(String id){
        return routes.get(id);
	}


	public Collection<String> getRouteIds() {

		if(routes != null && routes.size() != 0) {

			Collection<String> routeIds = new LinkedList<String>();

			for (Route route : routes.values()) {
				routeIds.add(route.getID());
			}

			return routeIds;
		}

		return null;
	}


	/*******************************************************************************************
	 * VALIDITY CHECKERS
	 *******************************************************************************************/

	public boolean checkFormat(String header){
		String fileFormatDescription = "route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color";
		if(header.equals(fileFormatDescription)){
			return true;
		}
		return false;
	}
	public boolean checkAttributeLength(int attributeLength){
		int correctAttributeLength = 9;
		if(attributeLength == correctAttributeLength){
			return true;
		}
		return false;
	}
	public boolean checkColor(String color){
		if(color.matches("[0-9A-F]+") && color.length() <= 6) {
			return true;
		}
		return false;
	}
	public boolean checkEmptyFile(File fileIn){
		if(fileIn.length()>0) {
			return true;
		}
		return false;
	}
}