package frc.handlers;

public class VisionTarget {
	private final int xCoord, area, numTargetsFound, distance;
	private final Zone zone; // the screen is evenly cut into three vertical zones

    public enum Zone {
        LEFT, CENTER, RIGHT, UNDEFINED;
    }
	
	public VisionTarget(int xCoord, int area, int numTargetsFound, int distance, int X_RESOLUTION) {
		this.xCoord = xCoord;
		this.area = area;
		this.numTargetsFound = numTargetsFound;
		this.distance = distance;

		if(numTargetsFound == 0) // targets not detected
			this.zone = Zone.UNDEFINED;
		else if(numTargetsFound == 1) { // robot is close enough that only one is detected
			// this might not be necessary
			this.zone = Zone.CENTER;
		} else if(numTargetsFound == 2) { // normal range of seeing both targets
			if(xCoord <= X_RESOLUTION / 3)
				this.zone = Zone.LEFT;
			else if(xCoord >= X_RESOLUTION * 2 / 3)
				this.zone = Zone.RIGHT;
			else
				this.zone = Zone.CENTER;
		} else // this shouldn't happen because there should never be more than two found.
			this.zone = Zone.UNDEFINED;
	}

	public int getXCoord() {
		return xCoord;
	}

	public int getArea() {
		return area;
	}

	public int getNumTargetsFound() {
		return numTargetsFound;
	}

	public int getDistance() {
		return distance;
	}

	public Zone getZone() {
		return zone;
	}
}