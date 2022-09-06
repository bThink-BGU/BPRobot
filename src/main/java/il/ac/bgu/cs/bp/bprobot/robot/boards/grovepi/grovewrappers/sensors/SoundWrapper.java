package il.ac.bgu.cs.bp.bprobot.robot.boards.grovepi.grovewrappers.sensors;

import com.github.yafna.raspberry.grovepi.devices.GroveSoundSensor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoundWrapper implements IGroveSensorGetWrapper {

    private Logger logger = Logger.getLogger(SoundWrapper.class.getName());
    private final GroveSoundSensor soundSensor;

    public SoundWrapper(GroveSoundSensor soundSensor){
        this.soundSensor = soundSensor;
        logger.setLevel(Level.SEVERE);
    }

    @Override
    public Double get(int mode) {
        try {
            return soundSensor.get();
        } catch (IOException e) {
            logger.severe("Error when reading data from port");
            return null;
        }
    }

    public void setLogger(Logger logger){ this.logger=logger; }

    public Logger getLogger(){ return this.logger; }
}
