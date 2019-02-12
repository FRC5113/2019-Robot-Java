package frc.handlers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.subsystems.*;

public class JoystickHandler {
    private final Joystick driverStick = new Joystick(0);
    private final XboxController xboxDriver = new XboxController(1);

    private final double DRIVE_THRESHOLD = 0.1;
    private final double xCalibration, yCalibration, zCalibration;

    private final double SPEED = 0.5;

    public JoystickHandler() {
        xCalibration = xboxDriver.getX(Hand.kLeft);
        yCalibration = xboxDriver.getY(Hand.kLeft);
        zCalibration = xboxDriver.getX(Hand.kRight);
    }

    // This method gets called every 20 milliseconds in teleopPeriodic,
    // and handles all of the teleop controls of the bot.
    public void enabledUpdate(DriveTrain driveTrain, HatchIntake hatchIntake, CargoIntake cargoIntake,
        Climber climber, Elevator elevator, VisionHandler visionHandler) {

        // Driving

        if(xboxDriver.getPOV() == 90) {
            visionHandler.updateVisionTarget();
            visionHandler.placeHatchPanel(driveTrain, hatchIntake);
        } else {
            double xAxis = Math.abs(xboxDriver.getX(Hand.kLeft) - xCalibration) > DRIVE_THRESHOLD? xboxDriver.getX(Hand.kLeft) * SPEED : 0;
            double yAxis = Math.abs(xboxDriver.getY(Hand.kLeft) - yCalibration) > DRIVE_THRESHOLD? xboxDriver.getY(Hand.kLeft) * SPEED : 0;
            double zAxis = Math.abs(xboxDriver.getX(Hand.kRight) - zCalibration) > DRIVE_THRESHOLD? xboxDriver.getX(Hand.kRight) * SPEED : 0;
    
            if(xboxDriver.getPOV() == 270)
                driveTrain.driveCartesianFOD(xAxis, yAxis, zAxis);
            else
                driveTrain.driveCartesian(xAxis, yAxis, zAxis);
        }

        // Cargo

        if(xboxDriver.getXButton())

            cargoIntake.spinIntake(0.5);
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

        if(xboxDriver.getPOV() == 0)
            elevator.lift(1);
        else if(xboxDriver.getPOV() == 180)
            elevator.lift(-1);
        else
            elevator.lift(0);
    }

    public void printJoystickInfo() {
        double xAxis = Math.abs(xboxDriver.getX(Hand.kLeft) - xCalibration) > 0.05? xboxDriver.getX(Hand.kLeft) : 0;
        double yAxis = Math.abs(xboxDriver.getY(Hand.kLeft) - yCalibration) > 0.05? -xboxDriver.getY(Hand.kLeft) : 0;
        double zAxis = Math.abs(xboxDriver.getX(Hand.kRight) - zCalibration) > 0.05? xboxDriver.getX(Hand.kRight) : 0;

        System.out.printf("(%.2f, %.2f, %.2f)\n", xAxis, yAxis, zAxis);
    }
}