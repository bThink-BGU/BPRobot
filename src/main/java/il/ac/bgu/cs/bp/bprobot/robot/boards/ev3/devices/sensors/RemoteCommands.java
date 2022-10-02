package il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.devices.sensors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * * <b>EV3 Infra Red sensor, Remote mode</b><br>
 * In seek mode the sensor locates up to four beacons and provides bearing and distance of each beacon.
 * Returns the current remote command from the specified channel. Remote commands
 * are a single numeric value  which represents which button on the Lego IR
 * remote is currently pressed (0 means no buttons pressed). Four channels are
 * supported (0-3) which correspond to 1-4 on the remote. The button values are:<br>
 * 1 TOP-LEFT<br>
 * 2 BOTTOM-LEFT<br>
 * 3 TOP-RIGHT<br>
 * 4 BOTTOM-RIGHT<br>
 * 5 TOP-LEFT + TOP-RIGHT<br>
 * 6 TOP-LEFT + BOTTOM-RIGHT<br>
 * 7 BOTTOM-LEFT + TOP-RIGHT<br>
 * 8 BOTTOM-LEFT + BOTTOM-RIGHT<br>
 * 9 CENTRE/BEACON<br>
 * 10 BOTTOM-LEFT + TOP-LEFT<br>
 * 11 TOP-RIGHT + BOTTOM-RIGHT<br>
 */
public enum RemoteCommands {
  TOP_LEFT(1, 1),
  BOTTOM_LEFT(2, 2),
  TOP_RIGHT(4, 3),
  BOTTOM_RIGHT(8, 4),
  CENTER_BEACON(16, 9),

  TOP_LEFT_TOP_RIGHT(1 | 4, 5),
  TOP_LEFT_BOTTOM_RIGHT(1 | 8, 6),
  BOTTOM_LEFT_TOP_RIGHT(2 | 4, 7),
  BOTTOM_LEFT_BOTTOM_RIGHT(2 | 8, 8),
  BOTTOM_LEFT_TOP_LEFT(1 | 2, 10),
  TOP_RIGHT_BOTTOM_RIGHT(4 | 8, 11),
  ;
  public final int bit;
  public final int EV3DevID;

  private RemoteCommands(int bit, int ev3DevID) {
    this.bit = bit;
    EV3DevID = ev3DevID;
  }

  public EnumSet<RemoteCommands> toSet() {
    return EnumSet.range(TOP_LEFT, CENTER_BEACON).stream()
        .filter(c -> (c.bit & this.bit) != 0)
        .collect(Collectors.toCollection(() -> EnumSet.noneOf(RemoteCommands.class)));
  }

  public static EnumSet<RemoteCommands> empty() {
    return EnumSet.noneOf(RemoteCommands.class);
  }

  public static EnumSet<RemoteCommands> fromEV3DevID(int id) {
    if (id == 0) {
      return EnumSet.noneOf(RemoteCommands.class);
    }
    var command = Arrays.stream(values()).filter(c -> c.EV3DevID == id).findFirst();
    if (command.isPresent()) {
      return command.get().toSet();
    } else
      throw new IllegalArgumentException("No such command: " + id);
  }
}
