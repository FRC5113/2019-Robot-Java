package frc.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class HatchIntake
{
    private Compressor compressor;
    private Solenoid deployer;

    
    public HatchIntake ()
    {
        // compressor = new Compressor();
        // deployer = new Solenoid (0);
    }

    public void deploy()
    {
        
    }
    
    public Compressor getCompressor ()
    {
        return compressor;
    }

    public void compressorOnorOff ()
    {
        if (compressor.enabled())
        {
            compressor.stop();
        }
        else if (!compressor.enabled())
        {
            compressor.start();
        }
    }

}
