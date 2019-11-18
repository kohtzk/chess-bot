package main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Piece {
	public PieceID id;
	public Piececolor color;
	public int turnstaken = 0;
	public boolean canpassant = false;
	public static BufferedImage[][] icon;
	public static int[][] pawntable, rooktable, knighttable, bishoptable, queentable, midkingtable, endkingtable;
	
	public Piece(PieceID id, Piececolor color) {
		this.color = color;
		this.id = id;
	}
	
	public void changepiece(PieceID id) {
		this.id = id;
	}
	
	public BufferedImage icon() {
		return icon[color.num()][id.num()];
	}
	
	public void updatemoves() {
		turnstaken++;
	}
	
	public Piece clone() {
		//creates a new piece with the same stats as this one
		Piece newpiece = new Piece(this.id, this.color);
		newpiece.turnstaken = this.turnstaken;
		newpiece.canpassant = this.canpassant;
		return newpiece;
	}
	
	public static void loadimages() {
		//loads the chess piece icons from their files
		icon = new BufferedImage[2][6];
		Piececolor[] colors = Piececolor.values();
		for (int i = 0; i < 2; i++) {
			int j = 0;
			for (PieceID id : PieceID.values()) {
				try {
					icon[i][j] = ImageIO.read(Piece.class.getResource("/images/128x/" + colors[i] + "_" + id + ".png"));
					System.out.println("/images/128x/" + colors[i] + "_" + id + ".png" + " loaded");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("/images/128x/" + colors[i] + "_" + id + ".png" + " failed to load");
				}
				j++;
				
			}
		}
	}
	
	public static void loadtables() {
		//loads these piece-square tables into memory
		pawntable = new int[][] {
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{50, 50, 50, 50, 50, 50, 50, 50},
			{10, 10, 20, 30, 30, 20, 10, 10},
			{ 5,  5, 10, 25, 25, 10,  5,  5},
			{ 0,  0,  0, 20, 20,  0,  0,  0},
			{ 5, -5,-10,  0,  0,-10, -5,  5},
			{ 5, 10, 10,-20,-20, 10, 10,  5},
			{ 0,  0,  0,  0,  0,  0,  0,  0}};
			
		rooktable = new int[][] {
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{ 5, 10, 10, 10, 10, 10, 10,  5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{ 0,  0,  0,  5,  5,  0,  0,  0}};
		
		knighttable = new int[][] {
			{-50,-40,-30,-30,-30,-30,-40,-50},
			{-40,-20,  0,  0,  0,  0,-20,-40},
			{-30,  0, 10, 15, 15, 10,  0,-30},
			{-30,  5, 15, 20, 20, 15,  5,-30},
			{-30,  0, 15, 20, 20, 15,  0,-30},
			{-30,  5, 10, 15, 15, 10,  5,-30},
			{-40,-20,  0,  5,  5,  0,-20,-40},
			{-50,-40,-30,-30,-30,-30,-40,-50}};
			
		bishoptable = new int[][] {
			{-20,-10,-10,-10,-10,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5, 10, 10,  5,  0,-10},
			{-10,  5,  5, 10, 10,  5,  5,-10},
			{-10,  0, 10, 10, 10, 10,  0,-10},
			{-10, 10, 10, 10, 10, 10, 10,-10},
			{-10,  5,  0,  0,  0,  0,  5,-10},
			{-20,-10,-10,-10,-10,-10,-10,-20}};
			
		queentable = new int[][] {
			{-20,-10,-10, -5, -5,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5,  5,  5,  5,  0,-10},
			{ -5,  0,  5,  5,  5,  5,  0, -5},
			{  0,  0,  5,  5,  5,  5,  0, -5},
			{-10,  5,  5,  5,  5,  5,  0,-10},
			{-10,  0,  5,  0,  0,  0,  0,-10},
			{-20,-10,-10, -5, -5,-10,-10,-20}};
			
		midkingtable = new int[][] {
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-20,-30,-30,-40,-40,-30,-30,-20},
			{-10,-20,-20,-20,-20,-20,-20,-10},
			{ 20, 20,  0,  0,  0,  0, 20, 20},
			{ 20, 30, 10,  0,  0, 10, 30, 20}};
			
		endkingtable = new int[][] {
			{-50,-40,-30,-20,-20,-30,-40,-50},
			{-30,-20,-10,  0,  0,-10,-20,-30},
			{-30,-10, 20, 30, 30, 20,-10,-30},
			{-30,-10, 30, 40, 40, 30,-10,-30},
			{-30,-10, 30, 40, 40, 30,-10,-30},
			{-30,-10, 20, 30, 30, 20,-10,-30},
			{-30,-30,  0,  0,  0,  0,-30,-30},
			{-50,-30,-30,-30,-30,-30,-30,-50}};
			
		//formats tables in black format
		pawntable = formatmatrix(pawntable);
		rooktable = formatmatrix(rooktable);
		knighttable = formatmatrix(knighttable);
		bishoptable = formatmatrix(bishoptable);
		queentable = formatmatrix(queentable);
		midkingtable = formatmatrix(midkingtable);
		endkingtable = formatmatrix(endkingtable);
		
		System.out.println("Loaded Piece-Square tables");
	}
	
	private static int[][] formatmatrix(int[][] matrix) {
		//flips and rotates the matrix
		int[][] tempmatrix = new int[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				tempmatrix[i][j] = matrix[7-j][i];
			}
		}
		return tempmatrix;
	}
}








