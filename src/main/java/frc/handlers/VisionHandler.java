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
    }

    public void updateVisionTarget()
    {
        X_RESOLUTION = (int) nettab.getEntry("X_RESOLUTION").getDouble(-1);
        
        int xCoord = (int) nettab.getEntry("xCoord").getDouble(-1);
        int area = (int) nettab.getEntry("area").getDouble(-1);
        int distance = (int) nettab.getEntry("distance").getDouble(-1);

        target = new VisionTarget(xCoord, area, distance, X_RESOLUTION);
        // would it be better to instead have a method for updating these values, so that I don't
        // instantiate a new object every loop? Would that make it faster, or is it negligible?
    }

    public void update(DriveTrain dt) {
        hatchAuton.update(dt, target);
    }

    public String toString() {
        // String 

        return "";
    }
}