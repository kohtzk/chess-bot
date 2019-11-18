package main;

import java.util.ArrayList;
import java.util.List;

public class Rules {

	public static List<Position> genmoves(Board board, Piece piece, Position position) {
		List<Position> moves = new ArrayList<Position>();
		// dont really need to pass piece through, could just use position to get from board
		//returns the possible moves for that piece depending on what piece it is
		switch (piece.id) {
		case pawn:
			moves = genpawn(board, position);
			break;
		case rook:
			moves = genrook(board, position);
			break;
		case knight:
			moves = genknight(board, position);
			break;
		case bishop:
			moves = genbishop(board, position);
			break;
		case king:
			moves = genking(board, position);
			break;
		case queen:
			moves = genqueen(board, position);
			break;
		}

		return moves;
	}

	public static List<Position> genspecial(Board board, Piece piece, Position position) {
		//generates special moves such as castle or en passant
		List<Position> moves = new ArrayList<Position>();
		switch (piece.id) {
		case pawn:
			moves = genenpassant(board, position);
			break;
		case king:
			moves = gencastle(board, position);
			break;
		default:
			break;
		}
		return moves;
	}

	private static List<Position> genenpassant(Board board, Position position) {
		List<Position> moves = new ArrayList<Position>();
		//check left and right (-1 and 1)
		//checks if a piece at a particular position can en passant on either side
		for (int i = -1; i < 2; i += 2) {
			if (position.x + i < 8 && position.x + i > -1) {
				Piece passantpiece = board.piece[position.x + i][position.y];
				if (passantpiece != null && passantpiece.id == PieceID.pawn && passantpiece.canpassant) {
					if (position.y == 3 && board.piece[position.x][position.y].color == Piececolor.white) {
						moves.add(new Position(position.x + i, position.y - 1));
					} else if (position.y == 4 && board.piece[position.x][position.y].color == Piececolor.black) {
						moves.add(new Position(position.x + i, position.y + 1));
					}
				}
			}
		}
		return moves;
	}

	private static List<Position> gencastle(Board board, Position position) {
		List<Position> moves = new ArrayList<Position>();
		// if king hasnt moved then it is in column 4
		// DOES NOT CHECK FOR CHECK
		//checks to see if the king can castle on either side
		if (board.piece[position.x][position.y].turnstaken == 0
				&& isincheck(board, board.piece[position.x][position.y].color).isEmpty()) {
			if (board.piece[0][position.y] != null && board.piece[0][position.y].turnstaken == 0
					&& board.piece[2][position.y] == null && board.piece[3][position.y] == null) {
				moves.add(new Position(2, position.y));
			}
			if (board.piece[7][position.y] != null && board.piece[7][position.y].turnstaken == 0
					&& board.piece[6][position.y] == null && board.piece[5][position.y] == null) {
				moves.add(new Position(6, position.y));
			}
		}
		return moves;
	}

	private static List<Position> genpawn(Board board, Position position) {
		List<Position> moves = new ArrayList<Position>();
		//generates possible moves for a pawn at the position
		int ydir;
		int xpos = position.x;
		int ypos = position.y;
		Piece piece = board.piece[position.x][position.y];
		if (piece.color == Piececolor.white)
			ydir = -1;
		else
			ydir = 1;
		// if space in front is a valid move and there is nothing there
		if (testpos(board, xpos, ypos + ydir, piece.color) && board.piece[xpos][ypos + ydir] == null) {
			moves.add(new Position(xpos, ypos + ydir));
			// if first move, then check space two ahead
			if (piece.turnstaken == 0 && testpos(board, xpos, ypos + ydir * 2, piece.color)
					&& board.piece[xpos][ypos + ydir * 2] == null)
				moves.add(new Position(xpos, ypos + ydir * 2));
		}
		// if forward diagonals have opposing pieces
		for (int i = 0; i < 2; i++) {
			if (testpos(board, xpos - 1 + i * 2, ypos + ydir, piece.color)
					&& board.piece[xpos - 1 + i * 2][ypos + ydir] != null)
				moves.add(new Position(xpos - 1 + i * 2, ypos + ydir));
		}
		return moves;
	}

	private static List<Position> genrook(Board board, Position position) {
		List<Position> moves = new ArrayList<Position>();
		//generates list of possible moves for a rook at that position
		moves.addAll(testpath(board, 1, 0, position.x, position.y));
		moves.addAll(testpath(board, -1, 0, position.x, position.y));
		moves.addAll(testpath(board, 0, 1, position.x, position.y));
		moves.addAll(testpath(board, 0, -1, position.x, position.y));
		return moves;
	}

	private static List<Position> testpath(Board board, int xdir, int ydir, int xpos, int ypos) {
		List<Position> moves = new ArrayList<Position>();
		//tests all the spaces along a path until it hits the edge of a board or another piece
		//used for pieces that travel along straight paths such as the bishop or rook
		Boolean end = false;
		Piececolor color = board.piece[xpos][ypos].color;
		while (!end) {
			xpos += xdir;
			ypos += ydir;
			if (xpos > -1 && xpos < 8 && ypos > -1 && ypos < 8) {
				Piece testpiece = board.piece[xpos][ypos];
				if (testpiece == null) {
					moves.add(new Position(xpos, ypos));
				} else if (testpiece.color == color) {
					end = true;
				} else {
					moves.add(new Position(xpos, ypos));
					end = true;
				}
			} else {
				end = true;
			}
		}
		return moves;
	}

	private static List<Position> genknight(Board board, Position position) {
		List<Position> moves = new ArrayList<Position>();
		int relx = 1;
		int rely = 2;
		//generates list of possible move for a knight at that position
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					if (testpos(board, position.x + relx, position.y + rely, board.piece[position.x][position.y].color))
						moves.add(new Position(position.x + relx, position.y + rely));
					relx *= -1;
				}
				rely *= -1;
			}
			relx = 2;
			rely = 1;
		}
		return moves;
	}

	private static boolean testpos(Board board, int xpos, int ypos, Piececolor color) {
		boolean valid = false;
		//this tests to see if a single space is ok to move to, i.e. it does not have a piece of the same color in it
		if (xpos > -1 && xpos < 8 && ypos > -1 && ypos < 8) {
			Piece testpiece = board.piece[xpos][ypos];
			if (testpiece == null) {
				valid = true;
			} else if (testpiece.color != color) {
				valid = true;
			}
		}
		return valid;
	}

	private static List<Position> genbishop(Board board, Position position) {
		List<Position> moves = new ArrayList<Position>();
		//generates moves for bishop
		moves.addAll(testpath(board, 1, 1, position.x, position.y));
		moves.addAll(testpath(board, 1, -1, position.x, position.y));
		moves.addAll(testpath(board, -1, 1, position.x, position.y));
		moves.addAll(testpath(board, -1, -1, position.x, position.y));
		return moves;
	}

	private static List<Position> genking(Board board, Position position) {
		List<Position> moves = new ArrayList<Position>();
		//generates move for king
		int relx = 1;
		int rely = 1;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (testpos(board, position.x + relx, position.y + rely, board.piece[position.x][position.y].color))
					moves.add(new Position(position.x + relx, position.y + rely));
				relx *= -1;
			}
			rely *= -1;
		}
		relx = 1;
		rely = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (testpos(board, position.x + relx, position.y + rely, board.piece[position.x][position.y].color))
					moves.add(new Position(position.x + relx, position.y + rely));
				relx *= -1;
				rely *= -1;
			}
			relx = 0;
			rely = 1;
		}
		return moves;
	}

	private static List<Position> genqueen(Board board, Position position) {
		List<Position> moves = new ArrayList<Position>();
		//generates move for queen (rook + bishop)
		moves.addAll(genrook(board, position));
		moves.addAll(genbishop(board, position));
		return moves;
	}

	public static Position findking(Board board, Piececolor color) {
		Position kingpos = null;
		//loops through the array to find the king of a particular color
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board.piece[i][j] != null && board.piece[i][j].id == PieceID.king
						&& board.piece[i][j].color == color) {
					kingpos = new Position(i, j);
					break;
				}
			}
			if (kingpos != null) {
				break;
			}
		}
		return kingpos;
	}

	public static List<Position> isincheck(Board board, Piececolor color) {
		// if there is a unit in that square that is the same type of unit, then king is
		// in check
		// have to do an extra check for pawns for 2 space move if king hasnt moved yet
		Position kingpos = findking(board, color);
		List<Position> checkspots = new ArrayList<Position>();
		for (Position checkspot : genqueen(board, kingpos)) {
			Piece piece = board.piece[checkspot.x][checkspot.y];
			if (piece != null && piece.id == PieceID.queen) {
				checkspots.add(checkspot);
			}
		}
		for (Position checkspot : genrook(board, kingpos)) {
			Piece piece = board.piece[checkspot.x][checkspot.y];
			if (piece != null && piece.id == PieceID.rook) {
				checkspots.add(checkspot);
			}
		}
		for (Position checkspot : genbishop(board, kingpos)) {
			Piece piece = board.piece[checkspot.x][checkspot.y];
			if (piece != null && piece.id == PieceID.bishop) {
				checkspots.add(checkspot);
			}
		}
		for (Position checkspot : genknight(board, kingpos)) {
			Piece piece = board.piece[checkspot.x][checkspot.y];
			if (piece != null && piece.id == PieceID.knight) {
				checkspots.add(checkspot);
			}
		}
		for (Position checkspot : genking(board, kingpos)) {
			Piece piece = board.piece[checkspot.x][checkspot.y];
			if (piece != null && piece.id == PieceID.king) {
				checkspots.add(checkspot);
			}
		}
		for (Position checkspot : genpawn(board, kingpos)) {
			Piece piece = board.piece[checkspot.x][checkspot.y];
			if (piece != null && piece.id == PieceID.pawn) {
				checkspots.add(checkspot);
			}
		}

		return checkspots;
	}

	public static List<Move> genall(Board board, Piececolor color) {
		List<Move> moves = new ArrayList<Move>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board.piece[i][j] != null && board.piece[i][j].color == color) {
					for (Position position : genmoves(board, board.piece[i][j], new Position(i, j))) {
						moves.add(new Move(new Position(i, j), position));
					}
				}
			}
		}
		return moves;
	}

	public static boolean checkallmoves(Board board, Piececolor color) {
		// small logic hole - does not check if en passant could get out of check
		Board testboard = new Board(false);
		List<Move> moves = genall(board, color);
		boolean allmovestocheck = true;
		//checks to see if all possible moves lead to check
		for (Move move : moves) {
			board.copyto(testboard);
			testboard.piece[move.to.x][move.to.y] = testboard.piece[move.from.x][move.from.y];
			testboard.piece[move.from.x][move.from.y] = null;
			if (isincheck(testboard, color).isEmpty()) {
				allmovestocheck = false;
				break;
			}
		}
		return allmovestocheck;
	}

	public static boolean onlykings(Board board) {
		boolean onlykings = true;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board.piece[i][j] != null && board.piece[i][j].id != PieceID.king) {
					onlykings = false;
					break;
				}
			}
			if (!onlykings)
				break;
		}
		return onlykings;
	}

}
