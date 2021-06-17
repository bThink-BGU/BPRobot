Unsubscribe
============

Event description
------------------

**Event name:** Unsubscribe

**Params:** Json that describe the ports on the boards that you want to stop tracking after the data that belong them.

**Description:** The program get the ports, check the sensors which connect to them,
and stop tracking after the values that back from the robot's sensors.

Json structure
----------------

The excepted structure of the json is exactly the same as the json of "Subscribe".

For each board type 'EV3' and 'GrovePi' as key specifies one of the two options as value:

If you have only one board of this type, the value can be array of strings that present the ports name.

The second option, the value will be a json such that the key will be the index of the board (depending on how it is written in the build event),
and the value will be array of strings that present the ports name the connected to this board.

For example if you have two EV3 and one Grove pi the json will be in this structure:

.. code-block:: javascript
  :linenos:

  bp.sync({request: bp.Event("Unsubscribe", {"EV3": {1: [], 2: []}, "GrovePi": []})});

In array you can select from the strings below:

For EV3 ports: 1-4, A-D. For Grove pi ports: A0-A2, D3-D8


Example
----------

.. code-block:: javascript
  :linenos:

  bp.registerBThread("Stop to do Something with Data", function () {
      bp.sync({request: bp.Event("Unsubscribe", {"EV3": {1: ["2"], 2: ["3"]}, "GrovePi": ["D3"]})});
  });
   