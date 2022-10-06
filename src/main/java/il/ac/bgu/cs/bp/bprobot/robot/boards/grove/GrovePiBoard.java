package il.ac.bgu.cs.bp.bprobot.robot.boards.grove;

import com.github.yafna.raspberry.grovepi.GrovePi;
import com.github.yafna.raspberry.grovepi.pi4j.GrovePi4J;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Board;
import il.ac.bgu.cs.bp.bprobot.robot.boards.Device;
import il.ac.bgu.cs.bp.bprobot.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrovePiBoard extends Board<GrovePiPort> {
  private final GrovePi grovePi;
  private static final Logger logger = Logger.getLogger(GrovePiBoard.class.getName());

  public GrovePiBoard(String name, boolean isMock) {
    super(name, List.of("il.ac.bgu.cs.bp.bprobot.robot.boards.grove.devices"), isMock);
    try {
      grovePi = new GrovePi4J();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    logger.setLevel(Level.SEVERE);
  }

  @Override
  protected Device createDeviceWrapper(String nickname, GrovePiPort port, String type, Object... ctorParams) throws Exception {
    Class<?> cl = ReflectionUtils.getClass(type, packages);
    Constructor<?> constructor;
    Class<?>[] ctorParamsTypes = new Class<?>[ctorParams.length + 3];
    Object[] fullCtorParams = new Object[ctorParams.length + 3];
    ctorParamsTypes[0] = String.class;
    ctorParamsTypes[1] = String.class;
    ctorParamsTypes[2] = GrovePiPort.class;
    ctorParamsTypes[3] = GrovePi.class;
    for (int i = 0; i < ctorParams.length; i++) {
      ctorParamsTypes[i + 3] = ctorParams[i].getClass();
    }
    fullCtorParams[0] = nickname;
    fullCtorParams[1] = port;
    fullCtorParams[2] = grovePi;
    System.arraycopy(ctorParams, 0, fullCtorParams, 3, ctorParams.length);
    try {
      constructor = cl.getConstructor(ctorParamsTypes);
    } catch (NoSuchMethodException e) {
      throw new NoSuchMethodException("No constructor found for " + type + " with params " + Arrays.toString(ctorParamsTypes));
    }
    return (Device) constructor.newInstance(fullCtorParams);
  }

  @Override
  public GrovePiPort getPort(String portName) {
    return GrovePiPort.valueOf(portName);
  }

  @Override
  public void close() {
    grovePi.close();
  }
}
