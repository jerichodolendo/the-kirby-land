package project;

import javafx.scene.image.Image;

public class BulletBoss extends Bullet{
	private final int BULLET_BOSS_SPEED = 10;
	public final static Image BULLET_BOSS_IMAGE= new Image("images/boss_bullet.png",Bullet.BULLET_WIDTH,Bullet.BULLET_WIDTH,false,false);

	public BulletBoss(int x, int y, int z) {
		super(x,y,z);
		this.loadImage(BulletBoss.BULLET_BOSS_IMAGE);
	}

	public void moveS(){
		if (this.x >=0 && this.isVisible()) this.x -=this.BULLET_BOSS_SPEED;
		else this.setVisible(false);
	}

}
