
Introduction
=============

Code usage
-----------

What can you do within the interface supply to you?

* Set the values of actuators in the robot.

* Set the modes of sensors in the robot.

* Get the values from sensors in the robot

* Make the robot drive

* Rotate motors of the robot

Your program in BPjs will include some bEvent that cause those effects. 

All the information you need will be specified later.

Coding the program
-------------------

In the next tutorials you will learn how to write the code according to the interface you given.

**Where to write?**

The code that contain the commands which send to the robot will be in js file written in BPjs.

In ``samplebpjsproject\HelloWorld.java`` in ``ResourceBProgram("FILE_NAME.js")`` will be the name of the file.

All the commands that you learn in the following tutorials will be written in this js file.

Running the program
--------------------
In the end, after you install and configure the rabbitMQ, add the ``Embedded Linux JVM`` extension and write the js file

To run the program:

1. Run the MainTest class in the RobotActuator repository, after you edit the configuration to use the
``Embedded Linux JVM`` as mention in the Setup instructions.

2. Run the HelloWorld class in the BPjsProject repository (on your local computer)

.. warning::

   When you stop your program and can't stop the commands that continue to send, you need to stop the java program and clean all the garbage data in the project.
   To stop this you need to run above the raspberry pi the following two .sh files that contain the next rows:

    **stopJava.sh:**

    .. code-block:: bash
        :linenos:

        pgrep -a java
        sudo kill $(pidof java)
        pgrep -a java

    **cleanProjects.sh:**

    .. code-block:: bash
        :linenos:

        sudo rm -rf /home/pi/IdeaProjects

    The files will saved in home directory in the raspberry pi and will run from there.