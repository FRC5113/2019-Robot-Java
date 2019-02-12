package frc.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class HatchIntake
{
    private Compressor compressor;
    private DoubleSolenoid deployer;
    private long x;

    public HatchIntake ()
    {
        compressor = new Compressor();
        deployer = new DoubleSolenoid(0, 1); // set the correct port later
        deployer.set(Value.kReverse);

        compressor.setClosedLoopControl(true);
    }

    public void set(boolean extended)
    {
        if(extended)
            deployer.set(Value.kForward);
        else
            deployer.set(Value.kReverse);

        System.out.println(deployer.get());
    }

    public boolean get() {
        System.out.println(deployer.get() == Value.kForward);
        return deployer.get() == Value.kForward;
    }
    
    public Compressor getCompressor()
    {
        return compressor;
    }

    public void toggleCompressor()
    {
        compressor.setClosedLoopControl(!compressor.getClosedLoopControl());
    }
}
