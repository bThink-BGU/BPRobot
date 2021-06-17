package il.ac.bgu.cs.bp.bprobot.robot.Boards;

import il.ac.bgu.cs.bp.bprobot.robot.Enums.IPortEnums;

public class DriveDataObject {
    private IPortEnums port;
    private double speed;
    private int angle;

    public DriveDataObject(IPortEnums port, double speed, int angle) {
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

    public IPortEnums getPort() {
        return port;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setPort(IPortEnums port) {
        this.port = port;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
