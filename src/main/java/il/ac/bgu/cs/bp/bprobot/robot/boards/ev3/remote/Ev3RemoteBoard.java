package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote;

import il.ac.bgu.cs.bp.bprobot.remote.model.ProtocolBase;
import il.ac.bgu.cs.bp.bprobot.remote.model.com.ICommunicator;
import il.ac.bgu.cs.bp.bprobot.remote.model.ev3.Ev3Protocol;
import il.ac.bgu.cs.bp.bprobot.robot.boards.DeviceWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.Ev3Board;
import lejos.internals.EV3DevPort;

public class Ev3RemoteBoard extends Ev3Board {
  protected ProtocolBase mProtocol;

  public Ev3RemoteBoard(ICommunicator comm, String name, boolean isMock) {
    super(name, isMock);
    mProtocol = new Ev3Protocol(comm);
  }

  @Override
  protected DeviceWrapper<?> createDeviceWrapper(String nickname, EV3DevPort port, String type, Object... ctorParams) throws Exception {
    return null;
  }

  @Override
  public void close() {
    mProtocol.close();
  }
}
