package il.ac.bgu.cs.bp.bprobot.robot.grovewrappers.get;

import org.iot.raspberry.grovepi.devices.GroveLightSensor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LightWrapper implements IGroveSensorGetWrapper {
    private Logger logger = Logger.getLogger(LightWrapper.class.getName());
    private final GroveLightSensor lightSensor;

    public LightWrapper(GroveLightSensor lightSensor) {
        this.lightSensor = lightSensor;
        logger.setLevel(Level.SEVERE);
    }

    @Override
    public Double get(int mode) {
        try {
            return lightSensor.get();
        } catch (IOException e) {
            logger.severe("Error when reading data from port");
            return null;
        }
    }

    public void setLogger(Logger logger){ this.logger=logger; }

    public Logger getLogger(){ return this.logger; }
}
