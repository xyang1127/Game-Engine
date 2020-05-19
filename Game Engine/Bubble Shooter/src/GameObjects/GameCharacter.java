package GameObjects;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.UUID;

import eventSystem.CharacterCollisionEvent;
import eventSystem.event;
import processing.core.PApplet;
import scriptSystem.ScriptManager;

public class GameCharacter extends Movable{
	
	public float radius; // character is a circle
	public float gravity;
	public float accelerate;
	public float xchange = 0;
	public float x_temp;
	
	public GameCharacter(PApplet p, float radius, float xspeed, float gravity) {
		id = UUID.randomUUID();
		parent = p;
		this.radius = radius;
		x = parent.random(this.radius, parent.width-this.radius);
		//x = parent.width/2;
		y = parent.height-this.radius;
		this.xspeed = xspeed; // xspeed is always > 0
		x_temp = xspeed;
		yspeed = 0;
		color = parent.color(125,125,125);
		this.gravity = gravity;
		accelerate = 0;
	}
	
	public void show() {
		parent.noStroke();
		parent.fill(color);
		parent.ellipse(x, y, radius*2, radius*2);
	}
	
	private void move(int i) {
		switch (i) {
		case 1: //horizontal left
			xchange = -xspeed;
			break;
		case 2: //horizontal right
			xchange = xspeed;
			break;
		case 3: //jump
			if(accelerate==0) {
				yspeed = (float) -0.6;
				accelerate = gravity;
			}
			//System.out.println("I have jumped!***********************************************************");
			break;
		case 4:
			StopHorizontalMove();
		}
	}
	
	public void StopHorizontalMove() {
		xchange = 0;
	}
	
	public void move() {
		x += (xchange*frameDuration);
		x = parent.constrain(x, radius, parent.width-radius);
		yspeed += (accelerate*frameDuration); // very important!!!!
		y += (yspeed*frameDuration);
		y = parent.constrain(y, radius, parent.height-radius);
		if(y==parent.height-radius && yspeed>0) {
			yspeed = 0;
			accelerate = 0;
		}
		if(y==radius)
			yspeed *= -1;
			//yspeed = 0;
	}
	
	private boolean isCollideWithPlatform(Platform platform) {
		Ellipse2D.Float circle = new Ellipse2D.Float(x-radius, y-radius, radius*2, radius*2);
		Rectangle2D.Float rectangle = new Rectangle2D.Float(platform.x, platform.y, platform.width, platform.height);
		return circle.intersects(rectangle);
	}
	
	public int PlatformCollisionDetector(Platform platform) {
		if(isCollideWithPlatform(platform)) {
			if(yspeed<0)
				return 2;
			return 1;
		}
		return 0; // means no collision happens
	}
	
	@Override
	public void onEvent(event e) {
		switch (e.eventType) {
		case "CharacterCollision":
			UUID id = (UUID) e.eventArguments.get("id");
			if(this.id == id) {
				int surface = (int) e.eventArguments.get("surface");
				Platform platform = (Platform) e.eventArguments.get("platform");
				ScriptManager.loadScript("scripts/CharacterCollisionEvent.js");
//				ScriptManager.bindArgument("gamecharacter", this);
//				ScriptManager.bindArgument("platform", platform);
				ScriptManager.executeScript("onCollision", surface, this, platform);
			}
			break;
		case "CharacterDeath":
			UUID id1 = (UUID) e.eventArguments.get("id");
			if(this.id == id1) {
				changeColor();
				xchange = 0;
				xspeed = 0;
				yspeed = 0;
				accelerate = (float) 0.000001; // trick
			}
			break;
		case "CharacterSpawn":
			UUID id2 = (UUID) e.eventArguments.get("id");
			SpawnPoint spawnPoint = (SpawnPoint) e.eventArguments.get("spawnpoint");
			if(this.id == id2) {
				changeBackColor();
				x = spawnPoint.x;
				y = spawnPoint.y;
				xspeed = x_temp;
				yspeed = 0;
				accelerate = 0;
			}
			break;
		case "Move":
			UUID id3 = (UUID) e.eventArguments.get("id");
			int this_operation = (int) e.eventArguments.get("operation");
			if(this.id == id3) {
				move(this_operation);
			}
			break;
		}
	}
	
	public boolean isCollideWithDeadZone(DeadZone deadZone) {
		double distanceSqu = Math.pow(x-deadZone.x, 2) + Math.pow(y-deadZone.y, 2);
		double threshold = Math.pow(radius+deadZone.radius, 2);
		return threshold>distanceSqu;
	}
	

	private void changeColor() {
		color = parent.color(240,40,130);
	}
	
	private void changeBackColor() {
		color = parent.color(125,125,125);
	}
}
