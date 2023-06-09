package project;

import javafx.scene.image.Image;

public class PowerUp extends Sprite {
	public static final int STAR_STRENGTH = 50; //50 additional strength from the star power-up
	public static final int DURATION = 5; //5 seconds duration of a power up until disappearing
	public final static int POWERUP_WIDTH = 90;
	public final static int POWERUP_HEIGHT = 40;
	public final static Image STAR_IMAGE = new Image("images/star.png",PowerUp.POWERUP_HEIGHT,PowerUp.POWERUP_HEIGHT,false,false);
	public final static Image HEART_IMAGE = new Image("images/heart_I.png",PowerUp.POWERUP_WIDTH,PowerUp.POWERUP_HEIGHT,false,false);
	private String type;
	private boolean isHeartImmunityActivated= false; //checks if a heart immunity power-up is collected

	public final static String HEART_TYPE = "heart";
	public final static String STAR_TYPE = "star";

	public PowerUp(int x, int y, String type) {
		super(x,y);
		this.type = type;
		this.setImage();
	}

	private void setImage() {
		switch(this.type) {
		case PowerUp.STAR_TYPE: this.img = PowerUp.STAR_IMAGE; break;
		case PowerUp.HEART_TYPE: this.img = PowerUp.HEART_IMAGE; break;
		}
		this.loadImage(this.img);
	}

	/* function that checks what type of power up is collected
	 * sets isHeartImmunityActivated if heart immunity power-up is collected, otherwise
	 * sets it to false
	 */
	protected boolean typeObtained(){
		switch(this.type) {
			case PowerUp.HEART_TYPE: this.isHeartImmunityActivated = true; break;
			case PowerUp.STAR_TYPE: this.isHeartImmunityActivated = false; break;
		}
		return this.isHeartImmunityActivated;
	}

	//getter that returns the power up's type
	protected String getType() {
		return this.type;
	}

}
