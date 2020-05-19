package GameObjects;

import java.io.Serializable;
import java.util.UUID;

import eventSystem.event;
import eventSystem.handleable;

public abstract class GameObject implements Serializable, handleable{
	public UUID id;
	
	public void onEvent(event e) {
		;
	}
}
