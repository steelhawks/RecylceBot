
package org.usfirst.frc.team2601.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import org.usfirst.frc.team2601.robot.commands.closeWriter;
import org.usfirst.frc.team2601.robot.commands.auton.DriveForwardToAutoZone;
import org.usfirst.frc.team2601.robot.commands.auton.MotorTestAuton;
import org.usfirst.frc.team2601.robot.commands.auton.SampleAuton;
import org.usfirst.frc.team2601.robot.commands.auton.StackRCOnToteMoveToAutoZoneArms;
import org.usfirst.frc.team2601.robot.subsystems.Drivetrain;
import org.usfirst.frc.team2601.robot.subsystems.Elevator;
import org.usfirst.frc.team2601.robot.subsystems.ExampleSubsystem;
import org.usfirst.frc.team2601.robot.subsystems.Pneumatics;
import org.usfirst.frc.team2601.robot.subsystems.Rollers;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

//	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	

	public static Drivetrain drivetrain = new Drivetrain();

	/*
	public static final Elevator elevator = null; //new Elevator();
	public static final Pneumatics pneumatics = null; //new Pneumatics();
	public static final Rollers rollers = null; //new Rollers();
*/
	public static final Elevator elevator = new Elevator();
	public static final Pneumatics pneumatics = new Pneumatics();
	public static final Rollers rollers = new Rollers();
	
	public static NetworkTable table;
	
	Compressor compressor;
	CameraServer cam;
	
    Command autonomousCommand;
    Command closeCommand;
     /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // instantiate the command used for the autonomous period
		try {
			
				oi = new OI();
				
		autonomousCommand = new StackRCOnToteMoveToAutoZoneArms();
        closeCommand = new closeWriter();

		
		table = NetworkTable.getTable("datatable");
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
		
		if (Constants.PNEUMATICS_ON){
	        compressor = new Compressor();
	     }
		         
		cam = CameraServer.getInstance();
        cam.startAutomaticCapture("cam0");
		
        table.putNumber(Constants.drivetrainPKey, Constants.drivetrainP);
        table.putNumber(Constants.drivetrainIKey, Constants.drivetrainI);
        table.putNumber(Constants.drivetrainDKey, Constants.drivetrainD);
        table.putNumber(Constants.drivetrainSetpointKey, Constants.drivetrainSetpoint);
        
        table.putNumber(Constants.elevatorPKey, Constants.elevatorP);
        table.putNumber(Constants.elevatorIKey, Constants.elevatorI);
        table.putNumber(Constants.elevatorDKey, Constants.elevatorD);
        table.putNumber(Constants.elevatorSetpointKey, Constants.elevatorSetpoint);
    
	}
    
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    	closeCommand.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
