/*global bp,ctx*/
const RemoteControlKeys = Packages.il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.RemoteControlKeys

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

/**
 * A set of sensors' related functions.
 * @type {{mockSensorSampleSize: (function(string, int): Event), getSensorsData: (function(): *), getRemoteControlPressedKeys: ((function(*, *): java.util.EnumSet<il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.RemoteControlKeys>)|*), getSensorsChanges: (function(): *), mockSensorValue: (function(string, int[], long): Event)}}
 */
const sensors = {
  /**
   * Cause {@link RobotSensorsDataCollector} to return {@param value} when collecting data.
   * @param {string}address The address (board.port or nickname).
   * @param {int[]}value An array of sensor values.
   * @param {long}delay  delay in milliseconds before the sensors data is changed to {@param value}
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
   * of {@link il.ac.bgu.cs.bp.bprobot.robot.boards.ev3.remote.enums.RemoteControlKeys|RemoteControlKeys}.
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
      return RemoteControlKeys.empty()
    }
    if (portEntity.mode !== 2) {
      throw new Error('Remote mode is not configured to "Remote". Value is ' + portEntity.mode)
    }
    if (channel < 0 || channel >= portEntity.data.length) {
      throw new Error('Illegal channel number: ' + channel)
    }
    return RemoteControlKeys.fromEV3DevID(portEntity.data[channel])
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
  }
}

/**
 * A set of actions for controlling the motors.
 * @type {{rotate: (function(string, int, boolean=): Event), setSpeed: (function(string, int): Event), stop: (function(string, boolean=): Event), forward: (function(string): Event), suspendRegulation: (function(string): Event), backward: (function(string): Event), resetTachoCount: (function(string): Event), rotateTo: (function(string, int, boolean=): Event), setAcceleration: (function(string, int): Event), flt: (function(string, boolean=): Event)}}
 */
const engine = {
  /**
   * Rotate engine by number of degrees.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#rotate-int-boolean-
   * @param {string}address board.port or nickname
   * @param {int}angle number of degrees to rotate relative to the current position
   * @param {boolean}[immediateReturn=true] if true do not wait for the action to complete.
   * @returns {bp.Event}
   */
  rotate: function (address, angle, immediateReturn) {
    if (immediateReturn === undefined || immediateReturn === null) {
      immediateReturn = true
    }
    return portCommand('rotate', address, [angle, immediateReturn])
  },
  /**
   * Rotate engine to the target angle.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#rotateTo-int-boolean-
   * @param {string}address board.port or nickname
   * @param {int}angle Angle to rotate to
   * @param {boolean}[immediateReturn=true] if true do not wait for the action to complete.
   * @returns {bp.Event}
   */
  rotateTo: function (address, angle, immediateReturn) {
    if (immediateReturn === undefined || immediateReturn === null) {
      immediateReturn = true
    }
    return portCommand('rotateTo', address, [angle, immediateReturn])
  },
  /**
   * Reset the tachometer associated with the motor. Note calling this method
   * will cause any current move operation to be halted.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#resetTachoCount--
   * @param {string}address board.port or nickname
   * @returns {bp.Event}
   */
  resetTachoCount: function (address) {
    return portCommand('resetTachoCount', address, [])
  },
  /**
   * Sets desired motors speed , in degrees per second;
   * The maximum reliably sustainable velocity is  100 x battery voltage under
   * moderate load, such as a direct drive robot on the level.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#setSpeed-int-
   * @param {string}address board.port or nickname
   * @param {int}speed value in degrees/sec
   * @returns {bp.Event}
   */
  setSpeed: function (address, speed) {
    return portCommand('setSpeed', address, [speed])
  },
  /**
   * Causes motors to stop, pretty much
   * instantaneously. In other words, the
   * motors doesn't just stop; it will resist
   * any further motion.
   * Cancels any rotate() orders in progress
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#stop-boolean-
   * @param {string}address board.port or nickname
   * @param {boolean}[immediateReturn=true] if true do not wait for the action to complete.
   * @returns {bp.Event}
   */
  stop: function (address, immediateReturn) {
    if (immediateReturn === undefined || immediateReturn === null) {
      immediateReturn = true
    }
    return portCommand('stop', address, [immediateReturn])
  },
  /**
   * sets the acceleration rate of this motor in degrees/sec/sec <br>
   * The default value is 6000; Smaller values will make speeding up. or stopping
   * at the end of a rotate() task, smoother;
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#setAcceleration-int-
   * @param {string}address board.port or nickname
   * @param {int}acceleration
   * @returns {bp.Event}
   */
  setAcceleration: function (address, acceleration) {
    return portCommand('setAcceleration', address, [acceleration])
  },
  /**
   * Set the motors into float mode. This will stop the motors without braking
   * and the position of the motors will not be maintained.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#flt-boolean-
   * @param {string}address board.port or nickname
   * @param {boolean}[immediateReturn=true] if true do not wait for the action to complete.
   * @returns {bp.Event}
   */
  flt: function (address, immediateReturn) {
    if (immediateReturn === undefined || immediateReturn === null) {
      immediateReturn = true
    }
    return portCommand('flt', address, [immediateReturn])
  },
  /**
   * Removes this motors from the motors regulation system. After this call
   * the motors will be in float mode and will have stopped. Note calling any
   * of the high level move operations (forward, rotate etc.), will
   * automatically enable regulation.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#suspendRegulation--
   * @param {string}address board.port or nickname
   * @returns {bp.Event}
   */
  suspendRegulation: function (address) {
    return portCommand('suspendRegulation', address)
  },
  /**
   * Causes motors to rotate forward until {@link engine.stop} or {@link engine.flt} is called.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#forward--
   * @param {string}address board.port or nickname
   * @returns {bp.Event}
   */
  forward: function (address) {
    return portCommand('forward', address)
  },
  /**
   * Causes motors to rotate backward until {@link engine.stop} or {@link engine.flt} is called.
   * @see https://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/BaseRegulatedMotor.html#forward--
   * @param {string}address board.port or nickname
   * @returns {bp.Event}
   */
  backward: function (address) {
    return portCommand('backward', address)
  }
}

// Object.freeze(engine)

/**
 * Subscribe to sensors readings.
 * @param {string|string[]}address board.port or nickname
 * @returns {bp.Event}
 */
function subscribe(address) {
  return portCommand('subscribe', address)
}

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
    if (obj === null || ''===obj) {
      ctx.getEntityById('sensors').data = null
      ctx.getEntityById('changes').data = new java.util.HashSet()
      return;
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