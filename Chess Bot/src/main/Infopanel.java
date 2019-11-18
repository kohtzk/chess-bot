package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Infopanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7732137534778474065L;
	private JLabel turninfo, title;
	private Menubutton menubutton ;
	private int width, height;
	private boolean singleplayer;
	private Piececolor turn = Piececolor.white;
	
	public Infopanel(int tilesize, int buffer, Buttonlistener buttonlistener) {
		//this panel just holds the title, main menu button and the current turn information
		width = 12 * tilesize + 4 * buffer;
		height = tilesize; //not sure if needed
		
		//creates the turn information label
		turninfo = new JLabel();
		turninfo.setFont(new Font("Default", Font.BOLD, 24)); // need to change font size to be relative to tilesize
		turninfo.setHorizontalAlignment(SwingConstants.CENTER);
		turninfo.setBorder(BorderFactory.createMatteBorder(tilesize*3/64, tilesize*3/64, tilesize*3/64, tilesize*3/64, new Color(64, 64, 64)));
		turninfo.setOpaque(true);
		turninfo.setVisible(false);
		
		//creates the title to display at the top of the window
		title = new JLabel("KK's Chess Bot");
		title.setFont(new Font("Default", Font.BOLD, tilesize*7/8));
		title.setForeground(new Color(32,32,32));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setVisible(false);
		
		//creates the 'back to main menu' button
		menubutton = new Menubutton("Main Menu", 3*tilesize, tilesize*3/4);
		menubutton.addMouseListener(buttonlistener);
		menubutton.setcolors(192, 96, 160, 64);
		menubutton.setVisible(false);
		
		setBackground(new Color(64, 64, 64));
		setPreferredSize(new Dimension(width, height));
		addMouseMotionListener(buttonlistener);
		addMouseListener(buttonlistener);
		
		//puts the components in the desired layout and sizings
		GroupLayout menulayout = new GroupLayout(this);
		setLayout(menulayout);
		menulayout.setHorizontalGroup(
				menulayout.createSequentialGroup()
				.addGap(buffer/4)
				.addComponent(menubutton, 3*tilesize, 3*tilesize, 3*tilesize)
				.addComponent(title, 6*tilesize + buffer*7/2, 6*tilesize + buffer*7/2, 6*tilesize + buffer*7/2)
				.addComponent(turninfo, 3*tilesize, 3*tilesize, 3*tilesize)
				.addGap(buffer/4)
				);
		menulayout.setVerticalGroup(
				menulayout.createSequentialGroup()
				.addGroup(menulayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(menubutton, tilesize*3/4, tilesize*3/4, tilesize*3/4)
						.addComponent(title, tilesize, tilesize, tilesize)
						.addComponent(turninfo, tilesize*3/4, tilesize*3/4, tilesize*3/4))
				);
	}
	
	public void startgame(boolean singleplayer) {
		//makes everything visible, and changes the background color
		this.singleplayer = singleplayer;
		turninfo.setVisible(true);
		turn = Piececolor.black;
		title.setVisible(true);
		menubutton.setVisible(true);
		setBackground(new Color(128, 128, 128));
		nextturn();
	}
	
	public void reset() {
		//makes everything invisible and changes the background color
		setBackground(new Color(64, 64, 64));
		turninfo.setVisible(false);
		title.setVisible(false);
		menubutton.setVisible(false);
		System.out.println("top panel should be reset");
	}
	
	public void endgame(String message) {
		turninfo.setText(message);		
	}
	
	public void nextturn() {
		//changes the display text for the turn info label
		if (turn == Piececolor.white) {
			turn = Piececolor.black;
			if (singleplayer)
				turninfo.setText("Bot is thinking");
			else
				turninfo.setText("Blacks's Go");
			turninfo.setForeground(Color.WHITE);
			turninfo.setBackground(Color.BLACK);

		} else if (turn == Piececolor.black) {
			turn = Piececolor.white;
			turninfo.setText("White's Go");
			turninfo.setForeground(Color.BLACK);
			turninfo.setBackground(Color.WHITE);
		}
	}
}
