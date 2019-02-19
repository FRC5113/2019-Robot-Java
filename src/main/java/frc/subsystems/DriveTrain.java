package frc.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

//import static org.junit.Assume.assumeTrue;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
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

        //Set the current limits
        frontRight.configPeakCurrentLimit(80);
        frontRight.configPeakCurrentDuration(500);
        frontRight.configContinuousCurrentLimit(60);
        frontRight.enableCurrentLimit(true);

        frontLeft.configPeakCurrentLimit(80);
        frontLeft.configPeakCurrentDuration(500);
        frontLeft.configContinuousCurrentLimit(60);
        frontLeft.enableCurrentLimit(true);

        backRight.configPeakCurrentLimit(80);
        backRight.configPeakCurrentDuration(500);
        backRight.configContinuousCurrentLimit(60);
        backRight.enableCurrentLimit(true);

        backLeft.configPeakCurrentLimit(80);
        backLeft.configPeakCurrentDuration(500);
        backLeft.configContinuousCurrentLimit(60);
        backLeft.enableCurrentLimit(true); 

        //Setup the encoder
        frontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        frontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        backRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        backLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        lidar.startMeasuring();

        ShuffleboardTab SensorTab = Shuffleboard.getTab("Sensors");
        SensorTab.add("Lidar", lidar);
    }

    public void driveCartesian(double xPower, double yPower, double rotation) {
        mecDrive.driveCartesian(yPower, xPower, rotation);
        //printCurrentDraw();
        //printEncoderVal();
    }

    public void driveCartesianFOD(double xPower, double yPower, double rotation) {
        mecDrive.driveCartesian(xPower, yPower, rotation, navx.getAngle());
        //printCurrentDraw();
        //printEncoderVal();
    }

    public void driveCartesianBackward(double xPower, double yPower, double rotation) {
        mecDrive.driveCartesian((-1*(xPower)), (-1*(yPower)), rotation);
    }
    
    public void drivePolar(double magnitude, double angle, double rotation) {
        mecDrive.drivePolar(magnitude, angle, rotation);
    }
    
    public void drivePolar(double magnitude, Direction direction, double rotation) {
        drivePolar(magnitude, direction.degrees, rotation);
    }

    public void driveStraightConsistent(double angle) {
        completeControllerNav.setSetpoint(0);
        mecDrive.driveCartesian(-0.3, 0, pidOutputN.get());
        printEncoderVal();
    }

    public void driveStrafeConsistent(double angle) {
        completeControllerNav.setSetpoint(angle);
        mecDrive.driveCartesian(0.5, 0, pidOutputN.get());
    }
    
    public void driveConsistent(double x, double y, double angle) {
        completeControllerNav.setSetpoint(angle);
        mecDrive.driveCartesian(x, y, pidOutputN.get());
        //printEncoderVal();
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

    public void printLidarDistance()
    {
        System.out.println("Lidar: " + lidar.getDistance());
    }

    public void printCurrentDraw() {
        System.out.println("fl: " + frontLeft.getOutputCurrent());
        System.out.println("bl: " + backLeft.getOutputCurrent());
        System.out.println("fr: " + frontRight.getOutputCurrent());
        System.out.println("br: " + backRight.getOutputCurrent()); 
    }

    public void printEncoderVal() {
        System.out.println("fl: " + Math.abs(frontLeft.getSelectedSensorVelocity()));
        System.out.println("bl: " + Math.abs(backLeft.getSelectedSensorVelocity()));
        System.out.println("fr: " + Math.abs(frontRight.getSelectedSensorVelocity()));
        System.out.println("br: " + Math.abs(backRight.getSelectedSensorVelocity()));
    }
}