package frc.handlers;

public class VisionTarget {
	private final int xCoord, angle;
	private final Zone zone; // the screen is evenly cut into three vertical zones

    public enum Zone {
        LEFT, CENTER, RIGHT;
    }
	
	public VisionTarget(int xCoord, int angle, int X_RESOLUTION) {
		this.xCoord = xCoord;
		this.angle = angle;

		if(xCoord <= X_RESOLUTION / 3)
			this.zone = Zone.LEFT;
		else if(xCoord >= X_RESOLUTION * 2 / 3)
			this.zone = Zone.RIGHT;
		else
			this.zone = Zone.CENTER;
	}

	public int getXCoord() {
		return xCoord;
	}

	public int getAngle() {
		return angle;
	}

	public Zone getZone() {
		return zone;
	}
}