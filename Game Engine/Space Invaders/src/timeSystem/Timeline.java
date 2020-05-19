package timeSystem;

import java.io.Serializable;

//invariant:
//before using any function of Timeline class, you should first call start()

public abstract class Timeline implements Serializable{
	
	protected long startTic;
	protected float localTicSize;
	public long cumulativeTics = 0; // used to log the pause time
	protected boolean isPaused = false;
	
	public abstract void start(); //anchor time, this is the time in the anchored time line
	public abstract long getCurrentTics(); // return the elapsed ticks on THIS time line, i.e. the current time in THIS time line
	
	public abstract void pause();
	
	public abstract void unpause();
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public abstract void changeTicSize(float tic);
}
