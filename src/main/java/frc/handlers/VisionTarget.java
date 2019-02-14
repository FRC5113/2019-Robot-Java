package frc.handlers;

public class VisionTarget {
	private final int xCoord, angle;
	private final Zone zone; // the screen is evenly cut into three vertical zones

    public enum Zone {
        LEFT1, LEFT2, LEFT3, CENTER, RIGHT3, RIGHT2, RIGHT1;
    }
	
	public VisionTarget(int xCoord, int angle, int X_RESOLUTION) {
		this.xCoord = xCoord;
		this.angle = angle;

		if(xCoord <= X_RESOLUTION * 1.0/7.0)
			this.zone = Zone.LEFT1;
		else if((xCoord >= X_RESOLUTION * 1.0/7.0) && (xCoord <= X_RESOLUTION * 2.0/7.0))
			this.zone = Zone.LEFT2;
		else if((xCoord >= X_RESOLUTION * 2.0/7.0) && (xCoord <= X_RESOLUTION * 3.0/7.0))
			this.zone = Zone.LEFT3;
		else if((xCoord >= X_RESOLUTION * 3.0/7.0) && (xCoord <= X_RESOLUTION * 4.0/7.0))
			this.zone = Zone.CENTER;
		else if((xCoord >= X_RESOLUTION * 4.0/7.0) && (xCoord <= X_RESOLUTION * 5.0/7.0))
			this.zone = Zone.RIGHT3;
		else if((xCoord >= X_RESOLUTION * 5.0/7.0) && (xCoord <= X_RESOLUTION * 6.0/7.0))
			this.zone = Zone.RIGHT2;
		else
			this.zone = Zone.RIGHT1;
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