package frc.handlers;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.autoncases.Case;
import frc.autoncases.Direction;
import frc.subsystems.DriveTrain;

public class AutonHandler {
    private final DriveTrain dt;

    private Case selectedCase = Case.DRIVE_FORWARD;
    private final SendableChooser<String> autonChooser = new SendableChooser<>();

    public AutonHandler(DriveTrain dt) {
        this.dt = dt;

        SmartDashboard.putData("Auto choices", autonChooser);

        for(Case c : Case.values())
        autonChooser.addOption(c.toString(), c.toString());
    }

    public void disabledUpdate() {
        selectedCase = Case.DRIVE_FORWARD; 
    }

    public void enabledUpdate() {
        switch(selectedCase) {
        case DRIVE_FORWARD:
            dt.drivePolar(1, Direction.FORWARD, 0);
            break;
        case VISION_RECOGNITION:
            break;
        }
    }

    public Case getSelectedCase() {
        return selectedCase;
    }
}