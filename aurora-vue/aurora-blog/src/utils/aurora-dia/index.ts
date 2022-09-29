interface AWFConfig {
  resourcePath: string
}
export class AuroraWaifu {
  configs: AWFConfig = {
    resourcePath: '/'
  }
  constructor(options?: AWFConfig) {
    if (options?.resourcePath) this.configs.resourcePath = options.resourcePath
    Promise.all([this.injectResources('live2d.min.js')]).then(() => {
      new AuroraBotSoftware({
        apiPath: 'https://cdn.jsdelivr.net/gh/fghrsh/live2d_api/',
        locale: 'en',
        containerId: 'waifu-tips',
        messageId: 'waifu-tips'
      })
    })
  }
  async injectResources(url: string): Promise<any> {
    let tag = null
    return new Promise((resolve, reject) => {
      tag = document.createElement('script')
      tag.src = this.configs.resourcePath + url
      tag.onload = () => resolve(url)
      tag.onerror = () => reject(url)
      document.head.appendChild(tag)
    })
  }
}
export interface DiaConfig {
  locale: string
  tips?: { [key: string]: { selector: string; text: string | string[] } }
}
export class AuroraDia {
  configs: DiaConfig = {
    locale: 'en',
    tips: {}
  }
  software = new AuroraBotSoftware()
  eyesAnimationTimer: number | undefined = undefined
  installSoftware(configs: DiaConfig): void {
    if (configs) {
      this.configs.locale = configs.locale
      this.configs.tips = configs.tips
    }
    this.software = new AuroraBotSoftware({
      locale: this.configs.locale,
      botScript: this.configs.tips,
      containerId: 'Aurora-Dia--tips-wrapper',
      messageId: 'Aurora-Dia--tips'
    })
  }
  on(): void {
    this.software.load()
    this.activateMotion()
  }
  activateMotion(): void {
    const leftEye = document.getElementById('Aurora-Dia--left-eye')
    const rightEye = document.getElementById('Aurora-Dia--right-eye')
    const eyesEl = document.getElementById('Aurora-Dia--eyes')
    if (leftEye instanceof HTMLElement && rightEye instanceof HTMLElement && eyesEl instanceof HTMLElement) {
      document.addEventListener('mousemove', (evt) => {
        clearTimeout(this.eyesAnimationTimer)
        eyesEl.classList.add('moving')
        const x = -(eyesEl.getBoundingClientRect().left - evt.clientX) / 100
        const y = -(eyesEl.getBoundingClientRect().top - evt.clientY) / 120
        leftEye.style.transform = `translateY(${y}px) translateX(${x}px)`
        rightEye.style.transform = `translateY(${y}px) translateX(${x}px)`
        this.eyesAnimationTimer = <any>setTimeout(() => {
          leftEye.style.transform = `translateY(0) translateX(0)`
          rightEye.style.transform = `translateY(0) translateX(0)`
          eyesEl.classList.remove('moving')
        }, 2000)
      })
    }
  }
}
interface ABConfig {
  botScript?: { [key: string]: { selector: string; text: string | string[] } }
  apiPath?: string
  botId?: string
  containerId: string
  messageId: string
  locale: string
}
type BotLocales = {
  [locale: string]: any
}
class AuroraBotSoftware {
  config: ABConfig = {
    botScript: {},
    containerId: '',
    messageId: '',
    botId: 'Aurora-Dia',
    locale: 'en'
  }
  messageCacheKey = '__AURORA_BOT_MESSAGE__'
  mouseoverEventCacheKey = '__AURORA_BOT_MOUSE_OVER__'
  userAction = false
  userActionTimer: number | undefined = undefined
  messageTimer: number | undefined = undefined
  messages: string[] = []
  locales: BotLocales = {}
  botTips: { [key: string]: any } = {}

  constructor(configs?: ABConfig) {
    if (configs) {
      this.config = {
        botScript: configs.botScript ? configs.botScript : this.config.botScript,
        containerId: configs.containerId ? configs.containerId : '',
        messageId: configs.messageId ? configs.messageId : '',
        botId: 'Aurora-Dia',
        locale: configs.locale ? configs.locale : 'en'
      }
    }
  }
  load() {
    this.loadLocaleMessages()
    this.injectBotScripts()
    this.messages = this.botTips.messages
    window.addEventListener('mousemove', () => (this.userAction = true))
    window.addEventListener('keydown', () => (this.userAction = true))
    sessionStorage.removeItem(this.messageCacheKey)

    setInterval(() => {
      if (this.userAction) {
        this.userAction = false
        clearInterval(this.userActionTimer)
        this.userActionTimer = undefined
      } else if (!this.userActionTimer) {
        this.userActionTimer = <any>setInterval(() => {
          this.showMessage(this.randomSelection(this.messages), 6000, 9)
        }, 20000)
      }
    }, 1000)

    this.registerEventListener()
    setTimeout(() => {
      this.showWelcomeMessage()
    }, 3000)
  }
  injectBotScripts() {
    let botScriptKeys: string[] = []
    const botScript = this.config.botScript
    this.botTips = this.locales[this.config.locale]

    if (botScript !== undefined) {
      botScriptKeys = Object.keys(botScript)

      if (botScriptKeys.length > 0) {
        botScriptKeys.forEach((key) => {
          this.botTips[key] = botScript[key]
        })
      }
    }
  }
  registerEventListener() {
    const devtools = () => {
      console.log('opened devtools')
    }
    console.log('%c', devtools)
    devtools.toString = () => {
      this.showMessage(this.botTips.console, 6000, 9)
    }
    document.addEventListener('copy', () => {
      this.showMessage(this.botTips.copy, 6000, 9)
    })
    document.addEventListener('visibilitychange', () => {
      if (!document.hidden) this.showMessage(this.botTips.visibility_change, 6000, 9)
    })
    if (this.botTips.mouseover && this.botTips.mouseover.length > 0) {
      document.addEventListener('mouseover', (event) => {
        for (const mouseoverEvents of this.botTips.mouseover) {
          const selector = mouseoverEvents.selector
          let text = mouseoverEvents.text
          event.preventDefault()
          if (event.target && event.target instanceof HTMLElement) {
            if (!event.target.matches(selector)) continue
            // Avoid message keep popping.
            if (
              sessionStorage.getItem(this.mouseoverEventCacheKey) &&
              sessionStorage.getItem(this.mouseoverEventCacheKey) === selector
            )
              return

            text = this.randomSelection(text)
            text = text.replace('{text}', event.target.innerText)
            this.showMessage(text, 4000, 8)
            sessionStorage.setItem(this.mouseoverEventCacheKey, selector)
            setTimeout(() => {
              sessionStorage.removeItem(this.mouseoverEventCacheKey)
            }, 4000)
            return
          }
        }
      })
    }
    if (this.botTips.click && this.botTips.click.length > 0) {
      document.addEventListener('click', (event) => {
        if (event.target && event.target instanceof HTMLElement)
          for (const mouseoverEvents of this.botTips.click) {
            const selector = mouseoverEvents.selector
            let text = mouseoverEvents.text
            if (event.target && event.target instanceof HTMLElement) {
              if (!event.target.matches(selector)) continue
              text = this.randomSelection(text)
              text = text.replace('{text}', event.target.innerText)
              this.showMessage(text, 4000, 8)
              return
            }
          }
      })
    }
    if (this.botTips.events && this.botTips.events.length > 0) {
      this.botTips.events.forEach((event: any) => {
        const now = new Date(),
          after = event.date.split('-')[0],
          before = event.date.split('-')[1] || after
        if (
          after.split('/')[0] <= now.getMonth() + 1 &&
          now.getMonth() + 1 <= before.split('/')[0] &&
          after.split('/')[1] <= now.getDate() &&
          now.getDate() <= before.split('/')[1]
        ) {
          event.text = this.randomSelection(event.text)
          event.text = event.text.replace('{year}', now.getFullYear())
          this.messages.push(event.text)
        }
      })
    }
  }
  showWelcomeMessage() {
    let text
    if (location.pathname === '/') {
      const now = new Date().getHours()
      if (now > 5 && now <= 7) text = this.botTips['5_7']
      else if (now > 7 && now <= 11) text = this.botTips['welcome']['7_11']
      else if (now > 11 && now <= 13) text = this.botTips['welcome']['11_13']
      else if (now > 13 && now <= 17) text = this.botTips['welcome']['13_17']
      else if (now > 17 && now <= 19) text = this.botTips['welcome']['17_19']
      else if (now > 19 && now <= 21) text = this.botTips['welcome']['19_21']
      else if (now > 21 && now <= 23) text = this.botTips['welcome']['21_23']
      else text = this.botTips['welcome']['24']
    } else if (document.referrer !== '') {
      const referrer = new URL(document.referrer),
        domain = referrer.hostname.split('.')[1]
      if (location.hostname === referrer.hostname)
        text = this.botTips['referrer']['self'].replace('[PLACEHOLDER]', document.title.split(' - ')[0])
      else if (domain === 'baidu')
        text = this.botTips['referrer']['baidu'].replace(
          '[PLACEHOLDER]',
          referrer.search.split('&wd=')[1].split('&')[0]
        )
      else if (domain === 'so')
        text = this.botTips['referrer']['so'].replace('[PLACEHOLDER]', referrer.search.split('&q=')[1].split('&')[0])
      else if (domain === 'google')
        text = this.botTips['referrer']['google'].replace('[PLACEHOLDER]', document.title.split(' - ')[0])
      else text = this.botTips['referrer']['site'].replace('[PLACEHOLDER]', referrer.hostname)
    } else {
      text = this.botTips['referrer']['other'].replace('[PLACEHOLDER]', document.title.split(' - ')[0])
    }
    this.showMessage(text, 7000, 8)
  }
  loadLocaleMessages() {
    const locales = require.context('./messages/', true, /[A-Za-z0-9-_,\s]+\.json$/i)
    const messages: {
      [key: string]: { [key: string]: { [key: string]: string } }
    } = {}
    locales.keys().forEach((key) => {
      const matched = key.match(/([A-Za-z0-9-_]+)\./i)
      if (matched && matched.length > 1) {
        const locale = matched[1]
        messages[locale] = locales(key)
      }
    })
    this.locales = messages
  }
  showMessage(text: string, timeout: number, priority: number) {
    const cacheMessage = sessionStorage.getItem(this.messageCacheKey) ?? ''
    if (!text || (cacheMessage !== '' && parseInt(cacheMessage) > priority)) return
    if (this.messageTimer) {
      clearTimeout(this.messageTimer)
      this.messageTimer = undefined
    }
    sessionStorage.setItem(this.messageCacheKey, String(priority))
    text = this.randomSelection(text)
    if (text === 'showQuote') {
      this.showQuote()
      return
    }
    const tipsContainerEl = document.getElementById(this.config.containerId)
    const tipsEl = document.getElementById(this.config.messageId)
    let diaEl = document.createElement('null')
    if (this.config.botId) diaEl = document.getElementById(this.config.botId) ?? document.createElement('null')
    if (tipsEl instanceof Element && tipsContainerEl instanceof Element) {
      tipsEl.innerHTML = text
      tipsContainerEl.classList.add('active')
      if (diaEl instanceof Element) diaEl.classList.add('active')
      this.messageTimer = <any>setTimeout(() => {
        sessionStorage.removeItem(this.messageCacheKey)
        tipsContainerEl.classList.remove('active')
        if (diaEl instanceof Element) diaEl.classList.remove('active')
      }, timeout)
    }
  }
  randomSelection(obj: string[] | string) {
    return Array.isArray(obj) ? obj[Math.floor(Math.random() * obj.length)] : obj
  }
  showQuote() {
    if (this.config.locale === 'cn') {
      this.getHitokoto()
    } else {
      this.getTheySaidSo()
    }
  }
  getHitokoto() {
    fetch('https://v1.hitokoto.cn')
      .then((response) => response.json())
      .then((result) => {
        this.showMessage(result.hitokoto, 6000, 9)
      })
  }
  getTheySaidSo() {
    fetch('https://quotes.rest/qod?language=en')
      .then((response) => response.json())
      .then((result) => {
        this.showMessage(result.contents.quotes[0].quote, 6000, 9)
      })
  }
}
