!(function (t, e) {
  'object' == typeof exports && 'object' == typeof module
    ? (module.exports = e())
    : 'function' == typeof define && define.amd
    ? define('TagCloud', [], e)
    : 'object' == typeof exports
    ? (exports.TagCloud = e())
    : (t.TagCloud = e())
})('undefined' != typeof self ? self : this, function () {
  return (function (t) {
    function e(o) {
      if (n[o]) return n[o].exports
      var r = (n[o] = { i: o, l: !1, exports: {} })
      return t[o].call(r.exports, r, r.exports, e), (r.l = !0), r.exports
    }
    var n = {}
    return (
      (e.m = t),
      (e.c = n),
      (e.d = function (t, n, o) {
        e.o(t, n) ||
          Object.defineProperty(t, n, {
            configurable: !1,
            enumerable: !0,
            get: o
          })
      }),
      (e.n = function (t) {
        var n =
          t && t.__esModule
            ? function () {
                return t.default
              }
            : function () {
                return t
              }
        return e.d(n, 'a', n), n
      }),
      (e.o = function (t, e) {
        return Object.prototype.hasOwnProperty.call(t, e)
      }),
      (e.p = '/dist/'),
      e((e.s = 1))
    )
  })([
    function (t, e, n) {
      'use strict'
      e.a = {
        name: 'tagCloud',
        props: {
          data: { type: Array, default: [] },
          config: { type: Object, default: null }
        },
        data: function () {
          return {
            option: {
              radius: 120,
              maxFont: 24,
              color: null,
              rotateAngleXbase: 500,
              rotateAngleYbase: 500,
              hover: !1
            },
            tagList: []
          }
        },
        created: function () {
          null != this.config && (this.option = Object.assign({}, this.option, this.config))
        },
        mounted: function () {
          this._initTags()
        },
        beforeDestroy: function () {
          this.timer && (clearInterval(this.timer), (this.timer = null))
        },
        watch: {
          data: function () {
            var t = this
            this.$nextTick(function () {
              t._initTags()
            })
          }
        },
        methods: {
          _initTags: function () {
            if (
              ((this.rotateAngleX = Math.PI / this.option.rotateAngleXbase),
              (this.rotateAngleY = Math.PI / this.option.rotateAngleYbase),
              this.option.hover)
            ) {
              var t = this
              this.$refs.wrapper.onmousemove = function (e) {
                ;(t.rotateAngleY = (e.pageX - this.offsetLeft - this.offsetWidth / 2) / 1e4),
                  (t.rotateAngleX = -(e.pageY - this.offsetTop - this.offsetHeight / 2) / 1e4)
              }
            } else this.$refs.wrapper.onmousemove = null
            for (var e = 0, n = this.data.length; e < n; e++) {
              var o = Math.acos((2 * (e + 1) - 1) / n - 1),
                r = o * Math.sqrt(n * Math.PI),
                i = this.option.radius * Math.sin(o) * Math.cos(r),
                a = this.option.radius * Math.sin(o) * Math.sin(r),
                s = this.option.radius * Math.cos(o)
              this.option.color
                ? (this.$refs.tag[e].style.color = this.option.color)
                : (this.$refs.tag[e].style.color =
                    'rgb(' +
                    Math.round(255 * Math.random()) +
                    ',' +
                    Math.round(255 * Math.random()) +
                    ',' +
                    Math.round(255 * Math.random()) +
                    ')')
              var u = { x: i, y: a, z: s, ele: this.$refs.tag[e] }
              this.tagList.push(u)
            }
            var f = this
            this.timer = setInterval(function () {
              for (var t = 0; t < f.tagList.length; t++)
                f.rotateX(f.tagList[t]),
                  f.rotateY(f.tagList[t]),
                  f.setPosition(f.tagList[t], f.option.radius, f.option.maxFont)
            }, 20)
          },
          setPosition: function (t, e, n) {
            this.$refs.wrapper &&
              ((t.ele.style.transform =
                'translate(' +
                (t.x + this.$refs.wrapper.offsetWidth / 2 - t.ele.offsetWidth / 2) +
                'px,' +
                (t.y + this.$refs.wrapper.offsetHeight / 2 - t.ele.offsetHeight / 2) +
                'px)'),
              (t.ele.style.opacity = t.z / e / 2 + 0.7),
              (t.ele.style.fontSize = (t.z / e / 2 + 0.5) * n + 'px'))
          },
          rotateX: function (t) {
            var e = Math.cos(this.rotateAngleX),
              n = Math.sin(this.rotateAngleX),
              o = t.y * e - t.z * n,
              r = t.y * n + t.z * e
            ;(t.y = o), (t.z = r)
          },
          rotateY: function (t) {
            var e = Math.cos(this.rotateAngleY),
              n = Math.sin(this.rotateAngleY),
              o = t.z * n + t.x * e,
              r = t.z * e - t.x * n
            ;(t.x = o), (t.z = r)
          },
          dbclickTag: function () {
            if (this.timer) clearInterval(this.timer), (this.timer = null)
            else {
              var t = this
              this.timer = setInterval(function () {
                for (var e = 0; e < t.tagList.length; e++)
                  t.rotateX(t.tagList[e]),
                    t.rotateY(t.tagList[e]),
                    t.setPosition(t.tagList[e], t.option.radius, t.option.maxFont)
              }, 20)
            }
          },
          clickTag: function (t) {
            this.$emit('clickTag', t)
          }
        }
      }
    },
    function (t, e, n) {
      'use strict'
      Object.defineProperty(e, '__esModule', { value: !0 })
      var o = n(2),
        r = {
          install: function (t) {
            'undefined' != typeof window && window.Vue && (t = window.Vue), t.component(o.a.name, o.a)
          }
        }
      e.default = r
    },
    function (t, e, n) {
      'use strict'
      function o(t) {
        n(3)
      }
      var r = n(0),
        i = n(9),
        a = n(8),
        s = o,
        u = a(r.a, i.a, !1, s, 'data-v-7f9ad8d8', null)
      e.a = u.exports
    },
    function (t, e, n) {
      var o = n(4)
      'string' == typeof o && (o = [[t.i, o, '']]), o.locals && (t.exports = o.locals)
      n(6)('3fb9a8be', o, !0, {})
    },
    function (t, e, n) {
      ;(e = t.exports = n(5)(!1)),
        e.push([
          t.i,
          '.tag-cloud[data-v-7f9ad8d8]{width:300px;height:300px;position:relative;color:#333;margin:0 auto;text-align:center}.tag-cloud p[data-v-7f9ad8d8]{position:absolute;top:0;left:0;color:#333;font-family:Arial;text-decoration:none;margin:0 10px 15px 0;line-height:18px;text-align:center;font-size:16px;padding:4px 9px;display:inline-block;border-radius:3px}',
          ''
        ])
    },
    function (t, e) {
      function n(t, e) {
        var n = t[1] || '',
          r = t[3]
        if (!r) return n
        if (e && 'function' == typeof btoa) {
          var i = o(r)
          return [n]
            .concat(
              r.sources.map(function (t) {
                return '/*# sourceURL=' + r.sourceRoot + t + ' */'
              })
            )
            .concat([i])
            .join('\n')
        }
        return [n].join('\n')
      }
      function o(t) {
        return (
          '/*# sourceMappingURL=data:application/json;charset=utf-8;base64,' +
          btoa(unescape(encodeURIComponent(JSON.stringify(t)))) +
          ' */'
        )
      }
      t.exports = function (t) {
        var e = []
        return (
          (e.toString = function () {
            return this.map(function (e) {
              var o = n(e, t)
              return e[2] ? '@media ' + e[2] + '{' + o + '}' : o
            }).join('')
          }),
          (e.i = function (t, n) {
            'string' == typeof t && (t = [[null, t, '']])
            for (var o = {}, r = 0; r < this.length; r++) {
              var i = this[r][0]
              'number' == typeof i && (o[i] = !0)
            }
            for (r = 0; r < t.length; r++) {
              var a = t[r]
              ;('number' == typeof a[0] && o[a[0]]) ||
                (n && !a[2] ? (a[2] = n) : n && (a[2] = '(' + a[2] + ') and (' + n + ')'), e.push(a))
            }
          }),
          e
        )
      }
    },
    function (t, e, n) {
      function o(t) {
        for (var e = 0; e < t.length; e++) {
          var n = t[e],
            o = c[n.id]
          if (o) {
            o.refs++
            for (var r = 0; r < o.parts.length; r++) o.parts[r](n.parts[r])
            for (; r < n.parts.length; r++) o.parts.push(i(n.parts[r]))
            o.parts.length > n.parts.length && (o.parts.length = n.parts.length)
          } else {
            for (var a = [], r = 0; r < n.parts.length; r++) a.push(i(n.parts[r]))
            c[n.id] = { id: n.id, refs: 1, parts: a }
          }
        }
      }
      function r() {
        var t = document.createElement('style')
        return (t.type = 'text/css'), l.appendChild(t), t
      }
      function i(t) {
        var e,
          n,
          o = document.querySelector('style[' + m + '~="' + t.id + '"]')
        if (o) {
          if (h) return g
          o.parentNode.removeChild(o)
        }
        if (y) {
          var i = p++
          ;(o = d || (d = r())), (e = a.bind(null, o, i, !1)), (n = a.bind(null, o, i, !0))
        } else
          (o = r()),
            (e = s.bind(null, o)),
            (n = function () {
              o.parentNode.removeChild(o)
            })
        return (
          e(t),
          function (o) {
            if (o) {
              if (o.css === t.css && o.media === t.media && o.sourceMap === t.sourceMap) return
              e((t = o))
            } else n()
          }
        )
      }
      function a(t, e, n, o) {
        var r = n ? '' : o.css
        if (t.styleSheet) t.styleSheet.cssText = x(e, r)
        else {
          var i = document.createTextNode(r),
            a = t.childNodes
          a[e] && t.removeChild(a[e]), a.length ? t.insertBefore(i, a[e]) : t.appendChild(i)
        }
      }
      function s(t, e) {
        var n = e.css,
          o = e.media,
          r = e.sourceMap
        if (
          (o && t.setAttribute('media', o),
          v.ssrId && t.setAttribute(m, e.id),
          r &&
            ((n += '\n/*# sourceURL=' + r.sources[0] + ' */'),
            (n +=
              '\n/*# sourceMappingURL=data:application/json;base64,' +
              btoa(unescape(encodeURIComponent(JSON.stringify(r)))) +
              ' */')),
          t.styleSheet)
        )
          t.styleSheet.cssText = n
        else {
          for (; t.firstChild; ) t.removeChild(t.firstChild)
          t.appendChild(document.createTextNode(n))
        }
      }
      var u = 'undefined' != typeof document
      if ('undefined' != typeof DEBUG && DEBUG && !u)
        throw new Error(
          "vue-style-loader cannot be used in a non-browser environment. Use { target: 'node' } in your Webpack config to indicate a server-rendering environment."
        )
      var f = n(7),
        c = {},
        l = u && (document.head || document.getElementsByTagName('head')[0]),
        d = null,
        p = 0,
        h = !1,
        g = function () {},
        v = null,
        m = 'data-vue-ssr-id',
        y = 'undefined' != typeof navigator && /msie [6-9]\b/.test(navigator.userAgent.toLowerCase())
      t.exports = function (t, e, n, r) {
        ;(h = n), (v = r || {})
        var i = f(t, e)
        return (
          o(i),
          function (e) {
            for (var n = [], r = 0; r < i.length; r++) {
              var a = i[r],
                s = c[a.id]
              s.refs--, n.push(s)
            }
            e ? ((i = f(t, e)), o(i)) : (i = [])
            for (var r = 0; r < n.length; r++) {
              var s = n[r]
              if (0 === s.refs) {
                for (var u = 0; u < s.parts.length; u++) s.parts[u]()
                delete c[s.id]
              }
            }
          }
        )
      }
      var x = (function () {
        var t = []
        return function (e, n) {
          return (t[e] = n), t.filter(Boolean).join('\n')
        }
      })()
    },
    function (t, e) {
      t.exports = function (t, e) {
        for (var n = [], o = {}, r = 0; r < e.length; r++) {
          var i = e[r],
            a = i[0],
            s = i[1],
            u = i[2],
            f = i[3],
            c = { id: t + ':' + r, css: s, media: u, sourceMap: f }
          o[a] ? o[a].parts.push(c) : n.push((o[a] = { id: a, parts: [c] }))
        }
        return n
      }
    },
    function (t, e) {
      t.exports = function (t, e, n, o, r, i) {
        var a,
          s = (t = t || {}),
          u = typeof t.default
        ;('object' !== u && 'function' !== u) || ((a = t), (s = t.default))
        var f = 'function' == typeof s ? s.options : s
        e && ((f.render = e.render), (f.staticRenderFns = e.staticRenderFns), (f._compiled = !0)),
          n && (f.functional = !0),
          r && (f._scopeId = r)
        var c
        if (
          (i
            ? ((c = function (t) {
                ;(t =
                  t ||
                  (this.$vnode && this.$vnode.ssrContext) ||
                  (this.parent && this.parent.$vnode && this.parent.$vnode.ssrContext)),
                  t || 'undefined' == typeof __VUE_SSR_CONTEXT__ || (t = __VUE_SSR_CONTEXT__),
                  o && o.call(this, t),
                  t && t._registeredComponents && t._registeredComponents.add(i)
              }),
              (f._ssrRegister = c))
            : o && (c = o),
          c)
        ) {
          var l = f.functional,
            d = l ? f.render : f.beforeCreate
          l
            ? ((f._injectStyles = c),
              (f.render = function (t, e) {
                return c.call(e), d(t, e)
              }))
            : (f.beforeCreate = d ? [].concat(d, c) : [c])
        }
        return { esModule: a, exports: s, options: f }
      }
    },
    function (t, e, n) {
      'use strict'
      var o = function () {
          var t = this,
            e = t.$createElement,
            n = t._self._c || e
          return n(
            'div',
            { ref: 'wrapper', staticClass: 'tag-cloud' },
            t._l(t.data, function (e, o) {
              return n(
                'p',
                {
                  key: o,
                  ref: 'tag',
                  refInFor: !0,
                  on: {
                    click: function (n) {
                      t.clickTag(e)
                    },
                    dblclick: function (n) {
                      t.dbclickTag(e)
                    }
                  }
                },
                [t._v(t._s(e.name))]
              )
            })
          )
        },
        r = [],
        i = { render: o, staticRenderFns: r }
      e.a = i
    }
  ])
})
//# sourceMappingURL=tag-cloud.js.map
