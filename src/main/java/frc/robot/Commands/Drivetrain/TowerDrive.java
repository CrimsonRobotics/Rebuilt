// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ProximitySensor;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TowerDrive extends Command {
  /** Creates a new TowerDrive. */
  private ProximitySensor sensor;
  private CommandSwerveDrivetrain dt;
  private Command driveRequestCmd;
  public TowerDrive(ProximitySensor sensor, CommandSwerveDrivetrain dt) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.sensor = sensor;
    this.dt = dt;

  // This command only requires the sensor so it can run while the drivetrain
  // is controlled by the drive-request command created below. If TowerDrive
  // required the drivetrain, scheduling the driveRequestCmd (which also
  // requires it) would immediately interrupt TowerDrive.
  addRequirements(this.sensor);

    // Pre-create the command that will apply the desired drive request.
    // We create it once and schedule/cancel it as needed so we don't allocate
    // a new Command on every scheduler tick.
    this.driveRequestCmd = this.dt.applyRequest(() ->
        this.dt.getField()
            .withVelocityX(Constants.dt.max_speed *.5)
            .withVelocityY(0)
            .withRotationalRate(0)
    );

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double distance = sensor.getDistance();
    // ProximitySensor.getDistance() returns inches.
    // We want the robot to drive TOWARD the object until it reaches a safe stop
    // distance. That means: while the object is farther than `stopDistance`,
    // request forward motion; when the object is at or closer than `stopDistance`,
    // stop requesting motion.
    final double stopDistanceInches = 10.0; // stop when <= 10 in

    if (distance > stopDistanceInches) {
      // If not already scheduled, start the drive request command
      if(!driveRequestCmd.isScheduled()) {
        driveRequestCmd.schedule();
      }
    }
    else {
      // Cancel the request so drivetrain goes idle / default command can take over
      if (driveRequestCmd.isScheduled()) {
        driveRequestCmd.cancel();
      }
    }
     
   
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Ensure the drive request is cancelled when this monitoring command ends
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
