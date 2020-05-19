package GameObjects;

import timeSystem.Timeline;

public abstract class Movable extends Renderable{
	public float x, y;
	public float xspeed, yspeed;
	//public Timeline timeline;
	public long frameDuration;
	
	public abstract void move();
	
//	public void addTimeline(Timeline timeline) {
//		this.timeline = timeline;
//	}
	
	public void setFrameDuration(long f) {
		frameDuration = f;
	}
}
