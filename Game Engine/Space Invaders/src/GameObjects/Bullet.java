package GameObjects;

import java.util.UUID;

import processing.core.PApplet;

public class Bullet extends Movable{

	// friendly is used to described a bullet
	// if a bullet is friendly, then it is generated by the players' gun turret,
	// and its yspeed < 0; Also, it can only collided with ships
	// if a bullet is unfriendly, then it is generated by ships,
	//	its yspeed > 0, and it can only collided with gun turrets
	public boolean friendly;
	public float width, height;
	
	public Bullet(PApplet p, float x, float y, boolean friendly, float w, float h) {
		id = UUID.randomUUID();
		parent = p;
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		this.friendly = friendly;
		xspeed = 0;
		if(friendly) {
			yspeed = (float) -1;
			color = 0;
		}else {
			yspeed = (float) 1;
			color = parent.color(255,0,0);
		}
	}
	
	@Override
	public void move() {
		y += (yspeed*frameDuration);
	}

	@Override
	public void show() {
		parent.noStroke();
		parent.fill(color);
		parent.rect(x, y, width, height);
	}

}
