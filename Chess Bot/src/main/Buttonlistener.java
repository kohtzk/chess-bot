package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Buttonlistener extends MouseAdapter {

	private Game game;
	private int lastx, lasty;
	private boolean currentpressvalid;
	private Menubutton currentbutton;

	public Buttonlistener(Game game) {
		this.game = game;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//moves window by the amount the mouse was dragged since its last position
		game.setLocation(game.getX() + (e.getXOnScreen() - lastx), game.getY() + (e.getYOnScreen() - lasty));
		lastx = e.getXOnScreen();
		lasty = e.getYOnScreen();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource().getClass() == Infopanel.class) {
			lastx = e.getXOnScreen();
			lasty = e.getYOnScreen();
		} else {
			currentpressvalid = true;
			currentbutton = (Menubutton) e.getSource();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (currentpressvalid && currentbutton == (Menubutton) e.getSource())
			mouseClicked(e);
		currentpressvalid = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource().getClass() != Infopanel.class && currentbutton == (Menubutton) e.getSource())
			currentpressvalid = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		currentpressvalid = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//carries out a particular action depending on which button the user pressed
		if (e.getSource().getClass() != Infopanel.class) {
			switch (((Menubutton) e.getSource()).text.toLowerCase()) {
			case "singleplayer":
				System.out.println("Single Player Button Pressed");
				game.startgame(true);
				break;
			case "local 2 player":
				System.out.println("2 Player Button Pressed");
				game.startgame(false);
				break;
			case "exit":
				System.out.println("Exit Button Pressed");
				System.exit(0);
				break;
			case "main menu":
				System.out.println("Main Menu Button Pressed");
				game.mainmenu();
				break;
			}
		}
	}

}
