const anyActuation = bp.EventSet('AnyActuation', function (e) {
  return e.name === 'Command' && ['rotate'].includes(e.data.action)
})

const sensorsDataEvent = bp.EventSet('SensorsDataEvent', function (e) {
  return e.name === 'SensorsData'
})

const sensorsDataChanged = bp.EventSet('SensorsDataChanged', function (e) {
  return e.name === 'SensorsData' && ctx.getEntityById('changes').size() > 0
})

function command(commandName, params) {
  return bp.Event('Command', { action: commandName, params: params })
}

function portParams(address, params) {
  return { address: address, params: params }
}

function portCommand(commandName, address, params) {
  return command(commandName, [portParams(address, params)])
}

/**
 * Cause {@link RobotSensorsDataCollector} to return {@param value} when collecting data.
 * @param address
 * @param delay  delay in milliseconds before the sensors data is changed to {@param value}
 * @param value An array of sensor values.
 */
function mockSensorValue(address, value, delay) {
  if (delay === undefined || delay === null) {
    delay = 0
  }
  return portCommand('mockSensorReadings', address, { value: value, delay: delay })
}

/**
 * Mock sampleSize() value of a device
 * @param address
 * @param size  the desired size
 */
function mockSensorSampleSize(address, size) {
  return portCommand('mockSensorSampleSize', address, size)
}

const remoteIsCurrentlyPressed = function (mode, dir) {
  return bp.EventSet('remoteIsCurrentlyPressed', function (e) {
    return e.name === 'SensorsData' &&
      ctx.getEntityById('sensors').mode === mode &&
      ctx.getEntityById('sensors').data[0] === dir
  })
}

ctx.populateContext([
  ctx.Entity('sensors', 'system', { name: null, data: null }),
  ctx.Entity('changes', 'system', { data: null })
])

function primitivesArraysEqual(array1, array2) {
  return array1.length === array2.length && array1.every((value, index) => value === array2[index])
}

ctx.registerEffect('Command', function (data) {
  switch (data.action) {
    case 'config':
      data.params.devices.forEach(d => {
        d.ports.forEach(p => {
          ctx.insertEntity(ctx.Entity(d.name + '.' + p.address, 'port', { name: p.name, mode: 0, data: [] }))
        })
      })
      break
  }
})

ctx.registerEffect('SensorsData', function (data) {
  var obj = data.get()
  if (obj === null) {
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

    if (!primitivesArraysEqual(sensorData.value, portEntity.data)) {
      portEntity.data = sensorData.value
      changed = true
    }
    if (portEntity.mode !== sensorData.mode) {
      portEntity.mode = sensorData.mode
      changed = true
    }
    if (changed) {
      changes.add(sensorData)
    }
  }
  bp.log.info('Changes: {0}', changes)
  ctx.getEntityById('changes').data = changes
})