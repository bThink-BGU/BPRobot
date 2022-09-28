// https://pybricks.com/ev3-micropython/examples/elephant.html

const actions = {
  walkForward: portCommand('rotate', 'legs', [900, true]),
  walkBackward: portCommand('rotate', 'legs', [-900, true]),
  stop_legs: portCommand('brake', 'EV3_1.A'),
  raise_trunk: portCommand('forward', 'trunk'),
  lower_trunk: portCommand('backward', 'trunk'),
  stop_trunk: portCommand('brake', 'trunk'),
  raise_neck: portCommand('forward', 'neck'),
  lower_neck: portCommand('backward', 'neck'),
  stop_neck: portCommand('brake', 'neck'),
  subscribe: address => portCommand('subscribe', address)
}

const device = 'EV3_1'

bthread('interleave', function () {
  while (true) {
    sync({ waitFor: sensorsDataEvent, block: anyActuation })
    sync({ waitFor: anyActuation })
  }
})

bthread('Initiation', function () {
  sync({ block: config.negate(), request: config })
  sync({ request: portCommand('setSpeed', 'neck', 750) })
  sync({ request: portCommand('setSpeed', 'trunk', 600) })
  sync({ request: actions.subscribe(['EV3_1.S1', 'EV3_1.S2', 'color']) })
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

bthread('mock', function () {
  sync({ request: mockSensorSampleSize('touch', 1) })
  sync({ request: mockSensorSampleSize('EV3_1.S4', 1) }) //color
  sync({ request: mockSensorValue('touch', [1], 10000) })
  sync({ request: mockSensorValue('color', [1], 5000) })
})