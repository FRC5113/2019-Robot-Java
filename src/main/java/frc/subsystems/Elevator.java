package frc.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Elevator 
{
    private WPI_TalonSRX elevator = new WPI_TalonSRX(11);
    
    public Elevator ()
    {
        elevator.configSelectedFeedbackSensor(FeedbackDevice.Analog);
    }

    public void lift (double power)
    {
        elevator.set(power);
    }
}