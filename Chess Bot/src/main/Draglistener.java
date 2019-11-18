package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import main.Chessgui.Painter;

public class Draglistener extends MouseAdapter {

	private Board board;
	public Piececolor turn;
	private int buffer;
	private Painter painter;
	private Infoboard infoboard;
	private int tilesize;
	public Dragpiece dragpiece;
	private boolean singleplayer;

	private Capturepanel capturedwhite;
	private Capturepanel capturedblack;

	public Draglistener(Board board, Piececolor turn, int buffer, Painter painter, Infoboard infoboard, int tilesize,
			Capturepanel capturedwhite, Capturepanel capturedblack) {
		this.board = board;
		this.turn = turn;
		this.buffer = buffer;
		this.painter = painter;
		this.infoboard = infoboard;
		this.tilesize = tilesize;
		this.capturedwhite = capturedwhite;
		this.capturedblack = capturedblack;
	}
	
	public void setsingleplayer(boolean singleplayer) {
		this.singleplayer = singleplayer;
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		int mousex = evt.getX();
		int mousey = evt.getY();

		int tilex = (mousex - buffer) / tilesize;
		int tiley = (mousey - buffer) / tilesize;
		//makes sure the mouse pressed on a tile on the board
		if (mousex - buffer < 0 || tilex > 7 || mousey - buffer < 0 || tiley > 7) {
			tilex = -1;
			tiley = -1;
		}
		//if valid, puts the piece that the user pressed over into a special dragpiece object, and removes it from the board
		if (tilex != -1 && tiley != -1 && board.piece[tilex][tiley] != null
				&& board.piece[tilex][tiley].color == turn && (board.piece[tilex][tiley].color != Piececolor.black || !singleplayer)) {
			int offsetx = tilex * tilesize + buffer - mousex;
			int offsety = tiley * tilesize + buffer - mousey;
			dragpiece = new Dragpiece(board, board.piece[tilex][tiley], offsetx, offsety, new Position(tilex, tiley));
			mouseDragged(evt);
			board.piece[tilex][tiley] = null;
			List<Position> moves = dragpiece.possiblemoves;
			moves.addAll(dragpiece.specialmoves);
			for (Position move : moves) {
				if (board.piece[move.x][move.y] == null)
					infoboard.tile[move.x][move.y].setgreen();
				else
					infoboard.tile[move.x][move.y].setred();
			}
			System.out.println("Picked up a " + dragpiece.piece.id + " from " + tilex + ", " + tiley);
			painter.paint();
		}

	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		//moves the render location for the dragpiece as the player drags the mouse
		if (dragpiece != null) {
			dragpiece.x = evt.getX() + dragpiece.offsetx;
			dragpiece.y = evt.getY() + dragpiece.offsety;
			painter.paint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if (dragpiece != null) {
			int tilex = (dragpiece.x - buffer + tilesize / 2) / tilesize;
			int tiley = (dragpiece.y - buffer + tilesize / 2) / tilesize;
			boolean valid = false;
			boolean special = false;
			//makes sure the piece is being dropped on the board
			if (dragpiece.x - buffer + tilesize / 2 < 0 || tilex > 7 || dragpiece.y - buffer + tilesize / 2 < 0
					|| tiley > 7) {
				tilex = -1;
				tiley = -1;
			}

			Board testboard = new Board(false);
			List<Position> moves = dragpiece.possiblemoves;
			moves.addAll(dragpiece.specialmoves);
			//could be optimised
			//tests to see if the place the the user is trying to drop the piece is in the list of valid moves that has been generated for that piece
			//this makes sure the piece is being dropped at a valid location for that piece i.e. only diagonally for a bishop
			for (Position move : moves) {
				if (move.x == tilex && move.y == tiley) {
					valid = true;
					board.copyto(testboard);
					testboard.piece[tilex][tiley] = dragpiece.piece;
					for (Position checkpos : Rules.isincheck(testboard, dragpiece.piece.color)) {
						if (valid == true) {
							valid = false;
						}
						infoboard.tile[checkpos.x][checkpos.y].checkflash();
					}
					if (dragpiece.specialmoves.contains(move)) {
						special = true;
						board.copyto(testboard);
						if (dragpiece.piece.id == PieceID.king) {
							testboard.piece[(tilex + dragpiece.OGpos.x) / 2][tiley] = dragpiece.piece;
							for (Position checkpos : Rules.isincheck(testboard, dragpiece.piece.color)) {
								if (valid == true) {
									valid = false;
								}
								infoboard.tile[checkpos.x][checkpos.y].checkflash();
							}
						}
					}
					break;
				}
			}
			
			if (valid && tilex != -1) {
				//this updates the canpassant variable in pawns in specific rows
				for (int i = 0; i < 8; i++) {
					if (board.piece[i][3] != null && board.piece[i][3].canpassant == true && board.piece[i][3].color == turn) {
						board.piece[i][3].canpassant = false;
					}
					if (board.piece[i][4] != null && board.piece[i][4].canpassant == true && board.piece[i][4].color == turn) {
						board.piece[i][4].canpassant = false;
					}
				}
				if (!special) {
					//this adds the piece to their respective capturepanel if a piece is captured
					if (board.piece[tilex][tiley] != null) {
						System.out.println(board.piece[tilex][tiley].color);
						if (board.piece[tilex][tiley].color == Piececolor.white)
							capturedwhite.addpiece(board.piece[tilex][tiley]);
						else
							capturedblack.addpiece(board.piece[tilex][tiley]);
					}
					//this puts the dragpiece back into the board where the user drops it
					board.piece[tilex][tiley] = dragpiece.piece;
					board.piece[tilex][tiley].updatemoves();
					if (board.piece[tilex][tiley].id == PieceID.pawn && Math.abs(tiley-dragpiece.OGpos.y) == 2) {
						board.piece[tilex][tiley].canpassant = true;
					}
					System.out.println("Dropped a " + dragpiece.piece.id + " into " + tilex + ", " + tiley);
					dragpiece = null;
					//this activates the pawn promotion selection window if a pawn reaches the other side of the board
					if (board.piece[tilex][tiley].id == PieceID.pawn && (tiley == 0 || tiley == 7)) {
						painter.paint();
						painter.promotepawn(new Position(tilex, tiley), board.piece[tilex][tiley]);
					}
				} else {
					// if king then castle, if pawn then en passant
					if (dragpiece.piece.id == PieceID.king) {
						Position rookpos;
						// if castle right
						if (tilex > dragpiece.OGpos.x) {
							rookpos = new Position(7, tiley);
						} else {
							rookpos = new Position(0, tiley);
						}
						//performs a castle
						board.piece[tilex][tiley] = dragpiece.piece;
						board.piece[(tilex + dragpiece.OGpos.x) / 2][tiley] = board.piece[rookpos.x][rookpos.y];
						board.piece[rookpos.x][rookpos.y] = null;
						dragpiece = null;
					} else {
						//performs an en passant
						board.piece[tilex][tiley] = dragpiece.piece;
						if (board.piece[tilex][dragpiece.OGpos.y].color == Piececolor.white)
							capturedwhite.addpiece(board.piece[tilex][dragpiece.OGpos.y]);
						else
							capturedblack.addpiece(board.piece[tilex][dragpiece.OGpos.y]);
						board.piece[tilex][dragpiece.OGpos.y] = null;
						dragpiece = null;
					}
				}
				board.move = null;
				nextturn();
			} else {
				//reverts piece to original position if the move is not valid
				board.piece[dragpiece.OGpos.x][dragpiece.OGpos.y] = dragpiece.piece;
				System.out.println(dragpiece.piece.id + " reverted to " + dragpiece.OGpos.x + ", " + dragpiece.OGpos.y);
				dragpiece = null;
			}
			infoboard.reset();
		}
		painter.paint();
	}

	public void nextturn() {
		//updates the turn variable as well as calls the painter's next turn method
		painter.nextturn();
		
		if (turn == Piececolor.white) {
			turn = Piececolor.black;
		} else if (turn == Piececolor.black) {
			turn = Piececolor.white;
		}
	}

	public void updateboard(Board board) {
		this.board = board;
	}
	
	public void endgame() {
		turn = Piececolor.none;
	}
}
