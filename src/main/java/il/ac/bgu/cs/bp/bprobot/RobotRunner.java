package il.ac.bgu.cs.bp.bprobot;

import il.ac.bgu.cs.bp.bpjs.context.ContextBProgram;
import il.ac.bgu.cs.bp.bpjs.execution.BProgramRunner;
import il.ac.bgu.cs.bp.bpjs.execution.listeners.PrintBProgramRunnerListener;
import il.ac.bgu.cs.bp.bpjs.model.BProgram;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RobotRunner {

  // Call with a path to a js file or a directory with js files.
  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      System.err.println("Usage: java -jar BPRobot-1.0-SNAPSHOT.uber.jar <path to js file or directory>");
      System.exit(1);
    }
    var file = new File(args[0]);
    if (!file.exists()) {
      System.err.println("File or directory does not exist: " + file.getAbsolutePath());
      System.exit(1);
    }

    File[] files = null;
    if (file.isDirectory()) {
      files = file.listFiles((dir, name) -> name.endsWith(".js"));
    } else {
      files = new File[]{file};
    }
    // This will load the program file  <Project>/src/main/resources/HelloBPjsWorld.js
    final BProgram bprog = new ContextBProgram("robot-base.js");
    for (int i = 0; i < files.length; i++) {
      bprog.appendSource(Files.readString(files[i].toPath()));
    }
    bprog.setWaitForExternalEvents(true);
//    bprog.getGlobalScope()..putInGlobalScope("Colors", lejos.robotics.Color);
    BProgramRunner rnr = new BProgramRunner(bprog);

    // Print program events to the console
    rnr.addListener(new PrintBProgramRunnerListener());
    rnr.addListener(new RobotBProgramRunnerListener());

    // go!
    rnr.run();
  }

}