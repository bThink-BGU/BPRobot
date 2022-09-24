function command(commandName, params) {
  return bp.Event('Command', JSON.stringify({ action: commandName, params: params }))
}

function portParams(address, params) {
  return { address: address, params: params }
}

function portCommand(commandName, address, params) {
  return command(commandName, [portParams(address, params)])
}

const sensorsDataChanged = bp.EventSet('SensorsData', function (e) {
  return e.name === 'SensorsData' && ctx.getEntityById('changes').size() > 0
})

const remoteIsCurrentlyPressed = function (mode, dir) {
  return bp.EventSet('remoteIsCurrentlyPressed', function (e) {
    return e.name === 'SensorsData' &&
      ctx.getEntityById('sensors').mode === mode &&
      ctx.getEntityById('sensors').data[0] === dir
  })
}

const ports = ['A', 'B', 'C', 'D', 'S1', 'S2', 'S3', 'S4']

ctx.populateContext([
  ctx.Entity('sensors', 'system', { name: null, data: null }),
  ctx.Entity('changes', 'system', { data: null }),
  ctx.Entity('sensors_changes', 'system', { data: [] })
])

ctx.populateContext(ports.map(p => ctx.Entity(p, 'port', { mode: 0, data: [] })))

function primitivesArraysEqual(array1, array2) {
  return array1.length === array2.length && array1.every((value, index) => value === array2[index])
}

ctx.registerEffect('SensorsData', function (data) {
  var json = JSON.parse(data)
  ctx.getEntityById('sensors').data = json
  var changes = new java.util.HashSet()
  for (let prop in json) {
    var port = json[prop]
    let changed = false
    let s = ctx.getEntityById(port.port)
    if (s.name != port.name) {
      s.name = port.name
    }
    if (!primitivesArraysEqual(s.data, port.data)) {
      s.data = port.data
      changed = true
    }
    if (s.mode !== port.mode) {
      s.mode = port.mode
      changed = true
    }
    if (changed) {
      changes.add(port)
    }
  }
  ctx.getEntityById('changes').data = changes
})