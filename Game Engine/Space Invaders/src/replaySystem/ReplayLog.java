package replaySystem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import GameObjects.GameObject;
import eventSystem.event;

public class ReplayLog {
	
	public Snapshot snapshot;
	
	public LinkedList<ReplayInformation> logEvents; // only log MoveEvent;
	
	public long duraiton;
	
	public long start;
	
	public boolean logisFinished = false;
	
	public ReplayLog() {
		logEvents = new LinkedList<>();
	}
	
	public void addEvent(ReplayInformation e) {
		logEvents.add(e);
	}
	
}
