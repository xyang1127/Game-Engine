package eventSystem;

import java.io.Serializable;import java.sql.Time;
import java.util.HashMap;

import timeSystem.Timeline;

public class event implements Serializable{
	public String eventType;
	public HashMap<String, Object> eventArguments;
	public int priority;
	public long createTime;
	public long delaytime;
	public long deliveryTime;
	private Timeline timeline;
	
	public event(long delay) {
		timeline = EventManager.timeline;
		createTime = timeline.getCurrentTics();
		delaytime =delay;
		deliveryTime = createTime + delaytime;
	}
	
	public void updateDeliveryTime(long time) {
		createTime = time;
		deliveryTime = createTime + delaytime;
	}
}
