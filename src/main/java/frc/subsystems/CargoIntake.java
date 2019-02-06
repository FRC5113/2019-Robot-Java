package frc.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class CargoIntake {
    private WPI_TalonSRX intake = new WPI_TalonSRX(7);
    private DoubleSolenoid lift = new DoubleSolenoid(6, 7);

    private boolean lifted = false;

    public CargoIntake()
    {
        intake.setInverted(true);
    }

    public void spinIntake(double power) {
        intake.set(power);
    }

    public void toggleLift()
    {
        if(lifted)
            lift.set(Value.kReverse);
        else
            lift.set(Value.kForward);

        lifted = !lifted;
    }
}