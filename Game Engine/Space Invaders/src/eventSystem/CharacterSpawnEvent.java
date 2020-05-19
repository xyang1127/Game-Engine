package eventSystem;

import java.util.HashMap;
import java.util.UUID;

import GameObjects.SpawnPoint;
import timeSystem.Timeline;

public class CharacterSpawnEvent extends event{

	public CharacterSpawnEvent(int delay, UUID id, SpawnPoint spawnPoint) {
		
		super(delay);
		eventType = "CharacterSpawn";
		eventArguments = new HashMap<>();
		eventArguments.put("id", id);
		eventArguments.put("spawnpoint", spawnPoint);
		priority = 3;
		
	}
	
}
