package frc.autoncases;

import edu.wpi.first.wpilibj.I2C.Port;
import frc.handlers.VisionTarget;
import frc.subsystems.DriveTrain;
import frc.subsystems.HatchIntake;
import frc.subsystems.LIDARLite;

public class PlaceHatchPanel {
	private final int ANGLE_TOLERANCE = 3, STRAFE_TOLERANCE = 43;
	private final double STRAFE_P = 0.002, ANGLE_P = 0.05;
	private int angleError, strafeError;
	private VisionTarget target;
	private LIDARLite lidar = new LIDARLite(Port.kMXP); // might be wrong port;

	public PlaceHatchPanel(VisionTarget target) {
		this.target = target;
	}

	// returns whether or not it should be placed
	public boolean update(DriveTrain driveTrain, HatchIntake hatchIntake) {
		if(target == null) {
			System.out.println("No target found!!!!");
			driveTrain.driveCartesian(0, 0, 0);
		} else { // target found
			if(STRAFE_P * strafeError > STRAFE_TOLERANCE)
				driveTrain.driveCartesian(STRAFE_P * strafeError, 0, 0); // x and y might be flipped
			else if(ANGLE_P * angleError > ANGLE_TOLERANCE || Math.abs(strafeError) > VisionTarget.X_RESOLUTION / 5)
				driveTrain.driveCartesian(0, 0, ANGLE_P * angleError);
			else if(angleError < ANGLE_TOLERANCE && strafeError < STRAFE_TOLERANCE)
				driveTrain.driveCartesian(0, 0.5, 0); // x and y might be flipped
		}
		
		return false;
	}

	public boolean update(DriveTrain driveTrain, HatchIntake hatchIntake, VisionTarget newTarget) {
		target = newTarget;
		return update(driveTrain, hatchIntake);
	}
}