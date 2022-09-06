package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.get;

import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.IGroveDeviceSensorWrapper;

public interface IGroveSensorGetWrapper extends IGroveDeviceSensorWrapper {
    Double get(int mode);
}
