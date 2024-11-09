package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Common.SnakeArm;
//import com.qualcomm.robotcore.hardware.DigitalChannelMode;

@TeleOp(name = "DriveCode24_25")
public class DriveCode24_25 extends LinearOpMode {
    private DcMotor BLeft;
    private DcMotor BRight;
    private DcMotor FLeft;
    private DcMotor FRight;
  // private CRServo SpinIntake;
   private DcMotor Viper;
  // private Servo Elbow;
   // private Servo Claw;
    private DigitalChannel magneticLimit;
    private SnakeArm arm = new SnakeArm();
    private Servo Bucket;

    //  private DcMotor Arm;
    // private DcMotor Linear;
    DistanceSensor distance;

    @Override
    public void runOpMode() {
        this.arm.initArm(hardwareMap,gamepad2);
        BLeft = hardwareMap.dcMotor.get("BLeft");
        BRight = hardwareMap.dcMotor.get("BRight");
        FLeft  = hardwareMap.dcMotor.get("FLeft");
        FRight = hardwareMap.dcMotor.get("FRight");
        Viper = hardwareMap.dcMotor.get("Viper");
        Bucket = hardwareMap.servo.get("Bucket");
        distance = hardwareMap.get(DistanceSensor.class, "Distance");
        magneticLimit = hardwareMap.get(DigitalChannel.class, "magneticLimit");


        FLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        Viper.setDirection(DcMotorSimple.Direction.REVERSE);

        Viper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Viper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Viper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                this.arm.runArm();
                drive();
                viper();
                bucketIntake();
            }
        }
    }
    public void drive() {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed!
            double x  = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            //different powers for the motors
            double FLeft = (y + x + rx) / denominator;
            double BLeft = (y - x + rx) / denominator;
            double FRight = (y - x - rx) / denominator;
            double BRight = (y + x - rx) / denominator;

            // Connects private to double bb
            this.FLeft.setPower(FLeft);
            this.BLeft.setPower(BLeft);
            this.FRight.setPower(FRight);
            this.BRight.setPower(BRight);
    }

    public void viper() {
        // Tracks if joystick is going up or down
        boolean goingUp = gamepad2.right_stick_y < 0;
        boolean goingDown = gamepad2.right_stick_y > 0;
        boolean magnetFound = !magneticLimit.getState();

        final int maxHeight=2650;

        if (magnetFound && goingDown) {
            Viper.setPower(0);
            Viper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            Viper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else {
            if (Viper.getCurrentPosition() < maxHeight) {
                Viper.setPower(-1 * gamepad2.right_stick_y);
            } else {
                Viper.setPower(0);
            }
        }




        //  if ((magneticLimit.getState() == false && goingDown) ||
          //        (Viper.getCurrentPosition() > 3000 && goingUp)) {
         //     Viper.setPower(0);
        //  } else if(gamepad2.right_stick_y !=0.0) {
//        if(gamepad2.right_stick_y !=0.0) {
//            Viper..setPower(-1 * gamepad2.right_stick_y);
//        }  else{
//            Viper.setPower(0);
//        }
       //   if(gamepad2.y) {
       //       Viper.setTargetPosition(1000);
       //       Viper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       //       Viper.setPower(-0.75);
      //    } else {
       //       Viper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       //       Viper.setPower(0);
      //    }



        telemetry.addData("goingUp", goingUp);
        telemetry.addData("goingDown", goingDown);

        telemetry.addData("Viper position", Viper.getCurrentPosition());
        telemetry.addData("Magnetic Switch", magnetFound);
    }


    public void bucketIntake() {
        if (gamepad2.dpad_up) {
            Bucket.setPosition(0.45);  // Set position to 0.5 (midpoint) when button A is pressed
        } else if (gamepad2.dpad_down) {
           Bucket.setPosition(0.23);    // Move to the minimum position when left bumper is pressed
        } else {
           Bucket.setPosition(0.45);  // Default position
        }
        telemetry.addData("Bucket", Bucket.getPosition());
        telemetry.update();

    }
}
