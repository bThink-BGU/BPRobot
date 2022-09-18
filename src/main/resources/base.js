function command(commandName, params) {
  return bp.Event('Command', JSON.stringify({ action: commandName, params: params }))
}

function portParams(address, params) {
  return { address: address, params: params }
}

function portCommand(commandName, address, params) {
  return command(commandName, [portParams(address, params)])
}