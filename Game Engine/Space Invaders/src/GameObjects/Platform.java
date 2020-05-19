package GameObjects;

import java.util.UUID;

import processing.core.PApplet;

public abstract class Platform extends Movable{
	public float width, height; // platform are rectangles
	
	public void show() {
		parent.noStroke();
		parent.fill(color);
		parent.rect(x,y,width, height);
	}
}
