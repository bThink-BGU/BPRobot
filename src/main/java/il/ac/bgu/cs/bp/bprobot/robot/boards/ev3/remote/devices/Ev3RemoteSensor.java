package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices;

import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.DeviceMode;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.DeviceType;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.Port;
import il.ac.bgu.cs.bp.bprobot.robot.boards.GenericSensorMode;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Sensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.ProtocolBase;
import lejos.hardware.sensor.SensorMode;

import java.util.Map;

public abstract class Ev3RemoteSensor extends Sensor<DeviceType> implements Ev3RemoteDevice {
  protected final ProtocolBase protocol;

  public Ev3RemoteSensor(String board, Port port, ProtocolBase protocol, String name, DeviceType device) {
    super(board, name, port, device, DeviceMode.DeviceModes.get(device).stream().map(m -> new GenericSensorMode(m.sampleSize, m.name)).toArray(SensorMode[]::new));
    this.protocol = protocol;
  }

  @Override
  protected void sample(float[] sample) throws Exception {
    var mode = (GenericSensorMode) getMode(getCurrentMode());
    var deviceMode = DeviceMode.DeviceModes.get(device).get(getCurrentMode());
    assert deviceMode.sampleSize == mode.sampleSize();
    var sampleDevice = (float[]) protocol.exec(deviceMode.unit.command, Map.of("mode", deviceMode, "port", port, "nvalue", deviceMode.sampleSize)).get("value");
    System.arraycopy(sampleDevice, 0, sample, 0, sample.length);
  }

  @Override
  public ProtocolBase getProtocol() {
    return protocol;
  }

  @Override
  public void runVoidMethod(String name, Object... params) throws NoSuchMethodException {
    try{
      super.runVoidMethod(name, params);
    } catch (NoSuchMethodException e) {
      device.execute(this, name, params);
    }

  }

  @Override
  public <V> V runMethod(String name, Object... params) throws NoSuchMethodException {
    try{
      return super.runMethod(name, params);
    } catch (NoSuchMethodException e) {
      return device.execute(this, name, params);
    }
  }
}
