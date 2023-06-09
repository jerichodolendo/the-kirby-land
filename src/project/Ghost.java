package project;


import java.util.Random;
import javafx.scene.image.Image;


public class Ghost extends Sprite{

	public static final int MAX_GHOST_SPEED = 5;
	public final static Image GHOST_IMAGE = new Image("images/ghost.png",Ghost.GHOST_WIDTH,Ghost.GHOST_WIDTH,false,false);
	public final static int GHOST_WIDTH=50;
	private boolean alive;
	//attribute that will determine if the ghost will initially move to the right
	private boolean moveRight;
	private int speed = 0;



	Ghost(int x, int y){
		super(x,y);
		this.alive = true;
		this.loadImage(Ghost.GHOST_IMAGE);
		/*
		 *TODO: Randomize speed of ghost and moveRight's initial value
		 */
		Random r = new Random();	//Initialize randomizer
		while (this.speed==0){
			this.speed = r.nextInt(MAX_GHOST_SPEED);	//initialize random speed
		}
		this.moveRight = false;	//initially false, so the ghost will move to the left
	}

	//method that changes the x position of the ghost
	void move(){
		/*
		 * TODO: 				If moveRight is true and if the ghost hasn't reached the right boundary yet,
		 *    						move the ghost to the right by changing the x position of the ghost depending on its speed
		 *    					else if it has reached the boundary, change the moveRight value / move to the left
		 * 					 Else, if moveRight is false and if the ghost hasn't reached the left boundary yet,
		 * 	 						move the ghost to the left by changing the x position of the ghost depending on its speed.
		 * 						else if it has reached the boundary, change the moveRight value / move to the right
		 */


		if (this.x<=0) this.moveRight = true; //left boundary reached, move ghost to the right
		else if(this.x>=GameStage.WINDOW_WIDTH-50) this.moveRight = false;
				//right boundary reached, move the ghost to the left

		if (this.moveRight==true) this.x += this.speed;	//x increases as ghost move to the right
		else this.x -= this.speed;	//x decreases as ghost moves to the left

	}

	//function that checks if the ghost is alive
	public boolean isAlive() {
		return this.alive;
	}

	//setter that sets if the fish is dead or alive
	public void setIsDead(){
		this.alive = false;
	}
}
