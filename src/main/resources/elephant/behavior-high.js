// https://pybricks.com/ev3-micropython/examples/elephant.html

const actions = {
  walk: portCommand('rotate', 'legs', [60, true]),
  stop_legs: portCommand('brake', 'EV3_1.A'),
  raise_trunk: portCommand('rotate', 'trunk', [200, true]),
  lower_trunk: portCommand('rotate', 'trunk', [-200, true]),
  stop_trunk: portCommand('brake', 'trunk'),
  raise_neck: portCommand('rotate', 'neck', [200, true]),
  lower_neck: portCommand('rotate', 'neck', [-200, true]),
  stop_neck: portCommand('brake', 'neck'),
  subscribe: address => portCommand('subscribe', address)
}

const device = 'EV3_1'

bthread('walk', function () {
  while (true) {
    sync({ request: actions.walk })
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
  sync({ block: [actions.raise_trunk] })
})

bthread('Initiation', function () {
  sync({ block: config.negate(), request: config })
  sync({ request: mockSensorSampleSize('touch', 1) })
  sync({ request: mockSensorSampleSize('EV3_1.S4', 1) }) //color
  sync({ request: actions.subscribe(['EV3_1.S1', 'EV3_1.S2', 'color']) })
})

bthread('interleave', function () {
  while (true) {
    sync({ waitFor: anyActuation })
    sync({ waitFor: sensorsDataEvent, block: anyActuation })
  }
})


bthread('mock', function () {
  sync({ request: mockSensorValue('touch', [1], 10000) })
  sync({ request: mockSensorValue('color', [1], 5000) })
})