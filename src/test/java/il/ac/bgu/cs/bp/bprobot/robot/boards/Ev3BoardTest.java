package il.ac.bgu.cs.bp.bprobot.robot.boards;

import il.ac.bgu.cs.bp.bprobot.ev3control.EV3;
import il.ac.bgu.cs.bp.bprobot.robot.enums.Ev3DrivePort;
import il.ac.bgu.cs.bp.bprobot.robot.enums.Ev3SensorPort;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Ev3BoardTest {

    private EV3 ev3Mock;
    private Ev3Board ev3Board;

    @Before
    public void setUp() {
        ev3Mock = mock(EV3.class);
        ev3Board = new Ev3Board(ev3Mock);
    }

    @Test
    public void testGetDoubleSensorData() {
        when(ev3Mock.sensor(1, 0)).thenReturn((float) 0.5);
        Double actual = ev3Board.getDoubleSensorData(Ev3SensorPort._1);
        assertEquals(0.5, actual, 0.01);
    }

    @Test
    public void testSetSensorData() {
        doNothing().when(ev3Mock).tone(440, 50, 200);
        boolean actual = ev3Board.setSensorMode(Ev3SensorPort._1, 1);
        assertTrue(actual);
    }

    @Test
    public void testGetBooleanSensorData() {
        assertFalse(ev3Board.getBooleanSensorData(Ev3SensorPort._1));
    }

    @Test
    public void testDrive() {
        doNothing().when(ev3Mock).spin(100, 0, 0, 0);

//        Map<IEv3Port, Double> param = Map.of(
//                Ev3DrivePort.A, 100.0
//        );
        ArrayList<DriveDataObject> param = new ArrayList<>
                (Collections.singletonList(new DriveDataObject(Ev3DrivePort.A, 100, 0)));

        ev3Board.drive(param);

        verify(ev3Mock, times(1)).spin(100, 0, 0, 0);
    }
}