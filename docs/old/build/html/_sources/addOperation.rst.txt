Add Command
=============
For now the system supported in the following commands:
Subscribe,
Unsubscribe,
Build,
Drive,
Rotate,
SetSensorMode,
SetActuatorData,
MyAlgorithm.

If you want to add more commands to the system, we show you here the way to do that.

We have two parts in the system related to the commands.

The first part is the listener in BPjsProject repository, there we get the command from the BPjs execution and

send the data of the command to the second part.

The second part is the actuator in RobotActuator repository, there we get the command name with the data

and decide what to do with the data according to the command type.

In the first step we explain the first part and in the second step we explain the second part.

1. Add ICommand in BPjsProject
-------------------------------
As mention before, in this part we get the command from the BPjs execution and send the data of the command to the

actuator part.

In RobotBProgramRunnerListener class, add new ICommand with initial value of a new following function:

``private void FUNCTION_NAME(BProgram bp, BEvent theEvent)``

In the new function you need to defined how to pass the data in theEvent over the queue you want.

You can look as example at the others ICommands to see how to pass the messages.

Add the new ICommand to the commandToMethod map, with the name you want the command will be in the BPjs program.

2. Add ICommand in RobotActuator
---------------------------------
As mention before, in this part we get the command name with the data and decide what to do with the data according

to the command type.

In CommandHandler class, add new ICommand with initial value of a new following function:

``private void FUNCTION_NAME(String json)``

In the new function you need to defined what to do with the data in the jsonString.

Usually, the data contain command name and boards with ports and values the command used them,

it is at your discretion.

Add the new ICommand to the commandToMethod map, with the appropriate name of the command.

3. Use the new command
-----------------------
Finally, you can use the command in the js file according to the name you give and the data you defined him.

.. admonition:: Note!

   If the new command you want to add is related to the board you may want to add new function to the IBoard interface

   or add this function to specific board that implements the IBoard interface.





