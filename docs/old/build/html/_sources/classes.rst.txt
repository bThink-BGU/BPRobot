Packages and Classes
=====================

Class Diagram
---------------

.. figure:: /images/Class_Diagram_complete.jpg
   :width: 800

Class Diagram Relations
-------------------------

.. figure:: /images/Class_Diagram.jpg
   :width: 800

RobotActuator
--------------

**CommandHandler**: handling the commands that gets from the program by the CommunicationHandler and update the RobotSensorData.

**MainTest**: Collect the data from the robot when the data is updated and send it by the CommunicationHandler.

Receive commands and execute the appropriate function as define by the CommandsHandler.

BPjsProject
------------

**RobotBPProgramRunnerListener**: Get the updated values from the queue that define by the CommunicationHandler and update the RobotSensorData.

Send the command from the js file over the queue that define by the CommunicationHandler

**HelloWorld**: The main class that define the js file native and add the RobotBPProgramRunnerListener.

Robot
------

**IBoard**: Interface for the boards in the system for the actions they can be supported.

**GrovePiBoard**: Represent the Grove Pi and responsible for performing operations related to Grove Pi.

The GrovePi has analog(A0-A2) and digital(D3-D8) sensors that derived in the GrovePiBoard to sensors their values can be set and sensors their values only get.

**Ev3Board**: Represent the Ev3 Mindstorms and responsible for performing operations related to EV3.

The EV3Board has EV3 instance such that the implementation of the functions in the IBoard interface calls the functions of the EV3.

**Ev3**: The EV3 send messages of bytes to the physical EV3 board which connected by the SerialPort.

RobotUtils
-----------

**CommunicationHandler**: Handle the communication between the commands that send to the robot from the user and the data that back from the robot.

Open different queues for different commands and send and receive the data over the appropriate queue.

- Commands queue: pass regular (not special) commands. Example: drive/rotate command.

- SOS queue: pass special commands that must receive and can’t be override for the speed communication of the system. Example: build command.

- Data queue: pass the data that back from the robots in json which contain the value for each port.

- Free queue: pass json the programmer wants for his algorithm.

**RobotSensorData**: Hold the values that back from the robot’s sensors in the portsMap.


