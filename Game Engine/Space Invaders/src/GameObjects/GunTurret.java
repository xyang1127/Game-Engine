package GameObjects;

import java.awt.geom.Rectangle2D;
import java.util.UUID;

import eventSystem.event;
import processing.core.PApplet;

public class GunTurret extends Movable{

	public float xchange;
	
	private int original_color;
	
	public boolean dead;
	
	@Override
	public void move() {
		x += (xchange*frameDuration);
		x = parent.constrain(x, 0, parent.width-80);
	}

	@Override
	public void show() {
		parent.stroke(0);
		parent.fill(color);
		parent.rect(x, parent.height-40, 80, 20);
		parent.rect(x+37, parent.height-50, 6, 10);
	}
	
	public GunTurret(PApplet p) {
		id = UUID.randomUUID();
		parent = p;
		xchange = 0;
		color = parent.color((int) parent.random(255), (int) parent.random(255), (int) parent.random(255));
		original_color = color;
		x = p.width/2-40;
		xspeed = (float)0.5;
		y = parent.height-40;
		dead = false;
	}
	
	private void move(int i) {
		switch (i) {
		case 1:
			xchange = -xspeed;
			break;
		case 2:
			xchange = xspeed;
			break;
		case 4:
			xchange = 0; // stop moving!
			break;
		default:
			System.out.println("operation undefined: GunTurret Object");
			break;
		}
	}
	
	public void onEvent(event e) {
		switch (e.eventType) {
		case "Move":
			if(dead)
				break;
			UUID id = (UUID)e.eventArguments.get("id");
			int this_operation = (int) e.eventArguments.get("operation");
			if(this.id == id)
				move(this_operation);
			break;
		case "TurretDead":
			UUID id1 = (UUID)e.eventArguments.get("id");
			if(this.id == id1) {
				xchange = 0;
				dead = true;
				color = 255;
			}
			break;
		case "TurretSpawn":
			UUID id2 = (UUID)e.eventArguments.get("id");
			if(this.id == id2) {
				x = parent.width/2-40;
				color = original_color;
				dead = false;	
			}
			break;
		}
	}
	
	public boolean isCollideWithBullet(Bullet bullet) {
		if(dead)
			return false;
		
		if(bullet.friendly) {
			System.out.println("wrong bullet: Space ship");
			return false;
		}
		
		Rectangle2D.Float r_bullet = new Rectangle2D.Float(bullet.x, bullet.y, bullet.width, bullet.height);
		Rectangle2D.Float r_turret_base = new Rectangle2D.Float(x, parent.height-40, 80, 20);
		Rectangle2D.Float r_turret_gun = new Rectangle2D.Float(x+37, parent.height-50, 6, 10);
		if(r_bullet.intersects(r_turret_base) || r_bullet.intersects(r_turret_gun))
			return true;
		return false;
	}
}
