package il.ac.bgu.cs.bp.bprobot.robot.boards;

import il.ac.bgu.cs.bp.bprobot.robot.Enums.IPortEnums;

import java.util.List;

public interface IBoard<BoardPortType extends IPortEnums> {
    Boolean getBooleanSensorData(BoardPortType port);
    Double getDoubleSensorData(BoardPortType port);
    Boolean setSensorMode(BoardPortType port, int value);
    Boolean setActuatorData(BoardPortType port, int value);
    void drive(List<DriveDataObject> driveData);
    void rotate(List<DriveDataObject> driveData);
    void disconnect();
    String myAlgorithm(String json);
}
