package graphics.levelEditor;

public class ObjectInfo {
	Integer width = null, height = null;
	Double x, y;
	public ObjectInfo(double x, double y) {
		this.x = new Double(x);
		this.y = new Double(y);
	}
	
	public ObjectInfo(int width, int height, double x, double y) {
		this.width = new Integer(width);
		this.height = new Integer(height);
		this.x = new Double(x);
		this.y = new Double(y);
	}
	
	@Override
	public String toString() {
		String dims = "";
		if(width != null)
			dims = width.toString() + " " + height.toString() + " ";
		return dims + x.toString() + " " + y.toString();
	}
}
