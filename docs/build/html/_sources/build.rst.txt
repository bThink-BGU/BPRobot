Build
=======

Event description
------------------

**Event name:** Build

**Params:** Json that describe the boards to support and their details.

* If the board is instance of EV3: for the key 'Port' we specifies the name of the port through him the bluetooth connect to the raspberry pi, as value.

* If the board is instance of Grove pi: for the key of each port the sensors connected we specifies the name of the sensor that connect to the port.

**Description:** The program get the boards and ports which connected to them and build their instances in the system and connect them.


Json structure
----------------

For each board type 'EV3' and 'GrovePi' as key specifies array as value.

In the array will be one or several json that represent the data of the appropriate boards.

The data for EV3 is: the key 'Port', and value is the serial port through him the bluetooth connect to the raspberry pi.

The data for GrovePi is: the keys are the ports(A0-A2, D3-D8) that the sensor connected to, and the values are the names of the sensors.

For example if you have one EV3 and one Grove pi the json without the values will be in this structure:

.. code-block:: javascript
  :linenos:

    {
        "EV3":
            [{
                "Port": ""
            }]
        ,
        "GrovePi":
            [{
                "A0": "",
                "A1": "",
                "A2": "",
                "D2": "",
                "D3": "",
                "D4": "",
                "D5": "",
                "D6": "",
                "D7": "",
                "D8": ""
            }]
    }

For Grove pi ports: The optional sensors that the system support with, are:

.. list-table::
   :widths: 15 10 30

   * - Led
     - Ultrasonic
     - Sound
   * - Button
     - Rotary
     - Relay
   * - Light
     - Buzzer
     - Temperature DHT11
   * - Temperature DHT21
     - Temperature DHT22
     - Temperature AM2301

As mention, the ports of the grove pi are A0-A2 and D0-D8

Port Description
-----------------
.. figure:: /images/GrovePi-Port-description.jpg
    :scale: 50 %

GrovePi sockets A0, A1, and A2 use the A/D converter and support analogRead() values 0-1023.

GrovePi sockets D2 through D8 are digital and support 1-bit input/output, values 0-1, using digitalRead() and digitalWrite().

GrovePi sockets D3, D5, D6 also support Pulse Width Modulation (PWM) which means you can write 8-bit values 0-255 with analogWrite().

.. figure:: /images/ports.jpg
    :scale: 95 %

Support nick names
-------------------
In addition, you can rename the boards and the ports you build, so that it will be easier for you to call them.

To rename the board you need to add 'Name' as key and the name as the value to the json of the board

To rename the ports: in the EV3 add the port name(A-D, 1-4) as key, json with 'Name' as key and the name as the value, as value to the json of the EV3 board

in the GrovePi add to the port name(A0-A2, D0-D8) as key, json with 'Name' and 'Device' as keys and the name want and the name of the sensor respectively as the values, as value.

In this way, you can specifies in the other commands, like: subscribe, unsubscribe, drive, rotate etc. the nicknames as parameters instead of the indexes.

.. warning::

   The name of the ports can't be '_' or numbers with prefix '_' like: '_1', '_2' etc.

Example
----------

.. code-block:: javascript
  :linenos:

   var allEventsButBuildEventSet = bp.EventSet("Block all for build", function (e) {
       return !e.name.equals("Build");
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

.. warning::
   
   The Build Event must to execute first, because all the other events depends on the robot that must build in our program first.