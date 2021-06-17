package il.ac.bgu.cs.bp.bprobot.robot.Boards;

import il.ac.bgu.cs.bp.bprobot.robot.Enums.IPortEnums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("unused")
public class MockBoard implements IBoard<IPortEnums> {

    private final Random random = new Random();
    private final Map<IPortEnums, Double> portsMap = new HashMap<>();

    @Override
    public Boolean getBooleanSensorData(IPortEnums port) {
        if (portsMap.containsKey(port)) {
            return portsMap.get(port) > 0;
        }
        return false;
    }

    @Override
    public Double getDoubleSensorData(IPortEnums port) {
        if (portsMap.containsKey(port)) {
            return portsMap.get(port);
        }
        return random.nextDouble() * 100;
    }

    @Override
    public Boolean setSensorMode(IPortEnums port, int value) {
        if (portsMap.containsKey(port)) {
            portsMap.replace(port, (double) value);
        } else {
            portsMap.put(port, (double) value);
        }
        return true;
    }

    @Override
    public Boolean setActuatorData(IPortEnums port, int value) {
        if (portsMap.containsKey(port)) {
            portsMap.replace(port, (double) value);
        } else {
            portsMap.put(port, (double) value);
        }
        return true;
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnected!");
    }

    @Override
    public String myAlgorithm(String json) {
        return json;
    }

    @Override
    public void rotate(List<DriveDataObject> driveData) {

    }

    @Override
    public void drive(List<DriveDataObject> driveData) {

    }
}
