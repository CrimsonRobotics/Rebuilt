// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.Commands.Climber.ClimbDownAuto;
import frc.robot.Commands.Climber.ClimbUp;
import frc.robot.Commands.Drivetrain.DriveAroundTheTag;
import frc.robot.Commands.Drivetrain.TowerDrive;
import frc.robot.Commands.Intake.DriveState;
import frc.robot.Commands.Intake.Intaking;
import frc.robot.Commands.Intake.RunRollers;
import frc.robot.Commands.Shooter.BasicShoot;
import frc.robot.Commands.Shooter.HoodReverse;
import frc.robot.Commands.Shooter.ManualShoot;
import frc.robot.Commands.Shooter.ShootAtPOI;
import frc.robot.generated.Telemetry;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.ProximitySensor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Tracking;


public class RobotContainer {

    private final CANrange range = new CANrange(11);
    private final Tracking tracking = new Tracking();
    private final ProximitySensor sensor = new ProximitySensor(range);
    private final Shooter shoot = new Shooter();
    private final Intake intake = new Intake();
    private final Climber climber = new Climber();

    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    //Joysticks
    private final Joystick joystickDL = new Joystick(0);
    private final Joystick joystickDR = new Joystick(1);
    private final Joystick joystickOL = new Joystick(2);
    private final Joystick joystickOR = new Joystick(3);


    private final JoystickButton reset_gyro = new JoystickButton(joystickDL, 1);
    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    //Driver Buttons
    private final JoystickButton driveAroundTag = new JoystickButton(joystickDR, 1);
    private final JoystickButton towerDrive = new JoystickButton(joystickDL, 3); //Maybe Auto Only?

    //Shooter Buttons
    private final JoystickButton shootAtPOI = new JoystickButton(joystickOR, 3); //TODO: Make 1
    private final JoystickButton spoolShooter = new JoystickButton(joystickOR, 2);
    private final JoystickButton basicShoot = new JoystickButton(joystickOR, 1);
    private final JoystickButton hoodReverse = new JoystickButton(joystickOR, 4);
    //Climber Buttons
    private final JoystickButton climbUp = new JoystickButton(joystickOR, 5);
    private final JoystickButton climbDown = new JoystickButton(joystickOR, 6);

    //Intake Buttons
    private final JoystickButton intaking = new JoystickButton(joystickOL, 1);
    private final JoystickButton intakeReset = new JoystickButton(joystickOL, 4);
    private final JoystickButton intakePivotTest = new JoystickButton(joystickOL, 3);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystickDL.getY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystickDL.getX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystickDR.getX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );
        
        //towerDrive.whileTrue(new TowerDrive(sensor, drivetrain)); // Button 3 DL
        //driveAroundTag.whileTrue(new DriveAroundTheTag(tracking, drivetrain, joystickDL)); // Button 1 DL

        //Shooter Button Bindings
       // shootAtPOI.whileTrue(new ShootAtPOI(shoot, tracking)); //Button 1 on OR
        spoolShooter.whileTrue(new ManualShoot(shoot)); //Button 2 on OR FOR NOW and Hood up for now
        basicShoot.whileTrue(new BasicShoot(shoot)); // Button 1 on OR FOR NOW 
        //hoodReverse.whileTrue(new HoodReverse(shoot)); // Button 4 on OR FOR NOW
       
        //Climber Button Bindings
        //climbUp.whileTrue(new ClimbUp(climber));
        //climbDown.whileTrue(new ClimbDownAuto());

        //Intake Button Bindings
        //intaking.whileTrue(new RunRollers(intake)); // Button 3 on OL
        intakeReset.onTrue(new DriveState(intake)); // Button 4 on OL
        intaking.whileTrue(new Intaking(intake)); //Button 1 OL
        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );
        
            /* 
        joystickDL.whileTrue(drivetrain.applyRequest(() -> brake));
        joystickDR.whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystickDR.getY(), -joystickDR.getX()))
        ));
        */
        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
       /*  joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
            */
        // Reset the field-centric heading on left bumper press.
        reset_gyro.onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        // Simple drive forward auton
        final var idle = new SwerveRequest.Idle();
        return Commands.sequence(
            // Reset our field centric heading to match the robot
            // facing away from our alliance station wall (0 deg).
            drivetrain.runOnce(() -> drivetrain.seedFieldCentric(Rotation2d.kZero)),
            // Then slowly drive forward (away from us) for 5 seconds.
            drivetrain.applyRequest(() ->
                drive.withVelocityX(0.5)
                    .withVelocityY(0)
                    .withRotationalRate(0)
            )
            .withTimeout(5.0),
            // Finally idle for the rest of auton
            drivetrain.applyRequest(() -> idle)
        );
    }
}
