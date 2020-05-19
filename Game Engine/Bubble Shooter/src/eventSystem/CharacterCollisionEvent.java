package eventSystem;

import java.util.HashMap;
import java.util.UUID;

import GameObjects.GameObject;
import GameObjects.Platform;
import timeSystem.Timeline;

// this event means character collide with platforms

public class CharacterCollisionEvent extends event{
	
	public CharacterCollisionEvent(int delay, int whichSurface, UUID id, GameObject platform) {
		
		super(delay);
		eventType = "CharacterCollision";
		eventArguments = new HashMap<>();
		eventArguments.put("surface", whichSurface); // i=1 means upper surface; i=2 means bottom surface
		eventArguments.put("id", id);
		eventArguments.put("platform", platform);
		priority = 3;
		
	}
	
}
