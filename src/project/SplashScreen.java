package project;

import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreen {
	public static final int WINDOW_HEIGHT = 500;
	public static final int WINDOW_WIDTH = 800;
	private Scene scene;
	private Stage stage;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;

	public final Image logo = new Image("images/logo.png",500,300,false,false);
	public final Image startBack = new Image("images/startBack.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);

	//set-up splash screen
	public SplashScreen() {
		this.root = new Group();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
	}

	public void setSplashScreen(Stage stage){
		this.stage = stage;

		//set stage elements here
		this.root.getChildren().add(canvas);
		this.root.getChildren().add(this.startAP());

		this.stage.setTitle("Kirby's Dream Land");
		this.stage.setScene(this.scene);

		this.stage.show();
	}

	public AnchorPane startAP() {
		AnchorPane splashscreen = new AnchorPane();
		splashscreen.setPrefSize(WINDOW_WIDTH-50, WINDOW_HEIGHT-50);

		this.gc.drawImage(this.startBack,0,0);

		ImageView imageView = new ImageView(logo); //displays the logo
		splashscreen.getChildren().add(imageView);

		ImageView center = (ImageView) (Node) splashscreen.getChildren().get(0); //makes ImageView a node so you can add it to the root
		center.setX(150); //positions the image on the center
		center.setY(85);
		return splashscreen;
	}

	private void startGame() {
		this.root.getChildren().remove(0); //removes splash screen
		GameStage theGameStage = new GameStage();
		theGameStage.setStartStage(this.stage); //starts the game
	}

	public void delay() {
		PauseTransition transition = new PauseTransition(Duration.seconds(3)); //pauses for three seconds
		transition.play();
		transition.setOnFinished(event -> startGame()); //after three seconds, startGame will be called
	}
}
