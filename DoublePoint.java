package IntellijRobots;

public class DoublePoint {
	private double x,y;
	
	public DoublePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
