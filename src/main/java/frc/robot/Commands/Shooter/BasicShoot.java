// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands.Shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class BasicShoot extends Command {
  /** Creates a new BasicShoot. */
  Shooter shoot;
  public BasicShoot(Shooter shoot) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shoot);
    this.shoot = shoot;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    shoot.setRoller(3000);
    if(Math.abs(shoot.getRollerSpeed()) -2870 >= Constants.shooter.shooterWheelTolerance){ //Turn this into RPM
    shoot.setKickWheelSpeed(Constants.shooter.kickWheelSpeed);
  }
}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
   shoot.setRollerSpeed(0);
   shoot.setKickWheelSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
