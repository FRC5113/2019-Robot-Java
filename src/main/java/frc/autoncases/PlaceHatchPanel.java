package frc.autoncases;

import frc.handlers.VisionTarget;
import frc.subsystems.DriveTrain;
import frc.subsystems.HatchIntake;

public class PlaceHatchPanel {
	private final int ANGLE_TOLERANCE = 3;
	private VisionTarget target;

	public PlaceHatchPanel(VisionTarget target) {
		this.target = target;
	}

	// returns whether or not it should be placed
	public boolean update(DriveTrain driveTrain, HatchIntake hatchIntake) {
		if(target == null) {
			System.out.println("No target found!!!!");
			driveTrain.driveCartesian(0, 0, 0);
		} else if (target.getAngle() < -ANGLE_TOLERANCE) {
			driveTrain.driveCartesian(0.5, -0.1, -0.15);
		} else if (target.getAngle() > ANGLE_TOLERANCE) {
			driveTrain.driveCartesian(-0.5, -0.1, 0.15);
		} else {
			if(true) { // get distance from lidar and make sure it is at least like 15 or something
				switch(target.getZone()) {
				case LEFT:
					driveTrain.drivePolar(0, Direction.LEFT, 0);
					return false;
				case CENTER:
					driveTrain.drivePolar(0.5, Direction.FORWARD, 0);
					return false;
				case RIGHT:
					driveTrain.drivePolar(0, Direction.RIGHT, 0);
					return false;
				}
			} else {
				hatchIntake.deploy();
				return true;
			}
		}

		return false;
	}

	public boolean update(DriveTrain driveTrain, HatchIntake hatchIntake, VisionTarget newTarget) {
		target = newTarget;
		return update(driveTrain, hatchIntake);
	}
}