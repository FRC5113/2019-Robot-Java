package frc.handlers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.subsystems.DriveTrain;

public class JoystickHandler {
    private final Joystick driverStick;

    private final JoystickButton left, right, forward, reverse;
    private final JoystickButton useJoystick, slow;

    private final JoystickButton useVisionRecognition;

    public JoystickHandler() {
        driverStick = new Joystick(0);

        left = new JoystickButton(driverStick, 3);
        right = new JoystickButton(driverStick, 4);
        forward = new JoystickButton(driverStick, 5);
        reverse = new JoystickButton(driverStick, 6);
        
        useJoystick = new JoystickButton(driverStick, 12);
        slow = new JoystickButton(driverStick, 11);

        useVisionRecognition = new JoystickButton(driverStick, 8);
    }

    public void enabledUpdate(DriveTrain dt, VisionHandler vh) {
        double power = slow.get()? 0.15 : 0.5;

        if(useJoystick.get())
            dt.driveCartesian(driverStick.getX(), driverStick.getY() * -1, Math.abs(driverStick.getZ()) > 0.25? driverStick.getZ() : 0);
        else if(useVisionRecognition.get()) {
            vh.updateVisionTarget();
            vh.update(dt);
        }
        else {
            if(left.get())
                dt.driveCartesian(-power, 0, 0);
            else if(right.get())
                dt.driveCartesian(power, 0, 0);
            else if(forward.get())
                dt.driveCartesian(0, power, 0);
            else if(reverse.get())
                dt.driveCartesian(0, -power, 0);
            else
                dt.driveCartesian(0, 0, 0);

        }
    }
}