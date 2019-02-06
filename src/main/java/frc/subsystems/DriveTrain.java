package frc.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;

//import static org.junit.Assume.assumeTrue;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.autoncases.Direction;

public class DriveTrain {
    private WPI_TalonSRX frontLeft = new WPI_TalonSRX(13), backLeft = new WPI_TalonSRX(12);
    private WPI_TalonSRX frontRight = new WPI_TalonSRX(14), backRight = new WPI_TalonSRX(15);

    // private MecanumDrive mecDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
    private MecanumDrive mecDrive = new MecanumDrive(frontRight, frontLeft, backRight, backLeft);

    public DriveTrain() {
        frontRight.setNeutralMode(NeutralMode.Brake);
        frontLeft.setNeutralMode(NeutralMode.Brake);
        backLeft.setNeutralMode(NeutralMode.Brake);
        backRight.setNeutralMode(NeutralMode.Brake);
    }

    public void driveCartesian(double xPower, double yPower, double rotation) {
        mecDrive.driveCartesian(yPower, xPower, rotation);
    }
    
    public void drivePolar(double magnitude, double angle, double rotation) {
        mecDrive.drivePolar(magnitude, angle, rotation);
    }
    
    public void drivePolar(double magnitude, Direction direction, double rotation) {
        drivePolar(magnitude, direction.degrees, rotation);
    }
}