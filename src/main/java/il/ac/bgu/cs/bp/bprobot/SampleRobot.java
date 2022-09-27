package il.ac.bgu.cs.bp.bprobot;

import il.ac.bgu.cs.bp.bpjs.context.ContextBProgram;
import il.ac.bgu.cs.bp.bpjs.execution.BProgramRunner;
import il.ac.bgu.cs.bp.bpjs.execution.listeners.PrintBProgramRunnerListener;
import il.ac.bgu.cs.bp.bpjs.model.BProgram;

/**
 * Simple class running a BPjs program that selects "hello world" events.
 *
 * @author michael
 */
public class SampleRobot {

  public static void main(String[] args) {
    // This will load the program file  <Project>/src/main/resources/HelloBPjsWorld.js
    final BProgram bprog = new ContextBProgram("robot-base.js", "Elephant.js");
    bprog.setWaitForExternalEvents(true);
    BProgramRunner rnr = new BProgramRunner(bprog);

    // Print program events to the console
    rnr.addListener(new PrintBProgramRunnerListener());
    rnr.addListener(new RobotBProgramRunnerListener());

    // go!
    rnr.run();
  }

}