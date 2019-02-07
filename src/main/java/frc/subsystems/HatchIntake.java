package frc.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class HatchIntake
{
    private Compressor compressor;
    private Solenoid deployer;

    public HatchIntake ()
    {
        compressor = new Compressor();
        // deployer = new Solenoid (0);

        compressor.setClosedLoopControl(true);
    }

    public void deploy()
    {
        
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
