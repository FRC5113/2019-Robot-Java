package frc.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator 
{
    private WPI_TalonSRX elevator = new WPI_TalonSRX(11);
    private ElevatorController controller = new ElevatorController();
    private PIDController elevatorPID = new PIDController(0.5, 0, 0, controller, controller); // this is most likely wrong
    
    public enum Level {
        ONE(0), TWO(0), THREE(0);

        public int encoderValue;

        private Level(int encoderValue) {
            this.encoderValue = encoderValue;
        }
    }

    public Elevator() {
        elevator.configSelectedFeedbackSensor(FeedbackDevice.Analog); // did this change to a MAG encoder?
        elevator.setNeutralMode(NeutralMode.Brake);
        elevator.setInverted(true);

        SmartDashboard.putData(elevatorPID);
    }

    public void lift(double power) {
        elevator.set(power);
    }

    public void liftToLevel(Level level) {
        switch(level) {
            case ONE:
                elevatorPID.pidWrite(elevator.getSelectedSensorPosition());
                break;
            case TWO:
                break;
            case THREE:
                break;
        }
    }
}