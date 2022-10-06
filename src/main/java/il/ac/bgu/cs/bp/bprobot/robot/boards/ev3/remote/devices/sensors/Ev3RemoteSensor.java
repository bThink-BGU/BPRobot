package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.sensors;

import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.DeviceMode;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.Port;
import il.ac.bgu.cs.bp.bprobot.robot.boards.GenericSensorMode;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Sensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.RemoteDevice;
import lejos.hardware.sensor.SensorMode;

import java.util.Map;

public abstract class Ev3RemoteSensor extends Sensor<RemoteDevice> {
  public Ev3RemoteSensor(String board, String name, RemoteDevice device) {
    super(board, name, device.port, device, DeviceMode.DeviceModes.get(device.type).stream().map(m -> new GenericSensorMode(m.sampleSize, m.name)).toArray(SensorMode[]::new));
  }

  @Override
  public void setCurrentMode(int mode) {
    super.setCurrentMode(mode);
//    device.protocol.exec()
  }

  @Override
  protected void sample(float[] sample) throws Exception {
    var mode = (GenericSensorMode) getMode(getCurrentMode());
    var deviceMode = DeviceMode.DeviceModes.get(device.type).get(getCurrentMode());
    assert deviceMode.sampleSize == mode.sampleSize();
    var sampleDevice = (float[]) device.protocol.exec(deviceMode.subCommand, Map.of("port", port, "nvalue", deviceMode.sampleSize)).get("value");
    System.arraycopy(sampleDevice, 0, sample, 0, sample.length);
  }

  //TODO: reimplement below
  @Override
  public void runVoidMethod(String name, Object... params) throws NoSuchMethodException {
    super.runVoidMethod(name, params);
  }

  @Override
  public <V> V runMethod(String name, Object... params) throws NoSuchMethodException {
    return super.runMethod(name, params);
  }
}
