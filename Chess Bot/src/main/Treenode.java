package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Treenode {
	
	private Bot bot;
	private Board board;
	private List<Treenode> nextnodes; // list of all possible next board states turn to take
										// i.e. if turn = white then nextnodes = all moves for white player
	private int value;
	private Piececolor turn; // whose turn is it now i.e. who didnt just move
	public int layersleft;

	public Treenode(Board board, Piececolor turn, int layersleft, Bot bot) {
		bot.nodecount += 1;
		this.bot = bot;
		this.board = board;
		this.turn = turn;
		nextnodes = new ArrayList<Treenode>();
		this.layersleft = layersleft;
		//generates more nodes beneath this one if there are more layers left to be generated
		if (layersleft > 0 && bot.nodecount < bot.maxnodes) {
			gennodes();
		}
	}
	
	public void increasedepth() {
		layersleft++;
		//this increases the depth of the tree which means more nodes get generated if this is a leaf node
		if (nextnodes.size() > 0) {
			for (Treenode node : nextnodes) {
				node.increasedepth();
			}
		} else {
			if (bot.nodecount < bot.maxnodes) {
				gennodes();
			}
		}
	}

	public void printvalue() {
		//prints out the value of this node and all its children in a recursive loop
		System.out.print("<");
		System.out.print(value);
		for (Treenode nextboard : nextnodes) {
			nextboard.printvalue();
		}
		System.out.print(">");
	}

	public void evaluate() {
		//if blacks turn, then takes the highest value of all its children, if white then takes the lowest value of all its children
		if (nextnodes.size() > 0) {
			if (turn == Piececolor.black)
				value = -99999999;
			else
				value = 99999999;
			
			for (Treenode node : nextnodes) {
				node.evaluate();
				if (turn == Piececolor.black) {
					if (node.value > value) {
						value = node.value;
					}
				} else {
					if (node.value < value) {
						value = node.value;
					}
				}
			}
		} else {
			value = bot.evaluate(board, Piececolor.black);
		}
	}

	private void gennodes() {
		nextnodes.clear();
		//generates all the next moves after this boardstate and creates new nodes from those
		for (Board nextboard : bot.gennextboards(board, turn)) {
			nextnodes.add(new Treenode(nextboard, turn.opposite(), layersleft - 1, bot));
		}
	}

	public Board getbestmove() {
		Random random = new Random();
		Board bestmove = null;
		evaluate();
		//chooses the best option out of all the possible moves
		if (nextnodes.size() > 0) {
			List<Board> bestboards = new ArrayList<Board>();
			int maxscore = -999999;
			for (Treenode node : nextnodes) {
				int score = node.value;
				if (score > maxscore) {
					bestboards.clear();
					bestboards.add(node.board);
					maxscore = score;
				} else if (score == maxscore) {
					bestboards.add(node.board);
				}
			}
			bestmove = bestboards.get((random.nextInt(bestboards.size())));
		} else {
			System.out.println("Could not find any moves");
			bestmove = board;
		}
		return bestmove;
	}
}
