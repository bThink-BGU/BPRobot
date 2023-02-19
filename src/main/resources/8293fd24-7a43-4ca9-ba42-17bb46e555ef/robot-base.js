/*global bp,ctx*/
const RemoteControlKey = Packages.il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.RemoteControlKey
const Color = Packages.il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.Color
const DeviceMode = Packages.il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.DeviceMode

const anyActuationEvent = bp.EventSet('AnyActuation', function (e) {
  return e.name === 'Command' && ['rotate', 'forward', 'backward'].includes(e.data.action)
})

const anySensorsDataEvent = bp.EventSet('SensorsDataEvent', function (e) {
  return e.name === 'SensorsData'
})

const anySensorsDataChangedEvent = bp.EventSet('SensorsDataChanged', function (e) {
  return e.name === 'SensorsData' && ctx.getEntityById('changes').size() > 0
})

/**
 * Create a Command event.
 * @param {string}commandName The name of the command.
 * @param {*}params The parameters of the command.
 * @returns {bp.Event} A Command event.
 */
function command(commandName, params) {
  return bp.Event('Command', { action: commandName, params: params })
}

function portParams(address, params) {
  return { address: address, params: params }
}

/**
 * Create a command event for a port. A command can be one of the following:
 * 1. A call to a void method of the {@link https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html|BaseRegulatedMotor} class.
 * @param {string}commandName The name of the method.
 * @param {string}address The address (board.port or nickname).
 * @param {object|*[]}[params] The method's parameters.
 * @returns {bp.Event} A Command event.
 */
function portCommand(commandName, address, params) {
  return command(commandName, [portParams(address, params)])
}

/**
 * A shallow equals for arrays.
 * @param {*[]}array1
 * @param {*[]}array2
 * @returns {boolean}
 */
function shallowArraysEqual(array1, array2) {
  return array1.length === array2.length && array1.every((value, index) => value === array2[index])
}

const sensors = {
  /**
   * Cause {@link RobotSensorsDataCollector} to return {@param value} when collecting data.
   * @param {string}address The address (board.port or nickname).
   * @param {int[]}value An array of sensor values.
   * @param {int}delay  delay in milliseconds before the sensors data is changed to {@param value}
   * @returns {bp.Event} A Command event.
   */
  mockSensorValue: function (address, value, delay) {
    if (delay === undefined || delay === null) {
      delay = 0
    }
    return portCommand('mockSensorReadings', address, { value: value, delay: delay })
  },

  /**
   * Mock the sampleSize() value of a device
   * @param {string}address The address (board.port or nickname).
   * @param {int}size  the desired size
   * @returns {bp.Event} A Command event.
   */
  mockSensorSampleSize: function (address, size) {
    return portCommand('mockSensorSampleSize', address, size)
  },

  /**
   * Get the pressed keys of the remote control.
   * The returned object is a {@link https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/class-use/EnumSet.html|java.util.EnumSet}
   * of {@link il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.RemoteControlKeys|RemoteControlKey}.
   * @param full_address board.port
   * @param channel the channel of the remote control (0-3)
   * @returns {java.util.EnumSet<il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.RemoteControlKeys>}
   */
  getRemoteControlPressedKeys: function (full_address, channel) {
    let portEntity = null
    try {
      portEntity = ctx.getEntityById(full_address)
    } catch (e) {
      throw new Error('Port ' + full_address + ' is not configured')
    }
    if (portEntity.data.length === 0) {
      return RemoteControlKey.empty()
    }
    if (portEntity.mode !== 2) {
      throw new Error('Remote mode is not configured to "Remote". Value is ' + portEntity.mode)
    }
    if (channel < 0 || channel >= portEntity.data.length) {
      throw new Error('Illegal channel number: ' + channel)
    }
    return RemoteControlKey.fromEV3DevID(portEntity.data[channel])
  },

  /**
   * Get the sensors readings from the last SensorsData event.
   * The returned object is a map from sensor address (board.port) to an object with the following keys:
   * board, port, name, value.
   * @returns {object}
   */
  getSensorsData: function () {
    return ctx.getEntityById('sensors').data
  },

  /**
   * Get the changes in the sensors readings since the last SensorsData event.
   * The returned object is a map from sensor address (board.port) to an object with the following keys:
   * board, port, name, value.
   * @returns {object}
   */
  getSensorsChanges: function () {
    return ctx.getEntityById('changes').data
  },

  /**
   * Subscribe to sensors readings.
   * @param {string|string[]}address board.port or nickname
   * @returns {bp.Event}
   */
  subscribe: function (address) {
    return portCommand('subscribe', address)
  }
}

const engine = {
  /**
   * Removes power from the motor and creates a passive electrical load. This is usually done by shorting the motor terminals together. This load will absorb the energy from the rotation of the motors and cause the motor to stop more quickly than coasting.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#brake--
   * @param {string|string[]}addresses board.port or nickname
   * @returns {bp.Event}
   */
  brake: function (addresses) {
    let address = addresses
    if (!Array.isArray(addresses)) {
      address = [addresses]
    }
    address.map(a => portParams(a, []))
    return command('brake', address)
  },
  /**
   * Rotate engine by number of degrees.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#rotate-int-boolean-
   * @param {string|string[]}addresses board.port or nickname
   * @param {int}angle number of degrees to rotate relative to the current position
   * @param {boolean}[immediateReturn=true] if true do not wait for the action to complete.
   * @returns {bp.Event}
   */
  rotate: function (addresses, angle, immediateReturn) {
    if (immediateReturn === undefined || immediateReturn === null) {
      immediateReturn = true
    }
    let address = addresses
    if (!Array.isArray(addresses)) {
      address = [addresses]
    }
    address.map(a => portParams(a, [angle, immediateReturn]))
    return command('rotate', address)
  },
  /**
   * Rotate engine to the target angle.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#rotateTo-int-boolean-
   * @param {string|string[]}addresses board.port or nickname
   * @param {int}angle Angle to rotate to
   * @param {boolean}[immediateReturn=true] if true do not wait for the action to complete.
   * @returns {bp.Event}
   */
  rotateTo: function (addresses, angle, immediateReturn) {
    if (immediateReturn === undefined || immediateReturn === null) {
      immediateReturn = true
    }
    let address = addresses
    if (!Array.isArray(addresses)) {
      address = [addresses]
    }
    address.map(a => portParams(a, [angle, immediateReturn]))
    return command('rotateTo', address)
  },
  /**
   * Reset the tachometer associated with the motor. Note calling this method
   * will cause any current move operation to be halted.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#resetTachoCount--
   * @param {string|string[]}addresses board.port or nickname
   * @returns {bp.Event}
   */
  resetTachoCount: function (addresses) {
    let address = addresses
    if (!Array.isArray(addresses)) {
      address = [addresses]
    }
    address.map(a => portParams(a, []))
    return command('resetTachoCount', address)
  },
  /**
   * Sets desired motors speed , in degrees per second;
   * The maximum reliably sustainable velocity is  100 x battery voltage under
   * moderate load, such as a direct drive robot on the level.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#setSpeed-int-
   * @param {string|string[]}addresses board.port or nickname
   * @param {int}speed value in degrees/sec
   * @returns {bp.Event}
   */
  setSpeed: function (addresses, speed) {
    let address = addresses
    if (!Array.isArray(addresses)) {
      address = [addresses]
    }
    address.map(a => portParams(a, [speed]))
    return command('setSpeed', address)
  },
  /**
   * Causes motors to stop, pretty much
   * instantaneously. In other words, the
   * motors doesn't just stop; it will resist
   * any further motion.
   * Cancels any rotate() orders in progress
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#stop-boolean-
   * @param {string|string[]}addresses board.port or nickname
   * @param {boolean}[immediateReturn=true] if true do not wait for the action to complete.
   * @returns {bp.Event}
   */
  stop: function (addresses, immediateReturn) {
    if (immediateReturn === undefined || immediateReturn === null) {
      immediateReturn = true
    }
    let address = addresses
    if (!Array.isArray(addresses)) {
      address = [addresses]
    }
    address.map(a => portParams(a, [immediateReturn]))
    return command('stop', address)
  },
  /**
   * sets the acceleration rate of this motor in degrees/sec/sec <br>
   * The default value is 6000; Smaller values will make speeding up. or stopping
   * at the end of a rotate() task, smoother;
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#setAcceleration-int-
   * @param {string|string[]}addresses board.port or nickname
   * @param {int}acceleration
   * @returns {bp.Event}
   */
  setAcceleration: function (addresses, acceleration) {
    let address = addresses
    if (!Array.isArray(addresses)) {
      address = [addresses]
    }
    address.map(a => portParams(a, [acceleration]))
    return command('setAcceleration', address)
  },
  /**
   * Set the motors into float mode. This will stop the motors without braking
   * and the position of the motors will not be maintained.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#flt-boolean-
   * @param {string|string[]}addresses board.port or nickname
   * @param {boolean}[immediateReturn=true] if true do not wait for the action to complete.
   * @returns {bp.Event}
   */
  flt: function (addresses, immediateReturn) {
    if (immediateReturn === undefined || immediateReturn === null) {
      immediateReturn = true
    }
    let address = addresses
    if (!Array.isArray(addresses)) {
      address = [addresses]
    }
    address.map(a => portParams(a, [immediateReturn]))
    return command('flt', address)
  },
  /**
   * Removes this motors from the motors regulation system. After this call
   * the motors will be in float mode and will have stopped. Note calling any
   * of the high level move operations (forward, rotate etc.), will
   * automatically enable regulation.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#suspendRegulation--
   * @param {string|string[]}addresses board.port or nickname
   * @returns {bp.Event}
   */
  suspendRegulation: function (addresses) {
    let address = addresses
    if (!Array.isArray(addresses)) {
      address = [addresses]
    }
    address.map(a => portParams(a, []))
    return command('suspendRegulation', address)
  },
  /**
   * Causes motors to rotate forward until {@link engine.stop} or {@link engine.flt} is called.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#forward--
   * @param {string|string[]}addresses board.port or nickname
   * @returns {bp.Event}
   */
  forward: function (addresses) {
    let address = addresses
    if (!Array.isArray(addresses)) {
      address = [addresses]
    }
    address.map(a => portParams(a, []))
    return command('forward', address)
  },
  /**
   * Causes motors to rotate backward until {@link engine.stop} or {@link engine.flt} is called.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#forward--
   * @param {string|string[]}addresses board.port or nickname
   * @returns {bp.Event}
   */
  backward: function (addresses) {
    let address = addresses
    if (!Array.isArray(addresses)) {
      address = [addresses]
    }
    address.map(a => portParams(a, []))
    return command('backward', address)
  }
}

// Object.freeze(sensors)
// Object.freeze(engine)

(() => {
  ctx.populateContext([
    ctx.Entity('sensors', 'system', { name: null, data: null }),
    ctx.Entity('changes', 'system', { data: null })
  ])

  ctx.registerEffect('Command', function (data) {
    switch (data.action) {
      case 'config':
        data.params.devices.forEach(d => {
          d.ports.forEach(p => {
            ctx.insertEntity(ctx.Entity(d.name + '.' + p.address, 'port', { name: p.name, mode: p.mode, data: [] }))
          })
        })
        break
    }
  })

  ctx.registerEffect('SensorsData', function (data) {
    var obj = data.get()
    if (obj === null || '' === obj) {
      ctx.getEntityById('sensors').data = null
      ctx.getEntityById('changes').data = new java.util.HashSet()
      return
    }
    var json = JSON.parse(data)
    ctx.getEntityById('sensors').data = json
    var changes = new java.util.HashSet()
    for (let prop in json) {
      let portEntity = null
      try {
        portEntity = ctx.getEntityById(prop)
      } catch (e) {
        throw new Error('Port ' + prop + ' is not configured')
      }
      var sensorData = json[prop]
      let changed = false

      if (!shallowArraysEqual(sensorData.value, portEntity.data)) {
        portEntity.data = sensorData.value
        changed = true
      }
      /*    if(sensorData.mode === 2){
            bp.log.info('prop {0}; data {1}; neq {2}', prop.mode, sensorData.mode, portEntity.mode !== sensorData.mode)
          }*/
      if (portEntity.mode !== sensorData.mode) {
        // bp.log.info("updating mode of {0}" , prop)
        portEntity.mode = sensorData.mode
        // bp.log.info("updated mode of {0}" , prop)
        changed = true
      }
      if (changed) {
        changes.add(sensorData)
      }
    }
    ctx.getEntityById('changes').data = changes
  })
})()