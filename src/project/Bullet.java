package project;

import javafx.scene.image.Image;

public class Bullet extends Sprite {
	private final int BULLET_SPEED = 20;
	public final static Image BULLET_IMAGE = new Image("images/bullet.png",Bullet.BULLET_WIDTH,Bullet.BULLET_WIDTH,false,false);
	public final static int BULLET_WIDTH = 50;
	private int damageInduced;

	public Bullet(int x, int y, int z){
		super(x,y);
		this.loadImage(Bullet.BULLET_IMAGE);
		this.damageInduced = z;
	}


	//method that will move/change the x position of the bullet
	public void move(){
		/*
		 * TODO: Change the x position of the bullet depending on the bullet speed.
		 * 					If the x position has reached the right boundary of the screen,
		 * 						set the bullet's visibility to false.
		 */
		if (this.x <=GameStage.WINDOW_WIDTH && this.isVisible()) this.x +=this.BULLET_SPEED;
		else this.setVisible(false);
	}

	//getter method that returns bullet damage
	public int getBulletDamage(){
		return this.damageInduced;
	}
}