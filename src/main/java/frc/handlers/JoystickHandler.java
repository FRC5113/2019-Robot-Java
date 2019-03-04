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
    private final XboxController driverController = new XboxController(0);
    private final XboxController subsystemController = new XboxController(1);
    //private final XboxController kidController = new XboxController(2);

    private final double DRIVE_THRESHOLD = 0.125;
    private final double xCalibration, yCalibration, zCalibration;

    private final double SPEED = 0.5;

    private PIDController xPIDControl, yPIDControl, zPIDControl;
    private SpeedControlPID xLoop, yLoop, zLoop;

    private int oldPOV;

    private int oldPOVElevator = -1;

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

        //xPIDControl.enable();
        //yPIDControl.enable();
        //zPIDControl.enable();

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
        System.out.println("The current POV is: " + subsystemController.getPOV());
        if(driverController.getPOV() == 270) {
            if (driverController.getPOV() != oldPOV)
                visionHandler.resetAutonState();

            visionHandler.placeHatchPanel(driveTrain, hatchIntake);
        } else {
            double xAxis = Math.abs(driverController.getX(Hand.kLeft) - xCalibration) > DRIVE_THRESHOLD? driverController.getX(Hand.kLeft) * SPEED : 0;
            double yAxis = Math.abs(driverController.getY(Hand.kLeft) - yCalibration) > DRIVE_THRESHOLD? driverController.getY(Hand.kLeft) * SPEED : 0;
            double zAxis = Math.abs(driverController.getX(Hand.kRight) - zCalibration) > DRIVE_THRESHOLD? driverController.getX(Hand.kRight) * SPEED : 0;
    
            xPIDControl.setSetpoint(xAxis);
            yPIDControl.setSetpoint(yAxis);
            zPIDControl.setSetpoint(zAxis);

            driveTrain.resetNavxAngle();

            //driveTrain.driveStraightConsistent(0);

            if(subsystemController.getPOV() == 270){
                //driveTrain.driveCartesianFOD(0, 0.25, zAxis);
                //driveTrain.driveCartesianFOD(xLoop.pidGet() * 1.5, yLoop.pidGet() * -1.5, zLoop.pidGet());
            }else{
                //driveTrain.driveCartesian(xLoop.pidGet() * 1.5, yLoop.pidGet() * -1.5, zLoop.pidGet());
                
                driveTrain.driveCartesian(xAxis*0.99, yAxis*0.99, zAxis*0.99);
            }

            if(driverController.getAButton()) // ask BEN
                driveTrain.driveCartesianBackward(xAxis, yAxis, zAxis);
        }
        oldPOV = subsystemController.getPOV();
    
        // Cargo

        //driveTrain.printLidarDistance();

        if(subsystemController.getAButton())
            cargoIntake.spinIntake(0.4);
        else if(subsystemController.getBButton())
            cargoIntake.spinIntake(-1);
        else
            cargoIntake.spinIntake(0);

        if(subsystemController.getYButtonPressed())
            cargoIntake.toggleLift();
        
        // Hatch

        if (subsystemController.getXButtonPressed()) {
            hatchIntake.deploy();
        }

        if(driverController.getBackButtonPressed())
            hatchIntake.toggleCompressor();


        // climber

        if(driverController.getBumperPressed(Hand.kRight)){
//            climber.toggleFront();
        }
        else if(driverController.getBumperPressed(Hand.kLeft))
//            climber.toggleBack();
        // Elevator
 
        if(subsystemController.getBumper(Hand.kRight)){
            elevator.lift(0.9);
            System.out.println("hiHere");
        }
        else if(subsystemController.getBumper(Hand.kLeft))
            elevator.lift(-0.5);
        else if(subsystemController.getPOV() > 180 || subsystemController.getPOV() < 0)
            elevator.lift(0.1);

        // Once the PID is written, we use these elevator controls: (I will change the driving dpad controls)
        if(subsystemController.getPOV() == 0) { // DPAD UP
            if(subsystemController.getPOV()!=oldPOVElevator)
                System.out.print("Changing level to three");
                oldPOVElevator = 0;
            elevator.liftToLevel(Elevator.Level.THREE);
        }
        else if(subsystemController.getPOV() == 90) { // DPAD RIGHT
            if(subsystemController.getPOV()!=oldPOVElevator)
                System.out.print("Changing level to two");
                oldPOVElevator = 90;
            elevator.liftToLevel(Elevator.Level.TWO);
        }
        else if(subsystemController.getPOV() == 180) { // DPAD DOWN
            if(subsystemController.getPOV()!=oldPOVElevator)
                System.out.print("Changing level to one");
                oldPOVElevator = 180;
            elevator.liftToLevel(Elevator.Level.ONE);
        }
    }

    public void printJoystickInfo() {
        double xAxis = Math.abs(subsystemController.getX(Hand.kLeft) - xCalibration) > DRIVE_THRESHOLD? subsystemController.getX(Hand.kLeft) * SPEED : 0;
        double yAxis = Math.abs(subsystemController.getY(Hand.kLeft) - yCalibration) > DRIVE_THRESHOLD? -subsystemController.getY(Hand.kLeft) * SPEED : 0;
        double zAxis = Math.abs(subsystemController.getX(Hand.kRight) - zCalibration) > DRIVE_THRESHOLD? subsystemController.getX(Hand.kRight) * SPEED : 0;

        System.out.printf("(%.2f, %.2f, %.2f)\n", xAxis, yAxis, zAxis);
        
    }
}