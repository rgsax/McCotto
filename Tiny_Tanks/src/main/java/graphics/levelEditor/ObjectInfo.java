package graphics.levelEditor;

public class ObjectInfo {
	private Integer width = null;
	private Integer height = null;
	private Double x;
	private Double y;
	public ObjectInfo(double x, double y) {
		this.setX(new Double(x));
		this.setY(new Double(y));
	}
	
	public ObjectInfo(int width, int height, double x, double y) {
		this.setWidth(new Integer(width));
		this.setHeight(new Integer(height));
		this.setX(new Double(x));
		this.setY(new Double(y));
	}
	
	@Override
	public String toString() {
		String dims = "";
		if(getWidth() != null)
			dims = getWidth().toString() + " " + getHeight().toString() + " ";
		return dims + getX().toString() + " " + getY().toString();
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}
}
