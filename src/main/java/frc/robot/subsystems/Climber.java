// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {
  /** Creates a new Climber. */
  SparkMax climberMotor1;
  SparkMaxConfig climberConfig;
  PIDController climberPID;
  SlewRateLimiter climberSlewRateLimiter;
  RelativeEncoder climberEncoder;

  public Climber() {
    climberMotor1 = new SparkMax(Constants.climber.climberMotorID, SparkMax.MotorType.kBrushless);
    climberConfig = new SparkMaxConfig();
    climberPID = new PIDController(Constants.climber.climberP, Constants.climber.climberI, Constants.climber.climberD);
    climberSlewRateLimiter = new SlewRateLimiter(Constants.climber.climberSlewRate);
    climberEncoder = climberMotor1.getEncoder();

    climberConfig
    .inverted(false)
    .idleMode(SparkMaxConfig.IdleMode.kBrake)
    .smartCurrentLimit(Constants.climber.climberCurrentLimit)
    .voltageCompensation(12);

    climberMotor1.configure(climberConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  
  }
 
  public void resetClimbEncoder(){
    climberEncoder.setPosition(0);
  }

  public double getCurrentClimberPosition(){
    return climberEncoder.getPosition();
  }

   public void runClimber(double speedInput){
    double speed = climberSlewRateLimiter.calculate(speedInput);
    climberMotor1.set(speed);
  }

  public void climberToPosition(double climberPos) {
    double voltage = MathUtil.clamp(climberPID.calculate(getCurrentClimberPosition(), climberPos), -.5, .5);
    runClimber(voltage);
   }

   public boolean atClimberPos(){
    if (getCurrentClimberPosition() - Constants.climber.climberTargetPos < Constants.climber.climberTolerance){
      return true;
   }
   else{
    return false;
   }
  }

   public boolean atClimberPosMid(double targetPos){
    if (getCurrentClimberPosition() - targetPos < Constants.climber.climberTolerance){
      return true;
   }
   else{
    return false;
   }
  }

  public void manualClimber(double speed){
    climberMotor1.set(speed);
  }
  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
   SmartDashboard.putNumber("Climber Position", getCurrentClimberPosition());
  }
}
