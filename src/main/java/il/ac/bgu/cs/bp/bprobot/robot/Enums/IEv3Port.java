package il.ac.bgu.cs.bp.bprobot.robot.Enums;

public interface IEv3Port extends IPortEnums {

    static IEv3Port getPortType(String port) {
        char portChar = port.charAt(0);
        if (portChar >= 65 && portChar <= 68) {
            return Ev3DrivePort.valueOf(port);
        } else {
            return Ev3SensorPort.valueOf(port);
        }
    }
}
