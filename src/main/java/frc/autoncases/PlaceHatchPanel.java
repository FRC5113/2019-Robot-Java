package frc.autoncases;

import frc.handlers.VisionTarget;
import frc.subsystems.DriveTrain;

public class PlaceHatchPanel {
	private final int ANGLE_TOLERANCE = 3;
	private VisionTarget target;

	public PlaceHatchPanel(VisionTarget target) {
		this.target = target;
	}

	// returns whether or not it should be placed
	public boolean update(DriveTrain dt) {
		if(target == null) {
			System.out.println("No target found!!!!");
			dt.driveCartesian(0, 0, 0);
		} else if (target.getAngle() < -ANGLE_TOLERANCE) {
			dt.driveCartesian(0.3, 0, 0.15);
		} else if (target.getAngle() > ANGLE_TOLERANCE) {
			dt.driveCartesian(-0.3, 0, -0.15);
		} else {
			if(true) { // get distance from lidar and make sure it is at least like 15 or something
				switch(target.getZone()) {
				case LEFT:
					dt.drivePolar(0, Direction.LEFT, 0);
					break;
				case CENTER:
					return true;
				case RIGHT:
					dt.drivePolar(0, Direction.RIGHT, 0);
					break;
				}
			} else {
				return true;
			}
		}

		return false;
	}

	public boolean update(DriveTrain dt, VisionTarget newTarget) {
		target = newTarget;
		return update(dt);
	}
}