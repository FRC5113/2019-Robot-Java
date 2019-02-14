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

	// returns whether or not it should have been placed
	public boolean update(DriveTrain driveTrain, HatchIntake hatchIntake) {
		if(target == null) {
			System.out.println("No target found!!!!");
			driveTrain.driveCartesian(0, 0, 0);
		// } else if (target.getAngle() < -ANGLE_TOLERANCE) {
		// 	driveTrain.driveCartesian(0.3, 0, -0.1);
		// 	System.out.println("rotate left");
		// } else if (target.getAngle() > ANGLE_TOLERANCE) {
		// 	driveTrain.driveCartesian(-0.3, 0, 0.1);
		// 	System.out.println("rotate right");
		} else {
			if(true) { // get distance from lidar and make sure it is at least like 15 or something
				switch(target.getZone()) {
				case LEFT1:
					driveTrain.drivePolar(0.55, Direction.LEFT, 0);
					System.out.println("strafe left");
					return false;
				case LEFT2:
					driveTrain.drivePolar(0.35, Direction.LEFT, 0);
					System.out.println("strafe left");
					return false;
				case LEFT3:
					driveTrain.drivePolar(0.25, Direction.LEFT, 0);
					System.out.println("strafe left");
					return false;
				case CENTER:
					driveTrain.drivePolar(/*0.5*/0, Direction.FORWARD, 0);
					System.out.println("go forward");
					return false;
				case RIGHT3:
					driveTrain.drivePolar(0.25, Direction.RIGHT, 0);
					System.out.println("strafe right");
					return false;
				case RIGHT2:
					driveTrain.drivePolar(0.35, Direction.RIGHT, 0);
					System.out.println("strafe right");
					return false;
				case RIGHT1:
					driveTrain.drivePolar(0.55, Direction.RIGHT, 0);
					System.out.println("strafe right");
					return false;
				}
			} else {
				driveTrain.driveCartesian(0, 0, 0);
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