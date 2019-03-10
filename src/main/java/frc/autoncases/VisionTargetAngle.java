package frc.autoncases;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.handlers.VisionTarget;

public class VisionTargetAngle implements PIDSource {

    private VisionTarget target;

	public void update(VisionTarget target) {
		this.target = target;
	}

    public VisionTargetAngle(VisionTarget target) {
        this.target = target;
    }

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
        //nothing yahoo
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		return target.getAngle();
	}

}