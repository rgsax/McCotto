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
}