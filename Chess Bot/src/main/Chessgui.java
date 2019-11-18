package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Chessgui extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7442775779715999376L;
	public Piececolor turn = Piececolor.white;
	public int tilesize = 64;
	public int infosize = tilesize * 7 / 8;
	public int buffer = tilesize / 2;
	private Draglistener listener;
	private Board gameboard;
	private Infoboard infoboard;
	public Capturepanel capturedwhite;
	public Capturepanel capturedblack;
	public Infopanel toppanel;
	public Bot chessbot;
	public boolean singleplayer;

	public Chessgui(Buttonlistener buttonlistener) {
		infoboard = new Infoboard(new Painter());
		gameboard = new Board(true);
		chessbot = new Bot(Piececolor.black, new Painter());

		toppanel = new Infopanel(tilesize, buffer, buttonlistener);

		capturedwhite = new Capturepanel(tilesize, buffer, Piececolor.white);
		capturedblack = new Capturepanel(tilesize, buffer, Piececolor.black);
		listener = new Draglistener(gameboard, turn, buffer, new Painter(), infoboard, tilesize, capturedwhite,
				capturedblack);

		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		setPreferredSize(new Dimension(8 * tilesize + 2 * buffer, 8 * tilesize + 2 * buffer));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (listener.dragpiece == null)
			g2d.setColor(new Color(64, 64, 64));
		g2d.setColor(new Color(64, 64, 64));
		g2d.fillRect(0, 0, tilesize * 8 + buffer * 2, tilesize * 8 + buffer * 2);
		int infooffset = (tilesize - infosize) / 2;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				//draws the tiles of the chess board
				if ((i + j) % 2 == 0) {
					g2d.setColor(new Color(192, 192, 192));
				} else {
					g2d.setColor(new Color(128, 128, 128));
				}
				int tilex = i * tilesize + buffer;
				int tiley = j * tilesize + buffer;
				g2d.fillRect(tilex, tiley, tilesize, tilesize);
				//draws the highlighed information tiles if they arent transparent
				if (infoboard.tile[i][j].color.getAlpha() != 0) {
					g2d.setColor(infoboard.tile[i][j].color);
					g2d.fillRect(tilex + infooffset, tiley + infooffset, infosize, infosize);
				}
				//draws the game piece if there is one in the tile
				if (gameboard.piece[i][j] != null) {
					Piece piece = gameboard.piece[i][j];
					g2d.drawImage(piece.icon(), tilex, tiley, tilesize, tilesize, null);
				}
			}
		}
		//draws a ghost of a piece to show where the piece was origially
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
		if (!singleplayer && listener.dragpiece != null) {
			g2d.drawImage(listener.dragpiece.piece.icon(), listener.dragpiece.OGpos.x * tilesize + buffer, listener.dragpiece.OGpos.y * tilesize + buffer, tilesize, tilesize, null);
		} else if (singleplayer) {
			if (listener.dragpiece != null)
				g2d.drawImage(listener.dragpiece.piece.icon(), listener.dragpiece.OGpos.x * tilesize + buffer, listener.dragpiece.OGpos.y * tilesize + buffer, tilesize, tilesize, null);
			if (gameboard.move != null) {
				Piece piece = gameboard.piece[gameboard.move.to.x][gameboard.move.to.y];
				g2d.drawImage(piece.icon(), gameboard.move.from.x * tilesize + buffer, gameboard.move.from.y * tilesize + buffer, tilesize, tilesize, null);
			}
		}
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		//draws the piece the user is currently dragging
		if (listener.dragpiece != null) {
			Dragpiece dragpiece = listener.dragpiece;
			g2d.drawImage(dragpiece.piece.icon(), dragpiece.x, dragpiece.y, tilesize, tilesize, null);
		}
	}

	public void startgame(boolean bot) {
		//initialises the game
		singleplayer = bot;
		listener.setsingleplayer(bot);
		toppanel.startgame(bot);
		// feel like i've forgotten something here
	}
	
	public void resetgame() {
		//wipes or resets all the information associated with the game
		gameboard = new Board(true);
		turn = Piececolor.white;
		listener.turn = Piececolor.white;
		listener.updateboard(gameboard);
		capturedwhite.clearpieces();
		capturedblack.clearpieces();
		infoboard.resetcheck();
		toppanel.reset();
		//will need to add more stuff to reset the bot once i've made it remember stuff
	}

	public class Painter {
		public void paint() {
			repaint();
		}

		public void nextturn() {
			//changes the current turn information and variable
			infoboard.resetcheck();
			// checkendgame(turn);
			toppanel.nextturn();
			System.out.println("Current board value: " + chessbot.evaluate(gameboard, Piececolor.black));
			if (turn == Piececolor.white) {
				turn = Piececolor.black;

			} else if (turn == Piececolor.black) {
				turn = Piececolor.white;
			}
			checkendgame(turn);
			//if singleplayer then starts the AI move
			if (singleplayer && turn == Piececolor.black) {
				chessbot.gennextmove(gameboard);
			}
		}

		public void takebotturn(Board board, Piece takenpiece) {
			//this updates the board and moves the game onto the next turn
			gameboard = board;
			listener.updateboard(gameboard);
			if (takenpiece != null)
				capturedwhite.addpiece(takenpiece);
			repaint();
			//calls the painter's nextturn as well
			listener.nextturn();
		}

		public void checkendgame(Piececolor turn) {
			//this checks to see if a player is in checkmate or stalemate and then acts accordingly
			boolean incheck = !Rules.isincheck(gameboard, turn).isEmpty();
			boolean leadtocheck = Rules.checkallmoves(gameboard, turn);
			if (incheck) {
				if (leadtocheck) {
					if (turn == Piececolor.white) {
						toppanel.endgame("Black Wins!");
					} else {
						toppanel.endgame("White Wins!");
					}
					Rules.checkallmoves(gameboard, turn);
					turn = Piececolor.none;
					endgame();
				} else {
					Position kingpos = Rules.findking(gameboard, turn);
					infoboard.tile[kingpos.x][kingpos.y].setorange();
				}
			} else if (leadtocheck) {
				toppanel.endgame("Stalemate");
				endgame();
			} else if (Rules.onlykings(gameboard)) {
				toppanel.endgame("Stalemate");
				endgame();
			}
		}

		public void endgame() {
			//this freezes the pieces
			turn = Piececolor.none;
			listener.endgame();
		}

		public void promotepawn(Position position, Piece piece) {
			//initialised the pawn promotion dialogue
			Point pos = getLocationOnScreen();
			int x = (int) pos.getX() + buffer;
			int y = (int) pos.getY() + buffer;
			new Promotedialog((JFrame) SwingUtilities.getWindowAncestor(Chessgui.this), position, tilesize, infosize,
					new Position(x, y), piece);
		}
	}

}
