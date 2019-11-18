package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Capturepanel extends JPanel {

	private static final long serialVersionUID = 8784271477716602621L;
	private List<Piece> pieces;
	private int tilesize;
	private int buffer;
	private Piececolor color;

	public Capturepanel(int tilesize, int buffer, Piececolor color) {
		this.tilesize = tilesize;
		this.buffer = buffer;
		this.color = color;
		setPreferredSize(new Dimension(2 * tilesize + buffer, 8 * tilesize + 2 * buffer));
		pieces = new ArrayList<Piece>();
	}

	public void addpiece(Piece piece) {
		//adds a piece to the list of captured pieces
		System.out.println("A " + piece.color + " " + piece.id + " was captured");
		pieces.add(piece);
		repaint();
	}
	
	public void clearpieces() {
		pieces.clear();
		repaint();
	} 

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		//draws the icons of the captured pieces in two columns in order from right to left
		g2d.setColor(new Color(64, 64, 64));
		g2d.fillRect(0, 0, 2 * tilesize + buffer, 8 * tilesize + 2 * buffer);
		int piecenum = 0;
		int leftbuffer = 0;
		if (color == Piececolor.black)
			leftbuffer = buffer;
		for (Piece piece : pieces) {
			g2d.drawImage(piece.icon(), (piecenum%2) * tilesize + leftbuffer,(piecenum/2) * tilesize + buffer, tilesize, tilesize, null);
			piecenum++;
		}
	}

}
