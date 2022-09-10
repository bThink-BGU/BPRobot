package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.devices.sensors;

import ev3dev.sensors.BaseSensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.SensorMode;
import lejos.internals.EV3DevPort;

import java.util.ArrayList;

public class Ev3BaseSensorWrapper extends SensorWrapper<BaseSensor> {
  public Ev3BaseSensorWrapper(String name, EV3DevPort port, BaseSensor device) {
    super(name, port, device);
  }
  @Override
  public ArrayList<String> getAvailableModes() {
    return device.getAvailableModes();
  }

  @Override
  public SensorMode getMode(int i) {
    return device.getMode(i);
  }

  @Override
  public SensorMode getMode(String s) {
    return device.getMode(s);
  }

  @Override
  public void setCurrentMode(int i) {
    device.setCurrentMode(i);
  }

  @Override
  public void setCurrentMode(String s) {
    device.setCurrentMode(s);
  }

  @Override
  public int getCurrentMode() {
    return device.getCurrentMode();
  }

  @Override
  public int getModeCount() {
    return device.getModeCount();
  }

  @Override
  public int sampleSize() {
    return device.sampleSize();
  }

  @Override
  protected void sample(float[] sample) throws Exception {
    throw new UnsupportedOperationException();
  }

  @Override
  public void fetchSample(float[] floats, int i) {
    device.fetchSample(floats, i);
  }
}