package Network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import GameObjects.Bubble;
import GameObjects.BubbleShooter;
import GameObjects.Bullet;
import GameObjects.ColorIndicator;
import GameObjects.DeadLine;
import GameObjects.DeadZone;
import GameObjects.GameCharacter;
import GameObjects.GameObject;
import GameObjects.GunTurret;
import GameObjects.Platform;
import GameObjects.SpaceShip;
import processing.core.PApplet;

public class Client extends PApplet{
	
	private static Socket socket;
	public static HashMap<String, CopyOnWriteArrayList<GameObject>> gameWorld;
	public static ClientTemp clientTemp;
	private static ClientOutputThread cot;
	private static ClientInputThread cit;
	public static PApplet pApplet;
	
	public static void main(String[] args) {
		PApplet.main("Network.Client");
	}
	
	public void settings() {
		size(820, 820);
	}
	
	public void setup() {
		try {
			socket = new Socket("127.0.0.1", 5000);
			clientTemp = new ClientTemp();
			pApplet = this;
			cot = new ClientOutputThread(socket, clientTemp);
			cit = new ClientInputThread(socket, clientTemp);
			(new Thread(cot)).start();
			(new Thread(cit)).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw() {
		
		if(gameWorld != null) {
			background(255);
			
			// display bubbles
			CopyOnWriteArrayList<GameObject> guns = gameWorld.get("gunturrets");
			for(GameObject g : guns) {
				((GunTurret)g).setPApplet(this);
				((GunTurret)g).show();
			}
			
			// display the dead line
			CopyOnWriteArrayList<GameObject> deadlines = gameWorld.get("deadlines");
			for(GameObject d : deadlines) {
				DeadLine deadLine = (DeadLine)d;
				deadLine.setPApplet(this);
				deadLine.show();
			}
			
			// display bullets
			CopyOnWriteArrayList<GameObject> bullets = gameWorld.get("bullets");
			for(GameObject b : bullets) {
				((Bullet)b).setPApplet(this);
				((Bullet)b).show();
			}
			
			//display spaceships
			CopyOnWriteArrayList<GameObject> spaceships = gameWorld.get("spaceships");
			for(GameObject s : spaceships) {
				((SpaceShip) s).setPApplet(this);
				((SpaceShip) s).show();
			}
			
		}
		
	}
	
	public void keyPressed() {
		synchronized (clientTemp) {
			if(key == CODED) {
				if(keyCode == LEFT) {
					clientTemp.operation = 1;
				}else if(keyCode == RIGHT)
					clientTemp.operation = 2;
			}else if(key == ' ') {
				clientTemp.operation = 3;
			}
			clientTemp.sendData = true;
		}
	}
	
	public void keyReleased() {
		synchronized (clientTemp) {
			if(key == CODED) {
				if(keyCode == LEFT || keyCode == RIGHT)
					clientTemp.operation = 4;
			}
			clientTemp.sendData = true;
		}
	}
}
