package GroupR;

import javafx.scene.paint.*;

import java.util.*;

/**
 * Route class that acts holds the data of a singular route
 * @author goreckinj, hueblergw, jinq
 * @version 1.0
 * @created 05-Oct-2017 09:39:31
 */
public class Route implements Subject {

	/*******************************************************************************************
	 * DATA OBJECTS
	 *******************************************************************************************/
	private String description;
	private String longName;
	private Color routeColor;
	private String id;
	private String shortName;
	private Map<String, Stop> associatedStops;
	private Map<String, Trip> associatedTrips;
	Collection<Observer> observers;


	/*******************************************************************************************
	 * CONSTRUCTOR
	 *******************************************************************************************/

	public Route(String id, String shortName, String longName, String description, Color routeColor){

		associatedStops = new TreeMap<>();
		associatedTrips = new TreeMap<>();
		observers = new ArrayList<>();

		this.id = id;
		this.shortName = shortName;
		this.longName = longName;
		this.description = description;
		this.routeColor = routeColor;
	}

	/*******************************************************************************************
	 * GETTERS
	 *******************************************************************************************/
	public Map<String, Stop> getAssociatedStops() {
		return associatedStops;
	}
	public Map<String, Trip> getAssociatedTrips() {
		return associatedTrips;
	}
	public Color getColor(){
		return routeColor;
	}
	public String getDescription(){
		return description;
	}
	public String getID(){
		return id;
	}
	public String getLongName(){
		return longName;
	}
	public String getShortName(){
		return shortName;
	}


	/*******************************************************************************************
	 * SETTERS
	 *******************************************************************************************/

	/**
	 * Sets the color of the route
	 * 
	 * @param color - color to change to
	 * @return oldColor - the old color
	 */
	public Color setColor(String color){
		Color oldColor = this.routeColor;
		routeColor = Color.web(color);
		return oldColor;
	}

	/**
	 * Adds a stop to associated stops
	 * @param stop
	 */
	public void addAssociatedStops(Stop stop) {
		String id = stop.getID();
		if(!associatedStops.containsKey(id)) {
			associatedStops.put(id, stop);
		}
	}

	/**
	 * Add a trip to associated trips
	 * @param trip
	 */
	public void addAssociatedTrip(Trip trip) {
		String id = trip.getTripID();
		if(!associatedTrips.containsKey(id)) {
			associatedTrips.put(id, trip);
		}
	}

	/**
	 * Sets the description of the route
	 * 
	 * @param description
	 * @return oldDescription The old description of the route
	 */
	public String setDescription(String description){
		String oldDescription = this.description;
		this.description = description;
		return oldDescription;
	}

	/**
	 * Sets the id of the route
	 * 
	 * @param id
	 * @return oldID - The old id of the route
	 */
	public String setID(String id){
		String oldID = this.id;
		this.id = id;
		return oldID;
	}

	public String toString() {
		return this.id;
	}

	/*******************************************************************************************
	 * SUBJECT METHODS
	 *******************************************************************************************/

	@Override
	public boolean addObserver(Observer o) {
		observers.add(o);
		return false;
	}

	@Override
	public Observer deleteObservers(Observer o) {
		observers.remove(o);
		return null;
	}

	@Override
	public void notifyObservers() {
	}
}