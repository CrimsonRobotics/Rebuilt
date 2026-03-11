// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands.Climber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Climber;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ClimbUp extends Command {
  /** Creates a new ClimbUp. */
  private Climber climber;
  private WaitCommand wait;
  public ClimbUp(Climber climber) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climber);
     this.climber = climber;


    wait = new WaitCommand(5);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //climber.resetClimbEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    /*climber.climberToPosition(Constants.climber.climberTargetPos);
    if (climber.atClimberPos()){
      CommandScheduler.getInstance().schedule(wait);
      climber.climberToPosition(Constants.climber.climberTargetPos / 2);
    
      }
      */
      climber.manualClimber(.2);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted){
    climber.manualClimber(0);
  }
  
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
