package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.sensors;

import com.github.yafna.raspberry.grovepi.devices.GroveUltrasonicRanger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UltrasonicWrapper implements IGroveSensorGetWrapper {

    private static final Logger logger = Logger.getLogger(UltrasonicWrapper.class.getName());
    private final GroveUltrasonicRanger ultrasonicRanger;

    public UltrasonicWrapper(GroveUltrasonicRanger ultrasonicRanger){
        this.ultrasonicRanger = ultrasonicRanger;
        logger.setLevel(Level.SEVERE);
    }

    @Override
    public Double get(int mode) {
        try {
            return ultrasonicRanger.get();
        } catch (IOException e) {
            logger.severe("Error when reading data from port");
            return null;
        }
    }
}
