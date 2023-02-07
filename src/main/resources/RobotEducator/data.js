/**
 * @see Building instructions for [Robot Educator with additions]{@link https://education.lego.com/en-us/product-resources/mindstorms-ev3/downloads/building-instructions#:~:text=DOWNLOAD-,Building%20Instructions%20for%20Robot%20Educator,-Building%20Instructions%20for}
 * @see Building instructions for [Robot Educator]{@link https://education.lego.com/v3/assets/blt293eea581807678a/bltc8481dd2666822ff/5f8801e3f4f4cf0fa39d2fef/ev3-rem-driving-base.pdf}
 * @see Building instructions for [Medium Motor Driving Base]{@link https://education.lego.com/v3/assets/blt293eea581807678a/blt9f94cc95ebe17900/5f8801dd69efd81ab4debf02/ev3-medium-motor-driving-base.pdf}
 * @see Building instructions for [Ultrasonic Sensor Driving Base]{@link https://education.lego.com/v3/assets/blt293eea581807678a/blte04fb7bf4f716f3d/5f8801e3bf5ab07ee90076c9/ev3-ultrasonic-sensor-driving-base.pdf}
 * @see Building instructions for [Color Sensor Down]{@link https://education.lego.com/v3/assets/blt293eea581807678a/blt8b300493e30608e9/5f8801dfb8b59a77a945d13c/ev3-rem-color-sensor-down-driving-base.pdf}
 */
const config = command('config', {
  mqtt: {
    address: 'localhost',
    port: 1833
  },
  devices: [
    {
      name: 'EV3_1',
      type: 'EV3BRICK', //corresponds to EV3BRICK, GROVEPI or any ev3dev.hardware.EV3DevPlatform.*
      // mock: true,

      address: 'bt:COM3', //either 'localhost' (default) or '<protocol>:<address>', where protocol = bt/wifi/usb and address = device name
      ports: [
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
        {
          address: 'D',
          name: 'arm',
          type: 'EV3MediumRegulatedMotor', //corresponds to a class that inherits ev3dev.hardware.EV3DevDevice (e.g., EV3LargeRegulatedMotor)
        },
        {
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
        },
        /*{
          address: 'S4',
          name: 'color',
          type: 'EV3ColorSensor',
          mode: 0
        }*/
      ]
    }
  ]
})


ctx.registerQuery('color.red', function (e) {
  return e.id === 'EV3_1.S4' && shallowArraysEqual(e.data, [1])
})

ctx.registerQuery('remote.pressed', function (e) {
  /*  if(e.id === 'EV3_1.S2') {
      bp.log.info("remote {0};{1};{2}" ,e,e.mode === 2,!primitivesArraysEqual(e.data, [0,0,0,0]))
    }*/
  return e.id === 'EV3_1.S1' && e.mode === 2 && !shallowArraysEqual(e.data, [0,0,0,0])
})