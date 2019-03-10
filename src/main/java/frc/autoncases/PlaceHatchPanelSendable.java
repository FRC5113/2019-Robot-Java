package frc.autoncases;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class PlaceHatchPanelSendable implements Sendable{

    PlaceHatchPanel hatchPanel;
    
    public PlaceHatchPanelSendable(PlaceHatchPanel p) {
        hatchPanel = p;
    }

    public String getState() {
        String result = "";/*
        switch (hatchPanel.getState()) {
            case -2:
                result = "Setting the Setpoint: ";
                break;
            case -1:
                result = "Strafing to be aligned with the target: ";
                break;

            case 0:
                result = "Using Lidar, going to target: ";
                break;
            case 1:
                result = "Ready to place hatch panel, press Y NOW: ";
                break;
        }*/
        return result;
    }

    @Override
    public String getName() {
        return "State";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getSubsystem() {
        return "PlaceHatchPanelPID";
    }

    @Override
    public void setSubsystem(String subsystem) {

    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addStringProperty("Current State", this::getState, null);
    }
}