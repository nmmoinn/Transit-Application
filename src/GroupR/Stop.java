package GroupR;

import com.lynden.gmapsfx.javascript.object.Marker;

import java.util.*;

/**
 * Stop class that holds the data of a singular stop
 * @author goreckinj, hueblergw, jinq
 * @version 1.0
 * @created 05-Oct-2017 09:39:32
 */
public class Stop implements Subject {


	/*******************************************************************************************
	 * DATA OBJECTS
	 *******************************************************************************************/
	private String description;
	private String id;
	private double latitude;
	private double longitude;
	private String name;
	private Map<String, Trip> associatedTrips;
	private Map<String, Route> associatedRoutes;
	private Collection<Observer> observers;



	/*******************************************************************************************
	 * CONSTRUCTOR
	 *******************************************************************************************/
	public Stop(String id, String name, String description, double latitude, double longitude){

		associatedTrips = new TreeMap<>();
		associatedRoutes = new TreeMap<>();
		observers = new ArrayList<>();

		this.id = id;
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
	}


	/*******************************************************************************************
	 * GETTERS
	 *******************************************************************************************/
	public Map<String, Route> getAssociatedRoutes() {
		return associatedRoutes;
	}
	public Map<String, Trip> getAssociatedTrips() {
		return associatedTrips;
	}
	public String getDescription(){
		return description;
	}
	public String getID(){
		return id;
	}
	public double getLatitude(){
		return latitude;
	}
	public double getLongitude(){
		return longitude;
	}
	public String getName(){
		return name;
	}
	public String toString() {
		return this.getID();
	}


	/*******************************************************************************************
	 * SETTERS
	 *******************************************************************************************/

	public void addAssociatedRoute(Route route) {
		String id = route.getID();
		if(!associatedRoutes.containsKey(id)) {
			associatedRoutes.put(id, route);
		}
	}

	public void addAssociatedTrip(Trip trip) {
		String id = trip.getTripID();
		if(!associatedTrips.containsKey(id)) {
			associatedTrips.put(id, trip);
		}
	}

	public String setDescription(String description){
		String oldDescription = this.description;
		this.description = description;
		return oldDescription;
	}

	public String setID(String id){
		String oldid = this.id;
		this.id = id;
		return oldid;
	}

	public double setLatitude(double latitude){
		double oldLatitude = this.latitude;
		this.latitude = latitude;
		return oldLatitude;
	}

	public double setLongitude(double longitude){
		double oldLongitude = this.longitude;
		this.longitude = longitude;
		return oldLongitude;
	}

	public String setName(String name){
		String oldName = this.name;
		this.name = name;
		return oldName;
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