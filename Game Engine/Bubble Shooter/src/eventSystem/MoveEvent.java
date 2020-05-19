package eventSystem;

import java.util.HashMap;
import java.util.UUID;

import timeSystem.Timeline;

public class MoveEvent extends event{

	public MoveEvent(int delay, int operation, UUID id) {
		
		super(delay);
		eventType = "Move";
		eventArguments = new HashMap<>();
		eventArguments.put("operation", operation);
		eventArguments.put("id", id);
		priority = 3;
		
	}
	
}
