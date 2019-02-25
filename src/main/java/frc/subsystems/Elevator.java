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
    private ElevatorMotorSendable elevatorMotorSendable = new ElevatorMotorSendable(elevator);
    private final ElevatorEncoder elevatorEncoder = new ElevatorEncoder(elevator);
    private PIDController elevatorPID = new PIDController(0.004, 0, 0.001, elevatorEncoder, elevator); // this is most likely wrong: MAX OF .05
    private final int ELEVATOR_ENCODER_THRESHOLD = 150;

    
    /*private CANSparkMax motor = new CANSparkMax(30, MotorType.kBrushless);
    private CANEncoder motorEncoder = motor.getEncoder();
    private ElevatorController motorController = new ElevatorController();
    private ElevatorEncoder motorEncoderControllerImp = new ElevatorEncoder(motorEncoder);*/
    
    
    public enum Level {
        ONE(0), TWO(25000), THREE(50000); // these are the enocder values that correspond FIX THESE ENCODER VALUES
                                    // to the different rocket levels
        public int encoderValue;

        private Level(int encoderValue) {
            this.encoderValue = encoderValue;
        }
    }

    public Elevator() {
        elevator.setNeutralMode(NeutralMode.Brake); //DONEREPLACED
        elevator.setInverted(true); //DONEREPLACED
        elevator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative); //DONEREPLACED
        elevator.setSelectedSensorPosition(0); //DONEREPLACED

        //elevator.setSensorPhase(false);

        elevatorPID.setAbsoluteTolerance(ELEVATOR_ENCODER_THRESHOLD);

        elevatorPID.setSetpoint(0);
        elevatorPID.setOutputRange(-0.9, 0.9);
        elevatorPID.enable();

        ShuffleboardTab SensorTab = Shuffleboard.getTab("Sensors");
        SensorTab.add("Elevator Counts", elevatorMotorSendable);

        ShuffleboardTab PIDTab = Shuffleboard.getTab("PID");
        PIDTab.add("Elevator PID", elevatorPID);

        ShuffleboardTab MotorTab = Shuffleboard.getTab("Motors");
        MotorTab.add("Elevator", elevatorMotorSendable);
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
        if(!elevatorPID.onTarget())
        {
            System.out.println("Changing height to " + level.encoderValue);
            elevatorPID.reset();
            elevatorPID.enable();
        }

        elevatorPID.setSetpoint(level.encoderValue);
    }
}