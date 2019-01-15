package frc.handlers;

public class VisionTarget {
	private final int xCoord, area, distance;
	private final Zone zone; // the screen is evenly cut into three vertical zones

    public enum Zone {
        LEFT, CENTER, RIGHT, UNDEFINED;
    }
	
	public VisionTarget(int xCoord, int area, int distance, int X_RESOLUTION) {
		this.xCoord = xCoord;
		this.area = area;
		this.distance = distance;

		if(xCoord <= X_RESOLUTION / 2)
			this.zone = Zone.LEFT;
		else if(xCoord >= X_RESOLUTION / 2)
			this.zone = Zone.RIGHT;
		else
			this.zone = Zone.CENTER;
	}

	public int getXCoord() {
		return xCoord;
	}

	public int getArea() {
		return area;
	}

	public int getDistance() {
		return distance;
	}

	public Zone getZone() {
		return zone;
	}
}