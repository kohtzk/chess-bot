package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JFrame {

	private static final long serialVersionUID = 4192979835196130185L;

	private Container cp;
	private Chessgui chesspanel;
	private Menupanel menu;
	private Buttonlistener buttonlistener;
	private int tilesize;

	public GraphicsDevice gd;
	public int screenwidth;
	public int screenheight;

	public Game() {
		//initialises the window as well as most of the panels, which in turn initialise everything
		gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		screenwidth = gd.getDisplayMode().getWidth();
		screenheight = gd.getDisplayMode().getHeight();
		Piece.loadimages();
		Piece.loadtables();
		cp = getContentPane();
		setLayout(new BorderLayout(0, 0));
		
		buttonlistener = new Buttonlistener(this);
		chesspanel = new Chessgui(buttonlistener);
		menu = new Menupanel(chesspanel.tilesize, chesspanel.buffer, buttonlistener);
		tilesize = chesspanel.tilesize;

		cp.add(menu);
		cp.add(chesspanel.capturedblack, BorderLayout.WEST);
		cp.add(chesspanel.capturedwhite, BorderLayout.EAST);
		cp.add(chesspanel.toppanel, BorderLayout.NORTH);
		((JPanel) cp).setBorder(BorderFactory.createMatteBorder(tilesize/16, tilesize/16, tilesize/16, tilesize/16, new Color(16, 16, 16)));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Chess Bot");

		setUndecorated(true);
		setResizable(false);
		pack();
		setLocation((screenwidth - getWidth()) / 2, (screenheight - getHeight()) / 2);
		setVisible(true);
	}

	public void startgame(boolean bot) {
		//starts game
		cp.remove(menu);
		cp.add(chesspanel, BorderLayout.CENTER);
		pack();
		repaint();
		chesspanel.startgame(bot);
	}
	
	public void mainmenu() {
		//switches out the chessboard panel with the main menu panel
		cp.remove(chesspanel);
		cp.add(menu, BorderLayout.CENTER);
		resetgame();
		pack();
		repaint();
	}
	
	public void resetgame() {
		chesspanel.resetgame();
	}

	public static void main(String[] args) {
		new Game();
	}

}
