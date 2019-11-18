package main;

public class Bottimer {

	public boolean done = true;
	private long starttime, time;
	
	public Bottimer(long time) {
		this.time = time;
	}
	
	public void start() {
		starttime = System.currentTimeMillis();
	}
	
	public long timepassed() {
		return System.currentTimeMillis() - starttime;
	}
	
	public boolean timeup() {
		return System.currentTimeMillis() > starttime + time;
	}
}
