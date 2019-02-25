package frc.handlers;

import java.util.concurrent.locks.ReentrantLock;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class SpeedControlPID implements PIDSource, PIDOutput
{
    private double m_dStateVal;
    private ReentrantLock m_mutex = new ReentrantLock();

    public SpeedControlPID(double initVal)
    {
        m_mutex.lock();
        try
        {m_dStateVal = initVal;}
        finally
        {m_mutex.unlock();}
    }

    @Override
    public void pidWrite(double output) {
        m_mutex.lock();
        try
        {m_dStateVal = output;}
        finally
        {m_mutex.unlock();}
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {

    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
        double dCopy = 0;

        m_mutex.lock();
        try
        {dCopy = m_dStateVal;}
        finally
        {m_mutex.unlock();}
        
        return dCopy;
	}
}