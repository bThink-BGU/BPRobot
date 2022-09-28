// noinspection JSCheckFunctionSignatures,InfiniteLoopJS
// noinspection InfiniteLoopJS

function command(commandName, params, address) {
  if (params == null) {
    // received action(s)
    params = commandName
    if (!Array.isArray(params))
      params = [commandName]
    commandName = 'do'
  } else if (address == null) {
    //received a command with no address, do nothing
  } else {
    params = [action(commandName, params, address)]
  }

  return bp.Event('Command', JSON.stringify({ action: commandName, params: params }))
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
        command([
          action('rotate', 'EV3_1.B', [60, true]),
          action('rotate', 'EV3_1.C', [60, true])
        ]), // drive straight
        command('rotate', 'EV3_1.B', [60, true]), //turn right
        command('rotate', 'EV3_1.C', [60, true]) //turn left
      ]
    })
  }
})

bthread('Stop on red', function () {
  command('subscribe', 'EV3_1.S3')
  while (true) {
    sync({ waitFor: e => e.name === 'sensors_data' && e.data.color === 'RED' })
    sync({ block: e => e.name === 'do' && e.data.action === 'rotate' })
  }
})

bthread('Initiation', function () {
  sync({ block: config.negate(), request: config })
})