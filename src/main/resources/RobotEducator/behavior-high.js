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
  sync({ request: subscribe(['color']) })
})
