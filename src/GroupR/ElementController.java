package GroupR;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.*;

public class ElementController {

    /*******************************************************************************************
     * DATA OBJECTS
     *******************************************************************************************/

    private Controller mainController;
    private Stage elementStage;
    private Stack<Object> prevData;
    private Stack<Object> nextData;
    private Object data;

    /*******************************************************************************************
     * GUI OBJECTS
     *******************************************************************************************/

    @FXML
    private Label dataLabel1, dataLabel2, dataLabel3, dataLabel4, dataLabel5, dataLabel, elementsLabel;
    @FXML
    private TextField dataField1, dataField2, dataField3, dataField4, dataField5;
    @FXML
    private Button backButton, forwardButton, applyButton;
    @FXML
    private Circle indicator1, indicator2, indicator3, indicator4, indicator5;
    @FXML
    private Tooltip prevDataToolTip, nextDataToolTip, applyToolTip;
    @FXML
    private SplitPane dataPane1, dataPane2, dataPane3, dataPane4, dataPane5;
    @FXML
    private javafx.scene.control.ListView elementsList;

    /*******************************************************************************************
     * CONSTRUCTORS
     *******************************************************************************************/

    public ElementController() {
        //TODO: Setup Listeners to detect when data is changed in TextField.
        //TODO: Set onActions for buttons. When clicked, update data information.
        //dataField1.setOnInputMethodTextChanged(());
        this.elementsList = new javafx.scene.control.ListView<>();
        this.elementsList.setOrientation(Orientation.VERTICAL);
        prevData = new Stack<>();
        nextData = new Stack<>();
    }

    /*******************************************************************************************
     * FUNCTIONS
     *******************************************************************************************/

    public ElementController setMainController(Controller mainController) {
        this.mainController = mainController;
        return this;
    }

    public ElementController initialize() {
        indicator1.setFill(Color.LIME);
        indicator2.setFill(Color.LIME);
        indicator3.setFill(Color.LIME);
        indicator4.setFill(Color.LIME);
        indicator5.setFill(Color.LIME);
        dataChangeListeners();
        prevData.clear();
        nextData.clear();
        this.data = null;
        backButton.setDisable(true);
        forwardButton.setDisable(true);
        this.elementsList.setOnMouseClicked((MouseEvent e) -> {
            //initialize data in GUI
            if (elementsList.getItems().size() != 0) {
                String mode = elementsLabel.getText().toLowerCase().split(" ")[1].toLowerCase();
                String id = ((String) elementsList.getSelectionModel().getSelectedItem());
                if (id != null) {
                    id = id.split(" ")[0];
                    if (data instanceof Route) {
                        Route route = (Route) data;
                        setData(route.getAssociatedStops().get(id));
                        fillData(true);
                    } else if (data instanceof Trip) {
                        Trip trip = (Trip) data;
                        LinkedList<StopTime> stopTimes = (LinkedList<StopTime>) trip.getAllStopTimes();
                        for (int i = 0; i < stopTimes.size(); i++) {
                            if (Integer.toString(stopTimes.get(i).getSequence()).equals(id)) {
                                setData(stopTimes.get(i));
                            }
                        }
                        fillData(true);
                    } else if (data instanceof Stop) {
                        Stop stop = (Stop) data;
                        setData(stop.getAssociatedTrips().get(id));
                        fillData(true);
                    } else if (data instanceof StopTime) {
                        StopTime stopTime = (StopTime) data;
                        setData(stopTime.getStop());
                        fillData(true);
                    }
                }
            }
        });
        return this;
    }

    public ElementController fillData(boolean mode) {
        if(data == null) {
            throw new NullPointerException("Must call setData(Object data) function first. Data reference is null.");
        }
        //set data
        if(mode) {
            if (data instanceof Route) {
                Route route = (Route) data;
                dataLabel.setText("Route: " + route.getID());
                setVisible(dataPane1, true);
                dataLabel1.setText("Route ID");
                dataField1.setText(route.getID());
                setVisible(dataPane2, true);
                dataLabel2.setText("Long Name");
                dataField2.setText(route.getLongName());
                setVisible(dataPane3, true);
                dataLabel3.setText("Short Name");
                dataField3.setText(route.getShortName());
                setVisible(dataPane4, true);
                dataLabel4.setText("Description");
                dataField4.setText(route.getDescription());
                setVisible(dataPane5, true);
                dataLabel5.setText("Color");
                dataField5.setText(route.getColor().toString());
                elementsLabel.setText("Associated Stops");
                ObservableList<String> writer = elementsList.getItems();
                writer.clear();
                Iterator<Stop> stops = route.getAssociatedStops().values().iterator();
                while(stops.hasNext()) {
                    Stop stop = stops.next();
                    writer.add(stop.getID());
                }

            } else if (data instanceof Trip) {
                Trip trip = (Trip) data;
                dataLabel.setText("Trip: " + trip.getTripID());
                setVisible(dataPane1, true);
                dataLabel1.setText("Trip ID");
                dataField1.setText(trip.getTripID());
                setVisible(dataPane2, true);
                dataLabel2.setText("Route ID");
                dataField2.setText(trip.getRoute().getID());
                setVisible(dataPane3, false);
                dataLabel3.setText("");
                dataField3.setText("");
                setVisible(dataPane4, false);
                dataLabel4.setText("");
                dataField4.setText("");
                setVisible(dataPane5, false);
                dataLabel5.setText("");
                dataField5.setText("");
                elementsLabel.setText("Associated StopTimes");
                ObservableList<String> writer = elementsList.getItems();
                writer.clear();
                Iterator<StopTime> stopTimes = trip.getAllStopTimes().iterator();
                while(stopTimes.hasNext()) {
                    StopTime stopTime = stopTimes.next();
                    writer.add(Integer.toString(stopTime.getSequence()) + " Stop: " + stopTime.getStop().getID());
                }
            } else if (data instanceof Stop) {
                Stop stop = (Stop) data;
                dataLabel.setText("Stop: " + stop.getID());
                setVisible(dataPane1, true);
                dataLabel1.setText("Stop ID");
                dataField1.setText(stop.getID());
                setVisible(dataPane2, true);
                dataLabel2.setText("Name");
                dataField2.setText(stop.getName());
                setVisible(dataPane3, true);
                dataLabel3.setText("Description");
                dataField3.setText(stop.getDescription());
                setVisible(dataPane4, true);
                dataLabel4.setText("Longitude");
                dataField4.setText(Double.toString(stop.getLongitude()));
                setVisible(dataPane5, true);
                dataLabel5.setText("Latitude");
                dataField5.setText(Double.toString(stop.getLatitude()));
                elementsLabel.setText("Associated Trips");
                ObservableList<String> writer = elementsList.getItems();
                writer.clear();
                Iterator<Trip> trips = stop.getAssociatedTrips().values().iterator();
                while(trips.hasNext()) {
                    Trip trip = trips.next();
                    writer.add(trip.getTripID());
                }
            } else if(data instanceof StopTime) {
                StopTime stopTime = (StopTime) data;
                dataLabel.setText("StopTime: " + stopTime.getSequence());
                setVisible(dataPane1, true);
                dataLabel1.setText("Trip ID");
                dataField1.setText(stopTime.getTrip().getTripID());
                setVisible(dataPane2, true);
                dataLabel2.setText("Arrival Time");
                dataField2.setText(stopTime.getArrivalTime().toString());
                setVisible(dataPane3, true);
                dataLabel3.setText("Departure Time");
                dataField3.setText(stopTime.getDepartTime().toString());
                setVisible(dataPane4, true);
                dataLabel4.setText("Stop ID");
                dataField4.setText(stopTime.getStop().getID());
                setVisible(dataPane5, true);
                dataLabel5.setText("Sequence");
                dataField5.setText(Integer.toString(stopTime.getSequence()));
                elementsLabel.setText("Associated Stops");
                ObservableList<String> writer = elementsList.getItems();
                writer.clear();
                writer.add(stopTime.getStop().getID());
            }
        //throw error
        } else {
            throw new IllegalArgumentException("Data to load is invalid: Must be a route, trip, stop or stop time.");
        }
        return this;
    }

    /**
     * Sets the data reference that the ElementController is listing.
     * @param data The data to be referenced to.
     * @return this
     */
    public ElementController setData(Object data) {
        if(this.data != null) {
            prevData.push(this.data);
        }
        this.data = data;
        if(backButton.isDisabled() && prevData.size() >= 1) {
            backButton.setDisable(false);
        }
        return this;
    }

    public ElementController setStage(Stage elementStage) {
        this.elementStage = elementStage;
        return this;
    }

    /*******************************************************************************************
     * ON ACTIONS
     *******************************************************************************************/

    public void prevData() {
        if(prevData.size() != 0) {
            Object data = prevData.pop();
            nextData.push(this.data);
            this.data = data;
            fillData(true);
        }
        if(!backButton.isDisabled() && prevData.size() == 0) {
            backButton.setDisable(true);
        }
        if(forwardButton.isDisabled()) {
            forwardButton.setDisable(false);
        }
    }

    /**
     * Pushes to prevData and Pops from nextData
     */
    public void nextData() {
        if(nextData.size() != 0) {
            Object data = nextData.pop();
            prevData.push(this.data);
            this.data = data;
            fillData(true);
        }
        if(!forwardButton.isDisabled() && nextData.size() == 0) {
            forwardButton.setDisable(true);
        }
        backButton.setDisable(false);
    }

    public void dataChangeListeners() {
        dataField1.textProperty().addListener(event -> dataChanged1());
        dataField2.textProperty().addListener(event -> dataChanged2());
        dataField3.textProperty().addListener(event -> dataChanged3());
        dataField4.textProperty().addListener(event -> dataChanged4());
        dataField5.textProperty().addListener(event -> dataChanged5());
        dataField1.setOnAction(event -> resetField(1));
        dataField2.setOnAction(event -> resetField(2));
        dataField3.setOnAction(event -> resetField(3));
        dataField4.setOnAction(event -> resetField(4));
        dataField5.setOnAction(event -> resetField(5));

    }

    public void dataChanged1() {
        if(data instanceof Route) {
            Route route = (Route) data;
            updateIndicator(indicator1, route.getID(), dataField1);
        } else if(data instanceof Trip) {
            Trip trip = (Trip) data;
            updateIndicator(indicator1, trip.getTripID(), dataField1);
        } else if(data instanceof Stop) {
            Stop stop = (Stop) data;
            updateIndicator(indicator1, stop.getID(), dataField1);
        } else if(data instanceof StopTime) {
            StopTime stopTime = (StopTime) data;
            updateIndicator(indicator1, stopTime.getTrip().getTripID(), dataField1);
        }
    }

    public void dataChanged2() {
        if(data instanceof Route) {
            Route route = (Route) data;
            updateIndicator(indicator2, route.getLongName(), dataField2);
        } else if(data instanceof Trip) {
            Trip trip = (Trip) data;
            updateIndicator(indicator2, trip.getRoute().getID(), dataField2);
        } else if(data instanceof Stop) {
            Stop stop = (Stop) data;
            updateIndicator(indicator2, stop.getName(), dataField2);
        } else if(data instanceof StopTime) {
            StopTime stopTime = (StopTime) data;
            updateIndicator(indicator2, stopTime.getArrivalTime().toString(), dataField2);
        }
    }

    public void dataChanged3() {
        if(data instanceof Route) {
            Route route = (Route) data;
            updateIndicator(indicator3, route.getShortName(), dataField3);
        } else if(data instanceof Stop) {
            Stop stop = (Stop) data;
            updateIndicator(indicator3, stop.getDescription(), dataField3);
        } else if(data instanceof StopTime) {
            StopTime stopTime = (StopTime) data;
            updateIndicator(indicator3, stopTime.getDepartTime().toString(), dataField3);
        }
    }

    public void dataChanged4() {
        if(data instanceof Route) {
            Route route = (Route) data;
            updateIndicator(indicator4, route.getDescription(), dataField4);
        } else if(data instanceof Stop) {
            Stop stop = (Stop) data;
            updateIndicator(indicator4, Double.toString(stop.getLongitude()), dataField4);
        } else if(data instanceof StopTime) {
            StopTime stopTime = (StopTime) data;
            updateIndicator(indicator4, stopTime.getStop().getID(), dataField4);
        }
    }

    public void dataChanged5() {
        if(data instanceof Route) {
            Route route = (Route) data;
            updateIndicator(indicator5, route.getColor().toString(), dataField5);
        } else if(data instanceof Stop) {
            Stop stop = (Stop) data;
            updateIndicator(indicator5, Double.toString(stop.getLatitude()), dataField5);
        } else if(data instanceof StopTime) {
            StopTime stopTime = (StopTime) data;
            updateIndicator(indicator5, Integer.toString(stopTime.getSequence()), dataField5);
        }
    }

    public void apply() {
        if(data instanceof Route) {
            Route route = (Route) data;
            updateRoute(route);
        } else if(data instanceof Trip) {
            Trip trip = (Trip) data;
            updateTrip(trip);
        } else if(data instanceof Stop) {
            Stop stop = (Stop) data;
            updateStop(stop);
        } else if(data instanceof StopTime) {
            StopTime stopTime = (StopTime) data;
            updateStopTime(stopTime);
        }
    }

    /*******************************************************************************************
     * HELPER METHODS
     *******************************************************************************************/

    private void setVisible(SplitPane dataPane, boolean mode) {
        if (mode) {
            if(!dataPane.isVisible()) {
                dataPane.setVisible(true);
            }
        } else {
            if(dataPane.isVisible()) {
                dataPane.setVisible(false);
            }
        }
    }

    private void updateIndicator(Circle indicator, String actual, TextField comparable) {
        if (!actual.equals(comparable.getText())) {
            indicator.setFill(Color.DEEPSKYBLUE);
        } else {
            if (indicator.getFill() != Color.LIME) {
                indicator.setFill(Color.LIME);
            }
        }
    }

    private void resetField(int field) {
        switch(field) {
            case 1:
                if(data instanceof Route) {
                    Route route = (Route) data;
                    resetData(indicator1, route.getID(), dataField1);
                } else if(data instanceof Trip) {
                    Trip trip = (Trip) data;
                    resetData(indicator1, trip.getTripID(), dataField1);
                } else if(data instanceof Stop) {
                    Stop stop = (Stop) data;
                    resetData(indicator1, stop.getID(), dataField1);
                } else if(data instanceof StopTime) {
                    StopTime stopTime = (StopTime) data;
                    resetData(indicator1, stopTime.getTrip().getTripID(), dataField1);
                }
                break;
            case 2:
                if(data instanceof Route) {
                    Route route = (Route) data;
                    resetData(indicator2, route.getLongName(), dataField2);
                } else if(data instanceof Trip) {
                    Trip trip = (Trip) data;
                    resetData(indicator2, trip.getRoute().getID(), dataField2);
                } else if(data instanceof Stop) {
                    Stop stop = (Stop) data;
                    resetData(indicator2, stop.getName(), dataField2);
                } else if(data instanceof StopTime) {
                    StopTime stopTime = (StopTime) data;
                    resetData(indicator2, stopTime.getArrivalTime().toString(), dataField2);
                }
                break;
            case 3:
                if(data instanceof Route) {
                    Route route = (Route) data;
                    resetData(indicator3, route.getShortName(), dataField3);
                } else if(data instanceof Stop) {
                    Stop stop = (Stop) data;
                    resetData(indicator3, stop.getDescription(), dataField3);
                } else if(data instanceof StopTime) {
                    StopTime stopTime = (StopTime) data;
                    resetData(indicator3, stopTime.getDepartTime().toString(), dataField3);
                }
                break;
            case 4:
                if(data instanceof Route) {
                    Route route = (Route) data;
                    resetData(indicator4, route.getDescription(), dataField4);
                } else if(data instanceof Stop) {
                    Stop stop = (Stop) data;
                    resetData(indicator4, Double.toString(stop.getLongitude()), dataField4);
                } else if(data instanceof StopTime) {
                    StopTime stopTime = (StopTime) data;
                    resetData(indicator4, stopTime.getStop().getID(), dataField4);
                }
                break;
            case 5:
                if(data instanceof Route) {
                    Route route = (Route) data;
                    resetData(indicator5, route.getColor().toString(), dataField5);
                } else if(data instanceof Stop) {
                    Stop stop = (Stop) data;
                    resetData(indicator5, Double.toString(stop.getLatitude()), dataField5);
                } else if(data instanceof StopTime) {
                    StopTime stopTime = (StopTime) data;
                    resetData(indicator5, Integer.toString(stopTime.getSequence()), dataField5);
                }
                break;
            default:
                throw new IllegalArgumentException("Improper Field Entry.");

        }
    }

    private void resetData(Circle indicator, String data, TextField field) {
        if(field.getText().equals("")) {
            field.setText(data);
            if(indicator.getFill() != Color.LIME) {
                indicator.setFill(Color.LIME);
            }
        }
    }

    private void updateRoute(Route route) {
        if(indicator1.getFill() != Color.LIME) {
            String info = dataField1.getText();
            //
            indicator1.setFill(Color.LIME);
        }
        if(indicator2.getFill() != Color.LIME) {
            String info = dataField2.getText();
            //
            indicator2.setFill(Color.LIME);
        }
        if(indicator3.getFill() != Color.LIME) {
            String info = dataField3.getText();
            //
            indicator3.setFill(Color.LIME);
        }
        if(indicator4.getFill() != Color.LIME) {
            String info = dataField4.getText();
            //
            indicator4.setFill(Color.LIME);
        }
        if(indicator5.getFill() != Color.LIME) {
            String info = dataField5.getText();
            //
            indicator5.setFill(Color.LIME);
        }
    }

    private void updateTrip(Trip trip) {
        if(indicator1.getFill() != Color.LIME) {
            String info = dataField1.getText();
            //
            indicator1.setFill(Color.LIME);
        }
        if(indicator2.getFill() != Color.LIME) {
            String info = dataField2.getText();
            //
            indicator2.setFill(Color.LIME);
        }
    }

    private void updateStop(Stop stop) {
        if(indicator1.getFill() != Color.LIME) {
            String info = dataField1.getText();
            //
            indicator1.setFill(Color.LIME);
        }
        if(indicator2.getFill() != Color.LIME) {
            String info = dataField2.getText();
            //
            indicator2.setFill(Color.LIME);
        }
        if(indicator3.getFill() != Color.LIME) {
            String info = dataField3.getText();
            //
            indicator3.setFill(Color.LIME);
        }
        if(indicator4.getFill() != Color.LIME) {
            String info = dataField4.getText();
            //
            indicator4.setFill(Color.LIME);
        }
        if(indicator5.getFill() != Color.LIME) {
            String info = dataField5.getText();
            //
            indicator5.setFill(Color.LIME);
        }
    }

    private void updateStopTime(StopTime stopTime) {
        if(indicator1.getFill() != Color.LIME) {
            String info = dataField1.getText();
            //
            indicator1.setFill(Color.LIME);
        }
        if(indicator2.getFill() != Color.LIME) {
            String info = dataField2.getText();
            //
            indicator2.setFill(Color.LIME);
        }
        if(indicator3.getFill() != Color.LIME) {
            String info = dataField3.getText();
            //
            indicator3.setFill(Color.LIME);
        }
        if(indicator4.getFill() != Color.LIME) {
            String info = dataField4.getText();
            //
            indicator4.setFill(Color.LIME);
        }
        if(indicator5.getFill() != Color.LIME) {
            String info = dataField5.getText();
            //
            indicator5.setFill(Color.LIME);
        }
    }
}
