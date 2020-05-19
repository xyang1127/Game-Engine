package GameObjects;

import java.util.UUID;

import eventSystem.event;
import processing.core.PApplet;
import scriptSystem.ScriptManager;

public class Bubble extends Movable{
	public float radius;
	private float speed; // need to adjust this later
	public boolean flag; //used for algorithm in Server
	
	public void show() {
		parent.stroke(0);
		parent.fill(color);
		parent.ellipse(x, y, radius*2, radius*2);
	}

	@Override
	public void move() { // this move is not related to lower the bubble layer, which is maintained in the server game loop
//		if((x > (parent.width-radius)) || (x < radius))
//			xspeed *= -1;
//		
//		x += (xspeed*frameDuration);
//		y += (yspeed*frameDuration);
		
		// using script
		ScriptManager.loadScript("scripts/BubbleMove.js");
		ScriptManager.executeScript("move", this);
		
	}
	
	public Bubble(PApplet p, float radius, float x, float y, float speed, float angle, int color_case) { // color case: 1 or 2 or 3
		id = UUID.randomUUID();
		flag = false;
		parent = p;
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.speed = speed;
		xspeed = speed * parent.cos(angle);
		yspeed = speed * parent.sin(angle);
		
		switch (color_case) {
		case 0:
			color = parent.color(0,0,255);
			break;
		case 1:
			color = parent.color(0,255,0);
			break;
		case 2:
			color = parent.color(255,0,0);
			break;
		default:
			System.out.println("invalud paremeter: Bubble's comstructor" + color_case);
			break;
		}
	}
	
	@Override
	public void onEvent(event e) {
		; // need to implement
	}
	
	public boolean isCollide(Bubble b) {
		double distance = Math.pow(x-b.x, 2) + Math.pow(y-b.y, 2);
		double threshold = Math.pow(2*radius, 2);
		return threshold > distance;
	}
}
