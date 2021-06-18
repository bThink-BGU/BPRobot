.. _setup:

Setup instructions
===================

1. Setting up the raspberry pi
--------------------------------

Your first steps with the raspberry pi, including familiarity with hardware and installation, can be supported
in this `site <https://projects.raspberrypi.org/en/projects/raspberry-pi-getting-started>`_.

After you finish to configure the raspberry pi, you can connect him from your local computer via VNC viewer. 
To download VNC click `here <https://www.realvnc.com/en/connect/download/viewer/>`__.

The connection between your local computer and the raspberry pi made in the VNC by entering the ip of the raspberry
with the user name and password you defined at the installation.

.. Note::
    You can install 'raspberrypi os light' instead of the 'raspberrypi os desktop' version without the interface.
    In this case the configuration of the settings made by commands. For the setup of the wi-fi and the ssh
    you can use this `site <https://www.tomshardware.com/reviews/raspberry-pi-headless-setup-how-to,6028.html>`__.
    For the setup of the bluetooth you can use this `site <https://www.raspberrypi.org/forums/viewtopic.php?t=214373>`__.

2. Setting up the grove pi
----------------------------

The connection between the raspberry and the grove is made physically by adding the grove above the raspberry. 

As you can see in the picture:

.. figure:: /images/merge_ras_and_grov.jpg
    :scale: 60 %

3. Setting up the EV3
-----------------------

The connection between the raspberry and the EV3 is made via bluetooth.
To configure the bluetooth connection follow the introductions `here <https://www.lego.com/en-us/service/help/products/themes-sets/mindstorms/connecting-with-bluetooth-to-lego-mindstorms-ev3-apps-408100000007982>`__.

After this part you will have Serial port through it you can connect in bluetooth to the EV3.
Later you will use it to build the robot in the system software environment.

You can build the Ev3 robot according to the instructions `here <https://education.lego.com/en-us/product-resources/mindstorms-ev3/downloads/building-instructions>`__.
Select the model you want and follow his instructions.

4. Install RabbitMQ
---------------------

In the project, there is communication between the commands that send to the robot from the user and the data that back from the robot.

The communication implemented in platform who called: RabbitMQ. Is famous IOT communication protocol.

To download RabbitMQ on the raspberry pi follow the introductions `here <http://pont.ist/rabbit-mq/>`__.

5. Download the code from github
----------------------------------

link to Robot repo: https://github.com/Dave-Zi/Robot

link to RobotActuator repo: https://github.com/Dave-Zi/RobotActuator

link to BPjsRobotProject repo: https://github.com/Dave-Zi/BPjsRobotProject

link to RobotUtils repo: https://github.com/Dave-Zi/RobotUtils

link to MindCtrlJava repo: https://github.com/Dave-Zi/MindCtrlJava

link to RobotBPDocs repo: https://github.com/pelegavraham/RobotBPDocs

Make sure there is no compilation errors, the project opens as Maven project and the jdk version at least 11.0.9.

.. admonition:: Note

   You don't need to download all the repos. For your first steps in the project you can download
   BPjsRobotProject and RobotActuator repos.
   If you later want to expand the project, you will need to download the other repos.

6. Prepare your coding environment
-------------------------------------

For your convenience, you can coding in your local computer and the running will apply directly on the raspberry pi.

If you use intellij, first you need to add the plugin: ``Embedded Linux JVM``.

In the RobotActuator repository, Go to ``Edit Configuration`` and add new ``Embedded Linux JVM`` configuration.

Then enter the ``Hostname`` (ip), ``Username`` and ``Password`` of the raspberry pi, and define the ``Module`` and the ``Main Class``.

.. Note::
    You can run the code without intellij too, just over the ssh connection.
    Maybe you prefer this way instead of intellij if you want to run the code only.
    In this case, you should run the projects directly on the raspberry pi with ssh connection and mvn exec:java.