package project;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/*
 * The GameTimer is a subclass of the AnimationTimer class. It must override the handle method.
 */

public class GameTimer extends AnimationTimer{

	private GraphicsContext gc;
	private Scene theScene;
	private Kirby kirby;
	private ArrayList<Ghost> ghosts;
	public static final int MAX_NUM_GHOST = 3;

	private int spawnSec = 5;	//serve as a flag for spawning ghosts with 5 sec interval
	private long startTime;	//
	private boolean BossTimeDone = false;	//confirms if the boss already appears
	private BossGhost boss; //the BOSS GHOST

	private ArrayList<PowerUp> powerups;
	private int spawnPowerupSec= 10;
	private long powerUpTime;
	private boolean heartImmunityBenefit = false;
	private int heartImmunityTimeStart = 0;
	private int bossShootingTime;

	public final Image bg = new Image("images/background.png",GameStage.WINDOW_WIDTH+200,GameStage.WINDOW_HEIGHT,false,false);
	public final Image land = new Image("images/land.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);

	Stage stage;

	GameTimer(GraphicsContext gc, Scene theScene, Stage stage){
		this.gc = gc;
		this.theScene = theScene;
		this.kirby = new Kirby(100,100);
		//instantiate the ArrayList of Ghosts
		this.ghosts = new ArrayList<Ghost>();

		this.powerups = new ArrayList<PowerUp>();

		//call the spawnGhosts method
		this.spawnGhosts(7);	//spawn 7 ghost, initial

		//call method to handle mouse click event
		this.handleKeyPressEvent();

		this.startTime = System.nanoTime();
		this.stage = stage;
	}

	@Override
	public void handle(long currentNanoTime) {
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		long startSec = TimeUnit.NANOSECONDS.toSeconds(startTime); //converts startTime to seconds
		long currentSec = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime); //converts current system's nano time

		int timeSecStart= (int)(currentSec-startSec); //seconds passed since the program has started

		if (timeSecStart<5) {
			this.gc.drawImage(this.land, 0, 0);
			this.gc.setFont(Font.font("Monospace", FontWeight.BOLD, 30));
			this.gc.fillText("Goal: Protect your Dream Land \nfrom invasion!", 100, 60);
			this.gc.setFill(Color.WHITE);
			this.gc.setFont(Font.font(25));
			this.gc.fillText("Starting in "+(5-timeSecStart)+"...", 325, 275);
		} else {
			this.gc.drawImage(this.bg, 0, 0);
			int timeSec = timeSecStart - 5;
			this.kirby.move();
			/*
			 * TODO: Call the moveBullets and moveGhosts methods
			 */

			//render Kirby
			this.kirby.render(this.gc);
			this.kirbyCollidesGhost();

			/*
			 * TODO: Call the renderGhosts and renderBullets methods
			 */

			//bullets
			this.renderBullets();
			this.moveBullets();

			//ghosts
			this.renderGhosts();
			this.moveGhosts();

			//statusBar
			this.statusBar(60-timeSec);	//count down seconds

			//Heart Immunity duration
			if (this.heartImmunityBenefit&&this.heartImmunityTimeStart==0) this.heartImmunityTimeStart= (int) currentSec;
			if ((currentSec - this.heartImmunityTimeStart) > 3){	// checks if 3 seconds has passed, then immunity disappears
				this.heartImmunityBenefit = false;
				this.heartImmunityTimeStart=0;
			}


		/*
		 * will spawn another batch of ghosts given that the condition is met
		 * this.spawnSec---the required time interval (5 sec) for spawning ghosts.
		 * 				---updates value to avoid repetition
		 */
			if (timeSec==this.spawnSec){
				System.out.println("5 sec passed! New spawn arriving!");
				this.spawnGhosts(GameTimer.MAX_NUM_GHOST);	//spawn 3 more ghosts
				this.spawnSec += 5;	//updates value
			}

		/* PowerUps will appear every 10 seconds */
			if (timeSec==this.spawnPowerupSec){
				System.out.println("10 sec passed! PowerUps time!");
				this.spawnPowerUps();
				this.powerUpTime= currentSec;
				this.spawnPowerupSec +=10;
			}

			//nested if statements that checks if its time to spawn power ups
			if (!this.powerups.isEmpty()){
				if((currentSec-this.powerUpTime)<=PowerUp.DURATION){
					this.renderPowerUps();//renders power ups if its less than 5 seconds
					this.getPowerUp();	//function when kirby collides with a powerup
				} else{
					for (int i=0; i<this.powerups.size();i++){
						this.powerups.remove(i);	//removes the power ups to render if 5 seconds passed
					}
				}
			}


		/* Boss Ghost will appear if 30 sec has passed */
			if (timeSec==30 && !this.BossTimeDone) {
				System.out.println("BOSS GHOST");
				//BIG BOSS appearance
				this.spawnBossGhost();	//spawns boss ghost
				this.BossTimeDone = true;		//confirms that the boss already appears
				this.bossShootingTime = (int)currentSec;
			}

			if(this.BossTimeDone&&this.boss.isAlive()){
				if((currentSec-this.bossShootingTime)==2){
					this.boss.bossShoots();
					this.bossShootingTime = (int)currentSec;
				}

				this.renderBossBullets();
				this.moveBossBullets();
			}


		/*if 60 sec passed, game ends*/
			if (timeSec==60 || !this.kirby.isAlive()){
				System.out.println("GAME ENDS...");
				//GAMEOVER stage
				this.stop();
				GameStage gs = new GameStage();
				if (this.kirby.isAlive()) gs.EndGameNow("GameWin", this.kirby.getGhostKilled(), this.stage);
				else gs.EndGameNow("GameLose", this.kirby.getGhostKilled(),this.stage);
			}
		}

	}

	//method that will render/draw the ghosts to the canvas
	private void renderGhosts() {
		for (Ghost g : this.ghosts){
			g.render(this.gc);
		}

		//render the Boss Ghost, given that it exist and is alive
		if (this.BossTimeDone&&this.boss.isAlive())this.boss.render(this.gc);
	}

	//method that will render/draw the bullets to the canvas
	private void renderBullets() {
		/*
		 *TODO: Loop through the bullets arraylist of Kirby
		 *				and render each bullet to the canvas
		 */
		ArrayList<Bullet> bList = this.kirby.getBullets();

		for (Bullet b: bList){
			b.render(this.gc);
		}
	}


	private void renderBossBullets() {
			ArrayList<BulletBoss> bList = this.boss.getBullets();

			for (BulletBoss b: bList){
				b.render(this.gc);
			}
		}

	//method that will spawn/instantiate max number of ghosts at a random x,y location
	private void spawnGhosts(int max){
		Random r = new Random();
		for(int i=0;i<max;i++){
			int y = r.nextInt(GameStage.WINDOW_HEIGHT-30);
			int x = GameStage.WINDOW_WIDTH-30;	//this is fixed so that all ghosts appears from the right screen
			/*
			 *TODO: Add a new object Ghost to the ghosts arraylist
			 */
			while (y<=30){
				y = r.nextInt(GameStage.WINDOW_HEIGHT-30);	//makes sure that ghosts are below the status bar
			}

			Ghost g = new Ghost(x,y);
			this.ghosts.add(g);
		}
	}

	//method that will instantiate the boss ghost given that 30 seconds has passed
	private void spawnBossGhost(){
		Random r = new Random();

		int y = r.nextInt(GameStage.WINDOW_HEIGHT-100);
		int x = GameStage.WINDOW_WIDTH-100;	//this is fixed so that the boss ghost appears from the right screen

		while (y<=30){
			y = r.nextInt(GameStage.WINDOW_HEIGHT-100);
		}

		BossGhost boss = new BossGhost(x,y);
		this.boss = boss;
	}

	//method that will move the bullets shot by Kirby
	private void moveBullets(){
		//create a local arraylist of Bullets for the bullets 'shot' by Kirby
		ArrayList<Bullet> bList = this.kirby.getBullets();

		//Loop through the bullet list and check whether a bullet is still visible.
		for(int i = 0; i < bList.size(); i++){
			Bullet b = bList.get(i);
			/*
			 * TODO:  If a bullet is visible, move the bullet, else, remove the bullet from the bullet array list.
			 */
			if (b.isVisible()){
				b.move();
				this.bulletHits(b);	//if the bullet hits a ghost
			}
			else bList.remove(i);
		}
	}

	private void moveBossBullets(){
		ArrayList<BulletBoss> bList = this.boss.getBullets();

		for(int i = 0; i < bList.size(); i++){
			BulletBoss b = bList.get(i);

			if (b.isVisible()){
				b.moveS();
				this.bossHitsKirby(b);	//if Kirby was shot by the boss
			}
			else bList.remove(i);
		}
	}

	//method that will move the ghosts
	private void moveGhosts(){
		//Loop through the ghosts arraylist
		for(int i = 0; i < this.ghosts.size(); i++){
			Ghost g = this.ghosts.get(i);
			/*
			 * TODO:  *If the ghost is alive, move the ghost. Else, remove the ghost from the ghost arraylist.
			 */
			if (g.isAlive()) g.move();
			else this.ghosts.remove(i);
		}
		//moves the Boss Ghost given that is exist and is alive
		if (this.BossTimeDone&&this.boss.isAlive()) this.boss.move();

	}

	private void spawnPowerUps(){
		Random r = new Random();
		//guarantees that two powerups are spawn
		String[] pList= {PowerUp.HEART_TYPE, PowerUp.STAR_TYPE};

		//same logic for spawning ghosts
		for (String i: pList){
			int x = r.nextInt(GameStage.WINDOW_WIDTH-50);
			int y = r.nextInt(GameStage.WINDOW_HEIGHT-50);

			while (y<=30){
				y = r.nextInt(GameStage.WINDOW_HEIGHT-50);
			}

			PowerUp p= new PowerUp(x,y,i);
			System.out.println(p.getType() + " Power-Up located at "+ x+", "+y);
			this.powerups.add(p);
		}
	}

	//same logic when rendering ghosts and the boss ghost
	private void renderPowerUps() {
		for (PowerUp p: this.powerups){
			p.render(this.gc);
		}
	}

	//same logic used when bullets hit a ghost
	private void getPowerUp(){
		for (int i=0; i<this.powerups.size(); i++){
			PowerUp p = this.powerups.get(i);
			if (this.kirby.collidesWith(p)){
				if(p.typeObtained()) this.heartImmunityBenefit = true;
				else this.kirby.setStrength(PowerUp.STAR_STRENGTH);
				System.out.println(p.getType() +" Power-Up obtained.");
				this.gc.setFill(Color.WHITE);
				this.gc.fillText(p.getType() + " was obtained", p.x, (p.y-10), 200);
				this.powerups.remove(i);
			}
		}
	}

	//method that will listen and handle the key press events
	private void handleKeyPressEvent() {
		this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                moveKirby(code);
			}
		});

		this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		            public void handle(KeyEvent e){
		            	KeyCode code = e.getCode();
		                stopKirby(code);
		            }
		        });
    }

	//method that will move the ship depending on the key pressed
	private void moveKirby(KeyCode ke) {
		if(ke==KeyCode.UP) this.kirby.setDY(-5);      //movement

		if(ke==KeyCode.LEFT) this.kirby.setDX(-5);

		if(ke==KeyCode.DOWN) this.kirby.setDY(5);

		if(ke==KeyCode.RIGHT) this.kirby.setDX(5);

		if(ke==KeyCode.SPACE) this.kirby.shoot();

		System.out.println(ke+" key pressed.");
   	}

	//method that will stop the Kirby's movement; set the Kirby's DX and DY to 0
	private void stopKirby(KeyCode ke){
		this.kirby.setDX(0);
		this.kirby.setDY(0);
	}

	/*
	 * this method is check if the bullet hits a ghost
	 * if the ghost hit by the bullet is the BOSS GHOST, the BOSS GHOST health was
	 * 		reduced by the damage induced by the bullet
	 * if the ghost hit is an ordinary one, the ghost immediately dies
	 *
	 */
	private void bulletHits(Bullet bullet){
		for (int i=0; i<this.ghosts.size(); i++){	//for ordinary ghosts
			Ghost ghost = this.ghosts.get(i);
			if (bullet.collidesWith(ghost)) {
				this.ghosts.remove(i);	//removes bullet and ghost altogether when they collide
				bullet.setVisible(false);
				this.kirby.setGhostsKilled();	//increments amount of ghosts killed by the player

			}
		}
		if (this.BossTimeDone&&bullet.collidesWith(this.boss)&&this.boss.isAlive()){	//for boss ghost
			this.boss.setHealth(bullet.getBulletDamage());	//decreases boss' health based on bullet's damage
			bullet.setVisible(false);	//removes bullet from the screen
		}
	}

	private void bossHitsKirby(BulletBoss b){
		if (b.collidesWith(this.kirby)&&!this.heartImmunityBenefit) {
			this.kirby.setStrength(-10);
			b.setVisible(false);
		}
	}

	private void kirbyCollidesGhost(){
		for (int i=0; i<this.ghosts.size(); i++){	//for ordinary fish
			Ghost ghost = this.ghosts.get(i);
			if (this.kirby.collidesWith(ghost)){
				if(!this.heartImmunityBenefit) this.kirby.setStrength(-30);	//if kirby collided with a ghost, decreases kirby's integrity
				this.ghosts.remove(i);
				this.gc.setFont(Font.font(10));
				this.gc.setFill(Color.RED);
				this.gc.fillText("-30", this.kirby.x-10, this.kirby.y-10);
			}
			//Boss Ghost
			//if Kirby has collided with the boss ghost, decreases Kirby's integrity drastically
			if (this.BossTimeDone&&this.kirby.collidesWith(this.boss)&&!this.heartImmunityBenefit) {
				this.kirby.setStrength(-50);

				this.gc.setFont(Font.font(10));
				this.gc.setFill(Color.RED);
				this.gc.fillText("-50", this.kirby.x-10, this.kirby.y-10);
			}
		}
	}

	private void statusBar(int time){
		Font font= Font.font("Monospaced", FontWeight.BOLD, 30);
		this.gc.setFont(font);
		this.gc.setFill(Color.ALICEBLUE);

		this.gc.fillText("Ghosts Killed: "+ this.kirby.getGhostKilled(), 350, 30,200);	//displays amount of ghosts killed by Kirby in real time

		if(time<=10) this.gc.setFill(Color.RED);
		this.gc.fillText("Time: "+time, 200, 30,100);	 //displays current time

		if(this.kirby.getStrength()<=30) this.gc.setFill(Color.RED); //sets strength's color to red indicating low health points
		else this.gc.setFill(Color.ALICEBLUE);

		this.gc.fillText("Strength: "+ this.kirby.getStrength(), 600, 30,150);	//displays amount of Kirby's strength in real time

		this.gc.setFill(Color.GOLD);
		if (this.heartImmunityBenefit) this.gc.fillText("Heart Immunity is on", 550, 60,230);	//displays when a kirby activated the Heart Immunity
	}
}
