package il.ac.bgu.cs.bp.bprobot.robot.boards;


import lejos.hardware.port.MotorPort;

public class DriveDataObject {
    private MotorPort port;
    private double speed;
    private int angle;

    public DriveDataObject(MotorPort port, double speed, int angle) {
        this.port = port;
        this.speed = speed;
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public double getSpeed() {
        return speed;
    }

    public MotorPort getPort() {
        return port;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setPort(MotorPort port) {
        this.port = port;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
