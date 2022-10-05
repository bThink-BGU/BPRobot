package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.sensors;

import ev3dev.sensors.BaseSensor;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandBase;
import il.ac.bgu.cs.bp.bprobot.remote.command.CommandFactory;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.DevicePort;
import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;
import il.ac.bgu.cs.bp.bprobot.robot.boards.GenericSensorMode;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SampleType;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.RemoteDeviceBase;
import lejos.internals.EV3DevPort;

import java.util.Map;

public class Ev3RemoteSensorWrapper extends SensorWrapper<RemoteDeviceBase> {
  public Ev3RemoteSensorWrapper(String board, String name, DevicePort port, RemoteDeviceBase device) {
    super(board, name, port, device);
  }

  @Override
  protected void sample(float[] sample) throws Exception {
    var mode = (GenericSensorMode)getMode(getCurrentMode());
    CommandType commandType = mode.getSampleType().getCommandType();
    CommandBase cmd = CommandFactory.createCommand(commandType, Map.of("nargs", mode.sampleSize()));
    device.exec(cmd);
  }
}
