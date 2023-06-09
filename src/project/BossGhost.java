package project;

import java.util.ArrayList;
import javafx.scene.image.Image;

public class BossGhost extends Ghost{

	private int health;
	public final static int BOSS_ENEMY_WIDTH = 100;
	public final static Image BOSSENEMY_IMAGE = new Image("images/boss.png",BossGhost.BOSS_ENEMY_WIDTH,BossGhost.BOSS_ENEMY_WIDTH,false,false);

	private ArrayList<BulletBoss> bullets;	//same concept as bullet

	//constructor
	BossGhost (int x, int y){
		super(x,y);
		this.health = 3000;
		this.loadImage(BossGhost.BOSSENEMY_IMAGE);
		this.bullets = new ArrayList<BulletBoss>();

	}

	//setter when a bullet hits the boss ghost, health's decreased by bullet's damage
	public void setHealth(int damage){
		this.health -= damage;
		System.out.println("BOSS ENEMY DAMAGED! Current health: "+ this.health);

		if (this.health<=0) this.setIsDead(); //sets boss as dead if health is equal or less than 0
	}

	public ArrayList<BulletBoss> getBullets(){
		return this.bullets;
	}

	public void bossShoots(){
		//compute for the x and y initial position of the bullet
		int x = (int) (this.x + this.width-80);
		int y = (int) (this.y + this.height/2);

		BulletBoss b= new BulletBoss(x,y, 10);

		this.bullets.add(b);
    }
}
