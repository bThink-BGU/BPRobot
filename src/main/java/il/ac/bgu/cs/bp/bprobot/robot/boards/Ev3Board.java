package il.ac.bgu.cs.bp.bprobot.robot.boards;

import il.ac.bgu.cs.bp.bprobot.ev3control.EV3;
import il.ac.bgu.cs.bp.bprobot.robot.enums.Ev3DrivePort;
import il.ac.bgu.cs.bp.bprobot.robot.enums.Ev3SensorPort;
import il.ac.bgu.cs.bp.bprobot.robot.enums.IEv3Port;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ev3Board implements IBoard<IEv3Port> {

    private final Logger logger = Logger.getLogger(EV3.class.getName());

    private final EV3 ev3;

    private final Map<IEv3Port, Integer> sensorModes = new HashMap<>();

    private int[] previousSpin = new int[4];

    public Ev3Board(EV3 ev3) {
        sensorModes.putAll(Map.of(
                Ev3SensorPort._1, 0,
                Ev3SensorPort._2, 0,
                Ev3SensorPort._3, 0,
                Ev3SensorPort._4, 0,
                Ev3DrivePort.A, 0,
                Ev3DrivePort.B, 0,
                Ev3DrivePort.C, 0,
                Ev3DrivePort.D, 0
        ));
        this.ev3 = ev3;
        this.ev3.setDelay(250);
        logger.setLevel(Level.SEVERE);
    }

    @Override

    public void disconnect() {
        ev3.disconnect();
    }

    @Override
    public String myAlgorithm(String json) {
        return "";
    }

    @Override
    /*
     * Ev3 sensor ports are 1 2 3 4
     */
    public Double getDoubleSensorData(IEv3Port port) {
        Ev3SensorPort newPort;
        try {
            newPort = (Ev3SensorPort) port;
        } catch (Exception e) {
            logger.severe("Wrong port type");
            return null;
        }
        Float value = ev3.sensor(newPort.portNumber, sensorModes.get(port));
        if (value == null) {
            return null;
        } else {
            return Double.valueOf(value);
        }
    }

    @Override
    public Boolean setSensorMode(IEv3Port port, int value) {
        sensorModes.replace(port, value);
        return true;
    }

    @Override
    public Boolean setActuatorData(IEv3Port port, int value) {
        sensorModes.replace(port, value);
        return true;
    }

    @Override
    public Boolean getBooleanSensorData(IEv3Port port) {
        Double value = getDoubleSensorData(port);
        return value != null && value > 0;
    }

    @Override
    /*
      Call spin with the specific port -
      Ev3 motor ports are : A B C D
     */
    public void drive(List<DriveDataObject> driveData) {
        int[] motorSpeed = new int[4];

        driveData.forEach(driveObj -> {
            switch ((Ev3DrivePort) driveObj.getPort()) {
                case A:
                    motorSpeed[0] = (int) driveObj.getSpeed();
                    break;
                case B:
                    motorSpeed[1] = (int) driveObj.getSpeed();
                    break;
                case C:
                    motorSpeed[2] = (int) driveObj.getSpeed();
                    break;
                case D:
                    motorSpeed[3] = (int) driveObj.getSpeed();
                    break;
            }
        });
        if (Arrays.equals(previousSpin, motorSpeed)) {
            return;
        }
        previousSpin = motorSpeed;
        ev3.spin(motorSpeed[0], motorSpeed[1], motorSpeed[2], motorSpeed[3]);
    }

    @Override
    public void rotate(List<DriveDataObject> driveData) {
        int[] angles = new int[4];
        double fastestSpeed = 0;

        for (DriveDataObject driveObj : driveData) {
            switch ((Ev3DrivePort) driveObj.getPort()) {
                case A:
                    angles[0] = driveObj.getAngle();
                    break;
                case B:
                    angles[1] = driveObj.getAngle();
                    break;
                case C:
                    angles[2] = driveObj.getAngle();
                    break;
                case D:
                    angles[3] = driveObj.getAngle();
                    break;
            }

            if (Math.abs(fastestSpeed) <= Math.abs(driveObj.getSpeed())) {
                fastestSpeed = driveObj.getSpeed();
            }
        }
        ev3.rotate(angles[0], angles[1], angles[2], angles[3], (int) fastestSpeed);
    }
}
