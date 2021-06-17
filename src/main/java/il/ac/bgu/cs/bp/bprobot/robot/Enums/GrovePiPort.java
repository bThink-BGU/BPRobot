package il.ac.bgu.cs.bp.bprobot.robot.Enums;
@SuppressWarnings("unused")
public enum GrovePiPort implements IPortEnums {
    A0("A0"),
    A1("A1"),
    A2("A2"),
    D2("D2"),
    D3("D3"),
    D4("D4"),
    D5("D5"),
    D6("D6"),
    D7("D7"),
    D8("D8");

    public final String portName;
    GrovePiPort(String portName) {
        this.portName = portName;
    }
}
