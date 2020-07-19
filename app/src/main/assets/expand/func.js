(function litDark() {
  const hostname = window.location.hostname;
  const isBlack = blackList.some(keyword => {
    if (hostname.match(keyword)) {
      return true;
    };
    return false;
  });

  if (isBlack) {
    return;
  };

  /* 开始执行代码 */
  class ChangeBackground {
    constructor() {
      this.init();
    };
    init() {
      this.addStyle(`
        /*所有元素的背景色都设置为黑夜模式，原来使用#201f26（rgb）会影响celltick资讯详情页里列表项的图片显示，这里改用rgba，有0.8的透明度*/
*, *:before, *:after {
    box-sizing: inherit;
    background-color: rgba(51, 51, 51, 0.6) !important;
}
 
/*背景颜色和一般字体颜色*/
div,h1,h2,h3,h4,h5,h6,p,body,em,html,link,textarea,form,select,input,span,button,em,menu,aside,table,tr,td,nav,dl,dt,dd,amp-iframe,main{
    /*background-color: rgba(32, 31, 38, 0.8) !important;*/
    color: #999999!important;
    border-color: #555555 !important;
    text-shadow            : 0 0 0 #000; /*去掉文本的阴影效果*/
}
 
/*超链接*/
a{
    color:#3c5180 !important;
}
 
strong {
	display: block;
}
 
img, video, iframe, canvas, svg,amp-social-share,
embed[type='application/x-shockwave-flash'],
object[type='application/x-shockwave-flash'],
*[style*='url('] {
    -webkit-filter : opacity(50%);
    -ms-filter     : opacity(50%);
    filter         : opacity(50%);
}
      `);
      this.selectAllNodes(node => {
        if (node.nodeType !== 1) {
          return;
        };
        const style = window.getComputedStyle(node, null);
        const whiteList = ['rgba(0, 0, 0, 0)', 'transparent'];
        const backgroundColor = style.getPropertyValue('background-color');
        const borderColor = style.getPropertyValue('border-color');
        if (whiteList.indexOf(backgroundColor) < 0) {
          if (this.isWhiteToBlack(backgroundColor)) {
            node.dataset.changeBackgroundColor = '';
            node.dataset.changeBackgroundColorImportant = '';
          } else {
            delete node.dataset.changeBackgroundColor;
            delete node.dataset.changeBackgroundColorImportant;
          };
        };
        if (whiteList.indexOf(borderColor) < 0) {
          if (this.isWhiteToBlack(borderColor)) {
            node.dataset.changeBorderColor = '';
            node.dataset.changeBorderColorImportant = '';
          } else {
            delete node.dataset.changeBorderColor;
            delete node.dataset.changeBorderColorImportant;
          };
        };
        if (borderColor.indexOf('rgb(255, 255, 255)') >= 0) {
          delete node.dataset.changeBorderColor;
          delete node.dataset.changeBorderColorImportant;
          node.style.borderColor = 'transparent';
        };
      });
    };
    addStyle(style = '') {
      const styleElm = document.createElement('style');
      styleElm.innerHTML = style;
      document.head.appendChild(styleElm);
    };
    /* 是否为灰白黑 */
    isWhiteToBlack(colorStr = '') {
      let hasWhiteToBlack = false;
      const colorArr = colorStr.match(/rgb.+?\)/g);
      if (!colorArr || colorArr.length === 0) {
        return true;
      };
      colorArr.forEach(color => {
        const reg = /rgb[a]*?\(([0-9]+),.*?([0-9]+),.*?([0-9]+).*?\)/g;
        const result = reg.exec(color);
        const red = result[1];
        const green = result[2];
        const blue = result[3];
        const deviation = 20;
        const max = Math.max(red, green, blue);
        const min = Math.min(red, green, blue);
        if (max - min <= deviation) {
          hasWhiteToBlack = true;
        };
      });
      return hasWhiteToBlack;
    };
    selectAllNodes(callback = () => { }) {
      const allNodes = document.querySelectorAll('*');
      Array.from(allNodes, node => {
        callback(node);
      });
      this.observe({
        targetNode: document.documentElement,
        config: {
          attributes: false
        },
        callback(mutations, observer) {
          const allNodes = document.querySelectorAll('*');
          Array.from(allNodes, node => {
            callback(node);
          });
        }
      });
    };
    observe({ targetNode, config = {}, callback = () => { } }) {
      if (!targetNode) {
        return;
      };

      config = Object.assign({
        attributes: true,
        childList: true,
        subtree: true
      }, config);

      const observer = new MutationObserver(callback);
      observer.observe(targetNode, config);
    };
  };
  new ChangeBackground();
})();