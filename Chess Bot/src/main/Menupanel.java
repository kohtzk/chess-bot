package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Menupanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1835344865932261949L;
	private int tilesize, buffer, width, height;
	private Menubutton singleplayer, multiplayer, quit;
	private JLabel title;
	
	public Menupanel(int tilesize, int buffer, Buttonlistener buttonlistener) {
		this.tilesize = tilesize;
		this.buffer = buffer;
		width = 8 * tilesize + 2 * buffer;
		height = 8 * tilesize + 2 * buffer;
		setPreferredSize(new Dimension(width, height));
		
		//creates the title label
		title = new JLabel("KK's Chess Bot");
		title.setFont(new Font("Default", Font.BOLD, tilesize));
		title.setForeground(new Color(196, 196, 196));
		title.setHorizontalAlignment(SwingConstants.CENTER);		
		
		//creates single player and 2 player buttons
		singleplayer = new Menubutton("Singleplayer", 4*tilesize, tilesize);
		multiplayer = new Menubutton("Local 2 Player", 4*tilesize, tilesize);
		
		//creates quit button
		quit = new Menubutton("Exit", 2*tilesize, tilesize*3/4);
		quit.setfont(tilesize/2);
		quit.setborderwidth(tilesize/16);
		
		//adds components
		singleplayer.addMouseListener(buttonlistener);
		multiplayer.addMouseListener(buttonlistener);
		quit.addMouseListener(buttonlistener);
		
		//sets the layout of the buttons and title
		GroupLayout menulayout = new GroupLayout(this);
		setLayout(menulayout);
		menulayout.setHorizontalGroup(
				menulayout.createSequentialGroup()
				.addGap(buffer)
				.addGroup(menulayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(title, 8*tilesize, 8*tilesize, 8*tilesize)
						.addComponent(singleplayer, 4*tilesize, 4*tilesize, 4*tilesize)
						.addComponent(multiplayer, 4*tilesize, 4*tilesize, 4*tilesize)
						.addComponent(quit, 2*tilesize, 2*tilesize, 2*tilesize))
				.addContainerGap()
				);
		menulayout.setVerticalGroup(
				menulayout.createSequentialGroup()
				.addGap(buffer)
				.addComponent(title)
				.addGap(tilesize*3/2)
				.addComponent(singleplayer, tilesize, tilesize, tilesize)
				.addGap(tilesize/2)
				.addComponent(multiplayer, tilesize, tilesize, tilesize)
				.addGap(tilesize/2)
				.addComponent(quit, tilesize*3/4, tilesize*3/4, tilesize*3/4)
				.addContainerGap()
				);
	}

	protected void paintComponent(Graphics g) {
		//colors the background
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(64, 64, 64));
		g2d.fillRect(0, 0, 8 * tilesize + 2 * buffer, 8 * tilesize + 2 * buffer);
	}
}











