package game.core;

public abstract class Entity extends GameObject {

	int dir_x;
    int dir_y;
    
    public Entity() {
    	dir_x = 0;
    	dir_y = 0;
    }
}
