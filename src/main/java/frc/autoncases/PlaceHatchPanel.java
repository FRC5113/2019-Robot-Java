package frc.autoncases;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.handlers.VisionTarget;
import frc.handlers.VisionTarget.Zone;
import frc.subsystems.DriveTrain;
import frc.subsystems.HatchIntake;
import frc.subsystems.LIDARLite;
import frc.subsystems.PIDoutputImp;

public class PlaceHatchPanel {
	private final int ANGLE_TOLERANCE = 3;
	private VisionTarget target;
	private Zone oldDirection = Zone.LEFT1;
	private Zone newDirection = Zone.LEFT1;

	private VisionTargetAngle targetAngle;
	private PIDoutputImp angleOutput;

	private VisionTargetStrafation targetStrafation;
	private PIDoutputImp strafationOutput;

	private PIDController rotation;
	private PIDController strafation;

	private int isItMoving;
	private int rotationState;
	private int strafationState;

	private AHRS navx = new AHRS(SPI.Port.kMXP);

	private PIDoutputImp pidOutputN = new PIDoutputImp();
    private PIDoutputImp pidOutputL = new PIDoutputImp();
    private LIDARLite lidar = new LIDARLite(I2C.Port.kOnboard);
    private PIDController completeControllerNav = new PIDController(0.1, 0, 0, navx, pidOutputN);
    private PIDController completeControllerLidar = new PIDController(0.1, 0, 0, lidar, pidOutputL);


	public PlaceHatchPanel(VisionTarget target) {
		this.target = target;
		targetAngle = new VisionTargetAngle(target);
		angleOutput = new PIDoutputImp();
		targetStrafation = new VisionTargetStrafation(target);
		strafationOutput = new PIDoutputImp();


		rotation = new PIDController(0.1, 0, 0, targetAngle, angleOutput);
		strafation = new PIDController(0.1, 0, 0, targetStrafation, strafationOutput);
		rotation.setPercentTolerance(.5);
		strafation.setPercentTolerance(.5);
		isItMoving = -1;
		rotationState = -1; // might need to be -2
		strafationState = -1;
		
		strafation.setOutputRange(-0.9, 0.9);

		ShuffleboardTab PIDTab = Shuffleboard.getTab("PID");
		PIDTab.add("Strafe", strafation);
		PIDTab.add("Rotation", rotation);
	}

	// returns whether or not it should have been placed
	public boolean update(DriveTrain driveTrain, HatchIntake hatchIntake) {		
		if(target == null) {
			System.out.println("No target found!!!!");
			driveTrain.driveCartesian(0, 0, 0);
		// } else if (target.getAngle() < -ANGLE_TOLERANCE) {
		// 	driveTrain.driveCartesian(0.3, 0, -0.1);
		// 	System.out.println("rotate left");
		// } else if (target.getAngle() > ANGLE_TOLERANCE) {
		// 	driveTrain.driveCartesian(-0.3, 0, 0.1);
		// 	System.out.println("rotate right");
		} else {
			switch(rotationState) {
			case -2:
				rotation.setSetpoint(0);
				strafation.setSetpoint((target.getXRes()/2));

				rotationState = -1;
				System.out.println("State change: " + rotationState);
				break;
			case -1:
				driveTrain.driveCartesian(strafationOutput.get(), 0, 0/*angleOutput.get()*/);
				System.out.println("Strafe output " + strafationOutput.get());
				if(/*rotation.onTarget() && */strafation.onTarget())
				{
					rotationState = 0;
					strafation.disable();
					System.out.println("State change: " + rotationState);
				}
				break;

			case 0:
				driveTrain.driveCartesian(0, pidOutputL.get(), pidOutputN.get());
				if(completeControllerLidar.onTarget() && completeControllerNav.onTarget())
				{
					rotationState = 1;
					System.out.println("State change: " + rotationState);
				}
				break;

			case 1:
				break;
			}
			/*
			oldDirection = newDirection;
			newDirection = target.getZone();
	
			if (newDirection != oldDirection && newDirection == Zone.CENTER){
				driveTrain.resetNavxAngle();
			}

			if(true) { // get distance from lidar and make sure it is at least like 15 or something
				switch(target.getZone()) {
				case LEFT1:
					driveTrain.drivePolar(0.55, Direction.LEFT, 0);
					System.out.println("strafe left");
					return false;
				case LEFT2:
					driveTrain.drivePolar(0.35, Direction.LEFT, 0);
					System.out.println("strafe left");
					return false;
				case LEFT3:
					driveTrain.drivePolar(0.25, Direction.LEFT, 0);
					System.out.println("strafe left");
					return false;
				case CENTER:
					driveTrain.driveStraightConsistentDistance(0, 50);
					System.out.println("go forward");
					return false;
				case RIGHT3:
					driveTrain.drivePolar(0.25, Direction.RIGHT, 0);
					System.out.println("strafe right");
					return false;
				case RIGHT2:
					driveTrain.drivePolar(0.35, Direction.RIGHT, 0);
					System.out.println("strafe right");
					return false;
				case RIGHT1:
					driveTrain.drivePolar(0.55, Direction.RIGHT, 0);
					System.out.println("strafe right");
					return false;
				}
			} else {
				driveTrain.driveCartesian(0, 0, 0);
				hatchIntake.deploy();
				return true;
			}
			*/
		}

		return false;
	}

	public boolean update(DriveTrain driveTrain, HatchIntake hatchIntake, VisionTarget newTarget) {
		target = newTarget;
		return update(driveTrain, hatchIntake);
	}

	public void resetState()
	{
		rotationState = -2;
		strafation.enable();
	}
} 