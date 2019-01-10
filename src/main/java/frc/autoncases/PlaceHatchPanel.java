package frc.autoncases;

import frc.handlers.VisionTarget;
import frc.subsystems.DriveTrain;

public class PlaceHatchPanel {
	private VisionTarget target;

	public PlaceHatchPanel(VisionTarget target) {
		this.target = target;
	}

	// returns whether or not it should be placed
	public boolean update(DriveTrain dt) {
		switch(target.getZone()) {
		case LEFT:
			dt.drivePolar(0.5, Direction.FORWARD_LEFT, 0);
			break;
		case CENTER:
			dt.drivePolar(0.5, Direction.FORWARD, 0);
			break;
		case RIGHT:
			dt.drivePolar(0.5, Direction.FORWARD_RIGHT, 0);
			break;
		case UNDEFINED:
			System.out.println("Can't find target!");
			break;
		}

		return false;
	}
}