package frc.handlers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.autoncases.PlaceHatchPanel;
import frc.subsystems.DriveTrain;

public class VisionHandler {
    private int X_RESOLUTION;

    private NetworkTable nettab;
    private VisionTarget target;

    private PlaceHatchPanel hatchAuton;
    
    public VisionHandler() {
        nettab = NetworkTableInstance.getDefault().getTable("contoursReport");   
        X_RESOLUTION = (int) nettab.getEntry("X_RESOLUTION").getDouble(-1);
        hatchAuton = new PlaceHatchPanel(null);
    }

    public void updateVisionTarget()
    {
        if(X_RESOLUTION == -1)
            X_RESOLUTION = (int) nettab.getEntry("X_RESOLUTION").getDouble(-1);
        
        if(nettab.getEntry("targetDetected").getBoolean(false)) {
            int xCoord = (int) nettab.getEntry("xCoord").getDouble(-1);
            int angle = 2 + (int) nettab.getEntry("angle").getDouble(-1); // in inches

            // would it be better to instead have a method for updating these values, so that I don't
            // instantiate a new object every loop? Would that make it faster, or is it negligible?
            target = new VisionTarget(xCoord, angle, X_RESOLUTION);
        } else
            target = null;
    }

    public void placeHatchPanel(DriveTrain dt) {
        hatchAuton.update(dt, target); // implement more vision autons?
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