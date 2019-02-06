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

    public JoystickHandler() {
        xCalibration = xboxDriver.getX(Hand.kLeft);
        yCalibration = xboxDriver.getY(Hand.kLeft);
        zCalibration = xboxDriver.getX(Hand.kRight);
    }

    public void enabledUpdate(DriveTrain dt, HatchIntake hi, CargoIntake ci, Climber cb, Elevator el) {
        double xAxis = Math.abs(xboxDriver.getX(Hand.kLeft) - xCalibration) > DRIVE_THRESHOLD? xboxDriver.getX(Hand.kLeft) : 0;
        double yAxis = Math.abs(xboxDriver.getY(Hand.kLeft) - yCalibration) > DRIVE_THRESHOLD? -xboxDriver.getY(Hand.kLeft) : 0;
        double zAxis = Math.abs(xboxDriver.getX(Hand.kRight) - zCalibration) > DRIVE_THRESHOLD? xboxDriver.getX(Hand.kRight) : 0;

        dt.driveCartesian(xAxis, -yAxis, zAxis);

        if(xboxDriver.getXButton())
            ci.spinIntake(0.5);
        else if(xboxDriver.getAButton())
            ci.spinIntake(-1);
        else
            ci.spinIntake(0);

        if (xboxDriver.getBackButtonPressed()) //Button for turning compressor on or off is X
        {
            hi.compressorOnorOff();
        }

        if (xboxDriver.getBButtonPressed())
            ci.toggleLift();

        if(xboxDriver.getPOV() == 0)
            el.lift(1);
        else if(xboxDriver.getPOV() == 180)
            el.lift(-1);
        else
            el.lift(0);

        if(xboxDriver.getBumperPressed(Hand.kLeft))
            ci.toggleLift();
    }

    public void printJoystickInfo() {
        double xAxis = Math.abs(xboxDriver.getX(Hand.kLeft) - xCalibration) > 0.05? xboxDriver.getX(Hand.kLeft) : 0;
        double yAxis = Math.abs(xboxDriver.getY(Hand.kLeft) - yCalibration) > 0.05? -xboxDriver.getY(Hand.kLeft) : 0;
        double zAxis = Math.abs(xboxDriver.getX(Hand.kRight) - zCalibration) > 0.05? xboxDriver.getX(Hand.kRight) : 0;

        System.out.printf("(%.2f, %.2f, %.2f)\n", xAxis, yAxis, zAxis);
    }
}