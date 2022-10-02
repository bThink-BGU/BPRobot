ctx.registerQuery('color.red', function (e) {
  return e.id === 'EV3_1.S4' && primitivesArraysEqual(e.data, [1])
})

ctx.registerQuery('touch.pressed', function (e) {
  return e.id === 'EV3_1.S1' && primitivesArraysEqual(e.data, [1])
})

ctx.registerQuery('remote.pressed', function (e) {
/*  if(e.id === 'EV3_1.S2') {
    bp.log.info("remote {0};{1};{2}" ,e,e.mode === 2,!primitivesArraysEqual(e.data, [0,0,0,0]))
  }*/
  return e.id === 'EV3_1.S2' && e.mode === 2 && !primitivesArraysEqual(e.data, [0,0,0,0])
})