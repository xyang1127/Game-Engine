package GameObjects;

import java.util.UUID;

import timeSystem.Timeline;

public class GameWorldTimeline extends GameObject{
	
	public Timeline timeline;
	
	public GameWorldTimeline(Timeline timeline) {
		id = UUID.randomUUID();
		this.timeline = timeline;
	}
	
}
