package GameObjects;

import java.util.UUID;

import processing.core.PApplet;

public class VerticalMovingPlatform extends Platform{
	
	float max, min;
	
	public VerticalMovingPlatform(PApplet p, float x, float y, float yspeed, float width,
			                      float height, int color1, int color2, int color3, float max, float min) {
		id = UUID.randomUUID();
		parent = p;
		this.x = x;
		this.y = y;
		this.xspeed = 0;
		this.yspeed = yspeed;
		this.width = width;
		this.height = height;
		color = parent.color(color1, color2, color3);
		this.max = max;
		this.min = min;
	}
	
	public void move() {
		if(y >= max) {
			y = max;
			yspeed = -Math.abs(yspeed);
		}
		if(y <= min) {
			y = min;
			yspeed = Math.abs(yspeed);
		}
		y += (yspeed*frameDuration);
	}
}
