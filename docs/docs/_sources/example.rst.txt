.. _exampels:

Project example
================

.. code-block:: javascript
  :linenos:

  var allEventsButBuildEventSet = bp.EventSet("Block all for build", function (e) {
      return !e.name.equals("Build");
  });

  var dataEventSet = bp.EventSet("", function (e) {
      return e.name.equals("GetSensorsData");
  });

  var AlgorithmEventSet = bp.EventSet("", function (e) {
      return e.name.equals("GetAlgorithmResult");
  });

  var direction = 1;

  bp.registerBThread("Do Something with Data", function () {
      bp.sync({request: bp.Event("Subscribe", {"GrovePi": ["D2", "D4"]})});

      while (true) {
          var e = bp.sync({waitFor: dataEventSet});
          var data = JSON.parse(e.data);
          var distance  = data.GrovePi._1.D4;
          if (distance < 15){
              direction = -1 * direction ;
          }
      }
  });

  bp.registerBThread("Just Right", function () {
      bp.sync({request: bp.Event("Subscribe", {"EV3": ["2"]})});

      while (true) {
          var e = bp.sync({waitFor: dataEventSet});
          var data = JSON.parse(e.data);
          var distance  = data.EV3._1._2;
          if (distance > 15 && distance < 20){
              bp.sync({request: bp.Event("Rotate", {"EV3": {"B": 60, "C": 60, "speed": 15}})});
          }
      }
  });

  bp.registerBThread("Too Far Right", function () {
      bp.sync({request: bp.Event("Subscribe", {"EV3": ["2"]})});

      while (true) {
          var e = bp.sync({waitFor: dataEventSet});
          var data = JSON.parse(e.data);
          var distance  = data.EV3._1._2;
          if (distance > 20){
              bp.sync({request: bp.Event("Rotate", {"EV3": {"B": 30, "C": 60, "speed": 15}})});
          }
      }
  });

  bp.registerBThread("Too Far Left", function () {
      bp.sync({request: bp.Event("Subscribe", {"EV3": ["2"]})});

      while (true) {
          var e = bp.sync({waitFor: dataEventSet});
          var data = JSON.parse(e.data);
          var distance  = data.EV3._1._2;
          if (distance < 15){
              bp.sync({request: bp.Event("Rotate", {"EV3": {"B": 60, "C": 30, "speed": 15}})});
          }
      }
  });

  bp.registerBThread("Do My Algorithm", function () {
      bp.sync({request: bp.Event("MyAlgorithm", {"EV3": {1: {"Param1": 10, "Param2": {"Param2_1" : "Hello"}}}, "GrovePi": {2: {"Param3": 100}}})});
      var e = bp.sync({waitFor: AlgorithmEventSet});
      var data = JSON.parse(e.data);
      bp.sync({request: bp.Event("Test", data)});
  });

  bp.registerBThread("Initiation", function () {
      bp.sync({
          block: allEventsButBuildEventSet, request: bp.Event("Build",
              {
                  "EV3":
                      [{
                          "Name": "EV3_1",
                          "Port": "rfcomm0",
                          "2": {"Name": "UV3"}
                      }]
                  ,
                  "GrovePi":
                      [{
                          "Name": "GrovePi1",
                          "A0": {"Name": "", "Device": ""},
                          "A1": "",
                          "A2": "",
                          "D2": {"Name": "MyLed", "Device": "Led"},
                          "D3": "",
                          "D4": {"Name": "UV", "Device": "Ultrasonic"},
                          "D5": "",
                          "D6": "",
                          "D7": "",
                          "D8": "Led"
                      }]
              }
          )
      })
  });

Program Details
------------------

**Description:** Keep the robot far from the wall for 15-20 cm.

**Devices used:**

- EV3 that will operate the robot and will move it using rotate.

- EV3 Range sensor that will always check the distance of the robot from the wall.

- RaspberryPi

- GrovePi that will be attached to the RaspberryPi.

Program Threads
----------------

Let's understand what's going on here

**1. Change direction**

.. code-block:: javascript
  :lineno-start: 15
  :linenos:

  bp.registerBThread("Change direction", function () {
      bp.sync({request: bp.Event("Subscribe", {"GrovePi": ["D2", "D4"]})});

      while (true) {
          var e = bp.sync({waitFor: dataEventSet});
          var data = JSON.parse(e.data);
          var distance  = data.GrovePi._1.D4;
          if (distance < 15){
              direction = -1 * direction ;
          }
      }
  });

This thread will be responsible for checking the distance of the robot from the
wall and changing it's direction in case the distance is less then 15.

It will subscribe to the grovePi port where a sensor will be,
and will read data from this sensor while it runs.


**2. Just Right**

.. code-block:: javascript
  :lineno-start: 28
  :linenos:

  bp.registerBThread("Just Right", function () {
      bp.sync({request: bp.Event("Subscribe", {"EV3": ["2"]})});

      while (true) {
          var e = bp.sync({waitFor: dataEventSet});
          var data = JSON.parse(e.data);
          var distance  = data.EV3._1._2;
          if (distance > 15 && distance < 20){
              bp.sync({request: bp.Event("Rotate", {"EV3": {"B": 60, "C": 60, "speed": 15}})});
          }
      }
  });

This thread will be responsible for keeping the robot turning right, using rotate in EV3,
if the distance of the robot is 15-20 cm from the wall - keep right.

It will subscribe to the EV3 port where the engine will be,
and it will always read data from this port to check if it's 15-20 cm from the wall.


**3. Too Far Right**

.. code-block:: javascript
  :lineno-start: 41
  :linenos:

  bp.registerBThread("Too Far Right", function () {
      bp.sync({request: bp.Event("Subscribe", {"EV3": ["2"]})});

      while (true) {
          var e = bp.sync({waitFor: dataEventSet});
          var data = JSON.parse(e.data);
          var distance  = data.EV3._1._2;
          if (distance > 20){
              bp.sync({request: bp.Event("Rotate", {"EV3": {"B": 30, "C": 60, "speed": 15}})});
          }
      }
  });

This thread will be responsible for keeping the distance of the robot from the wall less than 20
if the distance of the robot is more than 20 cm from the wall - rotate left.

It will subscribe to the EV3 port where the engine will be,
and it will always read data from this port to check if it's more than 20 cm from the wall.



**4. Too Far Left**

.. code-block:: javascript
  :lineno-start: 54
  :linenos:

  bp.registerBThread("Too Far Left", function () {
      bp.sync({request: bp.Event("Subscribe", {"EV3": ["2"]})});

      while (true) {
          var e = bp.sync({waitFor: dataEventSet});
          var data = JSON.parse(e.data);
          var distance  = data.EV3._1._2;
          if (distance < 15){
              bp.sync({request: bp.Event("Rotate", {"EV3": {"B": 60, "C": 30, "speed": 15}})});
          }
      }
  });

This thread will be responsible for keeping the distance of the robot from the wall less than 20
if the distance of the robot is more than 20 cm from the wall - rotate right.

It will subscribe to the EV3 port where the engine will be,
and it will always read data from this port to check if it's less than 15 cm from the wall.


**5. Do My Algorithm**

.. code-block:: javascript
  :lineno-start: 67
  :linenos:

  bp.registerBThread("Do My Algorithm", function () {
      bp.sync({request: bp.Event("MyAlgorithm", {"EV3": {1: {"Param1": 10, "Param2": {"Param2_1" : "Hello"}}}, "GrovePi": {2: {"Param3": 100}}})});
      var e = bp.sync({waitFor: AlgorithmEventSet});
      var data = JSON.parse(e.data);
      bp.sync({request: bp.Event("Test", data)});
  });

This thread will be responsible for running an algorithm (that the programmer will choose).

It will run "MyAlgorithm" event with the params that the programmer will bring,
and will wait for AlgorithmEventSet to return the result of the algorithm.
When recieving the result from AlgorithmEventSet it will send the data to "Test" event.



**6. Initiation**

.. code-block:: javascript
  :lineno-start: 74
  :linenos:

  bp.registerBThread("Initiation", function () {
  bp.sync({
      block: allEventsButBuildEventSet, request: bp.Event("Build",
          {
              "EV3":
                  [{
                      "Name": "EV3_1",
                      "Port": "rfcomm0",
                      "2": {"Name": "UV3"}
                  }]
              ,
              "GrovePi":
                  [{
                      "Name": "GrovePi1",
                      "A0": {"Name": "", "Device": ""},
                      "A1": "",
                      "A2": "",
                      "D2": {"Name": "MyLed", "Device": "Led"},
                      "D3": "",
                      "D4": {"Name": "UV", "Device": "Ultrasonic"},
                      "D5": "",
                      "D6": "",
                      "D7": "",
                      "D8": "Led"
                  }]
              }
          )
      })
  });

This thread will be responsible for building the robot data that will include the boards
that are in the robot (EV3, GrovePi etc') and the ports that the sensors and actuators are attached to.

It will run "build" event that will block all the other events until it happens.
We want "build" Event to be the first event to run in each program so we will have the robot data.

