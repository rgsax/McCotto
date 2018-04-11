package game.core;

public abstract class Entity extends GameObject {

	int dir_x;
    int dir_y;
    
    public Entity() {
    	dir_x = 0;
    	dir_y = 0;
    }
    
    public int getDirX () {
    	return dir_x;
    }
    
    public void setDirX (int dir_x) {
    	this.dir_x = dir_x;
    	}
    
    public int getDirY () {
    	return dir_y;
    	}
    
    public void setDirY (int dir_y) {
    	this.dir_y = dir_y;
        }
}
