package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.actuators;

import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.IGroveDeviceSensorWrapper;

/**
 * Create a wrapper for all the sensors that have set function
 */
public interface GroveSensorActuatorWrapper extends IGroveDeviceSensorWrapper {
    boolean set(boolean value);
}
