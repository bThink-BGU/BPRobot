package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.devices.sensors;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.devices.GroveRotarySensor;
import il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.GrovePiPort;

import java.io.IOException;

public class RotaryWrapper extends GroveSensorWrapper<GroveRotarySensor> {
  RotaryWrapper(String name, GrovePiPort port, GrovePi grove) throws IOException {
    super(name, port, new GroveRotarySensor(grove, port.ordinal()),
        new GenericGroveMode(4, "DEFAULT"));
  }

  @Override
  protected void sample(float[] sample) throws Exception {
    var res = device.get();
    sample[0] = (float) res.getSensorValue();
    sample[1] = (float) res.getVoltage();
    sample[2] = (float) res.getDegrees();
    sample[3] = (float) res.getFactor();
  }
}
