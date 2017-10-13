package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MechanumExample extends OpMode {
    private DcMotor frontLeftMotor, frontRightMotor, rearLeftMotor, rearRightMotor;

    @Override
    public void init() {
        this.frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        this.frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        this.rearLeftMotor = hardwareMap.dcMotor.get("rearLeftMotor");
        this.rearRightMotor = hardwareMap.dcMotor.get("rearRightMotor");

        this.frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.rearLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        driveMechanum(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
    }

    private void driveMechanum(double forwardsSpeed, double strafeSpeed, double rotarySpeed) {
        double frontLeft = strafeSpeed + forwardsSpeed + rotarySpeed;
        double rearLeft = -strafeSpeed + forwardsSpeed + rotarySpeed;
        double frontRight = -strafeSpeed + forwardsSpeed - rotarySpeed;
        double rearRight = strafeSpeed + forwardsSpeed - rotarySpeed;

        double largestMagnitude = Math.abs(frontLeft);
        if(Math.abs(rearLeft) > largestMagnitude) {
            largestMagnitude = Math.abs(rearLeft);
        }
        if(Math.abs(frontRight) > largestMagnitude) {
            largestMagnitude = Math.abs(frontRight);
        }
        if(Math.abs(rearRight) > largestMagnitude) {
            largestMagnitude = Math.abs(rearRight);
        }

        if(largestMagnitude > 1.0) {
            frontLeft = frontLeft / largestMagnitude;
            rearLeft = rearLeft / largestMagnitude;
            frontRight = frontRight / largestMagnitude;
            rearRight = rearRight / largestMagnitude;
        }

        frontLeftMotor.setPower(frontLeft);
        rearLeftMotor.setPower(rearLeft);
        frontRightMotor.setPower(frontRight);
        rearRightMotor.setPower(rearRight);
    }
}
