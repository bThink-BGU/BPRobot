package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3;

import ev3dev.sensors.BaseSensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.SensorMode;

import java.util.ArrayList;

public class Ev3SensorWrapper extends SensorWrapper<BaseSensor> {
  protected Ev3SensorWrapper(String name, Port port, BaseSensor device) {
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
  public String getName() {
    return device.getName();
  }

  @Override
  public int sampleSize() {
    return device.sampleSize();
  }

  @Override
  public void fetchSample(float[] floats, int i) {
    device.fetchSample(floats, i);
  }
}
