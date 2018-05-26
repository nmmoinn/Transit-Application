package GroupR;
/**
 * Controller class that holds the JavaFX GUI elements to be displayed, along with
 * corresponding information
 * @author goreckinj, hueblergw
 * @version 1.0
 * @created 05-Oct-2017 09:39:31
 */

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;
import com.lynden.gmapsfx.GoogleMapView;


public class Controller{

	/*******************************************************************************************
	 * GUI OBJECTS
	 *******************************************************************************************/

	private ListView listModel;
	private MapView mapModel;
	private ElementController elementController;
	private Stage elementStage;

	@FXML
	private TextField stopText;
	@FXML
	private TextField routeText;
	@FXML
	private Label leftStatus;
	@FXML
	private Label rightStatus;
	@FXML
	private javafx.scene.control.ListView<String> itemList;
	@FXML
	private Label listMode;
	@FXML
	private GoogleMapView mapView;

	/*******************************************************************************************
	 * DATA OBJECTS
	 *******************************************************************************************/
	private Routes routes;
	private Stops stops;
	private Trips trips;

	//indicator that files are loaded, starts off false
	private boolean filesLoaded = false;
	private final SimpleTimer timer = new SimpleTimer();



	/*******************************************************************************************
	 * INITIALIZATION
	 *******************************************************************************************/

	public void initialize() {
		try {
			importFiles();
		} catch( IOException e) {
			System.out.println("Error loading Element GUI");
		}
	}

	/**
	 * Initialize the MapView and ListView to be ready to display things. Initializes the
	 * objects by sending them the panes in which to display things.
	 *
	 * @author hueblergw
	 */
	private void initializeViews() throws IOException {

		//clear list display description
		listMode.setText("");
		if(elementStage != null && elementStage.isShowing()) {
			elementStage.close();
		}
		//create element stage and controller
		elementStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Element.fxml"));
		Parent root;
		root = loader.load();
		elementController = loader.getController();
		elementController.setMainController(this);
		elementStage.setTitle("Element");
		elementStage.setScene(new Scene(root, ApplicationDriver.SCENE_WIDTH/4,
				ApplicationDriver.SCENE_HEIGHT));

		//create new models
		//mapModel = new MapView(mapPane, webView);
		mapModel = new MapView(mapView,stops);
		listModel = new ListView(listMode, itemList, routes.getAllRoutes(), trips.getAllTrips(), stops.getAllStops(),
				elementController, elementStage);

		//add observers to all subjects
		Iterator<Stop> stopIter = stops.getAllStops().values().iterator();
		Stop stop;

		while(stopIter.hasNext()) {
			stop = stopIter.next();
			stop.addObserver(listModel);
			stop.addObserver(mapModel);
		}

		Iterator<Route> routeIter = routes.getAllRoutes().values().iterator();
		Route route;

		while(routeIter.hasNext()) {
			route = routeIter.next();
			route.addObserver(listModel);
			route.addObserver(mapModel);
		}

		Iterator<Trip> tripIter = trips.getAllTrips().values().iterator();
		Trip trip;

		while(tripIter.hasNext()) {
			trip = tripIter.next();
			trip.addObserver(listModel);
			trip.addObserver(mapModel);
		}
	}


	/*******************************************************************************************
	 * DISPLAY METHODS
	 *******************************************************************************************/

	/**
	 * Checks for when the MapView runs displayCurrentRoutes()
	 *
	 * @param routeID
	 * @author goreckinj
	 */
	public void displayCurrentRoutesListener(String routeID) {
		timer.start();

		leftStatus.setText(timer.stop());
		rightStatus.setText("Displaying current routes");
	}

	/**
	 * Checks for when routes are displayed on the MapView
	 *
	 * @param routeID
	 * @author goreckinj
	 */
	public void displayStopsOnRouteListener(String routeID) {
		timer.start();

		leftStatus.setText(timer.stop());
		rightStatus.setText("Displaying all stops on route " + routeID);
	}

	/**
	 * Display all stops on Google map
	 *
	 * @author goreckinj
	 */
	public void displayAllStops() {
		timer.start();
        LatLong stop1 = new LatLong(44.822390,-91.478580);
        MapOptions mapOption = new MapOptions();

		leftStatus.setText(timer.stop());
		rightStatus.setText("Displaying all stops");
	}

	/*******************************************************************************************
	 * LIST METHODS
	 *******************************************************************************************/

	/**
	 * List all stops in listModel
	 */
	public void listAllStops() {
		if (filesLoaded) {
			timer.start();

			listMode.setText("ALL STOPS");
			Collection<Stop> allStops = stops.getAllStops().values();
			listModel.listCollection(allStops);
			mapModel.plotStops(stops.getAllStops().values());

			leftStatus.setText(timer.stop());
			rightStatus.setText("Listing all stops");
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("No files loaded. Nothing to list.");
			alert.showAndWait();
		}
	}

	/**
	 * List all trips in listModel
	 */
	public void listAllTrips() {
		if (filesLoaded) {
			timer.start();

			listMode.setText("ALL TRIPS");
			listModel.listCollection(trips.getAllTrips().values());

			leftStatus.setText(timer.stop());
			rightStatus.setText("Listing all trips");
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("No files loaded. Nothing to list.");
			alert.showAndWait();
		}
	}

	/**
	 * List all routes in listModel
	 */
	public void listAllRoutes() {
		if (filesLoaded) {
			timer.start();

			listMode.setText("ALL ROUTES");
			listModel.listCollection(routes.getAllRoutes().values());

			leftStatus.setText(timer.stop());
			rightStatus.setText("Listing all routes");
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("No files loaded. Nothing to list.");
			alert.showAndWait();
		}
	}

	/**
	 * List all stops on an entered route in listModel
	 */
	public void listStopsOnRoute() {
		//if not text enter do nothing
		if (!routeText.getText().equals("")) {

			//if files not loaded alert
			if (filesLoaded) {

				Route route = routes.getRoute(routeText.getText());

				//if stop is not found alert user
				if (route != null) {
					timer.start();

					listMode.setText("ALL STOPS ON ROUTE: " + route.getID());
					listModel.listCollection(route.getAssociatedStops().values());
					mapModel.plotStops(route.getAssociatedStops().values());

					rightStatus.setText("Listing all stops on route " + route.getID());
					leftStatus.setText(timer.stop());
				} else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("That route ID is invalid");
					alert.showAndWait();
				}

			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("No files loaded. Nothing to list.");
				alert.showAndWait();
			}
		}
	}

	/**
	 * List all trips of on certain route that are happening in the future in listModel
	 */
	public void listFutureTrips() {

		//if not text enter do nothing
		if (!routeText.getText().equals("")) {

			//if files not loaded alert
			if (filesLoaded) {

				Route route = routes.getRoute(routeText.getText());

				//if stop is not found alert user
				if (route != null) {
					timer.start();

					Collection<Trip> futureTrips = new LinkedList<Trip>();
					for(Trip trip : route.getAssociatedTrips().values()) {
						if(trip.isFutureTrip()) {
							futureTrips.add(trip);
						}
					}

					listMode.setText("FUTURE TRIPS ON ROUTE: " + route.getID());
					listModel.listCollection(futureTrips);

					rightStatus.setText("Listing all future trips on route " + route.getID());
					leftStatus.setText(timer.stop());
				} else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("That route ID is invalid");
					alert.showAndWait();
				}

			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("No files loaded. Nothing to list.");
				alert.showAndWait();
			}
		}
	}

	/**
	 * Lists routes that contain certain stop in listModel
	 */
	public void listRoutesContainingStop() {
		//if not text enter do nothing
		if (!stopText.getText().equals("")) {

			//if files not loaded alert
			if (filesLoaded) {

				Stop stop = stops.getStop(stopText.getText());

				//if stop is not found alert user
				if (stop != null) {
					timer.start();

					listMode.setText("ALL ROUTES WITH STOP: " + stop.getID());
					listModel.listCollection(stop.getAssociatedRoutes().values());

					rightStatus.setText("Listing all routes that contain stop " + stop.getID());
					leftStatus.setText(timer.stop());
				} else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("That stop ID is invalid");
					alert.showAndWait();
				}

			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("No files loaded. Nothing to list.");
				alert.showAndWait();
			}
		}
	}

	public void changeStopID() {

	}

	public void changeRouteID() {

	}

	public void changeTripID() {

	}

	public void displayRoute() {

	}


	/*******************************************************************************************
	 * EXPORT
	 *******************************************************************************************/

	/**
	 * Exports the files to a specified directory
	 *
	 * @author goreckinj, hueblergw
	 */
	public void exportFiles() {
		if (filesLoaded) {

			//choose the directory for GTFS files
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("GTFS File Selector");
			File directory = directoryChooser.showDialog(new Stage());

			//ensure all exports are possible then export
			if (stops.isReadyToExport() && trips.isReadyToExport() && routes.isReadyToExport()) {
				timer.start();

				stops.exportFile(directory);
				trips.exportFile(directory);
				routes.exportFiles(directory);

				//set status
				rightStatus.setText("Successful export to directory " + directory);
				leftStatus.setText(timer.stop());
			} else {
				//set status, no alert needed, export check should alert
				rightStatus.setText("Unsuccessful export");
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("No files loaded. Nothing to export.");
			alert.showAndWait();
		}
	}


	/*******************************************************************************************
	 * IMPORT
	 *******************************************************************************************/

	/**
	 * Called on import click from menuBar. Prompts for directory selection then finds the needed files
	 * in the directory. If the files exist then create the trip, route, and stop objects from those files.
	 * If the files aren't found then print a list of the files that aren't found.
	 */
	public void importFiles() throws IOException {
		//choose the directory for GTFS files
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("GTFS File Selector");
		File directory = directoryChooser.showDialog(new Stage());

		timer.start();
		if (directory != null) {
			//add the file references for the needed files to the directory location
			File stopsFile = new File(directory, "stops.txt");
			File stop_timesFile = new File(directory, "stop_times.txt");
			File tripsFile = new File(directory, "trips.txt");
			File routesFile = new File(directory, "routes.txt");

			//if all the files exist in the director create the objects
			if (filesExist(stopsFile, stop_timesFile, tripsFile, routesFile)) {

				//save subjects for now
				Trips tripsSave = trips;
				Stops stopsSave = stops;
				Routes routesSave = routes;

				trips = new Trips();
				stops = new Stops();
				routes = new Routes();

				//if all files loaded properly then set status and initialize views
				if (routes.loadFile(routesFile) && stops.loadFile(stopsFile) &&
						trips.loadFile(tripsFile, stop_timesFile, routes.getAllRoutes(), stops.getAllStops())) {

					//update right status to indicate success
					rightStatus.setText("Files loaded successfully");

					//initalize the views (possibly otherthings later on)
					initializeViews();

					//set files loaded to true;
					filesLoaded = true;

					//set stops text field auto completion to stop ids
					TextFields.bindAutoCompletion(stopText, stops.getStopIds());

					//set routes text field auto completion to route ids
					TextFields.bindAutoCompletion(routeText, routes.getRouteIds());

				}
				//if they didnt load properly then restore subjects from saves
				else {
					trips = tripsSave;
					stops = stopsSave;
					routes = routesSave;
				}
			}
			//if any of the files dont exist then print which ones are missing as alert
			else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("The following necessary files were not found");

				if (!stopsFile.exists()) {
					alert.setContentText("stops.txt\n");
				}

				if (!stop_timesFile.exists()) {
					alert.setContentText(alert.getContentText() + "stop_times.txt\n");
				}

				if (!tripsFile.exists()) {
					alert.setContentText(alert.getContentText() + "trips.txt\n");
				}

				if (!routesFile.exists()) {
					alert.setContentText(alert.getContentText() + "routes.txt\n");
				}

				alert.showAndWait();

				//update right status to indicate error
				rightStatus.setText("Unsuccessful file load: files not found in directory " + directory);
			}
		}
		leftStatus.setText(timer.stop());
	}

	/*******************************************************************************************
	 * VALIDITY CHECKERS
	 *******************************************************************************************/

	/**
	 * Check if a route with a route id exists
	 * @param routeID - route id to check
	 * @return - true if the route exists
	 */
	public boolean validateRouteID(String routeID){
		return routes.getRoute(routeID) != null;
	}

	/**
	 * Check if a stop with a stop id exists
	 * @param stringID - stop id to check
	 * @return - true if the stop exists
	 */
	public boolean validateStopID(String stringID){
		return stops.getStop(stringID) != null;
	}

	/**
	 * Ensure all files exist
	 * @param files - arbitrary array to check
	 * @return - returns true if all files exist
	 */
	public boolean filesExist(File... files) {
		for (File file : files) {
			if (!file.exists()) {
				return false;
			}
		}

		return true;
	}

	/*******************************************************************************************
	 * EXIT
	 *******************************************************************************************/
	public void exit() {
		Platform.exit();
	}

}