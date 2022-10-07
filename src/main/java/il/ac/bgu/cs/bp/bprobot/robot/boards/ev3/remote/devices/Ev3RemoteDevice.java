package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.devices;

import il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.protocol.ProtocolBase;

public interface Ev3RemoteDevice {
  ProtocolBase getProtocol();
}
