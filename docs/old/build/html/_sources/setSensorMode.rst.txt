Set Sensor Mode
==================

Event description
------------------

**Event name:** SetSensorMode

**Params:** Json that describe the ports on the boards that you want to set the mode of the sensors that connected them.

**Description:** The program get the ports and the mode for each port, check the sensors connected them, and set the mode of the sensors.


Json structure
----------------

For each board type 'EV3' and 'GrovePi' as key specifies one of the two options as value:

If you have only one board of this type, the value is json with the port name as key and the value is double to set,
in the case of boolean 1.0 is true and 0.0 is false.

The second option, the value is a json such that the key will be the index of the board (depending on how it is written in the build event),
and the value is json which the key is the port string and the value is double to set, in the case of boolean 1.0 is true and 0.0 is false.

For example if you have one Grove pi the json will be in this structure:

.. code-block:: javascript
  :linenos:

  bp.sync({request: bp.Event("SetSensorMode", {"GrovePi": {}})});

in the json the key port you can select from the strings below:

For EV3 ports: 1-4, A-D. For Grove pi ports: A0-A2, D3-D8
and the value is double, as mention.

Example
----------

.. code-block:: javascript
  :linenos:

  bp.registerBThread("Change data", function () {
      bp.sync({request: bp.Event("SetSensorMode", {"GrovePi": {"A0":1.0}})});
  });
   