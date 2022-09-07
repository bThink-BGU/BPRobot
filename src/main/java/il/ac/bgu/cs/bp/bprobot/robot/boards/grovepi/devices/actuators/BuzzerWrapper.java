package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.devices.actuators;

import com.github.yafna.raspberry.grovepi.GroveDigitalOut;
import il.ac.bgu.cs.bp.bprobot.robot.boards.ActuatorWrapper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuzzerWrapper implements ActuatorWrapper {
    private Logger logger = Logger.getLogger(BuzzerWrapper.class.getName());
    private final GroveDigitalOut buzzer;

    public BuzzerWrapper(GroveDigitalOut buzzer){
        this.buzzer = buzzer;
        logger.setLevel(Level.SEVERE);
    }

    @Override
    public boolean setBooleanValue(boolean value) {
        try {
            buzzer.set(value);
            return true;
        } catch (IOException e) {
            logger.severe("Error when writing data to port");
            return false;
        }
    }

    public void setLogger(Logger logger){ this.logger=logger; }

    public Logger getLogger(){ return this.logger; }
}
