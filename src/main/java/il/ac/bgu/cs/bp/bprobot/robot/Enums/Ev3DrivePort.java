package il.ac.bgu.cs.bp.bprobot.robot.Enums;

public enum Ev3DrivePort implements IEv3Port {
    A('A'),
    B('B'),
    C('C'),
    D('D');

    public final char portChar;
    Ev3DrivePort(char portChar) {
        this.portChar = portChar;
    }
}
