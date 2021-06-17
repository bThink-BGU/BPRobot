package il.ac.bgu.cs.bp.bprobot.robot.Boards;

import il.ac.bgu.cs.bp.bprobot.robot.Enums.GrovePiPort;
import il.ac.bgu.cs.bp.bprobot.robot.GroveWrappers.GetWrappers.IGroveSensorGetWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.GroveWrappers.SetWrappers.IGroveSensorSetWrapper;
import org.iot.raspberry.grovepi.pi4j.GrovePi4J;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrovePiBoard extends GrovePi4J implements IBoard<GrovePiPort> {

    private final Logger logger = Logger.getLogger(GrovePiBoard.class.getName());
    /**
     * Map for the sensors that have set function.
     * key - port of the sensor, value - the specific sensor
     */
    private Map<String, IGroveSensorSetWrapper> sensorSetMap;
    /**
     * Map for the sensors that have get function.
     * key - port of the sensor, value - the specific sensor
     */
    private Map<String, IGroveSensorGetWrapper> sensorGetMap;


    protected final Map<GrovePiPort, Integer> sensorModes = new HashMap<>();

    public GrovePiBoard(Map<String, IGroveSensorGetWrapper> sensorGetMap, Map<String, IGroveSensorSetWrapper> sensorSetMap) throws IOException {
        super();
        sensorModes.putAll(Map.of(
                GrovePiPort.A0, 0,
                GrovePiPort.A1, 0,
                GrovePiPort.A2, 0,
                GrovePiPort.D2, 0,
                GrovePiPort.D3, 0,
                GrovePiPort.D4, 0,
                GrovePiPort.D5, 0,
                GrovePiPort.D6, 0,
                GrovePiPort.D7, 0,
                GrovePiPort.D8, 0
        ));
        this.sensorGetMap = sensorGetMap;
        this.sensorSetMap = sensorSetMap;
        logger.setLevel(Level.SEVERE);
    }

    /**
     * Take the sensor that is connected to the port 'port' and call its' get function
     *
     * @param port of the sensor
     * @return the result of the get function of the sensor
     */
    @Override
    public Boolean getBooleanSensorData(GrovePiPort port) {
        return sensorGetMap.get(port.portName).get(sensorModes.get(port)) > 0.0;
    }

    /**
     * Take the sensor that is connected to the port 'port' and call its' get function
     *
     * @param port of the sensor
     * @return the result of the get function of the sensor
     */
    @Override
    public Double getDoubleSensorData(GrovePiPort port) {
        return sensorGetMap.get(port.portName).get(sensorModes.get(port));
    }

    /**
     * Take the sensor that is connected to the port 'port' and call its' set function
     */
    @Override
    public Boolean setSensorMode(GrovePiPort port, int value) {
        sensorModes.replace(port, value);
        return true;
    }

    @Override
    public Boolean setActuatorData(GrovePiPort port, int value) {
        sensorModes.replace(port, value);
        return true;
    }

    @Override
    public void drive(List<DriveDataObject> driveData) {

    }

    @Override
    public void rotate(List<DriveDataObject> driveData) {

    }

    @Override
    public void disconnect() {
        logger.log(Level.FINE, "disconnected");
    }

    @Override
    public String myAlgorithm(String json) {
        return "";
    }

    //------------- getters and setters -------------//

    Map<String, IGroveSensorSetWrapper> getSensorSetMap() {
        return sensorSetMap;
    }

    Map<String, IGroveSensorGetWrapper> getSensorGetMap() {
        return sensorGetMap;
    }

    void setSensorSetMap(Map<String, IGroveSensorSetWrapper> sensorSetMap) {
        this.sensorSetMap = sensorSetMap;
    }

    void setSensorGetMap(Map<String, IGroveSensorGetWrapper> sensorGetMap) {
        this.sensorGetMap = sensorGetMap;
    }

}
