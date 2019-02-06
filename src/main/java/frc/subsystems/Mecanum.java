package frc.subsystems;

import edu.wpi.first.wpilibj.SpeedController;

public class Mecanum
{
    public final int FRONT_LEFT_INDEX = 0;
    public final int FRONT_RIGHT_INDEX = 1;
    public final int REAR_LEFT_INDEX = 2;
    public final int REAR_RIGHT_INDEX = 3;
    public final int TOTAL_MOTORS = 4;

    private SpeedController[] motors = new SpeedController[TOTAL_MOTORS];
    private double scales[] = {1.0, 1.0, 1.0, 1.0}; //index corrolates to motor in counter-clockwise pattern
    private double target[] = new double[TOTAL_MOTORS];

    public Mecanum(SpeedController frontLeftMotor, SpeedController frontRightMotor, SpeedController rearLeftMotor, SpeedController rearRightMotor)
    {
        motors[FRONT_LEFT_INDEX] = frontLeftMotor;
        motors[FRONT_RIGHT_INDEX] = frontRightMotor;
        motors[REAR_LEFT_INDEX] = rearLeftMotor;
        motors[REAR_RIGHT_INDEX] = rearRightMotor;
    }

    public void driveCartesian(double ySpeed, double xSpeed, double zRotation)
    {
        target[FRONT_RIGHT_INDEX] = (ySpeed-xSpeed-zRotation)/3;
        target[FRONT_LEFT_INDEX] = (ySpeed+xSpeed+zRotation)/3;
        target[REAR_RIGHT_INDEX] = (ySpeed+xSpeed-zRotation)/3;
        target[REAR_LEFT_INDEX] = (ySpeed-xSpeed+zRotation)/3;

        for(int i = 0; i < TOTAL_MOTORS; i++)
            motors[i].set(target[i] * scales[i]);
    }

    public void setInverse(int motor) //motors are in the same pattern as Cartesian quadrants
    {
        if(motor >= 0 && motor < TOTAL_MOTORS)
            motors[motor].setInverted(true);
    }

    public void setScale(int motor, double scale) //from -1.0 to 1.0
    {
        if (scale > 1.0)
            scale = 1.0;
        
        if (scale < 0)
            scale = 0;
            
        if(motor >= 0 && motor < TOTAL_MOTORS)
            scales[motor] = scale;
    }

    public double getScale(int motor)
    {
        return scales[motor];
    }

    public double getTarget(int motor)
    {
        return target[motor];
    }

    public SpeedController getMotor(int motor)
    {
        if(motor >= 0 && motor < TOTAL_MOTORS)
            return motors[motor];
        else
            return null;
    }

}