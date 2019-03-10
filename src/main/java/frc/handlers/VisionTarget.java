package frc.handlers;

public class VisionTarget {
	public static int X_RESOLUTION;
	private final int xCoord;
	private final double angle, strafeError;
	private final Zone zone; // the screen is evenly cut into three vertical zones

    public enum Zone {
        LEFT, CENTER, RIGHT;
    }
	
	public VisionTarget(int xCoord, double angle, int X_RESOLUTION) {
		this.xCoord = xCoord;
		this.angle = angle;
		this.strafeError = xCoord - X_RESOLUTION / 2;
		this.X_RESOLUTION = X_RESOLUTION;

		if(xCoord <= X_RESOLUTION / 3)
			zone = Zone.LEFT;
		else if(xCoord >= X_RESOLUTION * 2 / 3)
			zone = Zone.RIGHT;
		else
			zone = Zone.CENTER;
	}

	public int getXCoord() {
		return xCoord;
	}

	public double getAngle() {
		return angle;
	}

	public Zone getZone() {
		return zone;
	}

	public int getXRes() {
		return X_RESOLUTION;
	} 
}