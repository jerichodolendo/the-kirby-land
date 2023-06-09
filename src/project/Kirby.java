package project;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;

public class Kirby extends Sprite{
	private String name;
	private int strength;
	private boolean alive;

	private ArrayList<Bullet> bullets;
	public final static Image KIRBY_IMAGE = new Image("images/kirby.png",Kirby.KIRBY_WIDTH,Kirby.KIRBY_WIDTH,false,false);
	private final static int KIRBY_WIDTH = 60;

	private int ghostKilled; //keeps track of number of ghosts killed

	public Kirby(int x, int y){
		super(x,y);
		Random r = new Random();
		this.strength = r.nextInt(151)+100;
		this.alive = true;
		this.bullets = new ArrayList<Bullet>();
		this.loadImage(Kirby.KIRBY_IMAGE);
		this.ghostKilled = 0; //sets initial killed ghost to 0
	}

	public boolean isAlive(){
		if(this.alive) return true;
		return false;
	}
	public String getName(){
		return this.name;
	}

	public void die(){
    	this.alive = false;
    	System.out.println("Kirby has died...");
    }

	//method that will get the bullets 'shot' by Kirby
	public ArrayList<Bullet> getBullets(){
		return this.bullets;
	}

	//method called if spacebar is pressed
	public void shoot(){
		//compute for the x and y initial position of the bullet
		int x = (int) (this.x + this.width-10);
		int y = (int) (this.y + this.height/4);
		/*
		 * TODO: Instantiate a new bullet and add it to the bullets arraylist of ship
		 */

		Bullet b= new Bullet(x,y, this.strength);

		this.bullets.add(b);
    }

	//method called if up/down/left/right arrow key is pressed.
	public void move() {
		/*
		 *TODO: 		Only change the x and y position of Kirby if the current x,y position
		 *				is within the gamestage width and height so that Kirby won't exit the screen
		 */

		int xNow = this.x + this.dx;	//current x-value if still within bounds
		int yNow = this.y + this.dy;	//current y-value if still within bounds

		if (xNow<(GameStage.WINDOW_WIDTH-50)&&yNow<(GameStage.WINDOW_HEIGHT-50)&&xNow>0&&yNow>30){
			this.x = xNow;
			this.y = yNow;
		}

	}

	//setter method that allows other classes to modify the number of ghosts killed, given that the ghost was killed
	public void setGhostsKilled(){
		this.ghostKilled +=1;
		System.out.println("Ghost Killed: "+this.ghostKilled);
	}

	//getter method that returns number of ghosts killed, will be used for the gameEndStage and the status bar during gameTime
	public int getGhostKilled(){
		return this.ghostKilled;
	}

	//setter method that modifies the Kirby's strength
	public void setStrength(int damage){
		this.strength += damage;
		System.out.println("Kirby is injured! Current strength: "+ this.strength);
		if (this.strength <=0) this.die(); //player lost when Kirby reached strength less than or equal to 0

	}

	//getter method that gives Kirby's current strength
	public int getStrength(){
		return this.strength;
	}
}
