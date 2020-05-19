package GameObjects;

import java.util.UUID;

import processing.core.PApplet;

public class StaticPlatform extends Platform{
	
	public StaticPlatform(PApplet p, float x, float y, float width, float height, int color1, int color2, int color3) {
		id = UUID.randomUUID();
		parent = p;
		this.x = x;
		this.y = y;
		this.xspeed = 0;
		this.yspeed = 0;
		this.width = width;
		this.height = height;
		color = parent.color(color1, color2, color3);
	}

	@Override
	public void move() {}	
}
