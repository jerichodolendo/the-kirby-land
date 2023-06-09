package project;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		SplashScreen splash = new SplashScreen();
		splash.setSplashScreen(stage); //starts with splash screen
		splash.delay(); //starts delay of 3 seconds before displaying main menu
	}
}
