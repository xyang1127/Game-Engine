package Network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import GameObjects.Bubble;
import GameObjects.BubbleShooter;
import GameObjects.ColorIndicator;
import GameObjects.DeadLine;
import GameObjects.DeadZone;
import GameObjects.GameCharacter;
import GameObjects.GameObject;
import GameObjects.Platform;
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
			CopyOnWriteArrayList<GameObject> bubbles = gameWorld.get("bubbles");
			for(GameObject b : bubbles) {
				((Bubble)b).setPApplet(pApplet);
				((Bubble)b).show();
			}
			// display bubble shooter
			CopyOnWriteArrayList<GameObject> bubbleshooters = gameWorld.get("bubbleshooters");
			for(GameObject bs : bubbleshooters) {
				((BubbleShooter)bs).setPApplet(pApplet);
				((BubbleShooter)bs).show();
			}
			
			// display the dead line
			CopyOnWriteArrayList<GameObject> deadlines = gameWorld.get("deadlines");
			for(GameObject d : deadlines) {
				DeadLine deadLine = (DeadLine)d;
				deadLine.setPApplet(pApplet);
				deadLine.show();
			}
			
			// display color indicator
			GameObject ci = gameWorld.get("colorindicators").get(0);
			((ColorIndicator)ci).setPApplet(pApplet);
			((ColorIndicator)ci).show();
			
//			this.fill(0);
//			this.rect(0, 780, 820, 1);
		}
		
	}

	
	public void mousePressed() {
		clientTemp.operation = 1;
		clientTemp.sendData = true;
	}
	
	public void mouseMoved() {
		clientTemp.mouse_x = pApplet.mouseX;
		clientTemp.mouse_y = pApplet.mouseY;
		if(mousePressed == true)
			clientTemp.operation = 1;
		clientTemp.sendData = true;
	}
}
