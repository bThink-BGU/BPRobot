package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.sensors;

import il.ac.bgu.cs.bp.bprobot.remote.command.CommandBase;
import il.ac.bgu.cs.bp.bprobot.remote.machine.device.port.DevicePort;
import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;
import il.ac.bgu.cs.bp.bprobot.remote.model.ProtocolBase;
import il.ac.bgu.cs.bp.bprobot.robot.boards.GenericSensorMode;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Sensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.RemoteDevice;
import lejos.hardware.sensor.SensorMode;

import java.util.Map;

public abstract class Ev3RemoteSensor extends Sensor<RemoteDevice> {
  private final ProtocolBase protocol;
  private final byte type;

  public Ev3RemoteSensor(String board, String name, DevicePort port, ProtocolBase protocol, byte type, RemoteDevice device, SensorMode... modes) {
    super(board, name, port, device, modes);
    this.protocol = protocol;
    this.type = type;
  }

  public Map<String, Object> exec(CommandBase command) {
    return protocol.exec(((DevicePort) port).getRaw(), command);
  }

  @Override
  protected void sample(float[] sample) throws Exception {
    var mode = (GenericSensorMode) getMode(getCurrentMode());
    CommandType commandType = mode.getSampleType().getCommandType();
    CommandBase cmd = CommandFactory.createCommand(commandType,
        Map.of("nargs", mode.sampleSize(), "type", type));
    exec(cmd);
  }

  @Override
  public void runVoidMethod(String name, Object... params) throws NoSuchMethodException {
    super.runVoidMethod(name, params);
  }

  @Override
  public <V> V runMethod(String name, Object... params) throws NoSuchMethodException {
    return super.runMethod(name, params);
  }
}
