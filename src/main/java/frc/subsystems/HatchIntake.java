package frc.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class HatchIntake
{
    private Compressor compressor = new Compressor();;
    private DoubleSolenoid deployer = new DoubleSolenoid(0, 1);;
    private long initialHatchDeployTime = 0;
    private final int DEPLOY_DURATION = 500; // how long (in milliseconds) it should stay deployed

    public HatchIntake() {
        deployer.set(Value.kReverse);

        // setClosedLoopControl is the way you turn on/off the compressor.
        // It will automatically turn off at a certain psi.
        compressor.setClosedLoopControl(true);
    }

    public void deploy() {
        deployer.set(Value.kForward);
        initialHatchDeployTime = System.currentTimeMillis();
        // we save the time so that it can be retracted soon after
    }

    // this is called every 20 ms in teleopPeriodic and retracts the pistols
    // if they have been extended for a given duration.
    public void retractIfExtended() {
        if(this.get() && System.currentTimeMillis() - initialHatchDeployTime > DEPLOY_DURATION)
            deployer.set(Value.kReverse);
    }

    // Because the deployer can either be kOff, kReverse, or kForward,
    // I have decided that "true" should mean it is equal to kForward.
    public boolean get() {
        return deployer.get() == Value.kForward;
    }
    
    public Compressor getCompressor() {
        return compressor;
    }

    public void toggleCompressor() {
        compressor.setClosedLoopControl(!compressor.getClosedLoopControl());
    }
}
