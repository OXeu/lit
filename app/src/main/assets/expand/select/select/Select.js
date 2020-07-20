import { Emitter, isMobile } from '../lib/util'
import { isErudaEl } from '../lib/extraUtil'

export default class Select extends Emitter {
  constructor() {
    super()

    const self = this

    this._startListener = function(e) {
      if (isErudaEl(e.target)) return

      self._timer = setTimeout(function() {
        self.emit('select', e.target)
      }, 200)

      return false
    }

    this._moveListener = function() {
      clearTimeout(self._timer)
    }

    this._clickListener = function(e) {
      if (isErudaEl(e.target)) return

      e.preventDefault()
      e.stopImmediatePropagation()
    }
  }
  enable() {
    this.disable()
    function addEvent(type, listener) {
      document.body.addEventListener(type, listener, true)
    }
    if (isMobile()) {
      addEvent('touchstart', this._startListener)
      addEvent('touchmove', this._moveListener)
    } else {
      addEvent('mousedown', this._startListener)
      addEvent('mousemove', this._moveListener)
    }
    addEvent('click', this._clickListener)

    return this
  }
  disable() {
    function rmEvent(type, listener) {
      document.body.removeEventListener(type, listener, true)
    }
    if (isMobile()) {
      rmEvent('touchstart', this._startListener)
      rmEvent('touchmove', this._moveListener)
    } else {
      rmEvent('mousedown', this._startListener)
      rmEvent('mousemove', this._moveListener)
    }
    rmEvent('click', this._clickListener)

    return this
  }
}
