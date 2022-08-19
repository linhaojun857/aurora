export default {
  // 设置带过期时间的storage
  setExpire: (key, value, expire) => {
    let obj = {
      data: value,
      time: Date.now(),
      expire: expire
    }
    //localStorage 设置的值不能为对象,转为json字符串
    window['localStorage'].setItem(key, JSON.stringify(obj))
  },
  // 获取带过期时间的storage
  getExpire: (key) => {
    let val = window['localStorage'].getItem(key)
    val = JSON.parse(val)
    // storage不存在 或者不是一个对象
    if (!val || Object.prototype.toString.call(val) !== '[object Object]') {
      return val
    }
    // 未设置过期时间 则永久有效 直接返回
    if (!val.expire) {
      return val.data
    }
    // 判断是否过期
    if (Date.now() - val.time > val.expire) {
      window['localStorage'].removeItem(key)
      return null
    }
    return val.data
  }
}
