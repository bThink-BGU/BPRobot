package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums;

import ev3dev.sensors.Battery;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.Ev3RemoteDevice;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.Ev3RemoteMotor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices.Ev3RemoteSensor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotorListener;
import lejos.robotics.SampleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class DeviceType extends RemoteCode {
  public static final DeviceType DONT_CHANGE = new DeviceType("DONT_CHANGE", (byte) 0x00) {
    @Override
    public <T> T execute(Ev3RemoteDevice device, String command, Object... args) {
      throw new UnsupportedOperationException();
    }
  };
  public static final DeviceType L_MOTOR = new DeviceType("L_MOTOR", (byte) 0x07) {

    @Override
    public <T> T execute(Ev3RemoteDevice device, String command, Object... args) {
      BaseRegulatedMotor motor = new BaseRegulatedMotor((Ev3RemoteMotor) device, 4.0F, 0.04F, 10.0F, 2.0F, 0.02F, 8.0F, 0, 1050);
      throw new UnsupportedOperationException();
    }


  };
  public static final DeviceType M_MOTOR = new DeviceType("M_MOTOR", (byte) 0x08) {
    @Override
    public <T> T execute(Ev3RemoteDevice device, String command, Object... args) {
      throw new UnsupportedOperationException();
    }
  };
  public static final DeviceType ALL_MOTORS = new DeviceType("ALL_MOTORS", (byte) 0x0F) {
    @Override
    public <T> T execute(Ev3RemoteDevice device, String command, Object... args) {
      throw new UnsupportedOperationException();
    }
  };
  public static final DeviceType EV3_TOUCH = new DeviceType("EV3_TOUCH", (byte) 0x10) {
    @Override
    public <T> T execute(Ev3RemoteDevice device, String command, Object... args) throws NoSuchMethodException {
      if (!"isPressed".equals(command)) throw new NoSuchMethodException();
      return (T) (Object) isPressed((Ev3RemoteSensor) device);
    }

    public Boolean isPressed(Ev3RemoteSensor device) {
      float[] sample = new float[1];
      device.fetchSample(sample, 0);
      return sample[0] != 0.0F;
    }
  };
  public static final DeviceType EV3_COLOR = new DeviceType("EV3_COLOR", (byte) 0x1D) {
    @Override
    public <T> T execute(Ev3RemoteDevice device, String command, Object... args) throws NoSuchMethodException {
      var sensor = (Ev3RemoteSensor) device;
      switch (command) {
        case "getColorID":
          return (T) getColorID(sensor);
        case "isFloodlightOn":
          return (T) isFloodlightOn(sensor);
        case "getFloodlight":
          return (T) getFloodlight(sensor);
        case "setFloodlight":
          if (args[0] instanceof Boolean) {
            setFloodlight(sensor, (Boolean) args[0]);
          } else
            setFloodlight(sensor, (Integer) args[0]);
          return null;
        case "getColorIDMode":
          return (T) getColorIDMode(sensor);
        case "getRedMode":
          return (T) getRedMode(sensor);
        case "getAmbientMode":
          return (T) getAmbientMode(sensor);
        case "getRGBMode":
          return (T) getRGBMode(sensor);
      }
      throw new NoSuchMethodException();
    }

    public Integer getColorID(Ev3RemoteSensor device) {
      float[] sample = new float[1];
      device.setCurrentMode(DeviceMode.get(EV3_COLOR, "COL-COLOR"));
      device.fetchSample(sample, 0);
      return (int) sample[0];
    }

    public Boolean isFloodlightOn(Ev3RemoteSensor device) {
      return this.getFloodlight(device) != 0;
    }

    public Integer getFloodlight(Ev3RemoteSensor device) {
      switch (device.getCurrentModeName()) {
        case "COL-COLOR":
        case "RGB-RAW":
          return 6;
        case "REF-RAW":
        case "COL-REFLECT":
          return 5;
        case "COL-AMBIENT":
        case "COL-CAL":
        default:
          return 0;
      }
    }

    public void setFloodlight(Ev3RemoteSensor device, boolean floodlight) {
      this.setFloodlight(device, floodlight ? 5 : 2);
    }

    public boolean setFloodlight(Ev3RemoteSensor device, int color) {
      String mode;
      switch (color) {
        case 2:
          mode = "COL-AMBIENT";
          break;
        case 3:
        case 4:
        default:
          throw new IllegalArgumentException("Invalid color specified");
        case 5:
          mode = "COL-REFLECT";
          break;
        case 6:
          mode = "COL-COLOR";
      }

      device.setCurrentMode(mode);
      return true;
    }

    public SensorMode getColorIDMode(Ev3RemoteSensor device) {
      device.setCurrentMode("COL-COLOR");
      return device.getMode("COL-COLOR");
    }

    public SensorMode getRedMode(Ev3RemoteSensor device) {
      device.setCurrentMode("COL-REFLECT");
      return device.getMode("COL-REFLECT");
    }

    public SensorMode getAmbientMode(Ev3RemoteSensor device) {
      device.setCurrentMode("COL-AMBIENT");
      return device.getMode("COL-AMBIENT");
    }

    public SensorMode getRGBMode(Ev3RemoteSensor device) {
      device.setCurrentMode("RGB-RAW");
      return device.getMode("RGB-RAW");
    }
  };
  public static final DeviceType EV3_ULTRASONIC = new DeviceType("EV3_ULTRASONIC", (byte) 0x1E) {
    @Override
    public <T> T execute(Ev3RemoteDevice device, String command, Object... args) throws NoSuchMethodException {
      var sensor = (Ev3RemoteSensor) device;
      switch (command) {
        case "getListenMode":
          return (T) getListenMode(sensor);
        case "getDistanceMode":
          return (T) getDistanceMode(sensor);
        case "enable":
          enable(sensor);
          return null;
        case "disable":
          disable(sensor);
          return null;
        case "isEnabled":
          return (T) (Object) isEnabled(sensor);
      }
      throw new NoSuchMethodException();
    }

    public SampleProvider getListenMode(Ev3RemoteSensor sensor) {
      sensor.setCurrentMode("US-LISTEN");
      return sensor.getMode("US-LISTEN");
    }

    public SampleProvider getDistanceMode(Ev3RemoteSensor sensor) {
      sensor.setCurrentMode("US-DIST-CM");
      return sensor.getMode("US-DIST-CM");
    }

    public void enable(Ev3RemoteSensor sensor) {
      getDistanceMode(sensor);
    }

    public void disable(Ev3RemoteSensor sensor) {
      sensor.setCurrentMode("US-SI-CM");
      sensor.getMode("US-SI-CM");
    }

    public boolean isEnabled(Ev3RemoteSensor sensor) {
      return !Objects.equals(sensor.getCurrentModeName(), "US-SI-CM");
    }
  };
  public static final DeviceType EV3_GYRO = new DeviceType("EV3_GYRO", (byte) 0x20) {
    @Override
    public <T> T execute(Ev3RemoteDevice device, String command, Object... args) throws NoSuchMethodException {
      var sensor = (Ev3RemoteSensor) device;
      switch (command) {
        case "getAngleMode":
          return (T) getAngleMode(sensor);
        case "getRateMode":
          return (T) getRateMode(sensor);
        case "getAngleAndRateMode":
          return (T) getAngleAndRateMode(sensor);
        case "reset":
          reset(sensor);
          return null;
      }
      throw new NoSuchMethodException();
    }
    public SampleProvider getRateMode(Ev3RemoteSensor sensor) {
      sensor.setCurrentMode("GYRO-RATE");
      return sensor.getMode("GYRO-RATE");
    }

    public SampleProvider getAngleMode(Ev3RemoteSensor sensor) {
      sensor.setCurrentMode("GYRO-ANG");
      return sensor.getMode("GYRO-ANG");
    }

    public SampleProvider getAngleAndRateMode(Ev3RemoteSensor sensor) {
      sensor.setCurrentMode("GYRO-G&A");
      return sensor.getMode("GYRO-G&A");
    }

    public void reset(Ev3RemoteSensor sensor) {
      sensor.setCurrentMode("GYRO-RATE");
      sensor.setCurrentMode("GYRO-G&A");
    }
  };
  public static final DeviceType EV3_IR = new DeviceType("EV3_IR", (byte) 0x21) {
    @Override
    public <T> T execute(Ev3RemoteDevice device, String command, Object... args) throws NoSuchMethodException {
      var sensor = (Ev3RemoteSensor) device;
      switch (command) {
        case "getDistanceMode":
          return (T) getDistanceMode(sensor);
        case "getSeekMode":
          return (T) getSeekMode(sensor);
        case "getRemoteMode":
          return (T) getRemoteMode(sensor);
        case "getRemoteCommand":
          return (T) getRemoteCommand(sensor, (Integer) args[0]);
        case "getRemoteCommands":
          getRemoteCommands(sensor, (byte[]) args[0], (int) args[1], (int) args[2]);
          return null;
      }
      throw new NoSuchMethodException();
    }
    public SensorMode getDistanceMode(Ev3RemoteSensor sensor) {
      sensor.setCurrentMode("IR-PROX");
      return sensor.getMode("IR-PROX");
    }

    public SensorMode getSeekMode(Ev3RemoteSensor sensor) {
      sensor.setCurrentMode("IR-SEEK");
      return sensor.getMode("IR-SEEK");
    }

    public SensorMode getRemoteMode(Ev3RemoteSensor sensor) {
      sensor.setCurrentMode("IR-REMOTE");
      return sensor.getMode("IR-REMOTE");
    }

    public Integer getRemoteCommand(Ev3RemoteSensor sensor, int chan) {
      if (chan >= 0 && chan < 4) {
        float[] samples = new float[4];
        sensor.fetchSample(samples, 0);
        return (int)samples[chan];
      } else {
        throw new IllegalArgumentException("Bad channel");
      }
    }

    public void getRemoteCommands(Ev3RemoteSensor sensor, byte[] cmds, int offset, int len) {
      float[] samples = new float[4];
      sensor.fetchSample(samples, 0);
      for(int i = 0; i < 4; ++i) {
        cmds[offset + i] = (byte)((int)samples[i]);
      }
    }
  };

  private static final List<DeviceType> values = List.of(
      L_MOTOR,
      M_MOTOR,
      EV3_TOUCH,
      EV3_COLOR,
      EV3_ULTRASONIC,
      EV3_GYRO,
      EV3_IR
  );

  private DeviceType(String name, byte code) {
    super(name, code);
  }

  public abstract <T> T execute(Ev3RemoteDevice device, String command, Object... args) throws NoSuchMethodException;

  public static DeviceType fromCode(byte code) {
    for (DeviceType type : values) {
      if (type.code == code) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown device type: " + code);
  }

  private static class BaseRegulatedMotor {
    private static final Logger log = LoggerFactory.getLogger(BaseRegulatedMotor.class);
    protected final int MAX_SPEED_AT_9V;
    private int speed = 360;
    protected int acceleration = 6000;
    private boolean regulationFlag = true;
    private final Ev3RemoteMotor motor;

    public BaseRegulatedMotor(Ev3RemoteMotor motor, float moveP, float moveI, float moveD, float holdP, float holdI, float holdD, int offset, int maxSpeed) {
      this.motor = motor;
      this.MAX_SPEED_AT_9V = maxSpeed;

      /*this.detect("lego-port", port);
      if (log.isDebugEnabled()) {
        log.debug("Setting port in mode: {}", "tacho-motor");
      }

      this.setStringAttribute("mode", "tacho-motor");
      Delay.msDelay(1000L);
      this.detect("tacho-motor", port);
      Delay.msDelay(1000L);
      this.setStringAttribute("command", "reset");
      if (log.isDebugEnabled()) {
        log.debug("Motor ready to use on Port: {}", motorPort.getName());
      }*/

    }

    public Boolean suspendRegulation() {
      this.regulationFlag = false;
      return this.regulationFlag;
    }

    public Integer getTachoCount() {
      return this.getIntegerAttribute("position");
    }

    public Float getPosition() {
      return (float)this.getTachoCount();
    }

    public void forward() {
      this.setSpeedDirect(this.speed);
      if (!this.regulationFlag) {
        this.setStringAttribute("command", "run-direct");
      } else {
        this.setStringAttribute("command", "run-forever");
      }
    }

    public void backward() {
      this.setSpeedDirect(-this.speed);
      if (!this.regulationFlag) {
        this.setStringAttribute("command", "run-direct");
      } else {
        this.setStringAttribute("command", "run-forever");
      }
    }

    public void flt(boolean immediateReturn) {
      this.doStop("coast", immediateReturn);
    }

    public void flt() {
      this.flt(false);
    }

    public void coast() {
      this.doStop("coast", false);
    }

    public void brake() {
      this.doStop("brake", false);
    }

    public void hold() {
      this.doStop("hold", false);
    }

    public void stop() {
      this.stop(false);
    }

    public void stop(boolean immediateReturn) {
      this.doStop("hold", immediateReturn);
    }

    private void doStop(String mode, boolean immediateReturn) {
      if(!immediateReturn) {
        throw new UnsupportedOperationException("Cannot stop motors without immediate return");
      }
      this.setStringAttribute("stop_action", mode);
      this.setStringAttribute("command", "stop");
    }

    public boolean isMoving() {
      return this.getStringAttribute("state").contains("running");
    }

    public void setSpeed(int speed) {
      this.speed = speed;
      this.setSpeedDirect(speed);
    }

    private void setSpeedDirect(int speed) {
      if (!this.regulationFlag) {
        this.setIntegerAttribute("duty_cycle_sp", speed);
      } else {
        this.setIntegerAttribute("speed_sp", speed);
      }

    }

    public void resetTachoCount() {
      this.setStringAttribute("command", "reset");
      this.regulationFlag = true;
    }

    public void rotate(int angle, boolean immediateReturn) {
      if(!immediateReturn) {
        throw new UnsupportedOperationException("Cannot stop motors without immediate return");
      }
      this.setSpeedDirect(this.speed);
      this.setIntegerAttribute("position_sp", angle);
      this.setStringAttribute("command", "run-to-rel-pos");
    }

    public void rotate(int angle) {
      this.rotate(angle, false);
    }

    public void rotateTo(int limitAngle, boolean immediateReturn) {
      if(!immediateReturn) {
        throw new UnsupportedOperationException("Cannot stop motors without immediate return");
      }
      this.setSpeedDirect(this.speed);
      this.setIntegerAttribute("position_sp", limitAngle);
      this.setStringAttribute("command", "run-to-abs-pos");
    }

    public void rotateTo(int limitAngle) {
      this.rotateTo(limitAngle, false);
    }

    public int getSpeed() {
      return !this.regulationFlag ? this.getIntegerAttribute("duty_cycle_sp") : this.getIntegerAttribute("speed_sp");
    }

    public boolean isStalled() {
      return this.getStringAttribute("state").contains("stalled");
    }

    public int getRotationSpeed() {
      return 0;
    }

    public void addListener(RegulatedMotorListener regulatedMotorListener) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    public RegulatedMotorListener removeListener() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    public void waitComplete() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getMaxSpeed() {
      return Battery.getInstance().getVoltage() * (float)this.MAX_SPEED_AT_9V / 9.0F * 0.9F;
    }

    public void setAcceleration(int acceleration) {
      this.acceleration = Math.abs(acceleration);
      log.warn("Not executed internally the method: setAcceleration");
    }
  }
}
