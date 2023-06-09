package project;


import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GameStage {
	public static final int WINDOW_HEIGHT = 500;
	public static final int WINDOW_WIDTH = 800;
	private Scene scene;
	private Stage stage;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private GameTimer gametimer;

	public final Image start = new Image("images/start.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);
	public final Image startBack = new Image("images/startBack.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);
	public final Image logo = new Image("images/logo.png",500,300,false,false);

	//the class constructor
	public GameStage() {
		this.root = new Group();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
	}

	//method to add the stage elements for the game
	public void setStage(Stage stage) {
		this.stage = stage;


		//set stage elements here
		this.root.getChildren().add(canvas);

		//sets title
		this.stage.setTitle("Kirby's Dream Land");
		this.stage.setScene(this.scene);

		//this.scene.setFill(Color.CADETBLUE);
		this.gametimer = new GameTimer(this.gc,this.scene, this.stage);
		this.gametimer.start();

		this.stage.show();
	}

	//set-up stage for beforeGameStage
	public void setStartStage(Stage stage){
		this.stage = stage;

		//set stage elements here
		this.root.getChildren().add(canvas);
		this.root.getChildren().add(this.startBP());

		this.stage.setTitle("Kirby's Dream Land");
		this.stage.setScene(this.scene);

		this.stage.show();
	}

	public BorderPane startBP(){
		BorderPane border = new BorderPane();
		border.setPrefSize(GameStage.WINDOW_WIDTH-50, GameStage.WINDOW_HEIGHT-50);
		this.gc.drawImage(this.start, 0, 0);

		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(30);

		//buttons
		//also changes buttons based on what page the player is currently in
		Button newGame = new Button ("New Game"); //button for new game
		this.setButtons(newGame);
		this.buttonOnClicked(newGame, "new");

		Button about = new Button("About"); //button for about page
		this.setButtons(about);
		this.buttonOnClicked(about, "abt");

		Button instructions = new Button("Instruction"); //button for instructions page
		this.setButtons(instructions);
		this.buttonOnClicked(instructions, "ins");

		Button exitBtn = new Button("Exit"); //button to exit the program
		this.setButtons(exitBtn);
		this.buttonOnClicked(exitBtn, "exit");

		vbox.getChildren().add(newGame);
		vbox.getChildren().add(about);
		vbox.getChildren().add(instructions);
		vbox.getChildren().add(exitBtn);

		border.setRight(vbox);
		return border;
	}

	//changes button properties
	private void setButtons(Button b) {
		Font f = Font.font("SansSerif", FontWeight.EXTRA_BOLD,30);
		b.setFont(f);
		b.setTextFill(Color.DEEPPINK);
		b.setAlignment(Pos.CENTER);
		b.setStyle("-fx-border-color: #ff007f"); //setBorderColor
	}

	private void buttonOnClicked(Button button, String pass){
		button.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0){
				handleButtonClick(pass);
			}
		});

	}

	//function that's responsible for changing the content based on button clicked
	private void handleButtonClick(String buttonName) {
		if(buttonName.contentEquals("new")){
			System.out.println("[NEW GAME] was button clicked");
			this.startGameNow(); //calls startGameNow function when New Game button's clicked
		} else if (buttonName.contentEquals("abt")){
			System.out.println("[ABOUT] was button clicked");
			this.root.getChildren().remove(1);
			this.root.getChildren().add(this.instructionAbout("abt")); //displays text under about's page
		} else if(buttonName.contentEquals("ins")){
			System.out.println("[INSTRUCTION] was button clicked");
			this.root.getChildren().remove(1);
			this.root.getChildren().add(this.instructionAbout("ins")); //displays text under instruction's page
		} else if (buttonName.contentEquals("exit")){ //exits the program when clicked
			System.out.println("[EXIT] button clicked!");
			System.out.println("Terminating program...");
			System.out.println("-------Thank you for playing the game!-------\n");
			System.exit(0);
		} else if (buttonName.contentEquals("rtn")) { //returns to main menu if return button is clicked
			System.out.println("[RETURN] button was clicked");
			System.out.println("Going back to the menu...\n");
			this.root.getChildren().remove(1);
			this.root.getChildren().add(this.startBP());
		}
	}

	//if the game start by [new game] button
	private void startGameNow(){
		//removes the starting page
		this.root.getChildren().remove(1);
		this.scene.setFill(Color.MIDNIGHTBLUE);
		this.gametimer = new GameTimer(this.gc, this.scene, this.stage);
		this.gametimer.start();
	}

	//function that changes screen to game over stage
	public void EndGameNow(String result, int fK, Stage stage){
		GameEndStage end = new GameEndStage(result, fK);
		end.setStageProperties(stage);
	}

	private BorderPane instructionAbout(String btnName){
		this.gc.drawImage(this.startBack, 0, 0);
		Font fontHead = Font.font("Verdana", FontWeight.EXTRA_BOLD, 30);
		Font font = Font.font("Comic Sans MS", FontWeight.NORMAL, 15);
		Label head = null;
		Text content = null;
		if (btnName.contentEquals("ins")){
			head = new Label("INSTRUCTIONS: "); //displays instructions needed to win the game
			content = new Text(
					"MISSION:\n"
					+ "Kirby must survive for 60 seconds. Kirby can shoot shockwaves to kill enemies.\n"
					+ "Enemies spawn every 5 seconds. Meanwhile, power ups spawn every 10 seconds until it disappears after 5 seconds that gives certain amount of strength depending on the type. "
					+ "30 seconds in, as ghost boss will spawn.\n"
					+ "\nKEYBOARD CONTROLS:\n"
					+ "UP - moves Kirby upward\n"
					+ "RIGHT - moves Kirby towards the right\n"
					+ "LEFT - moves Kirby towards the left\n"
					+ "DOWN - moves Kirby downward\n"
					+ "SPACEBAR - shoot shockwave\n"
					+ "\nPOWER UPS:\n"
					+ "HEART - gives Kirby immunity for 3 seconds\n"
					+ "STAR - gives additional strength based on the amount of strength the STAR gives\n"
					);
			content.setStyle("-fx-background-color: #ffffff;");
		}
		else if (btnName.contentEquals("abt")){ //displays text about the developers
			head = new Label("ABOUT: ");
			content = new Text("DEVELOPED BY:\n"
					+ "Bartolome, Vallerie C.\n"
					+ "Dolendo, Jericho Paolo T."
					);
		}
		head.setFont(fontHead);
		head.setTextFill(Color.ALICEBLUE);
		content.setFont(font);
		content.setWrappingWidth(GameStage.WINDOW_WIDTH-200);
		content.setTextAlignment(TextAlignment.JUSTIFY);
		content.setFill(Color.GHOSTWHITE);

		Button rtn = new Button ("Return"); //creates a button that returns to main menu when clicked
		rtn.setFont(font);
		this.buttonOnClicked(rtn, "rtn");

		//This idea from https://examples.javacodegeeks.com/desktop-java/javafx/javafx-borderpane-example/
		BorderPane.setAlignment(head, Pos.TOP_CENTER);
		BorderPane.setAlignment(content, Pos.CENTER);
		BorderPane.setAlignment(rtn, Pos.BOTTOM_RIGHT);

        // Create a BorderPane with a Text node in each of the five regions
        BorderPane b = new BorderPane(content,head,rtn, null, null);
        // Set the Size of the VBox
        b.setPrefSize(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
        // Set the Style-properties of the BorderPane
        b.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: white;");
		return b;
	}

}

