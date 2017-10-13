package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * @author Jaxon A Brown
 */
public class WhiskerDrive extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        AnalogInput potentiometer = hardwareMap.analogInput.get("whisker");
        Servo whiskerarm = hardwareMap.servo.get("whiskerarm");

        double whiskerLevel = 0.5;//Guess, you need to tune it. This represents the potentiometer position at which the robot is parallel to the wall
        double p = 0.05;//Guess; you need to tune it. This is a proportional loop; increasing this will increase oscillation, making this too small will prevent the robot from achieving parallel.
        double potentiometerToServoFactor = 1;//Guess. This is a multiplier that should take the position of the potentiometer and determine what the servo position should be based on that value. Should be linear.
        double speed = 0.5;//Guess. This represents how fast the robot should go at base. Running too fast could be less accurate.

        waitForStart();

        while(opModeIsActive()) {
            double potentiometerPosition = potentiometer.getVoltage() / potentiometer.getMaxVoltage();//Finds the position of the potentiometer, 0-1
            whiskerarm.setPosition(potentiometerToServoFactor * potentiometerPosition + 0.05);//Sets the servo arm to the position of the potentiometer, plus 0.05's worth of pressing on the wall
            double error = whiskerLevel - potentiometerPosition;//How far off is the whisker from the parallel point?
            drive(speed + error * p, speed - error * p);//If we are off in one direction, move to correct it while still moving forwards.
        }
    }

    public void drive(double left, double right) {
        //Fill this. Left is the speed on the left side of the bot, right for the right.
    }
}
