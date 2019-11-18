package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import main.Chessgui.Painter;

public class Bot {

	private Piececolor botcolor;
	private Treenode tree;
	public Thread treethread;
	private Painter painter;
	public int nodecount;
	public int maxnodes = 100000;
	private Bottimer timer;

	public Bot(Piececolor color, Painter painter) {
		this.botcolor = color;
		this.painter = painter;
		timer = new Bottimer(2000);
	}
	
	private List<Move> getspecmoves(Board board, Piececolor color) {
		List<Move> moves = new ArrayList<Move>();
		int row = 0;
		if (color == Piececolor.white) {
			row = 3;
		}
		// looks through king row and row where pawns should be if they can passant (4 rows apart)
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 8; j++) {
				if (board.piece[j][row] != null)
					//adds generated special moves to the moves list
					for (Position position : Rules.genspecial(board, board.piece[j][row], new Position(j, row))) {
						moves.add(new Move(new Position(j, row), position));
					}
			}
			row += 4;
		}
		return moves;
	}

	public List<Board> gennextboards(Board board, Piececolor color) {
		List<Board> boards = new ArrayList<Board>();
		List<Move> moves = Rules.genall(board, color);
		List<Move> specmoves = getspecmoves(board, color);
		for (Move move : moves) {
			// gen list of boards from list of moves
			Board moveboard = new Board(false);
			board.copyto(moveboard);
			moveboard.piece[move.to.x][move.to.y] = moveboard.piece[move.from.x][move.from.y];
			moveboard.piece[move.from.x][move.from.y] = null;
			moveboard.piece[move.to.x][move.to.y].updatemoves();
			//changes pawns to queens and knights if they have reached the end of the board
			if (((move.to.y == 7 && moveboard.piece[move.to.x][move.to.y].color == Piececolor.black)
					|| (move.to.y == 0 && moveboard.piece[move.to.x][move.to.y].color == Piececolor.white))
					&& moveboard.piece[move.to.x][move.to.y].id == PieceID.pawn) {
				System.out.println(move.from.x + ", " + move.from.y + " to " + move.to.x + ", " + move.to.y);
				moveboard.piece[move.to.x][move.to.y].id = PieceID.queen;
				Board rookpromoteboard = new Board(false);
				moveboard.copyto(rookpromoteboard);
				rookpromoteboard.piece[move.to.x][move.to.y].id = PieceID.knight;
				boards.add(rookpromoteboard);
			}
			moveboard.move = move;
			boards.add(moveboard);
		}
		for (Move move : specmoves) {
			Board moveboard = new Board(false);
			board.copyto(moveboard);
			if (moveboard.piece[move.from.x][move.from.y].id == PieceID.king) {
				Position rookpos;
				//determines if it is a castle right or left, and sets the position of the rook accordingly
				if (move.to.x > move.from.x) {
					rookpos = new Position(7, move.to.y);
				} else {
					rookpos = new Position(0, move.to.y);
				}
				//performs castle on the board
				moveboard.piece[move.to.x][move.to.y] = moveboard.piece[move.from.x][move.from.y];
				moveboard.piece[move.from.x][move.from.y] = null;
				moveboard.piece[(move.from.x + move.to.x) / 2][move.to.y] = moveboard.piece[rookpos.x][rookpos.y];
				moveboard.piece[rookpos.x][rookpos.y] = null;
			} else {
				moveboard.piece[move.to.x][move.to.y] = moveboard.piece[move.from.x][move.from.y];
				moveboard.piece[move.from.x][move.from.y] = null;
				moveboard.piece[move.to.x][move.from.y] = null;
			}
			moveboard.move = move;
			boards.add(moveboard);
		}
		//returns list of possible future boards with the ones putting the current player in check filtered out
		return checkfilter(boards, color);
	}

	private List<Board> checkfilter(List<Board> boards, Piececolor color) {
		Iterator<Board> iterator = boards.iterator();
		//filters out all the board states that put the current player in check
		while (iterator.hasNext()) {
			Board testboard = iterator.next();
			if (!Rules.isincheck(testboard, color).isEmpty()) {
				iterator.remove();
			}
		}
		return boards;
	}

	public void gennextmove(Board board) {
		//starts the minimax tree generation thread
		treethread = new Thread(new Treethread(board, tree));
		treethread.start();
	}

	public Piece checkpiecetaken(Board lastboard, Board newboard) {
		Piece takenpiece = null; // could convert to use newboard.move instead of comparing every space
		//this compares every space of the last board to the next board to figure out what piece has been taken if any
		//this is used to display any pieces the AI takes on the side of the board
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (lastboard.piece[i][j] != newboard.piece[i][j] && lastboard.piece[i][j] != null) {
					if (lastboard.piece[i][j].color == Piececolor.white
							&& (newboard.piece[i][j] == null || newboard.piece[i][j].color == Piececolor.black)) {
						takenpiece = lastboard.piece[i][j];
					}
				}
			}
		}
		return takenpiece;
	}

	private class Treethread implements Runnable {
		private Board currentboard;
		private Treenode tree;

		public Treethread(Board currentboard, Treenode tree) {
			this.currentboard = currentboard;
			this.tree = tree;
		}

		public void run() {
			nodecount = 0;
			timer.start();
			// System.out.println("Timer started");
			//generates tree to 3 levels deep
			tree = new Treenode(currentboard, botcolor, 3, Bot.this);
			while (nodecount < maxnodes) {
				tree.increasedepth(); // increases depth until max nodes hit
			}
			System.out.println("Max depth reached: " + tree.layersleft);
			System.out.println("num of nodes: " + nodecount);
			// tree.printvalue();
			// System.out.println("");
			
			//gets best move for the AI to take (according to what it has simulated)
			Board bestmove = tree.getbestmove();
			
			System.out.println("Time taken for search: " + String.format("%.2f", ((double) timer.timepassed()) / 1000)
					+ " seconds");
			System.out.println("Time per node: "
					+ String.format("%.2f", ((double) timer.timepassed() * 1000) / nodecount) + " microseconds");
			//calls a function in the painter which updates the board as well as who's turn it is
			painter.takebotturn(bestmove, checkpiecetaken(currentboard, bestmove));
		}

	}

	public int evaluate(Board board, Piececolor color) {
		// need color passed in to know who's turn it is for check/checkmate/stalemate stuff
		// easter egg idea: do worst move possible if control-alt-k
		// [TODO] ADD CONNECTIVITY SCORE I.E. ADD THE AMOUNT OF AVAILABLE MOVES TO THE SCORE FOR THE PLAYER. more moves = better position
		//this checks to see if the board state is in the endgame
		boolean endgame = checkendgame(board);
		int value = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece piece = board.piece[i][j];
				if (piece != null) {
					//adds or subtracts a value for each piece depending on which color it is as well as it's position on the board
					if (piece.color == Piececolor.black) {
						switch (piece.id) {
						case pawn:
							value += piece.id.value() + Piece.pawntable[i][j];
							break;
						case rook:
							value += piece.id.value() + Piece.rooktable[i][j];
							break;
						case knight:
							value += piece.id.value() + Piece.knighttable[i][j];
							break;
						case bishop:
							value += piece.id.value() + Piece.bishoptable[i][j];
							break;
						case king:
							if (endgame)
								value += piece.id.value() + Piece.endkingtable[i][j];
							else 
								value += piece.id.value() + Piece.midkingtable[i][j];
							break;
						case queen:
							value += piece.id.value() + Piece.queentable[i][j];
							break;
						}
					} else {
						switch (piece.id) {
						case pawn:
							value -= piece.id.value() + Piece.pawntable[i][7-j];
							break;
						case rook:
							value -= piece.id.value() + Piece.rooktable[i][7-j];
							break;
						case knight:
							value -= piece.id.value() + Piece.knighttable[i][7-j];
							break;
						case bishop:
							value -= piece.id.value() + Piece.bishoptable[i][7-j];
							break;
						case king:
							if (endgame)
								value -= piece.id.value() + Piece.endkingtable[i][7-j];
							else 
								value -= piece.id.value() + Piece.midkingtable[i][7-j];
							break;
						case queen:
							value -= piece.id.value() + Piece.queentable[i][7-j];
							break;
						}
					}
				}
			}
		}
		// check if current player is in check
		if (Rules.checkallmoves(board, color) && !Rules.isincheck(board, color).isEmpty())
			value -= 999999;
		// check if other player is in check
		Piececolor playercolor = Piececolor.white;
		if (color == Piececolor.white)
			playercolor = Piececolor.black;
		if (Rules.checkallmoves(board, playercolor) && !Rules.isincheck(board, playercolor).isEmpty())
			value += 999999;
		return value;
	}

	private boolean checkendgame(Board board) {
		int whiteminors = 0, blackminors = 0;
		boolean whitequeen = false, blackqueen = false;
		//counts how many minor pieces besides the king are still in play on both sides as well as if the queen is still in play
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board.piece[i][j] != null) {
					if (board.piece[i][j].id == PieceID.queen) {
						if (board.piece[i][j].color == Piececolor.white)
							whitequeen = true;
						else
							blackqueen = true;
					} else if (board.piece[i][j].id != PieceID.pawn && board.piece[i][j].id != PieceID.king) {
						if (board.piece[i][j].color == Piececolor.white)
							whiteminors++;
						else
							blackminors++;
					}
				}
			}
		}
		//if both sides meet at least one of the requirements then board is in endgame
		return ((whitequeen && whiteminors <= 2) || (!whitequeen && whiteminors <= 5))
				&& ((blackqueen && blackminors <= 2) || (!blackqueen && blackminors <= 5));
	}
}
