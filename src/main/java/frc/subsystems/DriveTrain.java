package frc.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.autoncases.Direction;

public class DriveTrain {
    private WPI_TalonSRX frontLeft = new WPI_TalonSRX(13), backLeft = new WPI_TalonSRX(12);
    private WPI_TalonSRX frontRight = new WPI_TalonSRX(14), backRight = new WPI_TalonSRX(15);

    private MecanumDrive mecDrive = new MecanumDrive(frontRight, frontLeft, backRight, backLeft);

    private AHRS navx = new AHRS(SPI.Port.kMXP);

    // private PIDController driveController = new PIDController(0, 0, 0);
    // private PIDController rotationController = new PIDController(Kp, Ki, Kd, source, output);

    public DriveTrain() {       
        frontRight.setNeutralMode(NeutralMode.Brake);
        frontLeft.setNeutralMode(NeutralMode.Brake);
        backLeft.setNeutralMode(NeutralMode.Brake);
        backRight.setNeutralMode(NeutralMode.Brake);
    }

    public void driveCartesian(double xPower, double yPower, double rotation) {
        mecDrive.driveCartesian(yPower, xPower, rotation);
    }

    public void driveCartesianFOD(double xPower, double yPower, double rotation) {
        mecDrive.driveCartesian(yPower, xPower, rotation, navx.getAngle());
    }
    
    public void drivePolar(double magnitude, double angle, double rotation) {
        mecDrive.drivePolar(magnitude, angle, rotation);
    }
    
    public void drivePolar(double magnitude, Direction direction, double rotation) {
        drivePolar(magnitude, direction.degrees, rotation);
    }

    public boolean drivePID(double inches) { // returns whether or not it is finished driving
        double circumference = 8 * 2 * Math.PI;
        double revolutions = inches / circumference;
        double ticks = revolutions * 4096;

        return false;
    }

    public boolean rotate(double degrees) {

        return false;
    }

    public void printGyroAngle() {
        System.out.println(navx.getAngle());
    }
}