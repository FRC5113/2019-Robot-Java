package frc.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Elevator 
{
    private WPI_TalonSRX elevator = new WPI_TalonSRX(11);
    private ElevatorController controller = new ElevatorController();
    private final ElevatorEncoder elevatorEncoder = new ElevatorEncoder(elevator);
    private PIDController elevatorPID = new PIDController(0.004, 0, 0.001, elevatorEncoder, elevator); // this is most likely wrong
    private final int ELEVATOR_ENCODER_THRESHOLD = 100;
    
    public enum Level {
        ONE(0), TWO(25000), THREE(50000); // these are the enocder values that correspond
                                    // to the different rocket levels
        public int encoderValue;

        private Level(int encoderValue) {
            this.encoderValue = encoderValue;

            
        }
    }

    public Elevator() {
        elevator.setNeutralMode(NeutralMode.Brake);
        elevator.setInverted(true);
        elevator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        elevator.setSelectedSensorPosition(0);
        //elevator.setSensorPhase(false);

        elevatorPID.setSetpoint(0);
        elevatorPID.setOutputRange(-0.9, 0.9);
        elevatorPID.enable();

        ShuffleboardTab SensorTab = Shuffleboard.getTab("Sensors");
        SensorTab.add("Elevator Counts", elevatorEncoder);

        ShuffleboardTab PIDTab = Shuffleboard.getTab("PID");
        PIDTab.add("Elevator PID", elevatorPID);

        ShuffleboardTab MotorTab = Shuffleboard.getTab("Motors");
        MotorTab.add("Elevator", elevator);
    }
    
    public void lift(double power) {
        elevator.set(power);
    }

    public void reset()
    {
        elevator.set(0);
        elevator.setSelectedSensorPosition(0);
        
        elevatorPID.setSetpoint(0);
        elevatorPID.reset();
    }

    public void liftToLevel(Level level) {
        if(level.encoderValue != elevatorPID.getSetpoint())
        {
            System.out.println("Changing height to " + level.encoderValue);
            elevatorPID.reset();
            elevatorPID.enable();
        }

        elevatorPID.setSetpoint(level.encoderValue);
    }
}