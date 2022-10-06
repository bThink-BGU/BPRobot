package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3;

import ev3dev.hardware.EV3DevMotorDevice;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Device;
import lejos.hardware.port.Port;

public class EV3DevMotorDeviceWrapper extends Device<EV3DevMotorDevice> {

  public EV3DevMotorDeviceWrapper(String board, String name, Port port, EV3DevMotorDevice device) {
    super(board, name, port, device);
  }
}
