package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Promotedialog extends JDialog implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4839910754978994864L;
	private int tilesize;
	private Piece piece;

	public Promotedialog(JFrame parentframe, Position position, int tilesize, int infosize, Position coords, Piece piece) {
		super(parentframe, true);
		this.tilesize = tilesize;
		this.piece = piece;
		int width = 4 * tilesize;
		int height = (3 * tilesize) / 2;
		
		// could do this before passing into the dialog -
		// would eliminate needing to pass the position as well as coords objects
		// coords are location of top left of board, not chessgui panel
		int xoffset = (int) (coords.x + (position.x - 1.5) * tilesize);
		int yoffset;
		if (position.y == 0) {
			yoffset = coords.y + tilesize;
		} else {
			yoffset = coords.y - tilesize / 2 + 6 * tilesize;
		}

		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		addMouseListener(this);
		setLocation(xoffset, yoffset);
		setSize(width, height);

		Container cp = getContentPane();
		cp.add(new Promotepanel(piece.color, tilesize, infosize));
		setVisible(true);
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		//checks to see if the mouse clicked on one of the pieces to choose from
		//if they did, then it changes the pawn into the appropriate piece
		int ysel = tilesize / 2;
		int x = evt.getX();
		int y = evt.getY();
		if (piece.color == Piececolor.black) {
			ysel = 0;
		}
		if (y > ysel && y < ysel + tilesize) {
			if (x < tilesize) {
				piece.changepiece(PieceID.knight);
			} else if (x < tilesize * 2) {
				piece.changepiece(PieceID.bishop);
			} else if (x < tilesize * 3) {
				piece.changepiece(PieceID.rook);
			} else {
				piece.changepiece(PieceID.queen);
			}
			this.dispose();
		}

	}

	private class Promotepanel extends JPanel {
		
		private static final long serialVersionUID = -151993894570729253L;
		private Piece[] choices;
		private int tilesize;
		private int infosize;

		public Promotepanel(Piececolor color, int tilesize, int infosize) {
			setBackground(new Color(0, 0, 0, 0));
			this.tilesize = tilesize;
			this.infosize = infosize;
			choices = new Piece[] { new Piece(PieceID.knight, color), new Piece(PieceID.bishop, color),
					new Piece(PieceID.rook, color), new Piece(PieceID.queen, color) };
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			if (choices[0].color == Piececolor.black) {
				g2d.rotate(Math.PI, tilesize * 2, (tilesize * 3) / 4);
			}
			//draws the box with a triangle pointing at the pawn being promoted
			g2d.setColor(new Color(96, 96, 96));
			g2d.fillRect(0, tilesize / 2, tilesize * 4, tilesize);
			int[] xpoints = new int[] { 3 * tilesize / 2, 2 * tilesize, 5 * tilesize / 2 };
			int[] ypoints = new int[] { tilesize / 2, 0, tilesize / 2 };
			g2d.fillPolygon(xpoints, ypoints, 3);
			g2d.setColor(new Color(196, 196, 196));
			if (choices[0].color == Piececolor.black) {
				g2d.rotate(Math.PI, 2 * tilesize, tilesize);
			}
			//displays the icons of the possible upgrade pieces
			for (int i = 0; i < 4; i++) {
				int buffer = (tilesize - infosize) / 2;
				g2d.fillRect(i * tilesize + buffer, tilesize / 2 + buffer, infosize, infosize);
				g2d.drawImage(choices[i].icon(), i * tilesize, tilesize / 2, tilesize, tilesize, null);
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent evt) {}

	@Override
	public void mouseEntered(MouseEvent evt) {}

	@Override
	public void mouseExited(MouseEvent evt) {}

	@Override
	public void mouseClicked(MouseEvent evt) {}
}
