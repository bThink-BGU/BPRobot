/*global bp,sync,bthread,ctx*/

/**
 * @see Building instructions for [Robot Educator]{@link https://education.lego.com/en-us/product-resources/mindstorms-ev3/downloads/building-instructions#:~:text=DOWNLOAD-,Building%20Instructions%20for%20Robot%20Educator,-Building%20Instructions%20for}
 */

const device = 'EV3_1'

bthread('interleave', function () {
  while (true) {
    sync({ waitFor: anySensorsDataEvent, block: anyActuationEvent })
    sync({ waitFor: [anyActuationEvent, anySensorsDataEvent] })
  }
})

bthread('Initialization', function () {
  sync({ block: config.negate(), request: config })
  sync({ request: sensors.subscribe(['ultrasonic', 'remote']) })
})

bthread('Handle remote', function (entity) {
  let moving = false
  let lifting = false
  while (true) {
    sync({ waitFor: anySensorsDataEvent })
    let remoteCommand1 = sensors.getRemoteControlPressedKeys(entity.id, 0)
    let remoteCommand4 = sensors.getRemoteControlPressedKeys(entity.id, 3)

    if (remoteCommand1 !== RemoteControlKey.NONE) {
      if (remoteCommand1.contains(RemoteControlKey.TOP_LEFT)) {
        bp.log.info('1 top left')
      }
      if (remoteCommand1.contains(RemoteControlKey.TOP_RIGHT)) {
        bp.log.info('1 top right')
      }
      if (remoteCommand1.contains(RemoteControlKey.BOTTOM_LEFT)) {
        bp.log.info('1 bottom left')
      }
      if (remoteCommand1.contains(RemoteControlKey.BOTTOM_RIGHT)) {
        bp.log.info('1 bottom right')
      }
    } else {
      if(moving) {
        sync({ request: engine.stop() })
        moving = false
      }
      bp.log.info('1 none')
    }

    if (remoteCommand4 !== RemoteControlKey.NONE) {
      if (remoteCommand4.contains(RemoteControlKey.TOP_LEFT)) {
        bp.log.info('4 top left')
      }
      if (remoteCommand4.contains(RemoteControlKey.TOP_RIGHT)) {
        bp.log.info('4 top right')
      }
    } else {
      bp.log.info('4 none')
    }
  }
})
