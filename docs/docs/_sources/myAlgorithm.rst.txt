My Algorithm
=============

Event description
------------------

**Event name:** MyAlgorithm

**Params:** Json with the boards to run the algorithm belongs them and their indexes,
and for each board specifies the parameters that the programmer needs for the algorithm with free hand of the programmer.

**Description:** The system executes the algorithm of each board the programmer request with the appropriate params. The algorithm defines by the programmer.


Json structure
----------------

For each board type 'EV3' and 'GrovePi' as key specifies the value will be a json such that the key will be the index of the board (depending on how it is written in the build event),
and the value will be json with the params as the programmer want.

For example if you have one EV3 and two Grove pi the json will be in this structure:

.. code-block:: javascript
  :linenos:

    bp.sync({request: bp.Event("MyAlgorithm", {"EV3": {1: {}}, "GrovePi": {2: {}}})});

As mention, the parameters of the boards include json of what the programmer needs for the algorithm with free hand of the programmer.


Example
----------

.. code-block:: javascript
  :linenos:

    var AlgorithmEventSet = bp.EventSet("", function (e) {
      return e.name.equals("GetAlgorithmResult");
    });

    bp.registerBThread("Do My Algorithm", function () {
      bp.sync({request: bp.Event("MyAlgorithm", {"EV3": {1: {"Param1": 10, "Param2": {"Param2_1" : "Hello"}}}, "GrovePi": {2: {"Param3": 100}}})});
      var e = bp.sync({waitFor: AlgorithmEventSet});
      var data = JSON.parse(e.data);
      bp.sync({request: bp.Event("Test", data)});
    });
   