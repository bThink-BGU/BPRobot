package il.ac.bgu.cs.bp.bprobot.robot.boards;

import il.ac.bgu.cs.bp.bprobot.robot.enums.GrovePiPort;
import il.ac.bgu.cs.bp.bprobot.robot.grovewrappers.get.IGroveSensorGetWrapper;
import il.ac.bgu.cs.bp.bprobot.robot.grovewrappers.set.IGroveSensorSetWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@PrepareForTest({GrovePiBoard.class})
public class GrovePiBoardTest {

    private GrovePiBoard grovePiBoard;

    @BeforeEach
    public void setUp() {

        Map<String, IGroveSensorGetWrapper> sensorGetMap = new HashMap<>();
        Map<String, IGroveSensorSetWrapper> sensorSetMap = new HashMap<>();

        sensorGetMap.put("A0",mock(IGroveSensorGetWrapper.class));
        sensorGetMap.put("A1",mock(IGroveSensorGetWrapper.class));
        sensorSetMap.put("D2",mock(IGroveSensorSetWrapper.class));

        grovePiBoard = Mockito.mock(GrovePiBoard.class);

        doCallRealMethod().when(grovePiBoard).setSensorGetMap(sensorGetMap);
        doCallRealMethod().when(grovePiBoard).setSensorSetMap(sensorSetMap);

        grovePiBoard.setSensorGetMap(sensorGetMap);
        grovePiBoard.setSensorSetMap(sensorSetMap);

    }

    @Test
    public void testGetBooleanSensorData() {
        when(grovePiBoard.getSensorGetMap()).thenCallRealMethod();
        when(grovePiBoard.getSensorGetMap().get("A0")).thenCallRealMethod();
        when(grovePiBoard.getSensorGetMap().get("A0").get(1)).thenReturn(0.5);

        boolean actual = grovePiBoard.getBooleanSensorData(GrovePiPort.A0);
        assertFalse(actual);
    }

//    @Test
//    public void testGetDoubleSensorData() {
//        when(grovePiBoard.getSensorGetMap()).thenCallRealMethod();
//        when(grovePiBoard.getSensorGetMap().get("A1")).thenCallRealMethod();
//        when(grovePiBoard.getSensorGetMap().get("A1").get(0)).thenReturn(0.5);
//        when(grovePiBoard.getDoubleSensorData(GrovePiPort.A1)).thenCallRealMethod();
//
//        double actual = grovePiBoard.getDoubleSensorData(GrovePiPort.A1);
//        assertEquals(0.5, actual, 0.01);
//    }

//    @Test
//    public void testSetSensorData() {
//        when(grovePiBoard.getSensorSetMap()).thenCallRealMethod();
//        when(grovePiBoard.getSensorSetMap().get("D2")).thenCallRealMethod();
//        when(grovePiBoard.getSensorSetMap().get("D2").set(true)).thenReturn(true);
//        when(grovePiBoard.setSensorMode(GrovePiPort.D2, 1)).thenCallRealMethod();
//
//        boolean actual = grovePiBoard.setSensorMode(GrovePiPort.D2, 1);
//        assertTrue(actual);
//    }
}