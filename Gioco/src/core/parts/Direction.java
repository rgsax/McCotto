package core.parts;

public enum Direction {
	E(0.0), NE(315.0), N(270.0), NW(225.0), W(180.0), SW(135.0), S(90.0), SE(45.0);
	double angle;
	Direction(double angle) {
		this.angle = angle;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public Direction opposite() {
		Direction d = null;
		switch(this) {
		case E:
			d = W;
			break;
		case W:
			d = E;
			break;
		case N:
			d = S;
			break;
		case S:
			d = N;
			break;
		case NW:
			d = SE;
			break;
		case SE:
			d = NW;
			break;
		case NE:
			d = SW;
			break;
		case SW:
			d = NE;
			break;
		}
		
		return d;
	}
}