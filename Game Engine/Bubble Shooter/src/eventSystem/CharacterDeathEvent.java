package eventSystem;

import java.util.HashMap;
import java.util.UUID;

import timeSystem.Timeline;

public class CharacterDeathEvent extends event{
	
	public CharacterDeathEvent(int delay, UUID id) {
		
		super(delay);
		eventType = "CharacterDeath";
		eventArguments = new HashMap<>();
		eventArguments.put("id", id);
		priority = 3;

		
	}
}
