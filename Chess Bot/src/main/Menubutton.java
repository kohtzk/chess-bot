package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Menubutton extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5139669517740121798L;
	public String text;
	private JLabel label;
	private int width, height, borderwidth;
	private Color fill, border, pressfill, pressborder;
	private boolean pressed = false;

	public Menubutton(String text, int width, int height) {
		this.text = text;
		this.width = width;
		this.height = height;
		borderwidth = height / 16;
		
		//to make it take up all the space provided
		setLayout(new GridLayout(1,1));
		
		label = new JLabel(text);
		label.setForeground(Color.black);
		label.setFont(new Font("Default", Font.PLAIN, height/2));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label);
		
		//sets default colors
		fill = new Color(192, 192, 192);
		border = new Color(128, 128, 128);
		pressfill = new Color(160, 160, 160);
		pressborder = new Color(96, 96, 96);
		setOpaque(false);
		addMouseListener(this);
		
	}
	
	public void setfont(int fontsize) {
		//changes font size of button
		label.setFont(new Font("Default", Font.PLAIN, fontsize));
	}
	
	public void setborderwidth(int borderwidth) {
		this.borderwidth = borderwidth;
	}
	
	public void setcolors(int fill, int border, int pressfill, int pressborder) {
		//changes button colors
		this.fill = new Color(fill, fill, fill);
		this.border = new Color(border, border, border);
		this.pressfill = new Color(pressfill, pressfill, pressfill);
		this.pressborder = new Color(pressborder, pressborder, pressborder);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (pressed) {
			g2d.setColor(pressborder);
			g2d.fillRect(0, 0, width, height);
			g2d.setColor(pressfill);
			g2d.fillRect(0 + borderwidth, 0 + borderwidth, width - 2 * borderwidth, height - 2 * borderwidth);
		} else {
			g2d.setColor(border);
			g2d.fillRect(0, 0, width, height);
			g2d.setColor(fill);
			g2d.fillRect(0 + borderwidth, 0 + borderwidth, width - 2 * borderwidth, height - 2 * borderwidth);
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		pressed = true;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		pressed = false;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

}
