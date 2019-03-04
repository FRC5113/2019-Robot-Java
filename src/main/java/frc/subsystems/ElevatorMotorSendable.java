package frc.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class ElevatorMotorSendable implements Sendable {

    WPI_TalonSRX motor;

    public ElevatorMotorSendable(WPI_TalonSRX motor) {
        this.motor = motor;
    }

    public double getSpeed() {
        return motor.get();
    }

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getSubsystem() {
        return "Elevator";
    }

    @Override
    public void setSubsystem(String subsystem) {

    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Speed", this::getSpeed, null);
	}
}