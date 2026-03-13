// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Tracking;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class DriveAroundTheTag extends Command {
  /** Creates a new DriveAroundTheTag. */
  private Tracking tracking;
  private CommandSwerveDrivetrain dt;
  private Command driveRequestCmd;
  private Joystick DL;

  public DriveAroundTheTag(Tracking tracking, CommandSwerveDrivetrain dt, Joystick DL) {
    this.tracking = tracking;
    this.dt = dt;
    this.DL = DL;
    addRequirements(tracking);
    // Create a drive-request command that will be scheduled/cancelled while this
    // monitoring command is active. The rotational rate is computed each time
    // from the Tracking helper so it updates dynamically.
    this.driveRequestCmd = this.dt.applyRequest(() ->
        this.dt.getField()
            .withVelocityX(-this.DL.getY() * Constants.dt.max_speed)
            .withVelocityY(-this.DL.getX() * Constants.dt.max_speed)
            .withRotationalRate(Tracking.limelight_aim_proportional())
    );
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Schedule the drive request while the tag is off-center by more than a
    // small deadband. Use absolute angle so left/right both cause rotation.
    //tracking.setPipelineTx();
    final double angleDeadbandDeg = .1; // tune as needed
    if (Math.abs(tracking.getAngle()) > angleDeadbandDeg) {
      if (!driveRequestCmd.isScheduled()) {
        driveRequestCmd.schedule();
      }
    } else {
      if (driveRequestCmd.isScheduled()) {
        driveRequestCmd.cancel();
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (driveRequestCmd != null && driveRequestCmd.isScheduled()) {
      driveRequestCmd.cancel();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
