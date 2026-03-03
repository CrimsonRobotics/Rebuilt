// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.AbsoluteEncoderConfig;
import com.revrobotics.spark.config.LimitSwitchConfig.Type;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.intake;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  SparkMax intakePivot;
  TalonFX intakeRoller1;
  TalonFX intakeRoller2;
  SparkMaxConfig  intakePivotConfig;
  TalonFXConfiguration  intakeRoller1Config;
  TalonFXConfiguration  intakeRoller2Config;
  DutyCycleEncoder intakePivotEncoder;
  PIDController intakePivotPID;
  SlewRateLimiter intakePivotSlewRateLimiter;
  DutyCycleOut intakeOutput;
  double feedForwardIntake;
  
  
  public Intake() {

    intakePivot = new SparkMax(Constants.intake.intakePivotID, SparkMax.MotorType.kBrushless);
    intakeRoller1 = new TalonFX(Constants.intake.intakeRollerID1);
    intakeRoller2 = new TalonFX(Constants.intake.intakeRollerID2);
    intakePivotConfig = new SparkMaxConfig();
    intakeRoller1Config = new TalonFXConfiguration();
    intakeRoller2Config = new TalonFXConfiguration();
    intakePivotEncoder = new DutyCycleEncoder(4);
    intakePivotPID = new PIDController(Constants.intake.intakePivotKp, Constants.intake.intakePivotKi, Constants.intake.intakePivotKd);
    intakePivotSlewRateLimiter = new SlewRateLimiter(Constants.intake.rateLimiterIntakePivot);

     intakePivotConfig
    .inverted(false)
    .idleMode(IdleMode.kBrake)
    .smartCurrentLimit(Constants.intake.intakePivotCurrentLimit)
    .voltageCompensation(12);

    intakeRoller1Config
    .CurrentLimits.StatorCurrentLimit = Constants.intake.intakeRollerCurrentLimit;
    intakeRoller2Config
    .CurrentLimits.StatorCurrentLimit = Constants.intake.intakeRollerCurrentLimit;
    
  intakeRoller1.getConfigurator().apply(intakeRoller1Config);    
  intakeRoller2.getConfigurator().apply(intakeRoller2Config);    

  intakeOutput = new DutyCycleOut(0);

  }
  
  public void runIntakeRollers(double speed){
    intakeRoller1.setControl(intakeOutput.withOutput(-speed));
    intakeRoller2.setControl(intakeOutput.withOutput(speed));
  }

  public double getIntakeAngle(){
    return intakePivotEncoder.get() * 360;
  }

  public void runPivot(double speedInput){
    double speed = intakePivotSlewRateLimiter.calculate(speedInput);
    intakePivot.set(speed);
  }

  public void basicPivot(double percent){
    intakePivot.set(percent);
  }
  
  public void intakeToPosition(double targetAngle){
    double voltage = MathUtil.clamp(intakePivotPID.calculate(getIntakeAngle(), targetAngle), -.3, .3); //.05
    runPivot(voltage);
   }
   

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Intake Angle", getIntakeAngle());
    SmartDashboard.putData("PID-Intake",intakePivotPID);
  }
}
