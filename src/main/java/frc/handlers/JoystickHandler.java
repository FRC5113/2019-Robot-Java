package frc.handlers;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.XboxController;
import frc.subsystems.CargoIntake;
import frc.subsystems.Climber;
import frc.subsystems.DriveTrain;
import frc.subsystems.Elevator;
import frc.subsystems.HatchIntake;

public class JoystickHandler {
    private final XboxController driverStick = new XboxController(0);
    private final XboxController xboxDriver = new XboxController(1);

    private final double DRIVE_THRESHOLD = 0.125;
    private final double xCalibration, yCalibration, zCalibration;

    private final double SPEED = 0.5;

    private PIDController xPIDControl, yPIDControl, zPIDControl;
    private SpeedControlPID xLoop, yLoop, zLoop;

    private int oldPOV;

    public JoystickHandler() {
        xCalibration = xboxDriver.getX(Hand.kLeft);
        yCalibration = xboxDriver.getY(Hand.kLeft);
        zCalibration = xboxDriver.getX(Hand.kRight);

        xLoop = new SpeedControlPID(0);
        yLoop = new SpeedControlPID(0);
        zLoop = new SpeedControlPID(0);

        xPIDControl = new PIDController(0, 0.2, 0.05, xLoop, xLoop);
        yPIDControl = new PIDController(0, 0.4, 0.05, yLoop, yLoop);
        zPIDControl = new PIDController(0, 0.4, 0.05, zLoop, zLoop);

        xPIDControl.setSetpoint(0);
        yPIDControl.setSetpoint(0);
        zPIDControl.setSetpoint(0);

        xPIDControl.enable();
        yPIDControl.enable();
        zPIDControl.enable();

        oldPOV = -1;
    }

    // This method gets called every 20 milliseconds in teleopPeriodic,
    // and handles all of the teleop controls of the bot.

    /* List of all of the used Buttons:

    Up on the D-Pad    --> Lift the Elevator
    Right on the D-Pad --> Vision (Place Hatch Panel)
    Down on the D-Pad  --> Lower the Elevator
    Left on the D-Pad  --> DriveCartesianFOD

    Y  --> Deploy the Hatch
    B  --> Lift the 


    */
    public void enabledUpdate(DriveTrain driveTrain, HatchIntake hatchIntake, CargoIntake cargoIntake,
        Climber climber, Elevator elevator, VisionHandler visionHandler) {

        // Driving

        if(xboxDriver.getPOV() == 270) {
            if (xboxDriver.getPOV() != oldPOV)
                visionHandler.resetAutonState();

            visionHandler.placeHatchPanel(driveTrain, hatchIntake);
        } else {
            double xAxis = Math.abs(driverStick.getX(Hand.kLeft) - xCalibration) > DRIVE_THRESHOLD? xboxDriver.getX(Hand.kLeft) * SPEED : 0;
            double yAxis = Math.abs(driverStick.getY(Hand.kLeft) - yCalibration) > DRIVE_THRESHOLD? -xboxDriver.getY(Hand.kLeft) * SPEED : 0;
            double zAxis = Math.abs(driverStick.getX(Hand.kRight) - zCalibration) > DRIVE_THRESHOLD? xboxDriver.getX(Hand.kRight) * SPEED : 0;
    
            xPIDControl.setSetpoint(xAxis);
            yPIDControl.setSetpoint(yAxis);
            zPIDControl.setSetpoint(zAxis);

            driveTrain.resetNavxAngle();

            //driveTrain.driveStraightConsistent(0);

            if(xboxDriver.getPOV() == 270){
                //driveTrain.driveCartesianFOD(0, 0.25, zAxis);
                driveTrain.driveCartesianFOD(xLoop.pidGet() * 1.5, yLoop.pidGet() * -1.5, zLoop.pidGet());
            }else{
                driveTrain.driveCartesian(xLoop.pidGet() * 1.5, yLoop.pidGet() * -1.5, zLoop.pidGet());
                //driveTrain.driveCartesian(0, 0.25, zAxis);
            }

            if(xboxDriver.getStartButton())
                driveTrain.driveCartesianBackward(xAxis, yAxis, zAxis);
        }
        oldPOV = xboxDriver.getPOV();
    
        // Cargo

        //driveTrain.printLidarDistance();

        if(xboxDriver.getXButton())
            cargoIntake.spinIntake(0.4);
        else if(xboxDriver.getAButton())
            cargoIntake.spinIntake(-1);
        else
            cargoIntake.spinIntake(0);

        if(xboxDriver.getBButtonPressed())
            cargoIntake.toggleLift();
        
        // Hatch

        if (xboxDriver.getYButtonPressed()) {
            hatchIntake.deploy();
        }

        if(xboxDriver.getBackButtonPressed())
            hatchIntake.toggleCompressor();

        // Elevator

        if(xboxDriver.getBumper(Hand.kRight))
            elevator.lift(0.8);
        else if(xboxDriver.getBumper(Hand.kLeft))
            elevator.lift(-0.6);
        else if(xboxDriver.getPOV() > 180 || xboxDriver.getPOV() < 0)
            elevator.lift(0.1);

        // Once the PID is written, we use these elevator controls: (I will change the driving dpad controls)
        if(xboxDriver.getPOV() == 0) // DPAD UP
            elevator.liftToLevel(Elevator.Level.THREE);
        else if(xboxDriver.getPOV() == 90) // DPAD RIGHT
            elevator.liftToLevel(Elevator.Level.TWO);
        else if(xboxDriver.getPOV() == 180) // DPAD DOWN
            elevator.liftToLevel(Elevator.Level.ONE);
    }

    public void printJoystickInfo() {
        double xAxis = Math.abs(xboxDriver.getX(Hand.kLeft) - xCalibration) > DRIVE_THRESHOLD? xboxDriver.getX(Hand.kLeft) * SPEED : 0;
        double yAxis = Math.abs(xboxDriver.getY(Hand.kLeft) - yCalibration) > DRIVE_THRESHOLD? -xboxDriver.getY(Hand.kLeft) * SPEED : 0;
        double zAxis = Math.abs(xboxDriver.getX(Hand.kRight) - zCalibration) > DRIVE_THRESHOLD? xboxDriver.getX(Hand.kRight) * SPEED : 0;

        System.out.printf("(%.2f, %.2f, %.2f)\n", xAxis, yAxis, zAxis);
        
    }
}