package il.ac.bgu.cs.bp.bprobot.robot.grovewrappers.get;

import org.iot.raspberry.grovepi.devices.GroveRotarySensor;
import org.iot.raspberry.grovepi.devices.GroveRotaryValue;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RotaryWrapper implements IGroveSensorGetWrapper {
    private Logger logger = Logger.getLogger(RotaryWrapper.class.getName());
    private final GroveRotarySensor rotarySensor;

    public RotaryWrapper(GroveRotarySensor rotarySensor) {
        this.rotarySensor = rotarySensor;
        logger.setLevel(Level.SEVERE);
    }

    @Override
    public Double get(int mode) {
        try {
            GroveRotaryValue result = rotarySensor.get();
            switch (mode){
                case 0:
                    return result.getSensorValue();

                case 1:
                    return result.getVoltage();

                case 2:
                    return result.getDegrees();
            }
        } catch (IOException e) {
            logger.severe("Error when reading data from port");
        }
        return null;
    }

    public void setLogger(Logger logger){ this.logger=logger; }

    public Logger getLogger(){ return this.logger; }
}
