package il.ac.bgu.cs.bp.bprobot;

import il.ac.bgu.cs.bp.bpjs.context.ContextBProgram;
import il.ac.bgu.cs.bp.bpjs.execution.BProgramRunner;
import il.ac.bgu.cs.bp.bpjs.execution.listeners.PrintBProgramRunnerListener;
import il.ac.bgu.cs.bp.bpjs.model.BProgram;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RobotRunner {
  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      args = new String[] {"RobotEducator/data.js", "RobotEducator/behavior-low.js", "RobotEducator/behavior-high.js"};
    }

    final BProgram bprog = new RobotBProgram(args);
    bprog.setWaitForExternalEvents(true);
    BProgramRunner rnr = new BProgramRunner(bprog);

    // Print program events to the console
    rnr.addListener(new PrintBProgramRunnerListener());
    rnr.addListener(new RobotBProgramRunnerListener());

    // go!
    rnr.run();
  }
}