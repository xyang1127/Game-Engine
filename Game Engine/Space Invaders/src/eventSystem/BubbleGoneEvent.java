package eventSystem;


// in this event, dead bubbles are deleted from game world
public class BubbleGoneEvent extends event{

	public BubbleGoneEvent(long delay) {
		super(delay);
		eventType = "BubbleGone";
		priority = 3;
	}

}
