package GameObjects;

import java.util.UUID;

import processing.core.PApplet;

public class BubbleShooter extends Movable{
	
	public float angle;
	
	private float mouse_x, mouse_y;

	@Override
	public void move() {
		
		float x = mouse_x-parent.width/2;
		float y = parent.height - mouse_y;
		if(y == 0) {
			y = (float) 0.0000001; // trick: not to get divide 0 erro
		}
		angle = parent.atan(x/y);
		
	}

	@Override
	public void show() {
		parent.pushMatrix();
		
		parent.translate(parent.width/2, parent.height);
		parent.rotate(angle);
		parent.noStroke();
		parent.fill(color);
		parent.rect((float) -2.5, -60, 5, 60);
		parent.triangle(-5, -60 , 5, -60, 0, -70);
		
		parent.popMatrix();
	}
	
	public BubbleShooter(PApplet p) {
		id = UUID.randomUUID();
		parent = p;
		
		color = parent.color((int) parent.random(255), (int) parent.random(255), (int) parent.random(255));
		
		mouse_x = parent.width/2;
		mouse_y = 0;
	}
	
	public void setPosition(float x, float y) {
		mouse_x = x;
		mouse_y = y;
	}
	
}
