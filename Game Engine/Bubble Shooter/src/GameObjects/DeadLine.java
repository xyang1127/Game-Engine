package GameObjects;

import java.util.UUID;

import processing.core.PApplet;

public class DeadLine extends Renderable{

	public float x_1, y_1, x_2, y_2;
	
	public void show() {
		parent.fill(0);
		float width = Math.abs(x_2-x_1);
		parent.rect(x_1, y_1, width, 1);
	}

	public DeadLine(PApplet p, float x_1, float y_1, float x_2, float y_2) {
		id = UUID.randomUUID();
		parent = p;
		this.x_1 = x_1;
		this.x_2 = x_2;
		this.y_1 = y_1;
		this.y_2 = y_2;
	}
}
