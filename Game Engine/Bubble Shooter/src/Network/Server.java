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
import eventSystem.BubbleDeadEvent;
import eventSystem.BubbleGoneEvent;
import eventSystem.EventManager;
import eventSystem.ShootBubbleEvent;
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
	private static BubbleShooter shooter;
	private static PApplet t;
//	private static long startTime, endTime;
	private static long thisFrameStartTime, lastFrameStartTime=0;
	private static long frameDuration = 16;
	private static LinkedList<Long> temp_frameTime;
	private static int maxListLength = 10;
//	private int loopiteration;
	private static GlobalTimeline globalTimeline;
	private static LocalTimeline gameTimeline;
	private static EventManager eventManager;
	public static int sketch_width, sketch_height;
	public static int layer;
	private static int layer_i;
	private static int layer_mark;
	private static int click_count;
	
	private static HashSet<Bubble> falling_bubbles;
	private static HashSet<Bubble> d_b;

	private static boolean falling;
	
	private static HashMap<UUID, UUID> mbubble_ids; // maps a shooter.id to a moving bubble.id
	// distinguish the moving bubbles and static bubbles
	private static ArrayList<Bubble> statBubbles, dynaBubbles, stopBubbles;
	
	public void run() {
		try {
			while(true) {
				System.out.println("about to accept...");
				Socket socket = serverSocket.accept();
				System.out.println("New connection established");
				
				serverTemp = new ServerTemp();
				synchronized (server) {
					serverTemps.add(serverTemp);
					shooter = new BubbleShooter(this);
					serverTemp.id = shooter.id;
					gameWorld.get("bubbleshooters").add(shooter);
					mbubble_ids.put(shooter.id, null);
				}
				synchronized (EventManager.single_instance) {
//					eventManager.register("CharacterCollision", shooter);
//					eventManager.register("CharacterDeath", shooter);
//					eventManager.register("CharacterSpawn", shooter);
//					eventManager.register("Move", shooter);
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
			mbubble_ids = new HashMap<>();
			statBubbles = new ArrayList<>();
			dynaBubbles = new ArrayList<>();
			stopBubbles = new ArrayList<>();
			falling = false;
			layer_mark = 0;
			click_count = 0;
			
			layer = 8;
			
			// start creating the game world
			t = this;
			
			//-----------------------------------------------------------------------------------------------------------------			
			// create "Bubble objects"
			gameWorld.put("bubbles", new CopyOnWriteArrayList<>());
			CopyOnWriteArrayList<GameObject> bubbles = gameWorld.get("bubbles");
			int init_bubble_i;
			int bubble_color_case;
			for(layer_i=0; layer_i<layer; layer_i++) {
				for(init_bubble_i=0; init_bubble_i<20; init_bubble_i++) {
					bubble_color_case= (int)t.random(3);
					bubbles.add(new Bubble(this, 20, 20+init_bubble_i*40+20*(layer_i%2), 20+layer_i*40, 0, 0, bubble_color_case));
				}
			}
			
			// create "Bubble Shooter objects"
			gameWorld.put("bubbleshooters", new CopyOnWriteArrayList<>());
			CopyOnWriteArrayList<GameObject> bubbleshooters = gameWorld.get("bubbleshooters");
			
			// create "Dead Line objects"
			gameWorld.put("deadlines", new CopyOnWriteArrayList<>());
			CopyOnWriteArrayList<GameObject> deadlines = gameWorld.get("deadlines");
			deadlines.add(new DeadLine(this, 0, 760, 820, 760)); // dead line must be @horizontal@ !
			
			// create "ColorIndicator"
			gameWorld.put("colorindicators", new CopyOnWriteArrayList<>());
			gameWorld.get("colorindicators").add(new ColorIndicator(t, 20, 1));
			
			// GameWorld creation is done-------------------------------------------------------------------------------------------
			
			// register
			synchronized (EventManager.single_instance) {
				eventManager.register("ShootBubble", server);
				eventManager.register("BubbleDead", server);
				eventManager.register("BubbleGone", server);
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
			
			CopyOnWriteArrayList<GameObject> bubbles = gameWorld.get("bubbles");
			CopyOnWriteArrayList<GameObject> bubbleshooters = gameWorld.get("bubbleshooters");
			CopyOnWriteArrayList<GameObject> deadlines = gameWorld.get("deadlines");
			
			// distinguish static bubbles and dynamic bubbles
			for(GameObject b : bubbles) {
				boolean is_dyna = false;
				Bubble bu = (Bubble)b;
				for(GameObject bs : bubbleshooters) {
					if(mbubble_ids.get(bs.id) == bu.id) {
						is_dyna = true;
						break;
					}
				}
				if(is_dyna) {
					dynaBubbles.add(bu);
				}else
					statBubbles.add(bu);
			}
			
			//System.out.println("# static: "+statBubbles.size() + "# dynamic : " + dynaBubbles.size());
			
			// collision detection and handling
			// cite: https://stackoverflow.com/questions/18448671/how-to-avoid-concurrentmodificationexception-while-removing-elements-from-arr
			Iterator<Bubble> iter_1 = dynaBubbles.iterator();
			while(iter_1.hasNext()) {
				Bubble dBubble = iter_1.next();
				if(dBubble.y <= dBubble.radius) { // bubble reaches the top
					dBubble.xspeed = 0;
					dBubble.yspeed = 0;
					dBubble.y = dBubble.radius;
					dBubble.x = (((int)(dBubble.x-20))/((int)(2*dBubble.radius)))*(2*dBubble.radius)+20+20*(layer_mark%2);
					// adjust some data structures according to this
					iter_1.remove();
					stopBubbles.add(dBubble);
					statBubbles.add(dBubble);
					
					for(GameObject bs : bubbleshooters) {
						if(mbubble_ids.get(bs.id) == dBubble.id) {
							mbubble_ids.remove(bs.id);
							mbubble_ids.put(bs.id, null);
							break;
						}
					}
					break;
				}
				Iterator<Bubble> iter_2 = statBubbles.iterator();
				while(iter_2.hasNext()) {
					Bubble sBubble = iter_2.next();
					if(dBubble.isCollide(sBubble)) {
						
						dBubble.xspeed = 0;
						dBubble.yspeed = 0;
						
						// collision handling
						// move the bubble to the right place
						if(dBubble.y-sBubble.y < sBubble.radius) {
							dBubble.y = sBubble.y;
							if(dBubble.x > sBubble.x) {
								dBubble.x = sBubble.x + 2*sBubble.radius;
								System.out.println("placed: right");
							}else {
								dBubble.x = sBubble.x - 2*sBubble.radius;
								System.out.println("placed: left");
							}
						}else {
							dBubble.y = sBubble.y + 2*sBubble.radius;
							if(dBubble.x > sBubble.x) {
								dBubble.x = sBubble.x + sBubble.radius;
								System.out.println("placed: bottom right");
							}else {
								dBubble.x = sBubble.x - sBubble.radius;
								System.out.println("placed: bottom left");
							}
						}
						
						// adjust some data structures according to this
						iter_1.remove();
						stopBubbles.add(dBubble);
						statBubbles.add(dBubble);
						
						for(GameObject bs : bubbleshooters) {
							if(mbubble_ids.get(bs.id) == dBubble.id) {
								mbubble_ids.remove(bs.id);
								mbubble_ids.put(bs.id, null);
								break;
							}
						}
						
						break;
					}
				}
			}
			
			// detect falling bubble:
			if(!falling) {
				falling_bubbles = falling_bubble();
				if(falling_bubbles.size() > 0) {
					eventManager.enqueueEvent(new BubbleDeadEvent(0));
					eventManager.enqueueEvent(new BubbleGoneEvent(100));
					falling = true;
				}
			}
			
			d_b = dangling_bubble();
			for(Bubble b : d_b) {
				gameWorld.get("bubbles").remove(b);
				System.out.println("destroy dangling! ");
			}
			
			// death detection
			for(GameObject b : statBubbles) {
				Bubble x = (Bubble)b;
				if(x.y >= 780) {
					createWorld();
					break;
				}
			}
			
			// you win, and another game starts
			if(bubbles.size()==0)
				createWorld();
			
			if(click_count >= 6) {
				// not update dyna_bubbles, static_bubbles, ...
				for(Bubble bu : statBubbles) {
					bu.y += bu.radius*2;
				}
				layer_mark++;
				for(int j=0; j<20;j++) {
					Bubble newb = new Bubble(this, 20, 20+j*40+20*(layer_mark%2), 20, 0, 0, (int)t.random(3));
					bubbles.add(newb);
					//statBubbles.add(newb);
				}
				click_count = 0;
			}
			
			background(255);
			
			// react to the keyboard input
			for(ServerTemp temp : serverTemps) {	
				// first update the angle
				UUID t_id = temp.id;
				BubbleShooter t_bs = null;
				float m_x, m_y;
				m_x = temp.mouse_x;
				m_y = temp.mouse_y;
				
				for(GameObject bs : bubbleshooters) {
					if(bs.id == t_id) {
						t_bs = (BubbleShooter)bs;
						break;
					}
				}
				
				t_bs.setPosition(m_x, m_y);
				
				// then deal with the mouse click
				if(temp.operation != 0) {
					switch (temp.operation) {
					case 1:
						if(mbubble_ids.get(t_id) == null) { // there is only one bubble allow to be moved at a time in one client
							int temp_color = ((ColorIndicator)gameWorld.get("colorindicators").get(0)).color_case;
							eventManager.enqueueEvent(new ShootBubbleEvent(0, temp_color, t_bs.angle, temp.id));
							((ColorIndicator)gameWorld.get("colorindicators").get(0)).ChangeColor((int)t.random(3));
							click_count++;
						}
						break;
					}
				}
			}
		
			// update bubbles
			for(GameObject b : bubbles) {
				Bubble bubble = (Bubble)b;
				bubble.setFrameDuration(frameDuration);
				bubble.move();
				bubble.show();
			}
			
			// show the lines
			for(GameObject d : deadlines) {
				DeadLine deadLine = (DeadLine)d;
				deadLine.show();
			}
			
			// show the bubble shooter
			for(GameObject bs : bubbleshooters) {
				((BubbleShooter)bs).setPApplet(this);
				((BubbleShooter)bs).move();
				((BubbleShooter)bs).show();
			}
			
			// show color indicator
			((ColorIndicator)gameWorld.get("colorindicators").get(0)).show();
			
			//----------------------------------------------------------------------------------------------
			
			// all updates is done, now it's time to send data back to each client
			for(ServerTemp sTemp : serverTemps) {
				sTemp.ServerUpdateIsDone = true;
				sTemp.operation = 0;
			}
			
			eventManager.dispatch();
			
			dynaBubbles.clear();
			statBubbles.clear();
			stopBubbles.clear();
		}
	}

	@Override
	public void onEvent(event e) {
		switch (e.eventType) {
		case "ShootBubble":
			synchronized (server) {
				int c = (int) e.eventArguments.get("colorcase");
				float a = (float) e.eventArguments.get("angle");
				UUID shooter_id = (UUID) e.eventArguments.get("id");
				Bubble nB = new Bubble(t, 20, t.width/2, t.height, (float) 0.5, -t.PI/2+a, c);
				gameWorld.get("bubbles").add(nB);
				mbubble_ids.remove(shooter_id);
				mbubble_ids.put(shooter_id, nB.id);
			}
			break;
		
		case "BubbleDead":
			for(Bubble b : falling_bubbles)
				b.color = 255;
			break;
		
		case "BubbleGone":
			for(Bubble b : falling_bubbles) {
				gameWorld.get("bubbles").remove(b);
				
			}
			falling = false;
			falling_bubbles.clear();
			break;
		}	
	}
	
	private ArrayList<Bubble> get_neighbor_bubble(Bubble center){
		ArrayList<Bubble> neighbor = new ArrayList<>();
		
		// sanity check
		if(!statBubbles.contains(center)) {
			System.out.println("wrong parameter!: get_neighboor_bubble");
			return null;
		}
		
		for(Bubble bubble : statBubbles) {
			if((bubble.y+2*center.radius==center.y && bubble.x+center.radius==center.x) || // upper-left
			   (bubble.y+2*center.radius==center.y && bubble.x-center.radius==center.x) || // upper-right
			   (bubble.y==center.y && bubble.x+2*center.radius==center.x) || // left
			   (bubble.y==center.y && bubble.x-2*center.radius==center.x) || // right
			   (bubble.y-2*center.radius==center.y && bubble.x+center.radius==center.x) || // bottom-left
			   (bubble.y-2*center.radius==center.y && bubble.x-center.radius==center.x))  //bottom-right
				neighbor.add(bubble);
		}
		
		return neighbor;
	}
	
	private HashSet<Bubble> falling_bubble(){
		
		// clear the flag variable
		for(Bubble b : statBubbles)
			b.flag = false;
		
		HashSet<Bubble> f_Bubbles = new HashSet<>();
		
		for(Bubble b : stopBubbles) {
			HashSet<Bubble> bu = new HashSet<>();
			LinkedList<Bubble> queue = new LinkedList<>();
			b.flag = true;
			queue.addLast(b);
			
			while(!queue.isEmpty()) {
				Bubble head = queue.removeFirst();
				bu.add(head);
				ArrayList<Bubble> neighbor = get_neighbor_bubble(head);
				for(Bubble n : neighbor) {
					if(n.color==head.color && n.flag==false) {
						n.flag = true;
						queue.addLast(n);
					}
				}
			}
			
			if(bu.size() > 2) {
				for(Bubble i : bu)
					f_Bubbles.add(i);
			}
		}
		
		return f_Bubbles;
	}
	
	private HashSet<Bubble> dangling_bubble(){
		for(Bubble b : statBubbles)
			b.flag = false;
		
		HashSet<Bubble> d_Bubbles = new HashSet<>();
		
		ArrayList<Bubble> first_row = first_row();
		for(Bubble b : first_row) {
			LinkedList<Bubble> queue = new LinkedList<>();
			b.flag = true;
			queue.addLast(b);
			
			while(!queue.isEmpty()) {
				Bubble head = queue.removeFirst();
				ArrayList<Bubble> neighbor = get_neighbor_bubble(head);
				for(Bubble n : neighbor) {
					if (n.flag == false) {
						n.flag = true;
						queue.addLast(n);
					}
				}
			}
		}
		
		for(Bubble b : statBubbles) {
			if(b.flag == false)
				d_Bubbles.add(b);
		}
		
		return d_Bubbles;
	}

	private ArrayList<Bubble> first_row(){
		ArrayList<Bubble> fr = new ArrayList<>();
		for(Bubble bubble : statBubbles) {
			if(bubble.y == bubble.radius)
				fr.add(bubble);
		}
		return fr;
	}
	
	private void createWorld() {
		CopyOnWriteArrayList<GameObject> bubbles = gameWorld.get("bubbles");
		bubbles.clear();
		int init_bubble_i;
		int bubble_color_case;
		for(layer_i=0; layer_i<layer; layer_i++) {
			for(init_bubble_i=0; init_bubble_i<20; init_bubble_i++) {
				bubble_color_case= (int)t.random(3);
				bubbles.add(new Bubble(this, 20, 20+init_bubble_i*40+20*(layer_i%2), 20+layer_i*40, 0, 0, bubble_color_case));
			}
		}
		layer_mark = 0;
		click_count = 0;
		
		CopyOnWriteArrayList<GameObject> shooters = gameWorld.get("bubbleshooters");
		for(GameObject bs : shooters) {
			mbubble_ids.remove(bs.id);
			mbubble_ids.put(bs.id, null);
		}
	}
}
