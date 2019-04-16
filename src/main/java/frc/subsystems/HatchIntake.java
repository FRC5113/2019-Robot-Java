package frc.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;


public class HatchIntake
{
    private Compressor compressor = new Compressor();;
    private Solenoid clamp = new Solenoid(4);
    private Solenoid deployer = new Solenoid(5);
    private Boolean cout = false;
    private Boolean fout = true;

    public HatchIntake() {
        deployer.set(false);
        clamp.set(false);
        // setClosedLoopControl is the way you turn on/off the compressor.
        // It will automatically turn off at a certain psi.
        compressor.setClosedLoopControl(true);
    }

    public void deployClamp() {
        clamp.set(cout);
        cout = !cout;
    }

    public void deployForward(){
        deployer.set(fout);
        fout = !fout;
    }
    
    public Compressor getCompressor() {
        return compressor;
    }

    public void toggleCompressor() {
        compressor.setClosedLoopControl(!compressor.getClosedLoopControl());
    }
}
