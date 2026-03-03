// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands.Climber;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Climber;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ClimberArmUp extends Command {
  Climber climber;
  /** Creates a new ClimberArmUp. */
  public ClimberArmUp(Climber climber) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climber);
    this.climber = climber;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //climber.climberToPosition(Constants.climber.climberTargetPos);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
  /*   if(climber.atClimberPos()){
    return true;
  }
  */
    return false;
  
    
}
}