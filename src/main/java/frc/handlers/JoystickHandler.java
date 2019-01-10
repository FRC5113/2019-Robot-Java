package frc.handlers;

import edu.wpi.first.wpilibj.Joystick;
import frc.subsystems.DriveTrain;

public class JoystickHandler {
    private final Joystick driverStick;

    public JoystickHandler() {
        driverStick = new Joystick(0);
    }

    public void enabledUpdate(DriveTrain dt) {
        dt.driveCartesian(driverStick.getX(), driverStick.getY(), driverStick.getZ());
    }
}