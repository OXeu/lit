import {
  toStr,
  each,
  filter,
  isStr,
  keys,
  kebabCase,
  defaults,
  escapeRegExp
} from './util'
import themes from './themes'
import cssMap from './cssMap'

let styleList = []
let scale = 1

let curTheme = themes.Light

const exports = function(css, container) {
  css = toStr(css)

  for (let i = 0, len = styleList.length; i < len; i++) {
    if (styleList[i].css === css) return
  }

  container = container || exports.container || document.head
  const el = document.createElement('style')

  el.type = 'text/css'
  container.appendChild(el)

  const style = { css, el, container }
  resetStyle(style)
  styleList.push(style)

  return style
}

exports.setScale = function(s) {
  scale = s
  resetStyles()
}

exports.setTheme = function(theme) {
  if (isStr(theme)) {
    curTheme = themes[theme] || themes.Light
  } else {
    curTheme = defaults(theme, themes.Light)
  }

  resetStyles()
}

exports.getCurTheme = () => curTheme

exports.getThemes = () => themes

exports.clear = function() {
  each(styleList, ({ container, el }) => container.removeChild(el))
  styleList = []
}

exports.remove = function(style) {
  styleList = filter(styleList, s => s !== style)

  style.container.removeChild(style.el)
}

function resetStyles() {
  each(styleList, style => resetStyle(style))
}

function resetStyle({ css, el }) {
  css = css.replace(/(\d+)px/g, ($0, $1) => +$1 * scale + 'px')
  css = css.replace(/_/g, 'eruda-')
  each(cssMap, (val, key) => {
    css = css.replace(new RegExp(escapeRegExp(`$${val}:`), 'g'), key + ':')
  })
  const _keys = keys(themes.Light)
  each(_keys, key => {
    css = css.replace(
      new RegExp(`var\\(--${kebabCase(key)}\\)`, 'g'),
      curTheme[key]
    )
  })
  el.innerText = css
}

export default exports
