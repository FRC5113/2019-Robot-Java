package frc.handlers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.autoncases.PlaceHatchPanel;
import frc.subsystems.DriveTrain;
import frc.subsystems.HatchIntake;

public class VisionHandler implements Sendable {
    private int X_RESOLUTION = 426;

    private NetworkTable nettab;
    private VisionTarget target;

    private PlaceHatchPanel hatchAuton;
    
    public VisionHandler() {
        // This grabs our table that we wrote to on the RaspberryPi
        nettab = NetworkTableInstance.getDefault().getTable("contoursReport");   
        target = null;

        // The X_RESOLUTION is passed from the RPi so that we know
        // how to split the frame into zones.
        hatchAuton = new PlaceHatchPanel(null);
    }

    public void updateVisionTarget() {        
        if(nettab.getEntry("targetDetected").getBoolean(false)) {
            int xCoord = (int) nettab.getEntry("xCoord").getDouble(-1);
            double angle = nettab.getEntry("angle").getDouble(-1);

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
        if(target != null){
            System.out.printf("xCoord: %d (/%d)\n" +
                                "angle: %d\n",
                                target.getXCoord(), X_RESOLUTION, target.getAngle());  
            System.out.println("Target Found!!!!!!!!!!!!");               
        }else
            System.out.println("no target found");
    }

    public boolean isTarget() {
        if (target == null) 
            return false;
        else
            return true;
    }

    @Override
    public String getName() {
        return "Vision";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getSubsystem() {
        return "Info";
    }

    @Override
    public void setSubsystem(String subsystem) {

    }

    public double getXCoord() {
        if(target == null)
            return -1;
        else
            return target.getXCoord();
    }

    public double getAngle() {
        if(target == null)
            return -1;
        else
            return target.getAngle();
    }

    public double getXRes() {
        if(target == null)
            return -1;
        else
            return target.getXRes();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Target_XCoord", this::getXCoord, null);
        builder.addDoubleProperty("Target_Angle", this::getAngle, null);
        builder.addDoubleProperty("Target_XRes", this::getXRes, null);
        builder.addBooleanProperty("TargetThere", this::isTarget, null);
	}
}