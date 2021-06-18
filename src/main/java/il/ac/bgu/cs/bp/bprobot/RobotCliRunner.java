package il.ac.bgu.cs.bp.bprobot;

import il.ac.bgu.cs.bp.bpjs.execution.BProgramRunner;
import il.ac.bgu.cs.bp.bpjs.model.BProgram;
import il.ac.bgu.cs.bp.bpjs.model.ResourceBProgram;
import il.ac.bgu.cs.bp.bprobot.util.Communication.CommunicationHandler;
import il.ac.bgu.cs.bp.bprobot.util.Communication.ICommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class RobotCliRunner {

  public static void main(String[] args) throws IOException, TimeoutException {
    if (!List.of(0, 1, 3, 4).contains(args.length)) {
      printUsageAndExit();
    }

    String filename = args.length == 1 || args.length == 4 ? args[0] : "SampleRobot.js";
    BProgram bProgram = new ResourceBProgram(filename);
    bProgram.setWaitForExternalEvents(true);
    BProgramRunner rnr = new BProgramRunner(bProgram);

    // Print program events to the console
//        rnr.addListener( new PrintBProgramRunnerListener() );
    ICommunication communication = new CommunicationHandler();
    if (args.length == 3) {
      communication.setCredentials(args[0], args[1], args[2]);
    } else if (args.length == 4) {
      communication.setCredentials(args[1], args[2], args[3]);
    }
    rnr.addListener(new RobotBProgramRunnerListener(communication, bProgram));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        communication.closeConnection();
        System.out.println("Connection Closed!");
      } catch (IOException | TimeoutException e) {
        e.printStackTrace();
      }
    }));
    // go!
    rnr.run();
  }

  private static void printUsageAndExit() {
    try (BufferedReader rdr = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("RunFile-usage.txt"))))) {
      rdr.lines().forEach(System.out::println);
    } catch (IOException ex) {
      throw new RuntimeException("Cannot find 'RunFile-usage.txt'");
    }
    System.exit(-1);
  }

}
