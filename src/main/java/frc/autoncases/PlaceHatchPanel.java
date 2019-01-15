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
		if(target == null) {
			System.out.println("No target found!!!!");
			return false;
		} else if(target.getDistance() > 6) {
			switch(target.getZone()) {
			case LEFT:
				dt.drivePolar(0, Direction.FORWARD_LEFT, -0.1);
				break;
			case CENTER:
				dt.drivePolar(0.15, Direction.FORWARD, 0);
				break;
			case RIGHT:
				dt.drivePolar(0, Direction.FORWARD_RIGHT, 0.1);
				break;
			}
			
			return false;
		} else {
			dt.drivePolar(0, 0, 0);
			return true;
		}
	}

	public boolean update(DriveTrain dt, VisionTarget newTarget) {
		target = newTarget;
		return update(dt);
	}
}