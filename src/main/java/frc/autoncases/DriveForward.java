package frc.autoncases;

import frc.subsystems.DriveTrain;

public class DriveForward {
    private final int FORWARD = 90, LEFT = 180, REVERSE = -90, RIGHT = 0;
    
    public void update(DriveTrain dt) {
        dt.drivePolar(1, FORWARD, 0);
    }
}