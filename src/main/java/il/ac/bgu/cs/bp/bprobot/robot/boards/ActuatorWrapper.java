package il.ac.bgu.cs.bp.bprobot.robot.boards;

import lejos.hardware.port.Port;

/**
 * Create a wrapper for all the sensors that have set function
 */
public abstract class ActuatorWrapper<T> extends DeviceWrapper<T> {
    protected ActuatorWrapper(String name, Port port, T device) {
        super(name, port, device);
    }
}
