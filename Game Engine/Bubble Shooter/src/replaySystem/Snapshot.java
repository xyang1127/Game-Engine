package replaySystem;

import java.util.HashMap;
import java.util.LinkedList;

public class Snapshot {
	public HashMap<String, LinkedList<BasicInformation>> states;
	
	public Snapshot() {
		states = new HashMap<>();
		states.put("gamecharacters", new LinkedList<>());
		states.put("deadzones", new LinkedList<>());
		states.put("platforms", new LinkedList<>());
	}
	
	public void clear() {
		states.get("gamecharacters").clear();
		states.get("deadzones").clear();
		states.get("platforms").clear();
	}
}
