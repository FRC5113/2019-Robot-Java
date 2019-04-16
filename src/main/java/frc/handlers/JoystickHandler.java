package frc.handlers;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.XboxController;
import frc.subsystems.CargoIntake;
import frc.subsystems.Climber;
import frc.subsystems.DriveTrain;
import frc.subsystems.Elevator;
import frc.subsystems.HatchIntake;

public class JoystickHandler {
    private final XboxController driverController = new XboxController(0);
    private final XboxController subsystemController = new XboxController(1);
    
    private final double DRIVE_THRESHOLD = 0.125;
    private final double xCalibration, yCalibration, zCalibration;

    private final double SPEED = 0.5;

    private PIDController xPIDControl, yPIDControl, zPIDControl;
    private SpeedControlPID xLoop, yLoop, zLoop;

    private int oldPOV;
    //private boolean driveBackword = false;
    private boolean useEndDriving = false;

    private int oldPOVElevator = -1;

    private Boolean switched = true;
    private UsbCamera camera1;
    private UsbCamera camera2;
    private VideoSink server;

    public JoystickHandler() {

        xCalibration = driverController.getX(Hand.kLeft);
        yCalibration = driverController.getY(Hand.kLeft);
        zCalibration = driverController.getX(Hand.kRight);

        xLoop = new SpeedControlPID(0);
        yLoop = new SpeedControlPID(0);
        zLoop = new SpeedControlPID(0);

        xPIDControl = new PIDController(0, 0.2, 0.05, xLoop, xLoop);
        yPIDControl = new PIDController(0, 0.4, 0.05, yLoop, yLoop);
        zPIDControl = new PIDController(0, 0.4, 0.05, zLoop, zLoop);

        xPIDControl.setSetpoint(0);
        yPIDControl.setSetpoint(0);
        zPIDControl.setSetpoint(0);

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
 //       System.out.println("The current POV is: " + driverController.getPOV());
        if(driverController.getPOV() == 90) {
            // Using Vision Recognition
            visionHandler.updateVisionTarget();
            visionHandler.placeHatchPanel(driveTrain, hatchIntake);
        } else {
            // Using Joysticks
            double xAxis = Math.abs(driverController.getX(Hand.kLeft) - xCalibration) > DRIVE_THRESHOLD? driverController.getX(Hand.kLeft) * SPEED : 0;
            double yAxis = Math.abs(driverController.getY(Hand.kLeft) - yCalibration) > DRIVE_THRESHOLD? driverController.getY(Hand.kLeft) * SPEED : 0;
            double zAxis = Math.abs(driverController.getX(Hand.kRight) - zCalibration) > DRIVE_THRESHOLD? driverController.getX(Hand.kRight) * SPEED : 0;

            if(useEndDriving) {
                xAxis /= 3;
                yAxis /= 2;
                zAxis /= 3;
            }
    
            xPIDControl.setSetpoint(1.75 * xAxis);
            yPIDControl.setSetpoint(1.75 * yAxis);
            zPIDControl.setSetpoint(1.75 * zAxis);

            driveTrain.resetNavxAngle();
            driveTrain.driveCartesian(xAxis*1.6, yAxis*1.6, zAxis*1.6);
            //driveTrain.driveCartesian(xPIDControl.get(), yPIDControl.get(), zPIDControl.get());
            driveTrain.printEncoderVal();
            /*
            if(driveBackword)
                driveTrain.driveCartesian(xAxis*-1.5, yAxis*-1.5, zAxis*-1.5);
            else 
                driveTrain.driveCartesian(xAxis*1.5, yAxis*1.5, zAxis*1.5);

            if(driverController.getAButtonPressed())
                driveBackword = !driveBackword;
            */
        }
        oldPOV = driverController.getPOV();

        if(driverController.getBButtonPressed())
            useEndDriving = !useEndDriving;
    
        // Cargo

        if(subsystemController.getAButton())
            cargoIntake.spinIntake(-0.4);
        else if(subsystemController.getBButton())
            cargoIntake.spinIntake(0.6);
        else
            cargoIntake.spinIntake(0);

        if(subsystemController.getBumperPressed(Hand.kRight))
            cargoIntake.toggleLift();
        
        // Hatch

        if (subsystemController.getXButtonPressed()) {
            hatchIntake.deployClamp();
        }

        if (subsystemController.getYButtonPressed()) {
            hatchIntake.deployForward();
        }
        
        if(driverController.getBackButtonPressed())
            hatchIntake.toggleCompressor();


        // climber

        if(driverController.getXButtonPressed()){
            climber.toggleBack();
        }

        // Elevator
 
        if(driverController.getBumper(Hand.kRight)){
            elevator.lift(1);
        }
        else if(driverController.getBumper(Hand.kLeft))
            elevator.lift(-1);
        else{
            elevator.lift(0);
        }
        
//        System.out.println(elevator.getAngle());
    }

   /* public boolean driveState() {
        return driveBackword;
    }
    */
    public void printJoystickInfo() {
        double xAxis = Math.abs(subsystemController.getX(Hand.kLeft) - xCalibration) > DRIVE_THRESHOLD? subsystemController.getX(Hand.kLeft) * SPEED : 0;
        double yAxis = Math.abs(subsystemController.getY(Hand.kLeft) - yCalibration) > DRIVE_THRESHOLD? -subsystemController.getY(Hand.kLeft) * SPEED : 0;
        double zAxis = Math.abs(subsystemController.getX(Hand.kRight) - zCalibration) > DRIVE_THRESHOLD? subsystemController.getX(Hand.kRight) * SPEED : 0;

//        System.out.printf("(%.2f, %.2f, %.2f)\n", xAxis, yAxis, zAxis);
    }
}