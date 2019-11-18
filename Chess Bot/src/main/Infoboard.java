package main;

import main.Chessgui.Painter;

public class Infoboard {
	
	public Infotile[][] tile;
	
	public Infoboard(Painter painter) {
		//this is mainly just to store the information tiles
		tile = new Infotile[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				tile[i][j] = new Infotile(painter);
			}
		}
	}
	
	public void reset() {
		//resets all non orange tiles
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				//could make better way to detect if orange (the getgreen bit)
				if (tile[i][j].color.getAlpha() != 0 && tile[i][j].color.getGreen() != 110) {
					tile[i][j].clear();
					tile[i][j].stop();
				}
			}
		}
	}
	
	public void resetcheck() {
		//resets orange tiles
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				//could make better way to detect if orange (the getgreen bit)
				if (tile[i][j].color.getAlpha() != 0 && tile[i][j].color.getGreen() == 110) {
					tile[i][j].clear();
					tile[i][j].stop();
				}
			}
		}
	}
}
