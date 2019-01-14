package frc.autoncases;

import frc.subsystems.DriveTrain;

public class DriveForward {    
    public void update(DriveTrain dt) {
        dt.drivePolar(1, Direction.FORWARD, 0);
    }
}