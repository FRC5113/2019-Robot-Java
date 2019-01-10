package frc.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.autoncases.Direction;

public class DriveTrain {
    private WPI_TalonSRX frontLeft = new WPI_TalonSRX(0), backLeft = new WPI_TalonSRX(1);
    private WPI_TalonSRX frontRight = new WPI_TalonSRX(2), backRight = new WPI_TalonSRX(3);

    private MecanumDrive mecDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);

    // ^ these IDs are fake, replace them with the correct values

    public DriveTrain() {
        
    }

    public void driveCartesian(double xPower, double yPower, double rotation) {
        mecDrive.driveCartesian(xPower, yPower, rotation);
    }
    
    public void drivePolar(double magnitude, double angle, double rotation) {
        mecDrive.drivePolar(magnitude, angle, rotation);
    }
    
    public void drivePolar(double magnitude, Direction direction, double rotation) {
        drivePolar(magnitude, direction.degrees, rotation);
    }
}