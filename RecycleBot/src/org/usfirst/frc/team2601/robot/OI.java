package org.usfirst.frc.team2601.robot;

import org.usfirst.frc.team2601.robot.Constants;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team2601.robot.commands.ExtendPiston;
import org.usfirst.frc.team2601.robot.commands.ManualControlRollers;
import org.usfirst.frc.team2601.robot.commands.RetractPiston;
import org.usfirst.frc.team2601.robot.commands.closeOrOpenRollers;
import org.usfirst.frc.team2601.robot.commands.intakeRollers;
import org.usfirst.frc.team2601.robot.commands.openRollers;
import org.usfirst.frc.team2601.robot.commands.closeRollers;
import org.usfirst.frc.team2601.robot.commands.outtakeRollers;
import org.usfirst.frc.team2601.robot.commands.drivetrainCommands.DumbDrive;
import org.usfirst.frc.team2601.robot.commands.drivetrainCommands.StartDrivetrainPID;
import org.usfirst.frc.team2601.robot.commands.drivetrainCommands.StopDrivetrainMotors;
import org.usfirst.frc.team2601.robot.commands.drivetrainCommands.StopDrivetrainPID;
import org.usfirst.frc.team2601.robot.commands.elevatorCommands.AutomaticEjectTotes;
import org.usfirst.frc.team2601.robot.commands.elevatorCommands.ManualCloseEjectionPiston;
import org.usfirst.frc.team2601.robot.commands.elevatorCommands.ManualOpenEjectionPiston;
import org.usfirst.frc.team2601.robot.commands.elevatorCommands.StartElevatorPID;
import org.usfirst.frc.team2601.robot.commands.elevatorCommands.StopElevatorPID;
/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    
	public static Joystick stick = new Joystick(Constants.joystickPort);
	public static Joystick elevatorStick = new Joystick(Constants.secondaryJoystickPort);
	public static Joystick rollerStick = new Joystick(Constants.thirdJoystickPort);
	
	public OI(){
		//Roller buttons
		JoystickButton closeOrOpenRollersButton = new JoystickButton(rollerStick, Constants.closeOrOpenRollersButton);
		closeOrOpenRollersButton.toggleWhenPressed(new closeOrOpenRollers());
		JoystickButton intakeRollersButton = new JoystickButton(rollerStick, Constants.intakeRollersButton);
		intakeRollersButton.toggleWhenPressed(new intakeRollers());
		JoystickButton outtakeRollersButton = new JoystickButton(rollerStick, Constants.outtakeRollersButton);
		outtakeRollersButton.toggleWhenPressed(new outtakeRollers());
		
		//ElevatorButtons
		JoystickButton automaticEjectTotesButton = new JoystickButton(elevatorStick, Constants.automaticEjectTotesButton);
		automaticEjectTotesButton.whenPressed(new AutomaticEjectTotes());
		JoystickButton manualOpenEjectionPiston = new JoystickButton(elevatorStick, Constants.manualOpenEjectionPistonButton);
		manualOpenEjectionPiston.whenPressed(new ManualOpenEjectionPiston());
		JoystickButton manualCloseEjectionPiston = new JoystickButton(elevatorStick, Constants.manualCloseEjectionPistonButton);
		manualCloseEjectionPiston.whenPressed(new ManualCloseEjectionPiston());
			
		//PID buttons
		
		JoystickButton startDrivetrainPID = new JoystickButton(stick, Constants.startDrivetrainPID);
		startDrivetrainPID.whenPressed(new StartDrivetrainPID());
		
		JoystickButton stopDrivetrainPID = new JoystickButton(stick, Constants.stopDrivetrainPID);
		stopDrivetrainPID.cancelWhenPressed(new StartDrivetrainPID());
		stopDrivetrainPID.whenPressed(new StopDrivetrainPID());
		
		JoystickButton startElevatorPID = new JoystickButton(elevatorStick, Constants.startElevatorPID);
		startElevatorPID.whenPressed(new StartElevatorPID());
		
		JoystickButton stopElevatorPID = new JoystickButton(elevatorStick, Constants.stopElevatorPID);
		stopElevatorPID.cancelWhenPressed(new StartElevatorPID());
		stopElevatorPID.whenPressed(new StopElevatorPID());
		
		/*JoystickButton stopMotors = new JoystickButton(stick, Constants.stopMotors);
		stopMotors.cancelWhenPressed(new StartPID());
		stopMotors.whenPressed(new StopPID());
		stopMotors.whenPressed(new StopMotors());
		
		//general buttons
		JoystickButton restartDumbDrive = new JoystickButton(stick, Constants.restartDumbDriveButton);
		restartDumbDrive.whenPressed(new DumbDrive());*/
		
		//pneumatics buttons
		JoystickButton extend = new JoystickButton(stick, Constants.extend);
		extend.whenPressed(new ExtendPiston(1.5));
		JoystickButton retract = new JoystickButton(stick, Constants.retract);
		retract.whenPressed(new RetractPiston(1.5));
		
	}
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
}

