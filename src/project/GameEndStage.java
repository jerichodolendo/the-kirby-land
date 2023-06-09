package project;


import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameEndStage {
	private BorderPane root;
	private Scene scene;
	private String GameResult;
	private int totalFishKilled;
	private Stage stage;

	private Canvas canvas;
	private GraphicsContext gc;

	public final Image WIN_GAME = new Image("images/win.jpg",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);
	public final Image LOSE_GAME = new Image("images/fail.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);


	public GameEndStage(String result, int fishKilled) {
		this.root = new BorderPane();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);

		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();

		this.GameResult = result; //placeholder whether the player won or lost
		this.totalFishKilled = fishKilled; //sets total amount of fish killed by the player
	}

	public void setStageProperties(Stage stage) {
		this.stage = stage;
		this.root.setPrefSize(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT-50);
		this.root.getChildren().add(canvas);

		VBox vbox= new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(30);

		Font font = Font.font("SansSerif", FontWeight.BOLD, 20);
		Label fishKilled = new Label("Total Ghosts Killed: "+this.totalFishKilled); //displays the amount of total fish killed by the player
		fishKilled.setFont(font);

		boolean r;
		Label result;
		if(this.GameResult.contentEquals("GameWin")){ //if the player won, displays text saying the player won
			this.gc.drawImage(this.WIN_GAME, 0, 0);
			result = new Label("\t\t\t\tCongratulations! \nYou've successfully defended your Dream Land!");
			result.setAlignment(Pos.CENTER);
			result.setAlignment(Pos.CENTER);
			result.setTextFill(Color.DARKGREEN);
			r=true;
		}
		else {
			this.gc.drawImage(this.LOSE_GAME, 0, 0);
			fishKilled.setTextFill(Color.WHITESMOKE);
			result = new Label("\t\t\tYou Lose! \nDream Land was overtaken by enemies.");
			result.setTextFill(Color.RED);
			r=false;
		}
		font = Font.font("SansSerif", FontWeight.EXTRA_BOLD, 30);
		result.setAlignment(Pos.CENTER);
		result.setFont(font);

		vbox.getChildren().addAll(result,fishKilled);

		//displays three new buttons


		HBox hbox = new HBox();
		hbox.setSpacing(100);
		Button newGame = new Button("Retry"); //new button that resets the game when clicked
		this.buttonOnClicked(newGame, "new", r);

		Button menu = new Button("Menu"); //new button that returns to main menu
		this.buttonOnClicked(menu, "menu", r);

		Button exitBtn = new Button("Exit"); //new button that exits the program when clicked
		this.buttonOnClicked(exitBtn, "exit", r);

		hbox.setPrefSize(GameStage.WINDOW_WIDTH, 200);
		hbox.setAlignment(Pos.TOP_CENTER);
		hbox.getChildren().addAll(menu,newGame, exitBtn);

		this.root.setCenter(vbox);
		this.root.setBottom(hbox);

		this.stage.setTitle("Kirby: Dream Land"); //sets the title of the program
		this.stage.setScene(this.scene);
		this.stage.show();
	}

	private void buttonOnClicked(Button button, String pass, boolean win) {
		button.setFont(Font.font("SansSerif", FontWeight.BOLD, 25));
		button.setBackground(null);
		if(win){
			button.setTextFill(Color.DARKOLIVEGREEN);
			button.setStyle("-fx-border-color: white");
		} else{
			button.setTextFill(Color.WHITE);
			button.setStyle("-fx-border-color: white");
		}

		button.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0){
				buttonClickedHandler(pass);
			}
		});
	}

	private void buttonClickedHandler(String btn){
		if(btn.contentEquals("new")){//when RETRY button is clicked, new game starts
			System.out.println("\n\n[RETRY] button clicked!");
			System.out.println("------->Loading new game...");
			System.out.println("-------New Game Loaded-------\n\n");


			GameStage game = new GameStage();
			game.setStage(this.stage);

		} else if (btn.contentEquals("exit")){ //when EXIT button is clicked, terminates the program
			System.out.println("\n\n[EXIT] button clicked!");
			System.out.println("Terminating program...");
			System.out.println("-------Thank you for playing the game!-------\n");
			System.exit(0);
		} else if (btn.contentEquals("menu")) { //when MENU button is clicked, returns to main menu
			System.out.println("\n\n[MENU] button clicked!");
			System.out.println("-------> Going back to the Menu page \n\n");

			GameStage game = new GameStage();
			game.setStartStage(this.stage);
		}

	}

}
