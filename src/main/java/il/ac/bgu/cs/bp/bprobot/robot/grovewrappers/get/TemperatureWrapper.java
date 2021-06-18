package il.ac.bgu.cs.bp.bprobot.robot.grovewrappers.get;

import org.iot.raspberry.grovepi.devices.GroveTemperatureAndHumiditySensor;
import org.iot.raspberry.grovepi.devices.GroveTemperatureAndHumidityValue;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TemperatureWrapper implements IGroveSensorGetWrapper {
    private Logger logger = Logger.getLogger(TemperatureWrapper.class.getName());
    private final GroveTemperatureAndHumiditySensor temperatureAndHumiditySensor;

    public TemperatureWrapper(GroveTemperatureAndHumiditySensor temperatureAndHumiditySensor) {
        this.temperatureAndHumiditySensor = temperatureAndHumiditySensor;
        logger.setLevel(Level.SEVERE);
    }

    @Override
    public Double get(int mode) {
        try {
            GroveTemperatureAndHumidityValue result = temperatureAndHumiditySensor.get();
            switch (mode){
                case 0:
                    return result.getTemperature();

                case 1:
                    return result.getHumidity();
            }
        } catch (IOException e) {
            logger.severe("Error when reading data from port");
        }
        return null;
    }

    public void setLogger(Logger logger){ this.logger=logger; }

    public Logger getLogger(){ return this.logger; }
}
