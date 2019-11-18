package main;

import java.awt.Color;

import main.Chessgui.Painter;

public class Infotile implements Runnable{
	
	private Thread animator;
	public Color color;
	private int r, g, b, a;
	private Painter painter;
	
	public Infotile(Painter painter) {
		color = new Color(0,0,0,0);
		this.painter = painter;
	}
	
	private void setcolor() {
		color = new Color(r, g, b, a);
	}
	
	public void setred() {
		//changes color of tile to red
		r = 255;
		g = 0;
		b = 0;
		a = 255;
		setcolor();
	}
	
	public void setorange() {
		//changes color of tile to orange
		r = 255;
		g = 110;
		b = 0;
		a = 255;
		setcolor();
	}
	
	public void setgreen() {
		//changes color of tile to green
		r = 0;
		g = 255;
		b = 0;
		a = 255;
		setcolor();
	}
	
	public void checkflash() {
		//flashes the infotile in orange
		r = 255;
		g = 110;
		b = 0;
		a = 0;
		setcolor();
		start();
	}
	
	public void clear() {
		//clears the color of the infotile
		r = 0;
		g = 0;
		b = 0;
		a = 0;
		setcolor();
		stop();
	}
	
	private void start() {
		if (animator == null) {
			animator = new Thread(this);
			animator.start();
		}
	}
	
	public void run() {
		//animate orange flash
		for (int i = 0; i < 2; i++) {
			while (a < 255) {
				a++;
				setcolor();
				painter.paint();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
			}
			while (a > 0) {
				a--;
				setcolor();
				painter.paint();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
			}
		}
		stop();
	}
	
	public void stop() {
		if (animator != null) {
			animator.interrupt();
			animator = null;
		}
	}
}
