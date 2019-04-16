package frc.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Climber
{
    private DoubleSolenoid back = new DoubleSolenoid (0,1);
    private Boolean extended = false;

    public Climber() {
        back.set(Value.kReverse);
    }

    public void toggleBack() {
        if(extended)
            back.set(Value.kReverse);
        else
            back.set(Value.kForward);
        extended = !extended;
    }
}