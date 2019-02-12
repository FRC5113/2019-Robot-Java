package frc.autoncases;

import frc.subsystems.DriveTrain;

public class DriveForward {    
    public void update(DriveTrain dt, int inches) {
        double circumference = 8 * 2 * Math.PI;
        double revolutions = inches / circumference;
        double ticks = revolutions * 4096;

        

        dt.drivePolar(1, Direction.FORWARD, 0);
    }
}