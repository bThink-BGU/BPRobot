Add Board
==========

For now the system supported in GrovePi and EV3. The boards defines in Robot repository

In fact, support for additional boards can be added in a simple way such that

the structure of the system will be preserved.

So, assume you buy a new robot and you want the system support it. We show here the way to do that.

1. Add new ports support
-------------------------

The new robot comes with several ports, his sensors connected to the ports

through them you can send and receive information.

We create Enums for the existing boards to represent the ports to enforce improper input of the port name.

All the Enums exist in package called "Enums".

The user need to use the enums to deal with the appropriate ports, and in the implementation of the board's

functions will be the transformation from the enum to the desired behavior.

If you want to add the support in new ports:

Create new Enum for the ports that will implements the "IPortEnums" interface.

You can look as example at "GrovePiPort" enum.

If you have several types of ports you can create several enums that implements one interface that will

extend the "IPortEnums" interface.

You can look as example at "IEv3Port" interface.

.. figure:: /images/enums.jpg


2. Create new board & implements functions
-------------------------------------------

In Robot repository we define general interface for the boards called "IBoard".

In the interface there are several general functions we want the board be supported

like: get and set double and boolean sensor data, drive, rotate etc.

If you want your robot will support some of those functions:

create new class for the board that implements the IBoard interface.

Inside the appropriate functions define the implementation according to the manual of the robot.

For study you can look as an example on Ev3Board and GrovePiBoard which implements the IBoard interface.

In the implementation use the ports enums you create in the first step.

.. figure:: /images/IBoard.jpg

.. admonition:: Note!

   All the implementation of the bytes that send to the robot depending on each function,

   is your responsibility and need to be implement according to the hardware manual.

3. Use the new board
---------------------
Now, after we implement some of the function we can call them through requests in the js file.

For example, if we implement the rotate function in the new board called "newBoard" than we can call:

``bp.sync({request: bp.Event("Rotate", {"newBoard": {"B": 60, "C": 30, "speed": 15}})});``
