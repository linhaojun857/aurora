<template>
  <div class="popup-result">
    <p>最近5次运行时间</p>
    <ul class="popup-result-scroll">
      <template v-if="isShow">
        <li v-for="item in resultList" :key="item">{{ item }}</li>
      </template>
      <li v-else>计算结果中...</li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dayRule: '',
      dayRuleSup: '',
      dateArr: [],
      resultList: [],
      isShow: false
    }
  },
  name: 'crontab-result',
  methods: {
    expressionChange() {
      this.isShow = false
      let ruleArr = this.$options.propsData.ex.split(' ')
      let nums = 0
      let resultArr = []
      let nTime = new Date()
      let nYear = nTime.getFullYear()
      let nMonth = nTime.getMonth() + 1
      let nDay = nTime.getDate()
      let nHour = nTime.getHours()
      let nMin = nTime.getMinutes()
      let nSecond = nTime.getSeconds()
      this.getSecondArr(ruleArr[0])
      this.getMinArr(ruleArr[1])
      this.getHourArr(ruleArr[2])
      this.getDayArr(ruleArr[3])
      this.getMonthArr(ruleArr[4])
      this.getWeekArr(ruleArr[5])
      this.getYearArr(ruleArr[6], nYear)
      let sDate = this.dateArr[0]
      let mDate = this.dateArr[1]
      let hDate = this.dateArr[2]
      let DDate = this.dateArr[3]
      let MDate = this.dateArr[4]
      let YDate = this.dateArr[5]
      let sIdx = this.getIndex(sDate, nSecond)
      let mIdx = this.getIndex(mDate, nMin)
      let hIdx = this.getIndex(hDate, nHour)
      let DIdx = this.getIndex(DDate, nDay)
      let MIdx = this.getIndex(MDate, nMonth)
      let YIdx = this.getIndex(YDate, nYear)
      const resetSecond = function () {
        sIdx = 0
        nSecond = sDate[sIdx]
      }
      const resetMin = function () {
        mIdx = 0
        nMin = mDate[mIdx]
        resetSecond()
      }
      const resetHour = function () {
        hIdx = 0
        nHour = hDate[hIdx]
        resetMin()
      }
      const resetDay = function () {
        DIdx = 0
        nDay = DDate[DIdx]
        resetHour()
      }
      const resetMonth = function () {
        MIdx = 0
        nMonth = MDate[MIdx]
        resetDay()
      }
      if (nYear !== YDate[YIdx]) {
        resetMonth()
      }
      if (nMonth !== MDate[MIdx]) {
        resetDay()
      }
      if (nDay !== DDate[DIdx]) {
        resetHour()
      }
      if (nHour !== hDate[hIdx]) {
        resetMin()
      }
      if (nMin !== mDate[mIdx]) {
        resetSecond()
      }
      goYear: for (let Yi = YIdx; Yi < YDate.length; Yi++) {
        let YY = YDate[Yi]
        if (nMonth > MDate[MDate.length - 1]) {
          resetMonth()
          continue
        }
        goMonth: for (let Mi = MIdx; Mi < MDate.length; Mi++) {
          let MM = MDate[Mi]
          MM = MM < 10 ? '0' + MM : MM
          if (nDay > DDate[DDate.length - 1]) {
            resetDay()
            if (Mi == MDate.length - 1) {
              resetMonth()
              continue goYear
            }
            continue
          }
          goDay: for (let Di = DIdx; Di < DDate.length; Di++) {
            let DD = DDate[Di]
            let thisDD = DD < 10 ? '0' + DD : DD
            if (nHour > hDate[hDate.length - 1]) {
              resetHour()
              if (Di == DDate.length - 1) {
                resetDay()
                if (Mi == MDate.length - 1) {
                  resetMonth()
                  continue goYear
                }
                continue goMonth
              }
              continue
            }
            if (
              this.checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true &&
              this.dayRule !== 'workDay' &&
              this.dayRule !== 'lastWeek' &&
              this.dayRule !== 'lastDay'
            ) {
              resetDay()
              continue goMonth
            }
            if (this.dayRule == 'lastDay') {
              if (this.checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true) {
                while (DD > 0 && this.checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true) {
                  DD--

                  thisDD = DD < 10 ? '0' + DD : DD
                }
              }
            } else if (this.dayRule == 'workDay') {
              if (this.checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true) {
                while (DD > 0 && this.checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true) {
                  DD--
                  thisDD = DD < 10 ? '0' + DD : DD
                }
              }
              let thisWeek = this.formatDate(new Date(YY + '-' + MM + '-' + thisDD + ' 00:00:00'), 'week')
              if (thisWeek == 1) {
                DD++
                thisDD = DD < 10 ? '0' + DD : DD
                if (this.checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true) {
                  DD -= 3
                }
              } else if (thisWeek == 7) {
                if (this.dayRuleSup !== 1) {
                  DD--
                } else {
                  DD += 2
                }
              }
            } else if (this.dayRule == 'weekDay') {
              let thisWeek = this.formatDate(new Date(YY + '-' + MM + '-' + DD + ' 00:00:00'), 'week')
              if (this.dayRuleSup.indexOf(thisWeek) < 0) {
                if (Di == DDate.length - 1) {
                  resetDay()
                  if (Mi == MDate.length - 1) {
                    resetMonth()
                    continue goYear
                  }
                  continue goMonth
                }
                continue
              }
            } else if (this.dayRule == 'assWeek') {
              let thisWeek = this.formatDate(new Date(YY + '-' + MM + '-' + DD + ' 00:00:00'), 'week')
              if (this.dayRuleSup[1] >= thisWeek) {
                DD = (this.dayRuleSup[0] - 1) * 7 + this.dayRuleSup[1] - thisWeek + 1
              } else {
                DD = this.dayRuleSup[0] * 7 + this.dayRuleSup[1] - thisWeek + 1
              }
            } else if (this.dayRule == 'lastWeek') {
              if (this.checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true) {
                while (DD > 0 && this.checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true) {
                  DD--
                  thisDD = DD < 10 ? '0' + DD : DD
                }
              }
              let thisWeek = this.formatDate(new Date(YY + '-' + MM + '-' + thisDD + ' 00:00:00'), 'week')
              if (this.dayRuleSup < thisWeek) {
                DD -= thisWeek - this.dayRuleSup
              } else if (this.dayRuleSup > thisWeek) {
                DD -= 7 - (this.dayRuleSup - thisWeek)
              }
            }
            DD = DD < 10 ? '0' + DD : DD
            goHour: for (let hi = hIdx; hi < hDate.length; hi++) {
              let hh = hDate[hi] < 10 ? '0' + hDate[hi] : hDate[hi]
              if (nMin > mDate[mDate.length - 1]) {
                resetMin()
                if (hi == hDate.length - 1) {
                  resetHour()
                  if (Di == DDate.length - 1) {
                    resetDay()
                    if (Mi == MDate.length - 1) {
                      resetMonth()
                      continue goYear
                    }
                    continue goMonth
                  }
                  continue goDay
                }
                continue
              }
              goMin: for (let mi = mIdx; mi < mDate.length; mi++) {
                let mm = mDate[mi] < 10 ? '0' + mDate[mi] : mDate[mi]
                if (nSecond > sDate[sDate.length - 1]) {
                  resetSecond()
                  if (mi == mDate.length - 1) {
                    resetMin()
                    if (hi == hDate.length - 1) {
                      resetHour()
                      if (Di == DDate.length - 1) {
                        resetDay()
                        if (Mi == MDate.length - 1) {
                          resetMonth()
                          continue goYear
                        }
                        continue goMonth
                      }
                      continue goDay
                    }
                    continue goHour
                  }
                  continue
                }
                goSecond: for (let si = sIdx; si <= sDate.length - 1; si++) {
                  let ss = sDate[si] < 10 ? '0' + sDate[si] : sDate[si]
                  if (MM !== '00' && DD !== '00') {
                    resultArr.push(YY + '-' + MM + '-' + DD + ' ' + hh + ':' + mm + ':' + ss)
                    nums++
                  }
                  if (nums == 5) break goYear
                  if (si == sDate.length - 1) {
                    resetSecond()
                    if (mi == mDate.length - 1) {
                      resetMin()
                      if (hi == hDate.length - 1) {
                        resetHour()
                        if (Di == DDate.length - 1) {
                          resetDay()
                          if (Mi == MDate.length - 1) {
                            resetMonth()
                            continue goYear
                          }
                          continue goMonth
                        }
                        continue goDay
                      }
                      continue goHour
                    }
                    continue goMin
                  }
                }
              }
            }
          }
        }
      }
      if (resultArr.length == 0) {
        this.resultList = ['没有达到条件的结果！']
      } else {
        this.resultList = resultArr
        if (resultArr.length !== 5) {
          this.resultList.push('最近100年内只有上面' + resultArr.length + '条结果！')
        }
      }
      this.isShow = true
    },
    getIndex(arr, value) {
      if (value <= arr[0] || value > arr[arr.length - 1]) {
        return 0
      } else {
        for (let i = 0; i < arr.length - 1; i++) {
          if (value > arr[i] && value <= arr[i + 1]) {
            return i + 1
          }
        }
      }
    },
    getYearArr(rule, year) {
      this.dateArr[5] = this.getOrderArr(year, year + 100)
      if (rule !== undefined) {
        if (rule.indexOf('-') >= 0) {
          this.dateArr[5] = this.getCycleArr(rule, year + 100, false)
        } else if (rule.indexOf('/') >= 0) {
          this.dateArr[5] = this.getAverageArr(rule, year + 100)
        } else if (rule !== '*') {
          this.dateArr[5] = this.getAssignArr(rule)
        }
      }
    },
    getMonthArr(rule) {
      this.dateArr[4] = this.getOrderArr(1, 12)
      if (rule.indexOf('-') >= 0) {
        this.dateArr[4] = this.getCycleArr(rule, 12, false)
      } else if (rule.indexOf('/') >= 0) {
        this.dateArr[4] = this.getAverageArr(rule, 12)
      } else if (rule !== '*') {
        this.dateArr[4] = this.getAssignArr(rule)
      }
    },
    getWeekArr(rule) {
      if (this.dayRule == '' && this.dayRuleSup == '') {
        if (rule.indexOf('-') >= 0) {
          this.dayRule = 'weekDay'
          this.dayRuleSup = this.getCycleArr(rule, 7, false)
        } else if (rule.indexOf('#') >= 0) {
          this.dayRule = 'assWeek'
          let matchRule = rule.match(/[0-9]{1}/g)
          this.dayRuleSup = [Number(matchRule[1]), Number(matchRule[0])]
          this.dateArr[3] = [1]
          if (this.dayRuleSup[1] == 7) {
            this.dayRuleSup[1] = 0
          }
        } else if (rule.indexOf('L') >= 0) {
          this.dayRule = 'lastWeek'
          this.dayRuleSup = Number(rule.match(/[0-9]{1,2}/g)[0])
          this.dateArr[3] = [31]
          if (this.dayRuleSup == 7) {
            this.dayRuleSup = 0
          }
        } else if (rule !== '*' && rule !== '?') {
          this.dayRule = 'weekDay'
          this.dayRuleSup = this.getAssignArr(rule)
        }
      }
    },
    getDayArr(rule) {
      this.dateArr[3] = this.getOrderArr(1, 31)
      this.dayRule = ''
      this.dayRuleSup = ''
      if (rule.indexOf('-') >= 0) {
        this.dateArr[3] = this.getCycleArr(rule, 31, false)
        this.dayRuleSup = 'null'
      } else if (rule.indexOf('/') >= 0) {
        this.dateArr[3] = this.getAverageArr(rule, 31)
        this.dayRuleSup = 'null'
      } else if (rule.indexOf('W') >= 0) {
        this.dayRule = 'workDay'
        this.dayRuleSup = Number(rule.match(/[0-9]{1,2}/g)[0])
        this.dateArr[3] = [this.dayRuleSup]
      } else if (rule.indexOf('L') >= 0) {
        this.dayRule = 'lastDay'
        this.dayRuleSup = 'null'
        this.dateArr[3] = [31]
      } else if (rule !== '*' && rule !== '?') {
        this.dateArr[3] = this.getAssignArr(rule)
        this.dayRuleSup = 'null'
      } else if (rule == '*') {
        this.dayRuleSup = 'null'
      }
    },
    getHourArr(rule) {
      this.dateArr[2] = this.getOrderArr(0, 23)
      if (rule.indexOf('-') >= 0) {
        this.dateArr[2] = this.getCycleArr(rule, 24, true)
      } else if (rule.indexOf('/') >= 0) {
        this.dateArr[2] = this.getAverageArr(rule, 23)
      } else if (rule !== '*') {
        this.dateArr[2] = this.getAssignArr(rule)
      }
    },
    getMinArr(rule) {
      this.dateArr[1] = this.getOrderArr(0, 59)
      if (rule.indexOf('-') >= 0) {
        this.dateArr[1] = this.getCycleArr(rule, 60, true)
      } else if (rule.indexOf('/') >= 0) {
        this.dateArr[1] = this.getAverageArr(rule, 59)
      } else if (rule !== '*') {
        this.dateArr[1] = this.getAssignArr(rule)
      }
    },
    getSecondArr(rule) {
      this.dateArr[0] = this.getOrderArr(0, 59)
      if (rule.indexOf('-') >= 0) {
        this.dateArr[0] = this.getCycleArr(rule, 60, true)
      } else if (rule.indexOf('/') >= 0) {
        this.dateArr[0] = this.getAverageArr(rule, 59)
      } else if (rule !== '*') {
        this.dateArr[0] = this.getAssignArr(rule)
      }
    },
    getOrderArr(min, max) {
      let arr = []
      for (let i = min; i <= max; i++) {
        arr.push(i)
      }
      return arr
    },
    getAssignArr(rule) {
      let arr = []
      let assiginArr = rule.split(',')
      for (let i = 0; i < assiginArr.length; i++) {
        arr[i] = Number(assiginArr[i])
      }
      arr.sort(this.compare)
      return arr
    },
    getAverageArr(rule, limit) {
      let arr = []
      let agArr = rule.split('/')
      let min = Number(agArr[0])
      let step = Number(agArr[1])
      while (min <= limit) {
        arr.push(min)
        min += step
      }
      return arr
    },
    getCycleArr(rule, limit, status) {
      let arr = []
      let cycleArr = rule.split('-')
      let min = Number(cycleArr[0])
      let max = Number(cycleArr[1])
      if (min > max) {
        max += limit
      }
      for (let i = min; i <= max; i++) {
        let add = 0
        if (status == false && i % limit == 0) {
          add = limit
        }
        arr.push(Math.round((i % limit) + add))
      }
      arr.sort(this.compare)
      return arr
    },
    compare(value1, value2) {
      if (value2 - value1 > 0) {
        return -1
      } else {
        return 1
      }
    },
    formatDate(value, type) {
      let time = typeof value == 'number' ? new Date(value) : value
      let Y = time.getFullYear()
      let M = time.getMonth() + 1
      let D = time.getDate()
      let h = time.getHours()
      let m = time.getMinutes()
      let s = time.getSeconds()
      let week = time.getDay()
      if (type == undefined) {
        return (
          Y +
          '-' +
          (M < 10 ? '0' + M : M) +
          '-' +
          (D < 10 ? '0' + D : D) +
          ' ' +
          (h < 10 ? '0' + h : h) +
          ':' +
          (m < 10 ? '0' + m : m) +
          ':' +
          (s < 10 ? '0' + s : s)
        )
      } else if (type == 'week') {
        return week + 1
      }
    },
    checkDate(value) {
      let time = new Date(value)
      let format = this.formatDate(time)
      return value === format
    }
  },
  watch: {
    ex: 'expressionChange'
  },
  props: ['ex'],
  mounted: function () {
    this.expressionChange()
  }
}
</script>
