package main;

import java.util.List;

public class Dragpiece {

	public Piece piece;
	public int x;
	public int y;
	public int offsetx;
	public int offsety;
	public Position OGpos;
	public List<Position> possiblemoves;
	public List<Position> specialmoves;
	
	//public Dragpiece(List<Position> possiblemoves, Piece piece, int offsetx, int offsety, Position OGpos) {
	public Dragpiece(Board board, Piece piece, int offsetx, int offsety, Position OGpos) {
		this.possiblemoves = Rules.genmoves(board, piece, OGpos);
		this.specialmoves = Rules.genspecial(board, piece, OGpos);
		this.piece = piece;
		this.offsetx = offsetx;
		this.offsety = offsety;
		this.OGpos = OGpos;
	}
	
}
