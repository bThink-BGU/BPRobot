/*global bp,sync,bthread,ctx*/

/**
 * @see Building instructions for [Robot Educator]{@link https://education.lego.com/en-us/product-resources/mindstorms-ev3/downloads/building-instructions#:~:text=DOWNLOAD-,Building%20Instructions%20for%20Robot%20Educator,-Building%20Instructions%20for}
 */

const device = 'EV3_1'

bthread('interleave', function () {
  while (true) {
    sync({ waitFor: anySensorsDataEvent, block: anyActuationEvent })
    sync({ waitFor: anyActuationEvent })
  }
})

bthread('Initiation', function () {
  sync({ block: config.negate(), request: config })
  sync({ request: portCommand('setSpeed', 'right', 900) })
  sync({ request: portCommand('setSpeed', 'left', 900) })
  sync({ request: actions.subscribe(['EV3_1.S1', 'EV3_1.S2', 'color']) })
})


bthread('mock', function () {
  sync({ request: mockSensorSampleSize('touch', 1) })
  sync({ request: mockSensorSampleSize('remote', 4) })
  sync({ request: mockSensorSampleSize('EV3_1.S4', 1) }) //color
  sync({ request: mockSensorValue('touch', [1], 10000) })
  sync({ request: mockSensorValue('color', [1], 5000) })
  sync({ request: mockSensorValue('remote', [0,1,0,0], 1000) })
  sync({ request: mockSensorValue('remote', [0,5,0,0], 2000) })
})