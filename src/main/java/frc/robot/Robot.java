/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.handlers.AutonHandler;
import frc.handlers.JoystickHandler;
import frc.handlers.VisionHandler;
import frc.subsystems.DriveTrain;
import frc.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // Subsystems
  private final DriveTrain driveTrain = new DriveTrain();
  private final HatchIntake hatchIntake = new HatchIntake();
  private final CargoIntake cargoIntake = new CargoIntake();
  private final Climber climber = new Climber();
  private final Elevator elevator = new Elevator();

  // Handlers
  private final JoystickHandler controls = new JoystickHandler();
  private final AutonHandler autonHandler = new AutonHandler(driveTrain);
  private final VisionHandler visionHandler = new VisionHandler();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    autonHandler.disabledUpdate(); // find out how to only run this when the robot is disabled:

    // controls.printJoystickInfo();
    // driveTrain.printGyroAngle();
    visionHandler.printVisionInfo();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + autonHandler.getSelectedCase());
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    autonHandler.enabledUpdate();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // controls.enabledUpdate handles all controls
    controls.enabledUpdate(driveTrain, hatchIntake, cargoIntake, climber, elevator, visionHandler);

    hatchIntake.retractIfExtended(); // if the hatch was ever extende for any reason, this will retract it after a given duration.
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
