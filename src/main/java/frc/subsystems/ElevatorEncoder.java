package frc.subsystems;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class ElevatorEncoder implements Sendable, PIDSource {
    
    private WPI_TalonSRX motor;

    public ElevatorEncoder(WPI_TalonSRX motor) {
        this.motor = motor;
    }
    
    public double getcounts()
    {
        return (motor.getSelectedSensorPosition());
    }

	public double getspeed()
	{
		return motor.getSelectedSensorVelocity();
	}

    @Override
	public String getName()
	{
		return "Counts";
	}

	@Override
	public String getSubsystem()
	{
		return "Elevator";
	}

	@Override
	public void setName(String name)
	{}

	@Override 
	public void setName(String subsystem, String name)
	{}

	@Override
	public void setSubsystem(String subsystem) 
	{}

	@Override
	public void initSendable(SendableBuilder builder)
	{
		builder.addDoubleProperty("Counts", this::getcounts, null);
		builder.addDoubleProperty("Speed", this::getspeed, null);
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {

	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		return getcounts();
	}

}