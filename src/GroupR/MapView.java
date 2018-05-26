package GroupR;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.directions.*;
import com.sun.javafx.geom.Point2D;
import com.sun.prism.PhongMaterial;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.net.URL;
import java.util.*;

/**
 * MapView class that acts as a JavaFX GUI Canvas to display stops, routes and
 * trips visually to a user
 * @author goreckinj, hueblergw, jinq
 * @version 1.0
 * @created 05-Oct-2017 09:39:31
 */
public class MapView implements Observer, DirectionsServiceCallback {

	private GoogleMap map;
	private GoogleMapView mapView;
	private MapOptions mapOptions;
	private MarkerOptions markerOptions;
	private Stops stops;
	private DirectionsService directionsService;
	private DirectionsPane directionsPane;

	/**
	 * Default constructor for the mapview class
	 */
	//public MapView(Pane displayPane, WebView webView){
	public MapView(GoogleMapView mapView, Stops stops){
	    this.mapView = mapView;
	    this.stops = stops;
//	    ArrayList<Stop> stopList = (ArrayList<Stop>)(stops.getAllStops().values());
//	    Stop firstStop = stopList.get(0);
//	    double firstLat = firstStop.getLatitude();
//	    double firstLon = firstStop.getLongitude();
		Collection<Stop> stopCollection = stops.getAllStops().values();
		Stop firstStop = stopCollection.iterator().next();
		double firstLat = firstStop.getLatitude();
		double firstLon = firstStop.getLongitude();
		//System.out.println(stops.getAllStops().values());
		LatLong location1 = new LatLong(firstLat,firstLon);
		mapOptions = new MapOptions();
		markerOptions = new MarkerOptions();
		mapOptions.center(location1)
				.mapType(MapTypeIdEnum.ROADMAP)
				.overviewMapControl(false)
				.panControl(false)
				.rotateControl(false)
				.scaleControl(false)
				.streetViewControl(false)
				.zoomControl(false)
				.zoom(12);
		map = mapView.createMap(mapOptions);
		directionsService = new DirectionsService();
		directionsPane = mapView.getDirec();
	}
	public void plotRoute(String departure, String destination){
		DirectionsRequest request = new DirectionsRequest(departure, destination, TravelModes.TRANSIT);
		directionsService.getRoute(request, this, new DirectionsRenderer(true, mapView.getMap(), directionsPane));
	}

	/**
	 * Plots the current routes. Must have stops plotted first. Returns true if
	 * plotted successfully
	 * 
	 * @param routes
	 * @param trips
	 */
	public boolean plotCurrentRoutes(Collection<Route> routes, Collection<Trip> trips){
		return false;
	}

	/**
	 * Plots the specified route. Must have stops plotted first. Returns true if
	 * plotted successfully
	 *
     * @author Nick
	 * @param route
	 */
	public boolean plotRoute(Route route){
        /*DirectionsService directionsService = new DirectionsService();
        DirectionsPane directionsPane = mapView.getDirec();
        DirectionsRequest request = new DirectionsRequest("Milwaukee", "Madison", TravelModes.DRIVING);
        directionsService.getRoute(request, new DirectionsServiceCallback() {
            @Override
            public void directionsReceived(DirectionsResult directionsResult, DirectionStatus directionStatus) {

            }
        }, new DirectionsRenderer(true, mapView.getMap(), directionsPane));
        */

	    return true;
	}

	/**
	 * Plots the stops on the map. Returns true if plotted successfully
	 *
     * @author: Gavin, Nick
	 * @param stops
	 */
	public boolean plotStops(Collection<Stop> stops){
	    map.clearMarkers();
		Iterator<Stop> stopIter = stops.iterator();
		while(stopIter.hasNext()){
			Stop stopObj = stopIter.next();
			LatLong location = new LatLong(stopObj.getLatitude(), stopObj.getLongitude());
			markerOptions.position(location);
			Marker marker = new Marker(markerOptions);
			map.addMarker(marker);
		}

		return true;
	}

	/**
	 * Updates the observers display based on the data
	 *
	 * @param data - The data to update the observer with
	 * @author goreckinj
	 */
	public void update(){
        //TODO: Revamp observers [Set reference of data MapView observes. When this method is called, update said data]
	}

	@Override
	public void directionsReceived(DirectionsResult results, DirectionStatus status) {

	}
}