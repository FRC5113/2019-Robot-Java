package frc.handlers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.autoncases.PlaceHatchPanel;
import frc.subsystems.DriveTrain;
import frc.subsystems.HatchIntake;

public class VisionHandler {
    private int X_RESOLUTION = 426;

    private NetworkTable nettab;
    private VisionTarget target;

    private PlaceHatchPanel hatchAuton;
    
    public VisionHandler() {
        // This grabs our table that we wrote to on the RaspberryPi
        nettab = NetworkTableInstance.getDefault().getTable("contoursReport");   

        // The X_RESOLUTION is passed from the RPi so that we know
        // how to split the frame into zones.
        // X_RESOLUTION = (int) nettab.getEntry("X_RESOLUTION").getDouble(-1);
        hatchAuton = new PlaceHatchPanel(null);
    }

    public void updateVisionTarget() {
        // System.out.println("Latency Test: " + nettab.getEntry("latency test").getBoolean(false));
        // if(X_RESOLUTION == -1) // this allows us to only set the resolution once
        //     X_RESOLUTION = (int) nettab.getEntry("X_RESOLUTION").getDouble(-1);
        
        if(nettab.getEntry("targetDetected").getBoolean(false)) {
            int xCoord = (int) nettab.getEntry("xCoord").getDouble(-1);
            int angle = (int) nettab.getEntry("angle").getDouble(-1);

            // would it be better to instead have a method for updating these values, so that I don't
            // instantiate a new object every loop? Would that make it faster, or is it negligible?
            target = new VisionTarget(xCoord, angle, X_RESOLUTION);
        } else
            target = null;
    }

    public void placeHatchPanel(DriveTrain driveTrain, HatchIntake hatchIntake) {
        updateVisionTarget();
        hatchAuton.update(driveTrain, hatchIntake, target); // implement more vision autons?
    }

    public void printVisionInfo() {
        if(target != null)
            System.out.printf("xCoord: %d (/%d)\n" +
                                "angle: %d\n",
                                target.getXCoord(), X_RESOLUTION, target.getAngle());
        else
            System.out.println("no target found");
    }
}