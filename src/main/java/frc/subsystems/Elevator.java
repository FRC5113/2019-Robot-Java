package frc.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Elevator 
{
    //private WPI_TalonSRX elevator = new WPI_TalonSRX(11);
    //private ElevatorController controller = new ElevatorController();
    private CANSparkMax motor = new CANSparkMax(30, MotorType.kBrushless);
    private CANEncoder motorEncoder = motor.getEncoder();
    private ElevatorController motorController = new ElevatorController();
    private ElevatorEncoder motorEncoderControllerImp = new ElevatorEncoder(motorEncoder);
    private ElevatorMotorSendable motorSendable = new ElevatorMotorSendable(motor);
    //private final ElevatorEncoder elevatorEncoder = new ElevatorEncoder(elevator);
    private PIDController motorPID = new PIDController(0.004, 0, 0.001, motorEncoderControllerImp, motor); // this is most likely wrong
    private final int ELEVATOR_ENCODER_THRESHOLD = 100;
    
    
    public enum Level {
        ONE(0), TWO(25000), THREE(50000); // these are the enocder values that correspond FIX THESE ENCODER VALUES
                                    // to the different rocket levels
        public int encoderValue;

        private Level(int encoderValue) {
            this.encoderValue = encoderValue;
        }
    }

    public Elevator() {
        //elevator.setNeutralMode(NeutralMode.Brake); //DONEREPLACED
        motor.setIdleMode(IdleMode.kBrake);
        //elevator.setInverted(true); //DONEREPLACED
        motor.setInverted(true);
        //elevator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative); //DONEREPLACED
        motorEncoder.setPosition(0);
        //elevator.setSelectedSensorPosition(0); //DONEREPLACED

        //elevator.setSensorPhase(false);

        motorPID.setSetpoint(0);
        motorPID.setOutputRange(-0.9, 0.9);
        motorPID.enable();

        ShuffleboardTab SensorTab = Shuffleboard.getTab("Sensors");
        SensorTab.add("Elevator Counts", motorEncoderControllerImp);

        ShuffleboardTab PIDTab = Shuffleboard.getTab("PID");
        PIDTab.add("Elevator PID", motorPID);

        ShuffleboardTab MotorTab = Shuffleboard.getTab("Motors");
        MotorTab.add("Elevator", motorSendable);
    }
    
    public void lift(double power) {
        motor.set(power);
    }

    public void reset()
    {
        motor.set(0);
        motorEncoder.setPosition(0);
        
        motorPID.setSetpoint(0);
        motorPID.reset();
    }

    public void liftToLevel(Level level) {
        if(level.encoderValue != motorPID.getSetpoint())
        {
            System.out.println("Changing height to " + level.encoderValue);
            motorPID.reset();
            motorPID.enable();
        }

        motorPID.setSetpoint(level.encoderValue);
    }
}