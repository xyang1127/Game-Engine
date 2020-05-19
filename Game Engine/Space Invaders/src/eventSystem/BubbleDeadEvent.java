package eventSystem;

import java.awt.Event;

// in this event bubble changes its color to pale white
public class BubbleDeadEvent extends event{

	public BubbleDeadEvent(long delay) {
		super(delay);
		eventType = "BubbleDead";
		priority = 3;
	}

}
