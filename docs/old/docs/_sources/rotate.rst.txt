Rotate
===========

Event description
------------------

**Event name:** Rotate

**Params:** Json that describe for each port the motor connect, the angel the motor spin and the speed that all the motors move.

**Description:** The program get angle for each port and the speed, check the motors which connect to them,
and spin the robot's motors according to those speed and angels.


Json structure
----------------

For each board type 'EV3' and 'GrovePi' as key specifies one of the two options as value:

If you have only one board of this type, the value can be json that contain the angel for each port and key 'speed' with value of the speed for all tha motors.

The second option, the value will be a json such that the key will be the index of the board (depending on how it is written in the build event),
and the value will be json that contain the angel for each port and key 'speed' with value of the speed for all tha motors.

For example if you have one EV3 and motors that connected to port B and C, the json will be in this structure:

.. code-block:: javascript
  :linenos:

  bp.sync({request: bp.Event("Rotate", {"EV3": {"B": , "C": , "speed": }})});

The values of keys B and C will be angels you want the motor move respectively and the value of speed will be the total speed.

The ports in the keys can be: 

For EV3 ports: 1-4, A-D. For Grove pi ports: A0-A2, D3-D8


Example
----------

.. code-block:: javascript
  :linenos:

  bp.registerBThread("Pickup in 90 degree", function(){
          bp.sync({request: bp.Event("Rotate", {"EV3": {"B": 60, "C": 30, "speed": 15}})});
      }
  });