package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.actuators;

import com.github.yafna.raspberry.grovepi.devices.GroveLed;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LedWrapper implements GroveSensorActuatorWrapper {
    private Logger logger = Logger.getLogger(LedWrapper.class.getName());
    private final GroveLed led;

    public LedWrapper(GroveLed led){
        this.led = led;
        logger.setLevel(Level.SEVERE);
    }

    @Override
    public boolean set(boolean value) {
        try {
            led.set(value);
            return true;
        } catch (IOException e) {
            logger.severe("Error when writing data to port");
            return false;
        }
    }

    public void setLogger(Logger logger){ this.logger=logger; }

    public Logger getLogger(){ return this.logger; }
}
