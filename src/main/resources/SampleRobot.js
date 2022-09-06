function command(commandName, params) {
  return bp.Event('Command', { action: commandName, params: params })
}

function portParams(address, params) {
  return { address: address, params: params }
}

function portCommand(commandName, address, params) {
  return command(commandName, [portParams(address, params)])
}

const allEventsButBuildEventSet = bp.EventSet('Block all for build', function (e) {
  return e.name.equals('Command') && e.data.action.equals('Build')
})

const dataEventSet = bp.EventSet('', function (e) {
  return e.name.equals('Command') && e.data.action.equals('GetSensorsData')
})

bthread('Drive', function () {
  while (true) {
    sync({
      request: [
        command('rotate', [portParams('EV3_1.B', [60, true]), portParams('EV3_1.C', [60, true])]),
        portCommand('rotate', 'EV3_1.B', [0, true]),
        portCommand('rotate', 'EV3_1.C', [0, true])
      ]
    })
  }
})

bthread('Initiation', function () {
  sync({
    block: allEventsButBuildEventSet, request: command('Build', [
      {
        Name: 'EV3_1',
        Type: 'EV3',
        Ports: [
          {
            Port: 2,
            Name: 'UV3',
            Type: 'Ultrasonic'
          }
        ]
      }
    ])
  })
})