package GameObjects;

import java.util.UUID;

import processing.core.PApplet;

public class DeadZone extends Movable{
	
	public float radius; // assume GameObject is a circle
	
	public DeadZone(PApplet p, float radius, float speed) {
		id = UUID.randomUUID();
		parent = p;
		float angle = parent.random(0, 2*parent.PI);
		x = parent.width/2;
		y = parent.height/2;
		xspeed = speed*parent.cos(angle);
		yspeed = speed*parent.sin(angle);
		this.radius = radius;
		color = parent.color(0);
	}
	
	public void show() {
		parent.noStroke();
		parent.fill(color);
		parent.ellipse(x, y, radius*2, radius*2);
	}
	
	public void move() {
		if((x > (parent.width-radius)) || (x < radius))
			xspeed *= -1;
		
		if((y > (parent.height-radius)) || (y < radius))
			yspeed *= -1;
		
		x += (xspeed*frameDuration);
		y += (yspeed*frameDuration);
		
//		x += xspeed;
//		y += yspeed;
	}
}
