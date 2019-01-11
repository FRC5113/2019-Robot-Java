package frc.handlers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class VisionHandler {
    private final int X_RESOLUTION;

    private NetworkTable nettab;
    private VisionTarget target;
    
    public VisionHandler() {
        nettab = NetworkTableInstance.getDefault().getTable("contoursReport");        
    
        X_RESOLUTION = 100; // this will be changed to a resolution that is received from the network table
        
        int numTargetsFound = (int) nettab.getEntry("numTargetsFound").getDouble(-1);
        
        if(numTargetsFound > 0) {
            int xCoord = (int) nettab.getEntry("xCoord").getDouble(-1);
            int area = (int) nettab.getEntry("area").getDouble(-1);
            int distance = (int) nettab.getEntry("distance").getDouble(-1);

            target = new VisionTarget(xCoord, area, numTargetsFound, distance, X_RESOLUTION);
        } else
            System.out.println("No targets are found!");
    }
}