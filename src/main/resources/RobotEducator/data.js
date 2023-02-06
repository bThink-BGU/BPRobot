const config = command('config', {
/*  mqtt: {
    address: 'localhost',
    port: 1833
  },*/
  devices: [
    {
      name: 'EV3_1',
      type: 'EV3BRICK', //corresponds to GROVEPI or any ev3dev.hardware.EV3DevPlatform.*
      // mock: true,

      address: 'bt:COM4', //either 'localhost' (default) or '<protocol>:<address>', where protocol = bt/wifi/usb and address = device name
      ports: [
/*        {
          address: 'A',
          name: 'arm',
          type: 'EV3MediumRegulatedMotor', //corresponds to a class that inherits or ev3dev.hardware.EV3DevDevice (e.g., EV3LargeRegulatedMotor)
        },*/
        {
          address: 'B',
          name: 'right',
          type: 'EV3LargeRegulatedMotor'
        },
        {
          address: 'C',
          name: 'left',
          type: 'EV3LargeRegulatedMotor'
        },
        /*{
          address: 'S1',
          name: 'remote',
          type: 'EV3IRSensor',
          mode: 2
        },

        {
          address: 'S2',
          name: 'ultrasonic',
          type: 'EV3UltrasonicSensor',
          mode: 0
        },*/
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


ctx.registerQuery('color.red', function (e) {
  return e.id === 'EV3_1.S4' && shallowArraysEqual(e.data, [1])
})