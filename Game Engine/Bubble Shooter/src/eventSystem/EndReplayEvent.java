package eventSystem;

public class EndReplayEvent extends event{
	
	public EndReplayEvent(long duraiton) {
		super(duraiton);
		eventType = "EndReplay";
		priority = 3;
	}

}
