package il.ac.bgu.cs.bp.bprobot.robot.grovewrappers.get;

import org.iot.raspberry.grovepi.devices.GroveUltrasonicRanger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UltrasonicWrapper implements IGroveSensorGetWrapper {

    private Logger logger = Logger.getLogger(UltrasonicWrapper.class.getName());
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

    public void setLogger(Logger logger){ this.logger=logger; }

    public Logger getLogger(){ return this.logger; }
}
