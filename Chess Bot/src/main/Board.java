package main;

public class Board {

	public Piece[][] piece;
	public Move move; // move that created this boardstate - for bot's use

	public Board(boolean populate) {
		piece = new Piece[8][8];
		if (populate) {
			
			//for checking pawn promotion
//			piece[2][1] = new Piece(PieceID.pawn, Piececolor.white);
			
			//for checking passant
//			piece[3][4] = new Piece(PieceID.pawn, Piececolor.black);
//			piece[4][6] = new Piece(PieceID.pawn, Piececolor.white);
			
			//for checking dans win/freeze
//			piece[0][0] = new Piece(PieceID.rook, Piececolor.black);
//			piece[2][0] = new Piece(PieceID.queen, Piececolor.black);
//			piece[6][7] = new Piece(PieceID.rook, Piececolor.white);
//			piece[2][1] = new Piece(PieceID.pawn, Piececolor.black);
//			piece[4][1] = new Piece(PieceID.king, Piececolor.black);
//			piece[7][1] = new Piece(PieceID.queen, Piececolor.white);
//			piece[0][2] = new Piece(PieceID.knight, Piececolor.black);
//			piece[1][2] = new Piece(PieceID.pawn, Piececolor.black);
//			piece[4][2] = new Piece(PieceID.pawn, Piececolor.black);
//			piece[7][2] = new Piece(PieceID.bishop, Piececolor.white);
//			piece[0][3] = new Piece(PieceID.pawn, Piececolor.black);
//			piece[4][3] = new Piece(PieceID.pawn, Piececolor.white);
//			piece[0][4] = new Piece(PieceID.pawn, Piececolor.white);
//			piece[7][4] = new Piece(PieceID.pawn, Piececolor.white);
//			piece[2][5] = new Piece(PieceID.pawn, Piececolor.white);
//			piece[5][4] = new Piece(PieceID.rook, Piececolor.black);
//			piece[0][6] = new Piece(PieceID.pawn, Piececolor.white);
//			piece[3][7] = new Piece(PieceID.rook, Piececolor.white);
//			piece[4][7] = new Piece(PieceID.king, Piececolor.white);
			
			//populates the board with the starting pieces in their original positions

			piece[0][7] = new Piece(PieceID.rook, Piececolor.white);
			piece[1][7] = new Piece(PieceID.knight, Piececolor.white);
			piece[2][7] = new Piece(PieceID.bishop, Piececolor.white);
			piece[3][7] = new Piece(PieceID.queen, Piececolor.white);
			piece[4][7] = new Piece(PieceID.king, Piececolor.white);
			piece[5][7] = new Piece(PieceID.bishop, Piececolor.white);
			piece[6][7] = new Piece(PieceID.knight, Piececolor.white);
			piece[7][7] = new Piece(PieceID.rook, Piececolor.white);
			for (int i = 0; i < 8; i++) {
				piece[i][6] = new Piece(PieceID.pawn, Piececolor.white);
			}

			piece[0][0] = new Piece(PieceID.rook, Piececolor.black);
			piece[1][0] = new Piece(PieceID.knight, Piececolor.black);
			piece[2][0] = new Piece(PieceID.bishop, Piececolor.black);
			piece[3][0] = new Piece(PieceID.queen, Piececolor.black);
			piece[4][0] = new Piece(PieceID.king, Piececolor.black);
			piece[5][0] = new Piece(PieceID.bishop, Piececolor.black);
			piece[6][0] = new Piece(PieceID.knight, Piececolor.black);
			piece[7][0] = new Piece(PieceID.rook, Piececolor.black);
			for (int i = 0; i < 8; i++) {
				piece[i][1] = new Piece(PieceID.pawn, Piececolor.black);
			}
			
		}
	}

	public void copyto(Board toboard) {
		//this copies this board to the toboard board
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (this.piece[i][j] != null)
					toboard.piece[i][j] = this.piece[i][j].clone();
				else
					toboard.piece[i][j] = null;
			}
		}
	}

}
