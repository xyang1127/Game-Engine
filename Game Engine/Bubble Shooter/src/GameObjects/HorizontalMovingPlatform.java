package GameObjects;

import java.util.UUID;

import processing.core.PApplet;

public class HorizontalMovingPlatform extends Platform{
	
	public float max,min;
	
	public HorizontalMovingPlatform(PApplet p, float x, float y, float xspeed, float width,
			                        float height, int color1, int color2, int color3, float max, float min) {
		id = UUID.randomUUID();
		parent = p;
		this.x = x;
		this.y = y;
		this.xspeed = xspeed;
		this.yspeed = 0;
		this.width = width;
		this.height = height;
		color = parent.color(color1, color2, color3);
		this.max = max;
		this.min = min;
	}
	
	public void move() {
		if(x >= max) {
			x = max;
			xspeed = -Math.abs(xspeed);
		}
		if(x <= min) {
			x = min;
			xspeed = Math.abs(xspeed);
		}
		x += (xspeed*frameDuration);
	}
}
