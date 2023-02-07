package il.ac.bgu.cs.bp.bprobot;

import il.ac.bgu.cs.bp.bpjs.context.ContextBProgram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RobotBProgram extends ContextBProgram {
  public RobotBProgram(String aResourceName) {
    this(Collections.singletonList(aResourceName));
  }

  public RobotBProgram(String... resourceNames) {
    this(List.of(resourceNames));
  }

  public RobotBProgram(Collection<String> someResourceNames) {
    this(someResourceNames, String.join("+", someResourceNames));
  }

  public RobotBProgram(Collection<String> someResourceNames, String aName) {
    super(append(someResourceNames), aName);
  }

  private static Collection<String> append(Collection<String> resourceNames) {
    return new ArrayList<>(resourceNames.size() + 1) {{
      add("8293fd24-7a43-4ca9-ba42-17bb46e555ef/robot-base.js");
      addAll(resourceNames);
    }};
  }
}
