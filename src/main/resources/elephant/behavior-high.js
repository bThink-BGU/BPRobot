// https://pybricks.com/ev3-micropython/examples/elephant.html

const actions = {
  walkForward: engine.rotate('legs', 900),
  walkBackward: engine.rotate('legs', -900),
  stop_legs: engine.brake('EV3_1.A'),
  raise_trunk: engine.forward('trunk'),
  lower_trunk: engine.backward('trunk'),
  stop_trunk: engine.brake('trunk'),
  raise_neck: engine.forward('neck'),
  lower_neck: engine.backward('neck'),
  stop_neck: engine.brake('neck')
}

const device = 'EV3_1'
const remoteChannel = 1

bthread('interleave', function () {
  while (true) {
    sync({ waitFor: anySensorsDataEvent, block: anyActuationEvent })
    sync({ waitFor: anyActuationEvent })
  }
})

bthread('Initiation', function () {
  sync({ block: config.negate(), request: config })
  sync({ request: portCommand('setSpeed', 'neck', 750) })
  sync({ request: portCommand('setSpeed', 'trunk', 600) })
  sync({ request: subscribe(['EV3_1.S1', 'EV3_1.S2', 'color']) })
})


bthread('walk', function () {
  while (true) {
    sync({ request: [actions.walkForward, actions.walkBackward] })
  }
})

bthread('move neck', function () {
  while (true) {
    sync({ request: [actions.raise_neck, actions.lower_neck] })
  }
})

bthread('move trunk', function () {
  while (true) {
    sync({ request: [actions.raise_trunk, actions.lower_trunk] })
  }
})

ctx.bthread('Stop raise neck on red', 'color.red', function (entity) {
  sync({ request: actions.stop_neck, block: actions.raise_neck })
  sync({ block: [actions.raise_neck] })
})

ctx.bthread('Stop raise trunk on touch pressed', 'touch.pressed', function (entity) {
  sync({ request: actions.stop_trunk, block: actions.raise_trunk })
  // ev3.speaker.play_file(SoundFile.ELEPHANT_CALL)
  sync({ block: [actions.raise_trunk] })
})

ctx.bthread('Handle remote', 'remote.pressed', function (entity) {
  while(true) {
    let remoteCommand = sensors.getRemoteControlPressedKeys(entity.id, remoteChannel)
    if (remoteCommand.contains(RemoteControlKey.TOP_LEFT)) {
      bp.log.info('top left')
    }
    if (remoteCommand.contains(RemoteControlKey.TOP_RIGHT)) {
      bp.log.info('top right')
    }
    if (remoteCommand.contains(RemoteControlKey.BOTTOM_LEFT)) {
      bp.log.info('bottom left')
    }
    if (remoteCommand.contains(RemoteControlKey.BOTTOM_RIGHT)) {
      bp.log.info('bottom right')
    }
    if (remoteCommand.contains(RemoteControlKey.CENTER_BEACON)) {
      bp.log.info('center beacon')
    }
    sync({waitFor: anySensorsDataEvent})
  }
})

bthread('mock', function () {
  sync({ request: sensors.mockSensorSampleSize('touch', 1) })
  sync({ request: sensors.mockSensorSampleSize('remote', 4) })
  sync({ request: sensors.mockSensorSampleSize('EV3_1.S4', 1) }) //color
  sync({ request: sensors.mockSensorValue('touch', [1], 10000) })
  sync({ request: sensors.mockSensorValue('color', [1], 5000) })
  sync({ request: sensors.mockSensorValue('remote', [0,1,0,0], 1000) })
  sync({ request: sensors.mockSensorValue('remote', [0,5,0,0], 2000) })
})