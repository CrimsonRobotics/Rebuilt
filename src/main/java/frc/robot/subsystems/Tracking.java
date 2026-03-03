// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Tracking extends SubsystemBase {
  /** Creates a new Tracking. */
  private NetworkTable table = 
    NetworkTableInstance.getDefault().getTable("limelight");
  public Tracking() {
  }


  /** Returns the angular velocity propotional to the tx value from the limelight (Units)*/
  public static double limelight_aim_proportional(){

    //TODO: Tune the kP  .035 is given
    double kP = .0105; //.011 //.01

    // Use horizontal offset (tx) to compute rotational velocity toward the tag.
    double targetingAngularVelocity = LimelightHelpers.getTX("limelight") * kP;

    targetingAngularVelocity *= Constants.dt.max_angular_speed;

    targetingAngularVelocity *= -1.0;

    return targetingAngularVelocity;
  }

  //*returns the velocity of approcaching or moving away from the tag (A positive is movign towards, a negative is moving away) (*Units*) */
  public static double limelight_range_proportional(){

    //Need to tune kP. .1 is given
    double kP = 0.01; //.1
    
    //Flipped from TY/TA because the camera is rotated 90 degrees
    double targetingForwardSpeed = LimelightHelpers.getTX("limelight") * kP;
    targetingForwardSpeed *= Constants.dt.max_speed;

    targetingForwardSpeed *= -1.0;

    return targetingForwardSpeed;
  }
/** An angle (degrees) represting the vertical offset from the crosshair
 of the camera to the AprilTag. Postive degree mean AprilTag is above the camera */
  private double getTy(){
    NetworkTableEntry ty;
    switch (Constants.camera.limelight_rotation) {
      case DEFAULT:
        ty = table.getEntry("ty");
        //Revist what jappens when camera disconnects
        return ty.getDouble(0.0);
        
      case CW_90:
        ty = table.getEntry("tx");
        //Revist what jappens when camera disconnects
        return ty.getDouble(0.0);

      case CW_180:
        ty = table.getEntry("ty");
        //Revist what jappens when camera disconnects
        return -ty.getDouble(0.0);
      case CW_270:
        ty = table.getEntry("tx");
        //Revist what jappens when camera disconnects
        return -ty.getDouble(0.0);
      default:
        return 0.0;
    } 
  }
//TODO: Add a getter for Tags in Frame
  public double getDistance(){
    double ty = this.getTy();
    double limelightMountAngleDegrees = -Constants.camera.camera_lean_angle;

    double limelightMountingHeightInches = Constants.camera.camera_height;

    double goalHeightInches = Constants.camera.target_height_3;

    double angleToGoalDegrees = limelightMountAngleDegrees + ty;

    double angleToGoalRadians = angleToGoalDegrees * (3.14159/180.0);

    double distanceFromLimelightToGoalInches = (goalHeightInches - limelightMountingHeightInches) / Math.tan(angleToGoalRadians);
    return distanceFromLimelightToGoalInches;
  }

  private double getTx(){
    NetworkTableEntry tx;
    switch (Constants.camera.limelight_rotation) {
      case DEFAULT:
        tx = table.getEntry("tx");
        //Revist what happens when camera disconnects
        return tx.getDouble(0.0);
        
      case CW_90:
        tx = table.getEntry("ty");
        //Revist what jappens when camera disconnects
        return -tx.getDouble(0.0);

      case CW_180:
        tx = table.getEntry("tx");
        //Revist what jappens when camera disconnects
        return -tx.getDouble(0.0);
      case CW_270:
        tx = table.getEntry("ty");
        //Revist what jappens when camera disconnects
        return tx.getDouble(0.0);
      default:
        return 0.0;
    } 
  }
  public double getAngle(){
    double tx = this.getTx();
    return tx;
  }

  public double getTagID(){
    double id = LimelightHelpers.getFiducialID("limelight");
    return id;
  }
  public void setPipeline(int pipelineNumber){
    LimelightHelpers.setPipelineIndex("limelight", pipelineNumber);
  }
  //25, 26 blue //9, 10 red
  public void setPipelineTx(){
    double currentID = getTagID();
    if(currentID == 9){
      setPipeline(0);
    }
    else if (currentID == 10 ){
      setPipeline(1);
    }
    else if (currentID == 25){
      setPipeline(2);
    }
    else if (currentID == 26){
      setPipeline(3);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run


    // Publish computed values
    SmartDashboard.putNumber("Tracking System - Angle", getAngle());
    SmartDashboard.putNumber("Tracking System - Distance", getDistance());
    SmartDashboard.putNumber("Ty", getTy());
    SmartDashboard.putNumber("Tx", getTx());
    SmartDashboard.putNumber("Aim-Proportional",limelight_aim_proportional());
    
  }
}