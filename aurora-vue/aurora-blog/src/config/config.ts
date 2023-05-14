export default {
  qqLogin: {
    QQ_APP_ID: '101999415',
    QQ_REDIRECT_URI: 'https://www.linhaojun.top/oauth/login/qq'
  },
  routes: [
    {
      name: 'Home',
      path: '/',
      i18n: {
        cn: '首页',
        en: 'Home'
      },
      children: []
    },
    {
      name: 'Talks',
      path: '/talks',
      i18n: {
        cn: '说说',
        en: 'talks'
      },
      children: []
    },
    {
      name: 'About',
      path: '/about',
      i18n: {
        cn: '关于',
        en: 'About'
      },
      children: []
    },
    {
      name: 'Archives',
      path: '/archives',
      i18n: {
        cn: '归档',
        en: 'Archives'
      },
      children: []
    },
    {
      name: 'Tags',
      path: '/tags',
      i18n: {
        cn: '标签',
        en: 'Tags'
      },
      children: []
    },
    {
      name: 'Message',
      path: '/message',
      i18n: {
        cn: '留言',
        en: 'Message'
      },
      children: []
    },
    {
      name: 'Friends',
      path: '/friends',
      i18n: {
        cn: '友链',
        en: 'Friends'
      },
      children: []
    }
  ]
}
