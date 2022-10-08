const config = command('config', {
  mqtt: {
    address: 'localhost',
    port: 1833
  },
  devices: [
    {
      name: 'EV3_1',
      type: 'EV3BRICK', //corresponds to GROVEPI or any ev3dev.hardware.EV3DevPlatform.*
      // mock: true,
      address: 'bt:COM1', //either 'localhost' (default) or '<protocol>:<address>', where protocol = bt/wifi/usb and address = device name, MAC address or IP address
      ports: [
        {
          address: 'A',
          name: 'legs',
          type: 'EV3LargeRegulatedMotor' //corresponds to a class that inherits or ev3dev.hardware.EV3DevDevice (e.g., EV3LargeRegulatedMotor)
        },
        {
          address: 'B',
          name: 'trunk',
          type: 'EV3MediumRegulatedMotor'
        },
        {
          address: 'D',
          name: 'neck',
          type: 'EV3LargeRegulatedMotor'
        },
        {
          address: 'S1',
          name: 'touch',
          type: 'EV3TouchSensor',
          mode: 0
        },
        {
          address: 'S2',
          name: 'remote',
          type: 'EV3IRSensor',
          mode: 2
        },
        {
          address: 'S4',
          name: 'color',
          type: 'EV3ColorSensor',
          mode: 0
        }
      ]
    }
  ]
})

