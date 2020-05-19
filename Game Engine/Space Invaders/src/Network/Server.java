package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import GameObjects.*;
import eventSystem.EventManager;
import eventSystem.MoveEvent;
import eventSystem.TurretDeadEvent;
import eventSystem.TurretSpawnEvent;
import eventSystem.event;
import eventSystem.handleable;
import processing.core.PApplet;
import timeSystem.GlobalTimeline;
import timeSystem.LocalTimeline;

public class Server extends PApplet implements Runnable, handleable{
	
	private static ServerSocket serverSocket;
	public static HashMap<String, CopyOnWriteArrayList<GameObject>> gameWorld;
	public static CopyOnWriteArrayList<ServerTemp> serverTemps; // use for sending data
	public static Server server;
	private static ServerTemp serverTemp;
	private static ServerInputThread sit;
	private static ServerOutputThread sot;
	private static PApplet t;
	private static long thisFrameStartTime, lastFrameStartTime=0;
	private static long frameDuration = 16;
	private static LinkedList<Long> temp_frameTime;
	private static int maxListLength = 10;
	private static GlobalTimeline globalTimeline;
	private static LocalTimeline gameTimeline;
	private static EventManager eventManager;
	public static int sketch_width, sketch_height;
	
	public static int layer;
	private static float probability; // this number is (0,10) means probability is 0%-100%
	
	private static CopyOnWriteArrayList<Bullet> FriendlyBullets;
	private static CopyOnWriteArrayList<Bullet> UnfriendlyBullets;
	
	public static GunTurret gun;
	
	public void run() {
		try {
			while(true) {
				System.out.println("about to accept...");
				Socket socket = serverSocket.accept();
				System.out.println("New connection established");
				
				serverTemp = new ServerTemp();
				synchronized (server) {
					serverTemps.add(serverTemp);
					gun = new GunTurret(t);
					serverTemp.id = gun.id;
					gameWorld.get("gunturrets").add(gun);
				}
				synchronized (EventManager.single_instance) {
					eventManager.register("Move", gun);
					eventManager.register("TurretDead", gun);
					eventManager.register("TurretSpawn", gun);
				}
				// using socket and serveTemp to create two new threads
				sit = new ServerInputThread(socket, serverTemp);
				sot = new ServerOutputThread(socket, serverTemp);
				
				(new Thread(sot)).start();
				(new Thread(sit)).start();
				
				System.out.println("Server Input & Output threads are successfully created");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		PApplet.main("Network.Server");
	}
	
	public void settings() {
		sketch_width = 820;
		sketch_height = 820;
		size(sketch_width, sketch_height);
	}
	
	public void setup() {
		try {
			server = new Server();
			serverSocket = new ServerSocket(5000);
			gameWorld = new HashMap<>();
			serverTemps = new CopyOnWriteArrayList<>();
			globalTimeline = new GlobalTimeline(1);
			globalTimeline.start();
			gameTimeline = new LocalTimeline(globalTimeline, 1);
			gameTimeline.changeTicSize((float) 1); // this is used for debugging
			gameTimeline.start();
			eventManager = new EventManager(gameTimeline);
			temp_frameTime = new LinkedList<>();
			layer = 6;
			probability = (float) 0.01;
			FriendlyBullets = new CopyOnWriteArrayList<>();
			UnfriendlyBullets = new CopyOnWriteArrayList<>();
			
			// start creating the game world
			t = this;
			
			// create "Gun Turret objects"
			gameWorld.put("gunturrets", new CopyOnWriteArrayList<>());
			
			// create "Dead Line objects"
			gameWorld.put("deadlines", new CopyOnWriteArrayList<>());
			CopyOnWriteArrayList<GameObject> deadlines = gameWorld.get("deadlines");
			deadlines.add(new DeadLine(this, 0, 760, 820, 760)); // dead line must be @horizontal@ !
			
			// create "Bullet objects"
			gameWorld.put("bullets", new CopyOnWriteArrayList<>());
			
			// create "Space Ship objects"
			gameWorld.put("spaceships", new CopyOnWriteArrayList<>());
			CopyOnWriteArrayList<GameObject> spaceships = gameWorld.get("spaceships");
			int i;
			int j;
			for(i=0; i<layer; i++) {
				for(j=0; j<5; j++) {
					spaceships.add(new SpaceShip(t, 120*j, 30*i, 60 ,10));
				}
			}
						
			// GameWorld creation is done-------------------------------------------------------------------------------------------
			
			// register events on the server
			synchronized (EventManager.single_instance) {
				// no event will be handled on the server
			}
			
			(new Thread(server)).start();
			System.out.println("server set up");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw() {
		synchronized (server) {
			// set frame duration----------------------------------------------------------------------------------
			thisFrameStartTime = gameTimeline.getCurrentTics();
			//thisFrameStartTime = System.currentTimeMillis();
			
			if(lastFrameStartTime == 0) // special case when system is started
				lastFrameStartTime = thisFrameStartTime-frameDuration;
			
			frameDuration = thisFrameStartTime - lastFrameStartTime;
			
			lastFrameStartTime = thisFrameStartTime;

			temp_frameTime.addFirst(frameDuration);
			if(temp_frameTime.size() > maxListLength)
				temp_frameTime.removeLast();
			
			if(temp_frameTime.size() < maxListLength) {
				frameDuration = temp_frameTime.getFirst();
			}else {
				assert temp_frameTime.size() == maxListLength : "Wrong linkedlist size";
				int sum = 0;
				for(long i : temp_frameTime)
					sum += i;
				frameDuration = sum/maxListLength;
			}
			
			// above is for frame time speculation------------------------------------------------------------------			
			
			CopyOnWriteArrayList<GameObject> gunturrets = gameWorld.get("gunturrets");
			CopyOnWriteArrayList<GameObject> deadlines = gameWorld.get("deadlines");
			CopyOnWriteArrayList<GameObject> bullets = gameWorld.get("bullets");
			CopyOnWriteArrayList<GameObject> spaceships = gameWorld.get("spaceships");
			
			// distinguish bullets between friendly and unfriendly
			for(GameObject b : bullets) {
				Bullet bullet = (Bullet)b;
				if(bullet.friendly) {
					FriendlyBullets.add(bullet);
				}else {
					UnfriendlyBullets.add(bullet);
				}
			}
			
			// react to the keyboard input
			for(ServerTemp temp : serverTemps) {	
				// find the ID game object
				GunTurret g_t = null;
				for(GameObject g : gunturrets) {
					if(g.id == temp.id) {
						g_t = (GunTurret)g;
						break;
					}
				}
				if(temp.operation != 0) {
					switch (temp.operation) {
					case 1: 
					case 2:
					case 4:
						eventManager.enqueueEvent(new MoveEvent(0, temp.operation, temp.id));
						break; // all the move event
					case 3:
						if(!g_t.dead)
							bullets.add(new Bullet(t, g_t.x+40-3, g_t.y-20, true, 6, 20));
						break;
					}
				}
			}
			
			// check the condition: whether the spaceship touch the side of screen
			if(touch())
				touchBoundary();
			
			// check for friendly bullet collide with space ship
			for(Bullet b : FriendlyBullets) {
				for(GameObject s : spaceships) {
					SpaceShip spaceShip = (SpaceShip)s;
					if(spaceShip.isCollideWithBullet(b)) {
						bullets.remove(b);
						spaceships.remove(spaceShip);
					}
				}
			}
			
			// check for unfriendly bullet collide with gunturret
			for(Bullet b : UnfriendlyBullets) {
				for(GameObject g : gunturrets) {
					GunTurret gunTurret = (GunTurret)g;
					if(gunTurret.isCollideWithBullet(b)) {
						bullets.remove(b);
						eventManager.enqueueEvent(new TurretDeadEvent(0, gunTurret.id));
						eventManager.enqueueEvent(new TurretSpawnEvent(1000, gunTurret.id));
					}
				}
			}
			
			// space ship shoot bullet randomly
			for(GameObject s : spaceships) {
				SpaceShip ss = (SpaceShip)s;
				float random = t.random(10);
				if(random < probability)
					bullets.add(new Bullet(t, ss.x+30-3, ss.y+10, false, 6, 20));
			}
			
			// restrat detection
			// case 1: space ships is cleared
			// case 2: on of the space ship touch the bottom dead line
			if(spaceships.size() == 0)
				restart();
			
			for(GameObject s : spaceships) {
				SpaceShip spaceShip = (SpaceShip)s;
				if(spaceShip.y > 760-spaceShip.height) {
					restart();
					break;
				}
			}
		
			background(255);
			// show gun turret
			for(GameObject g : gunturrets) {
				GunTurret gt = (GunTurret)g;
				gt.setFrameDuration(frameDuration);
				gt.move();
				gt.show();
			}
			
			// show the lines
			for(GameObject d : deadlines) {
				DeadLine deadLine = (DeadLine)d;
				deadLine.show();
			}
			
			// show bullets
			//System.out.println(bullets.size());
			for(GameObject b : bullets) {
				Bullet bullet = (Bullet)b;
				bullet.setFrameDuration(frameDuration);
				if(bullet.y>t.height || bullet.y<-20)
					bullets.remove(bullet);
				bullet.move();
				bullet.show();
			}
			
			// show space ships
			//System.out.println(spaceships.size());
			for(GameObject s : spaceships) {
				SpaceShip spaceShip = (SpaceShip)s;
				spaceShip.setFrameDuration(frameDuration);
				spaceShip.move();
				spaceShip.show();
			}
			
			//----------------------------------------------------------------------------------------------
			
			// all updates is done, now it's time to send data back to each client
			for(ServerTemp sTemp : serverTemps) {
				sTemp.ServerUpdateIsDone = true;
				sTemp.operation = 0;
			}
			
			eventManager.dispatch();
			
			FriendlyBullets.clear();
			UnfriendlyBullets.clear();
		}
	}

	@Override
	public void onEvent(event e) {
		switch (e.eventType) {
		case "???":
			break;
		}	
	}
	
	private boolean touch() {
		
		CopyOnWriteArrayList<GameObject> spaceships = gameWorld.get("spaceships");
		for(GameObject s : spaceships) {
			SpaceShip ship = (SpaceShip) s;
			if(ship.x>t.width-60 || ship.x<0)
				return true;
		}
		
		return false;
	}
	
	private void touchBoundary() {
		CopyOnWriteArrayList<GameObject> spaceships = gameWorld.get("spaceships");
		for(GameObject s : spaceships) {
			SpaceShip ship = (SpaceShip) s;
			ship.y += 40;
			ship.xspeed *= -1;
		}
	}
	
	public void restart() {
		CopyOnWriteArrayList<GameObject> spaceships = gameWorld.get("spaceships");
		CopyOnWriteArrayList<GameObject> bullets = gameWorld.get("bullets");
		bullets.clear();
		spaceships.clear();
		int i;
		int j;
		for(i=0; i<layer; i++) {
			for(j=0; j<5; j++) {
				spaceships.add(new SpaceShip(t, 120*j, 30*i, 60 ,10));
			}
		}
	}
}
