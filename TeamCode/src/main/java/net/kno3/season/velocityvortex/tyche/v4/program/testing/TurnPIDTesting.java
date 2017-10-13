package net.kno3.season.velocityvortex.tyche.v4.program.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import net.kno3.opMode.DriverControlledProgram;
import net.kno3.robot.Robot;
import net.kno3.season.velocityvortex.tyche.v4.program.TycheAuto;
import net.kno3.season.velocityvortex.tyche.v4.robot.DriveSystem;
import net.kno3.season.velocityvortex.tyche.v4.robot.Tyche;
import net.kno3.util.Color;
import net.kno3.util.SimpleColor;

import org.firstinspires.ftc.robotcore.external.Func;

/**
 * Created by jaxon on 3/15/2017.
 */
@TeleOp(group = "testing", name = "turntest")
public class TurnPIDTesting extends TycheAuto {
        double max = 0.2, kp = 0.026, ki = 0, kd = 0.07, setpoint = 45, tolerance = 2;
        double increment = 0.1;
        int varInx = 0, histSize = 10;

    public TurnPIDTesting() {
        super(SimpleColor.BLUE);
    }


    public void updateVar(double mult) {
        switch (varInx) {
            case 0:
                tolerance += mult * increment;
                break;
            case 1:
                max += mult * increment;
                break;
            case 2:
                kp += mult * increment;
                break;
            case 3:
                ki += mult * increment;
                break;
            case 4:
                kd += mult * increment;
                break;
            case 5:
                if(mult > 0) {
                    histSize++;
                } else {
                    histSize--;
                }
                break;
            case 6:
                setpoint += mult * increment;
                break;
        }
    }

    @Override
    protected Robot buildRobot() {
        return new Tyche(this, null);
    }

    @Override
    public void main() {
        telemetry.addData("tolerance", new Func<Object>() {
            @Override
            public Object value() {
                return tolerance;
            }
        });
        telemetry.addData("max", new Func<Object>() {
            @Override
            public Object value() {
                return max;
            }
        });
        telemetry.addData("kp", new Func<Object>() {
            @Override
            public Object value() {
                return kp;
            }
        });
        telemetry.addData("ki", new Func<Object>() {
            @Override
            public Object value() {
                return ki;
            }
        });
        telemetry.addData("kd", new Func<Object>() {
            @Override
            public Object value() {
                return kd;
            }
        });
        telemetry.addData("histSize", new Func<Object>() {
            @Override
            public Object value() {
                return histSize;
            }
        });
        telemetry.addData("setpoint", new Func<Object>() {
            @Override
            public Object value() {
                return setpoint;
            }
        });
        telemetry.addData("var", new Func<Object>() {
            @Override
            public Object value() {
                switch (varInx) {
                    case 0:
                        return "tolerance";
                    case 1:
                        return "max";
                    case 2:
                        return "kp";
                    case 3:
                        return "ki";
                    case 4:
                        return "kd";
                    case 5:
                        return "histSize";
                    case 6:
                        return "setpoint";
                    default:
                        return "err=" + varInx;
                }
            }
        });
        telemetry.addData("increment", new Func<Object>() {
            @Override
            public Object value() {
                return increment;
            }
        });

        waitForStart();

        while (opModeIsActive()) {
            if(gamepad1.back) {
                while (gamepad1.back) {}
                increment *= 10;
            }
            if(gamepad1.start) {
                while (gamepad1.start) {}
                increment /= 10;
            }
            if(gamepad1.dpad_left) {
                while (gamepad1.dpad_left) {}
                varInx--;
            }
            if (gamepad1.dpad_right) {
                while (gamepad1.dpad_right) {}
                varInx++;
            }
            if(gamepad1.left_bumper) {
                while (gamepad1.left_bumper) {}
                updateVar(-1);
            }
            if(gamepad1.right_bumper) {
                while (gamepad1.right_bumper) {}
                updateVar(1);
            }


            if(gamepad1.a) {
                getRobot().getSubSystem(DriveSystem.class).turnPID(max, kp, ki, kd, setpoint, tolerance, 0.05);
                while (gamepad1.a) {
                    telemetry.addData("Finished", "Finished");
                }
            }
            if(gamepad1.b) {
                getRobot().getSubSystem(DriveSystem.class).turnPIDfast(setpoint);
            }

            telemetry.update();
        }
    }
}
