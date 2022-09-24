// https://pybricks.com/ev3-micropython/examples/elephant.html
const config = command('config', {
  mqtt: {
    address: 'localhost',
    port: 1833
  },
  devices: [
    {
      name: 'EV3_1',
      type: 'EV3BRICK', //corresponds to GROVEPI or any ev3dev.hardware.EV3DevPlatform.*
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
          address: 'S4',
          name: 'color',
          type: 'EV3ColorSensor',
          mode: 0
        }
      ]
    }
  ]
})

bthread('reset', function () {
  while (true) {
    sync({ waitFor: bp.Event('reset') })
    sync({
      request: [
        command('rotate', [portParams('EV3_1.B', [60, true]), portParams('EV3_1.C', [60, true])]),
        portCommand('rotate', 'EV3_1.B', [0, true]),
        portCommand('rotate', 'EV3_1.C', [0, true])
      ]
    })
  }
})

bthread('walk', function () {
  while (true) {
    sync({ waitFor: bp.Event('A') })
    sync({
      request: [
        command('rotate', [portParams('EV3_1.B', [60, true]), portParams('EV3_1.C', [60, true])]),
        portCommand('rotate', 'EV3_1.B', [0, true]),
        portCommand('rotate', 'EV3_1.C', [0, true])
      ]
    })
  }
})

bthread('Stop on red', function () {
    while (true) {
      sync({ waitFor: bp.Event('A') })
      sync({
        request: [
          command('rotate', [portParams('EV3_1.B', [60, true]), portParams('EV3_1.C', [60, true])]),
          portCommand('rotate', 'EV3_1.B', [0, true]),
          portCommand('rotate', 'EV3_1.C', [0, true])
        ]
      })
    }
  }
)

bthread('Initiation', function () {
  sync({ block: config.negate(), request: config })
})