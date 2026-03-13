// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;



/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
  public static class dt {

    public static final double max_speed = 5;
    public static final double max_angular_speed = 7.0;
  }

  

  public static class climber {
    public static final int climberMotorID= 54; 
    public static final double climberP = 0;
    public static final double climberI = 0;
    public static final double climberD = 0;
    public static final double climberSlewRate = 0.5; //TODO: Change this jawn

    public static final double climberTargetPos = .45; //TODO: Change this jawn

    public static final int climberCurrentLimit = 10; //TODO: Change this jawn

    public static final double climberTolerance = .1;
  }

  public static class intake {
    public static final int intakePivotID = 50; 
    public static final int intakeRollerID1= 11;
    public static final int intakeRollerID2 = 12;

    public static final int intakePivotCurrentLimit = 10; //TODO: Hope and pray that ts works it was originally 20
    public static final int intakeRollerCurrentLimit = 20;

    public static final double intakeRollerSpeed = .8;  //.8
    public static final double intakePivotKp = .1; //TODO: Change
    public static final double intakePivotKi = 0.01; 
    public static final double intakePivotKd = 0; 
    
    public static final double rateLimiterIntakePivot = 4; //TODO: Change this jawn

    public static final double intakingAngle = 25;
    public static final double driveIntakeAngle = 180;  

     
  } 
  public static class shooter{
    public static final int hoodPivotID = 46;
    public static final int shooterWheelID = 15;
    public static final int kickWheelID = 55;

    public static final int shooterPivotCurrentLimit = 10;
    public static final int shooterWheelCurrentLimit = 20;
    public static final int kickWheelCurrentLimit = 15;

    public static final double kickWheelSpeed = .4;   
    public static final double shooterWheelSpeed = -.7; //-.42
    public static final double rollerRPM = 3000;
    public static final double shooterWheelTolerance = 30; 



    public static final double hoodP = .1; //TODO: Change this jawn
    public static final double hoodI = 0; //TODO: Change this jawn
    public static final double hoodD = 0; //TODO: Change this jawn

    public static final double rateLimiterHood = 0.5; //TODO: Change this jawn
    public static final double manualAngle = 15; 

    public static final double shooterP = .006;
    public static final double shooterI = 0.000265; 
    public static final double shooterD = 0;
  }

    public static enum rotation{
      DEFAULT,
      CW_90,
      CW_180,
      CW_270
    
    }

  public static class autos {
   
  }

  public static class camera{
    
    //Leaning forward would be positive, leaning back is negative
    public static final double camera_lean_angle = 0;

    //Camera height off the ground in inches
    public static final double camera_height = 34.5;

    //Height of Target Off the Floor to the middle of the Tag
    public static final double target_height_2 = 9;

    public static final double target_height_3 = 70;

    //Can only be 0,90,180 or 270 and is clockwise from looking at the lens
    public static final rotation limelight_rotation = rotation.DEFAULT;
  }
  
}

