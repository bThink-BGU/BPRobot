ctx.registerQuery('color.red', function (e) {
  return e.id === 'EV3_1.S4' && primitivesArraysEqual(e.data, [1])
})