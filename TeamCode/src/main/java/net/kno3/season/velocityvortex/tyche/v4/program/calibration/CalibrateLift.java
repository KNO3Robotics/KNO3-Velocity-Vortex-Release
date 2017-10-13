package net.kno3.season.velocityvortex.tyche.v4.program.calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import net.kno3.season.velocityvortex.tyche.v4.robot.TycheSettings;

/**
 * @author Jaxon A Brown
 */
@TeleOp(group = "calibration", name = "Calibrate Lift")
public class CalibrateLift extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("1", "Press start");
        telemetry.update();

        waitForStart();

        DcMotor lift = hardwareMap.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);

        TycheSettings settings = new TycheSettings();

        while(opModeIsActive()) {
            lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            int topLimit;
            int loweringLimit;

            int liftRst = lift.getCurrentPosition();
            while (opModeIsActive() && !gamepad1.a) {
                lift.setPower(-gamepad1.right_stick_y);
                telemetry.addData("1", "Press A to set top limit");
                telemetry.addData("Pos", lift.getCurrentPosition() - liftRst);
                telemetry.update();
            }
            lift.setPower(0);

            topLimit = lift.getCurrentPosition() - liftRst;

            telemetry.addData("2", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            while (opModeIsActive() && !gamepad1.a) {
                lift.setPower(-gamepad1.right_stick_y);
                telemetry.addData("3", "Press A to set lowering limit");
                telemetry.addData("Pos", lift.getCurrentPosition() - liftRst);
                telemetry.update();
            }
            lift.setPower(0);

            loweringLimit = lift.getCurrentPosition() - liftRst;

            telemetry.addData("4", "Release A");
            telemetry.update();
            while(opModeIsActive() && gamepad1.a);

            telemetry.addData("5", "Lower the lift to starting position.");
            telemetry.addData("Uppper Limit", topLimit);
            telemetry.addData("Lower Limit", loweringLimit);
            settings.setLiftTopLimit(topLimit);
            settings.setLiftLoweringLimit(loweringLimit);
            settings.save();
            telemetry.addData("6", "Saved! Press Y to try again!");
            telemetry.update();
            lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            while(opModeIsActive() && !gamepad1.y);
        }
    }
}