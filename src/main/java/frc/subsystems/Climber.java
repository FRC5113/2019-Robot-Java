
package frc.subsystems;

import edu.wpi.first.wpilibj.Solenoid;

public class Climber
{
    private Solenoid front = new Solenoid (4);
    private Solenoid back = new Solenoid (5);

    public Climber() {
        front.set(false);
        back.set(false);
    }

    public void setFront(boolean isExtended) {
        front.set(isExtended);
    }

    public void setBack(boolean isExtended) {
        back.set(isExtended);
    }

    public void toggleFront() {
        front.set(!front.get());
    }

    public void toggleBack() {
        back.set(!back.get());
    }
}