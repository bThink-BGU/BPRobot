package il.ac.bgu.cs.bp.bprobot.robot.boards;

import il.ac.bgu.cs.bp.bprobot.remote.model.CommandType;

public enum SampleType {
  UNSET {
    @Override
    public CommandType getCommandType() {
      throw new UnsupportedOperationException("CommandType is not defined");
    }
  },
  SI{
    @Override
    public CommandType getCommandType() {
      return CommandType.GET_SI_VALUES;
    }
  },
  PERCENT{
    @Override
    public CommandType getCommandType() {
      return CommandType.GET_PERCENT_VALUES;
    }
  };

  public abstract CommandType getCommandType();
}
