package frc.subsystems;

import edu.wpi.first.wpilibj.Solenoid;

public class Climber
{
    private Solenoid front;
    private Solenoid back;

    public Climber ()
    {
        // front = new Solenoid (3);
        // back = new Solenoid (4);
    }

    public void setFront(boolean isExtended)
    {
        front.set(isExtended);
    }

    public void setBack(boolean isExtended)
    {
        back.set(isExtended);
    }

    public void toggleFront()
    {
        front.set(!front.get());
    }

    public void toggleBack()
    {
        back.set(!back.get());
    }
}