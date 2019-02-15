package frc.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;

//import static org.junit.Assume.assumeTrue;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.autoncases.Direction;

public class DriveTrain {
    private WPI_TalonSRX frontLeft = new WPI_TalonSRX(13), backLeft = new WPI_TalonSRX(12);
    private WPI_TalonSRX frontRight = new WPI_TalonSRX(14), backRight = new WPI_TalonSRX(15);

    private MecanumDrive mecDrive = new MecanumDrive(frontRight, frontLeft, backRight, backLeft);

    private AHRS navx = new AHRS(SPI.Port.kMXP);
    private PIDoutputImp pidOutputN = new PIDoutputImp();
    private PIDoutputImp pidOutputL = new PIDoutputImp();
    private LIDARLite lidar = new LIDARLite(I2C.Port.kOnboard);
    private PIDController completeControllerNav = new PIDController(0.5, 0, 0.5, navx, pidOutputN);
    private PIDController completeControllerLidar = new PIDController(0.5, 0, 0.5, lidar, pidOutputL);
    

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
        mecDrive.driveCartesian(xPower, yPower, rotation, navx.getAngle());
    }
    
    public void drivePolar(double magnitude, double angle, double rotation) {
        mecDrive.drivePolar(magnitude, angle, rotation);
    }
    
    public void drivePolar(double magnitude, Direction direction, double rotation) {
        drivePolar(magnitude, direction.degrees, rotation);
    }

    public void driveStraightConsistent(double angle) {
        completeControllerNav.setSetpoint(angle);
        mecDrive.driveCartesian(0, 0.5, pidOutputN.get());
    }

    public void driveStraightConsistentDistance(double angle, double distance) {
        completeControllerLidar.setSetpoint(distance);
        completeControllerNav.setSetpoint(angle);
        mecDrive.driveCartesian(0, pidOutputL.get(), pidOutputN.get());
    }

    public void printGyroAngle() {
        System.out.println(navx.getAngle());
    }

    public void resetNavxAngle() {
        navx.reset();
    }
}