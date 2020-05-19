package GameObjects;

import processing.core.PApplet;

public class ColorIndicator extends Renderable{
	public float radius;
	public int color_case;
	
	@Override
	public void show() {
		parent.stroke(0);
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
			System.out.println("invalud paremeter: Colorindicator comstructor" + color_case);
			break;
		}
		parent.fill(color);
		parent.ellipse(parent.width/2, parent.height, radius*2, radius*2);
	}
	
	public ColorIndicator(PApplet p, float radius, int cs) {
		parent = p;
		this.radius = radius;
		color_case = cs;
	}
	
	public void ChangeColor(int c) {
		color_case = c;
	}
}
