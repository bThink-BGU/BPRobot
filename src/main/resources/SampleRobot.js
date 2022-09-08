function command(commandName, params) {
  return bp.Event('Command', { action: commandName, params: params })
}

function portParams(address, params) {
  return { address: address, params: params }
}

function portCommand(commandName, address, params) {
  return command(commandName, [portParams(address, params)])
}

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
          address: 'B',
          name: 'MotorB',
          type: 'EV3LargeRegulatedMotor' //corresponds to a class that inherits or ev3dev.hardware.EV3DevDevice (e.g., EV3LargeRegulatedMotor)
        },
        {
          address: 'C',
          name: 'MotorC',
          type: 'EV3LargeRegulatedMotor'
        },
        {
          address: 'S3',
          name: 'Color',
          type: 'EV3ColorSensor',
          mode: 0
        }
      ]
    }
  ]
})

bthread('Drive', function () {
  while (true) {
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
  command('subscribe', 'EV3_1.S3')
  while (true) {
    sync({
      request: [
        command('rotate', [portParams('EV3_1.B', [60, true]), portParams('EV3_1.C', [60, true])]),
        portCommand('rotate', 'EV3_1.B', [0, true]),
        portCommand('rotate', 'EV3_1.C', [0, true])
      ]
    })
  }
})

bthread('Initiation', function () {

  sync({ block: config.not(), request: config })
})