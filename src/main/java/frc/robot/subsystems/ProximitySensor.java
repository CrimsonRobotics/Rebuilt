// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.CANrange;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ProximitySensor extends SubsystemBase {
  /** Creates a new ProximitySensor. */
  private CANrange range;
  public ProximitySensor(CANrange range) {
    this.range = range;
  }

  //Get the Distance to the Pbject in Inches
  public double getDistance(){
    return range.getDistance().getValueAsDouble() * 100 / 2.54;
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("DTO", getDistance()); //DTO = Distance to Object
  }
}
