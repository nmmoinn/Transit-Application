package GroupR;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.plaf.synth.Region;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Time;

/**
 * Driver class for the Application. Creates the JavaFX GUI
 * @author goreckinjn, jinq, hueblergw
 * @version 1.0
 * @created 05-Oct-2017 09:39:31
 */

public class ApplicationDriver extends Application {
	protected static Stage STAGE;
	public static final int SCENE_WIDTH = 1100;
	public static final int SCENE_HEIGHT = 620;

	/**
	 * Main method for the application. Launches the controller
	 * 
	 * @param args
	 * @author goreckinj
	 */
	public static void main(String[] args){
		launch(args);
	}

	/**
	 * Start method for the application. Sets up the stage for the controller to be
	 * displayed
	 * 
	 * @param primaryStage
	 * @author goreckinj
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.STAGE = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("ApplicationDriver.fxml"));
		primaryStage.setTitle("Transit Application");
		primaryStage.setScene(new Scene(root, SCENE_WIDTH, SCENE_HEIGHT));
		primaryStage.setResizable(false);
		primaryStage.setX(Screen.getPrimary().getBounds().getWidth()/2 - SCENE_WIDTH/2);
		primaryStage.setY(Screen.getPrimary().getBounds().getHeight()/2 - (SCENE_HEIGHT*2/3));
		primaryStage.setOnHidden(e -> Platform.exit());
		primaryStage.show();

	}


}
