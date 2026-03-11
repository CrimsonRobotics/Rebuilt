// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.shooter;


public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */
  SparkMax hoodPivot;
  TalonFX rollerWheel;
  SparkMax kickWheel;
  RelativeEncoder hoodEncoder;
  RelativeEncoder kickWheelEncoder;
  PIDController hoodPID;
  SlewRateLimiter hoodSlewRateLimiter;
  PIDController rollerWheelPID;
  SlewRateLimiter rollerWheelSlewRateLimiter;
  DutyCycleOut shooterRollerWheelOutput;
  AbsoluteEncoder hoodAbsolute;
  TalonFXConfiguration shooterConfig;
  double hoodVoltage;
  double speed;

  public Shooter() {
     
    hoodPivot = new SparkMax(Constants.shooter.hoodPivotID, SparkMax.MotorType.kBrushed);
    rollerWheel = new TalonFX(Constants.shooter.shooterWheelID);
    kickWheel = new SparkMax(Constants.shooter.kickWheelID, SparkMax.MotorType.kBrushless);
    hoodEncoder = hoodPivot.getEncoder();
    hoodPID = new PIDController(Constants.shooter.hoodP, Constants.shooter.hoodI, Constants.shooter.hoodD);
    kickWheelEncoder = kickWheel.getEncoder();
    rollerWheelPID = new PIDController(Constants.shooter.shooterP, Constants.shooter.shooterI, Constants.shooter.shooterD);
    hoodAbsolute = hoodPivot.getAbsoluteEncoder();
    shooterConfig = new TalonFXConfiguration();

    SparkMaxConfig shooterPivotConfig = new SparkMaxConfig();
    SparkMaxConfig shooterWheelConfig = new SparkMaxConfig();
    SparkMaxConfig kickWheelConfig = new SparkMaxConfig();
    

     shooterPivotConfig
    .idleMode(IdleMode.kBrake)
    .inverted(false)
    .smartCurrentLimit(Constants.shooter.shooterPivotCurrentLimit)
    .voltageCompensation(12);

    kickWheelConfig
    .idleMode(IdleMode.kBrake)
    .inverted(false)
    .smartCurrentLimit(Constants.shooter.kickWheelCurrentLimit)
    .voltageCompensation(12);

    shooterConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    hoodPivot.configure(shooterPivotConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    //ollerWheel.configure(shooterWheelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    kickWheel.configure(kickWheelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
    hoodSlewRateLimiter = new SlewRateLimiter(Constants.shooter.rateLimiterHood);

    shooterRollerWheelOutput = new DutyCycleOut(0);
    rollerWheel.getConfigurator().apply(shooterConfig);
  }

  public void runHood(double speedInput){
    double speed = hoodSlewRateLimiter.calculate(speedInput);
    hoodPivot.set(speed);
  }
  public void hoodToPosition(double hoodAngle) {
    double voltage = MathUtil.clamp(hoodPID.calculate(getHoodAngle(), hoodAngle), -1, 1);
    runHood(voltage);
   }

  public void runRoller(double speedInput) {
    double speed = rollerWheelSlewRateLimiter.calculate(speedInput);
    rollerWheel.set(speed);
  }
 
  public void setRoller(double targetRPM){
    double voltage = MathUtil.clamp(rollerWheelPID.calculate(getRollerSpeed(), targetRPM), -1, 1);
    rollerWheel.setControl(shooterRollerWheelOutput.withOutput(voltage));
  }

  /**Set Roller Speed to a percentage output */
  public void setRollerSpeed(double percentage){
    rollerWheel.setControl(shooterRollerWheelOutput.withOutput(percentage)); //.3833 is 2100 RPM //-.42
  }

  public void setHoodSpeed(double speed){
    hoodPivot.set(speed);
  }

  public void setHoodSpeedReverse(){
    hoodPivot.set(-.2);
  }
 
  public void setKickWheelSpeed(double kickWheelSpeed) {
    kickWheel.set(kickWheelSpeed); //Target: 3200
  }

  public double getHoodAngle() {
    return hoodEncoder.getPosition(); 
  }

  public double getHoodAngle2(){
    return hoodAbsolute.getPosition();
  }
  public double getTargetAngle(){
    double targetAngle = 36; //TODO: Change this jawn to be based on the distance to the target (Equation)
    return targetAngle;
  }

  public boolean atHoodAngle(double currentAngle){
    if (Math.abs(currentAngle - getTargetAngle() ) < 1){
      return true;
  }
    else{
      return false;
    }
  }
  public boolean atHoodAngleManual(double currentAngle){
    if (Math.abs(currentAngle - Constants.shooter.manualAngle) < 1){
      return true;
  }
    else{
      return false;
    }
  }

  public double getRollerSpeed(){
    return Math.abs(rollerWheel.getVelocity().getValueAsDouble() * 60); 
  }
  
  public double getKickWheelSpeed(){
    return kickWheelEncoder.getVelocity();
  }

  @Override
  // This method will be called once per scheduler run
  public void periodic() {
    SmartDashboard.putNumber("RollerSpeed", getRollerSpeed());
    SmartDashboard.putNumber("KickWheelSpeed-RPM", getKickWheelSpeed());
    SmartDashboard.putNumber("HoodAngle", getHoodAngle());
    SmartDashboard.putNumber("HoodAngle2", getHoodAngle2());
  }
    
}

