package game.core;

public final class Player extends Entity implements Playable {

	public Player() {
		super();
	}
	
	public void move() {
		pos_x += dir_x;
		pos_y += dir_y;
	}
}
