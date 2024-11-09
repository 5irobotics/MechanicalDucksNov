package org.firstinspires.ftc.teamcode.Common;

import androidx.lifecycle.ViewModelProvider;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class SnakeArm  {

    private Servo SnakeJaw;
    private Servo SnakeShoulder;
    private Servo SnakeElbow;
    private Gamepad gamepad2;


    /**
     * Describe this function...
     */
    public void initArm(HardwareMap hardwareMap, Gamepad gamepad2) {
        SnakeJaw = hardwareMap.get(Servo.class, "SnakeJaw");
        SnakeShoulder = hardwareMap.get(Servo.class, "SnakeShoulder");
        SnakeElbow = hardwareMap.get(Servo.class, "SnakeElbow");
        this.gamepad2 = gamepad2;
        moveArm(0.3296, 0.587);
    }

    /**
     * Describe this function...
     */
    public void runArm() {
        //pick up
        if (this.gamepad2.b) {
            moveArm(0.11, 0.37); //585);
        }
        //drop off - 0.3296,0.65
        else if (this.gamepad2.a) {
            moveArm(0.44, 0.64); // .45, .67
        }
        //rest
        else if (this.gamepad2.x) {
            moveArm(0.3296, 0.587);
        }
        if (this.gamepad2.left_bumper) {
            //0.4 for other robot
            SnakeJaw.setPosition(0.75); //.7
        } else {
            SnakeJaw.setPosition(0);
        }
    }

    /**
     * Describe this function...
     */
    private void moveArm(double shoulder, double elbow) {
        SnakeShoulder.setPosition(shoulder);
        //telemetry.addData("shoulder", shoulder);
        SnakeElbow.setPosition(elbow);
        //telemetry.addData("elbow", elbow);
        //telemetry.update();
    }
}