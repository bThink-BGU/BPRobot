package il.ac.bgu.cs.bp.bprobot.robot.boards.grove.devices.sensors;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.GroveLightSensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.SensorWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grove.GrovePiPort;

import java.io.IOException;

public class LightWrapper extends SensorWrapper<GroveLightSensor> {
  LightWrapper(String name, GrovePiPort port, GrovePi grove) throws IOException {
    super(name, port, new GroveLightSensor(grove, port.ordinal()));
  }
  @Override
  protected void sample(float[] sample) throws Exception {
    double ret = device.get();
    sample[0] = (float) ret;
  }
}
