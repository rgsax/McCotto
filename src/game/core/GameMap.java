package game.core;

public class GameMap {
	Tile[][] gmap;
	int width;
	int height;
	
	GameMap() {
		// TODO: lettura da file
	}
	
	Tile getTile(int i, int j) {
		return gmap[i][j];
	}
	
}
