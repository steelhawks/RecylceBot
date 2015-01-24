package org.usfirst.frc.team2601.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.usfirst.frc.team2601.robot.Constants;
import org.usfirst.frc.team2601.robot.commands.DumbDrive;
import org.usfirst.frc.team2601.robot.commands.OmniDrive;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;

import org.usfirst.frc.team2601.robot.Robot;



/**
 *
 */
public class Drivetrain extends Subsystem {
    //set up all of the things involved in the drivetrain
	String filename = "/logs/drivetrainlog.csv";
	CANTalon leftTalonI = new CANTalon(Constants.leftTalonAddressI);
	CANTalon rightTalonI = new CANTalon(Constants.rightTalonAddressI);
	CANTalon leftTalonII = new CANTalon(Constants.leftTalonAddressII);
	CANTalon rightTalonII = new CANTalon(Constants.rightTalonAddressII);
	CANTalon centerTalon = new CANTalon(Constants.centerTalonAddress);
	RobotDrive drive = new RobotDrive(leftTalonI, leftTalonII, rightTalonI, rightTalonII);
	Encoder leftEncoder = new Encoder(Constants.leftEncoderPortI,Constants.leftEncoderPortII, true, Encoder.EncodingType.k4X);
	DriverStation driver;
	
	//setup first version of PID controller
	PIDController control = new PIDController(Constants.kP,Constants.kI,Constants.kD, leftEncoder, leftTalonI);
	boolean CSVstart; 
	
	SmartDashboard dash;
	
	//gets output values of Talons
	public void printStats(CANTalon Talon){
		double currentAmps = Talon.getOutputCurrent();
		double outputV = Talon.getOutputVoltage();
		double busV = Talon.getBusVoltage();
		double dualEncoderPos = Talon.getEncPosition();
		double dualEncoderVelocity = Talon.getEncVelocity();
		int analogPos = Talon.getAnalogInPosition();
		int analogVelocity = Talon.getAnalogInVelocity();
		double selectedSensorPos = Talon.getPosition();
		double selectedSensorVelocity = Talon.getSpeed();
		int closeLoopErr = Talon.getClosedLoopError();
		
		System.out.println("currentAmps" + currentAmps);
		System.out.println("outputV" + outputV);
		System.out.println("busV" + busV);
		System.out.println("dualEncoderPos" + dualEncoderPos);
		System.out.println("dualEncoderVelocity" + dualEncoderVelocity);
		System.out.println("analogPos" + analogPos);
		System.out.println("analogVelocity" + analogVelocity);
		System.out.println("selectedSensorPos" + selectedSensorPos);
		System.out.println("selectedSensorVelocity" + selectedSensorVelocity);
		System.out.println("closeLoopErr" + closeLoopErr);
		
	}
		
    public void initDefaultCommand() {
    	//DumbDrive on start
    	setDefaultCommand(new OmniDrive());
    }
    
    public void dumbDrive(Joystick stick){
    	//start code here, simple arcadedrive
    	leftEncoder.reset();
    	drive.arcadeDrive(stick);
    }
    
    public void getPIDvalues(){
    	//retrieve PID vals from NetTables
    	Constants.kP = Robot.table.getNumber("P");
    	Constants.kI = Robot.table.getNumber("I");
    	Constants.kD = Robot.table.getNumber("D");
    	
    }
    public void getSetpoint(){
    	Constants.setpoint = Robot.table.getNumber("setpoint");
    }
    
    public void startPID(){
    	//get PID values from Net Tables
    	getPIDvalues();
    	getSetpoint();
    	
    	//set up encoders
    	leftEncoder.setDistancePerPulse(Constants.distancePerPulse);
    	leftEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);
    	
    	//set up PID loop parameters
    	control.setPID(Constants.kP, Constants.kI, Constants.kD);
    	control.setSetpoint(Constants.setpoint);
    	control.setAbsoluteTolerance(Constants.absoluteTolerance);
    	control.setOutputRange(Constants.minOutput, Constants.maxOutput);
    	
    	//enable loop, match motors
    	control.enable();
    	matchMotors();
    	
    	//check if it's ok to stop
    	if (control.onTarget()){
    		stopPID();
    	}
    	
    	//let's see if this is working
    	SmartDashboard.putNumber("LeftEncoder Distance", leftEncoder.getDistance());
    	SmartDashboard.putNumber("Error", control.getError());
    	SmartDashboard.putNumber("setpoint", Constants.setpoint);
    	
    }
    
    public void matchMotors(){
    	//keep motors together for straight line PID
    	rightTalonI.set(leftTalonI.get()* -1);
    	rightTalonII.set(leftTalonII.get()* -1);
    }
    
    public void stopMotors(){
    	//stop everything
    	rightTalonI.set(0.0);
    	leftTalonI.set(0.0);
    	rightTalonII.set(0.0);
    	leftTalonII.set(0.0);
    }
    
    public void stopPID(){
    	//disable PID loop
    	control.disable();
    }
    
    public void distanceDriveForwardPID(){
    	//get PID values from Net Tables
    	getPIDvalues();
    	
    	//set up encoders
    	leftEncoder.setDistancePerPulse(Constants.distancePerPulse);
    	leftEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);
    	
    	//set up PID loop parameters
    	control.setPID(Constants.kP, Constants.kI, Constants.kD);
    	control.setSetpoint(Constants.setpoint);
    	control.setAbsoluteTolerance(Constants.absoluteTolerance);
    	control.setOutputRange(Constants.minOutput, Constants.maxOutput);
    	
    	//enable loop, match motors
    	control.enable();
    	matchMotors();
    	
    	//check if it's ok to stop
    	if (control.onTarget()){
    		stopPID();
    		return;
    	} 	
    }
    
    public void moveForward(){
    	
    	leftTalonI.set(Constants.speed * Constants.leftTalonMultiplier);
    	rightTalonI.set(Constants.speed * Constants.rightTalonMultiplier);
    	leftTalonII.set(Constants.speed * Constants.leftTalonMultiplier);
    	rightTalonII.set(Constants.speed * Constants.rightTalonMultiplier);
    	printStats(leftTalonI);
    	printStats(rightTalonI);
    	printStats(leftTalonII);
    	printStats(rightTalonII);
    	
    }
    
    public void moveBackward(){
    	leftTalonI.set(Constants.speed * Constants.leftTalonMultiplier);
    	rightTalonI.set(Constants.speed * Constants.rightTalonMultiplier);
    	leftTalonII.set(Constants.speed * Constants.leftTalonMultiplier);
    	rightTalonII.set(Constants.speed * Constants.rightTalonMultiplier);
    	printStats(leftTalonI);
    	printStats(rightTalonI);
    	printStats(leftTalonII);
    	printStats(rightTalonII);
    }
    
    public void omniDrive(Joystick stick){
    	double yval = stick.getY();
    	double xval = -stick.getX();
    	double twist = -stick.getTwist();
    	centerTalon.set(xval * Constants.centerTalonMultiplier);
    	
    	//begin screwing around with rolling my own method
    	
    	/*if (twist == 0){
    		rightTalon.set(yval*Constants.rightTalonMultiplier);
    		leftTalon.set(yval*Constants.leftTalonMultiplier);
    	}
    	else if (yval == 0 && twist != 0){
    		rightTalon.set(twist * -1 * Constants.rightTalonMultiplier);
    		leftTalon.set(twist * Constants.leftTalonMultiplier);
    	}
    	else if (yval != 0 && twist != 0){
    		rightTalon.set(twist * yval * -1 * Constants.rightTalonMultiplier);
    		leftTalon.set(twist * yval * Constants.leftTalonMultiplier);
    	}*/
    	
    	//this line below pretty much does everything i was trying to do in the method above ~Marcus
    	
    	//drive.arcadeDrive(yval, twist);
    	drive.arcadeDrive(yval, twist, false); 
    	printStats(leftTalonI);
    	printStats(rightTalonI);
    	printStats(leftTalonII);
    	printStats(rightTalonII);
    }
    

    public void logStart(String param){
    	try
    	{
    	    FileWriter writer = new FileWriter(filename);
     
    	    writer.append("Time");
    	    writer.append(',');
    	    writer.append(param);
    	    writer.append('\n');
     
    	    //generate whatever data you want
     
    	    writer.flush();
    	    writer.close();
    	}
    	catch(IOException e)
    	{
    	     e.printStackTrace();
    	} 
    	CSVstart = true;
        }
    
    public void logData(double val){
    	driver = DriverStation.getInstance();
    	if (CSVstart){
    		try
    		{
    		    FileWriter writer = new FileWriter(filename);
    	 String value = Double.toString(val);
    		    writer.append(Double.toString(driver.getMatchTime()));
    		    writer.append(',');
    		    writer.append(value);
    		    writer.append('\n');
    	 
    		    writer.flush();
    		    writer.close();
    		}
    		catch(IOException e)
    		{
    		     e.printStackTrace();
    		} 
    	    }
    	}
    }
	
    
    
    
    
    















