
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.shooter.launch_calculator.LaunchCalculator;
import frc.robot.subsystems.swervedrive.SwerveDriveSubsystem;
import frc.robot.utils.AutoHelper;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;

  public Robot() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();

    LaunchCalculator.getInstance().clearLaunchingParameters();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {
    String selectedAutoName = m_robotContainer.getAutonomousCommand().getName();

    if (!selectedAutoName.equals("InstantCommand")) {
      AutoHelper.publishAutoPath(m_robotContainer.getAutonomousCommand().getName());
    }
  }

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    teleopAndAutonomousInit();
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }

    SwerveDriveSubsystem.getInstance().zeroGyroWithAlliance();
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    teleopAndAutonomousInit();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  private void teleopAndAutonomousInit() {

    CommandScheduler.getInstance().schedule(m_robotContainer.stopAllSubsystems());

    CommandScheduler.getInstance().schedule(m_robotContainer.setFlywheelDefaultSpeed());
  }

  @Override
  public void simulationPeriodic() {
    m_robotContainer.publishComponentPoses();
    SmartDashboard.putBoolean("passing", LaunchCalculator.getInstance().getParameters().passing());
    LaunchCalculator.getInstance().publishLookaheadRobotPose();
  }
}
