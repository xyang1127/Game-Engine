package GameObjects;

import java.awt.geom.Rectangle2D;
import java.util.UUID;

import processing.core.PApplet;

public class SpaceShip extends Movable{
	
	public float width, height;
	
	public SpaceShip(PApplet p, float x, float y, float w, float h) {
		id = UUID.randomUUID();
		parent = p;
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		xspeed = (float) 0.05;
	}

	@Override
	public void move() {
		x += (xspeed*frameDuration);
	}

	@Override
	public void show() {
		parent.noStroke();
		parent.fill(parent.color(255,0,0));
		parent.rect(x, y, width, height);
	}
	
	public boolean isCollideWithBullet(Bullet bullet){
		// sanity check
		if(!bullet.friendly) {
			System.out.println("wrong bullet: Space ship");
			return false;
		}
		
		Rectangle2D.Float r_bullet = new Rectangle2D.Float(bullet.x, bullet.y, bullet.width, bullet.height);
		Rectangle2D.Float r_spaceship = new Rectangle2D.Float(x, y, width, height);
		return r_bullet.intersects(r_spaceship);
	}
	
}
