// https://pybricks.com/ev3-micropython/examples/elephant.html

const actions = {
  walk: portCommand('rotate', 'legs', [60, true]),
  stop_legs: portCommand('brake', 'EV3_1.A'),
  raise_trunk: portCommand('rotate', 'trunk', [60, true]),
  lower_trunk: portCommand('rotate', 'trunk', [-60, true]),
  stop_trunk: portCommand('brake', 'trunk'),
  stop_neck: portCommand('brake', 'neck'),
  subscribe: address => portCommand('subscribe', address)
}

const device = 'EV3_1'

bthread('walk', function () {
  while (true) {
    sync({ request: actions.walk })
  }
})

bthread('move trunk', function () {
  while (true) {
    sync({ request: [actions.raise_trunk,actions.lower_trunk] })
  }
})

ctx.bthread('Stop trunk on red', 'color.red', function (entity) {
  sync({ request: actions.stop_trunk, block: [actions.raise_trunk, actions.lower_trunk] })
  sync({ block: [actions.raise_trunk, actions.lower_trunk] })
})

bthread('Initiation', function () {
  sync({ block: config.negate(), request: config })
  sync({ request: mockSensorSampleSize('EV3_1.S4', 1) })
  sync({ request: actions.subscribe(['EV3_1.S1', 'EV3_1.S2', 'color']) })
})

bthread('interleave', function () {
  while (true) {
    sync({ waitFor: anyActuation })
    sync({ waitFor: sensorsDataEvent, block: anyActuation })
  }
})


bthread('mock', function () {
  sync({ waitFor: sensorsDataEvent })
  sync({ waitFor: sensorsDataEvent })
  sync({ waitFor: sensorsDataEvent })
  sync({ waitFor: sensorsDataEvent })
  sync({ request: mockSensorValue('color', [1]) })
})