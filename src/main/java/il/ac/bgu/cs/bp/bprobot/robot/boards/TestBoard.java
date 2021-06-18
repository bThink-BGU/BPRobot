package il.ac.bgu.cs.bp.bprobot.robot.boards;


import il.ac.bgu.cs.bp.bprobot.robot.enums.IPortEnums;

import java.util.List;
import java.util.Map;

public class TestBoard implements IBoard<IPortEnums> {

    private final Map<IPortEnums, Double> portsMap;

    public TestBoard(Map<IPortEnums, Double> portsMap) {
        this.portsMap = portsMap;
    }

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
//            System.out.print(port.toString() + ": " + ((Ev3SensorPort)port).portNumber + " - " + portsMap.get(port));
            return portsMap.get(port);
        }
        return 10.0;
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
