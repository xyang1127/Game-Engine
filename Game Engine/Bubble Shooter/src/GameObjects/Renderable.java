package GameObjects;

import processing.core.PApplet;

public abstract class Renderable extends GameObject{
	
	public transient PApplet parent;
	
	public int color;
	
	public abstract void show();
	
	public void setPApplet(PApplet p) {
		parent = p;
	}
}
