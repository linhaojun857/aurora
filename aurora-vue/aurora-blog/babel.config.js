module.exports = {
  presets: ['@vue/cli-plugin-babel/preset'],
  plugins: [
    [
      'prismjs',
      {
        languages: ['javascript', 'css','sql','java','c','cpp','nginx','markup','shell','json'],
        plugins: ['line-numbers','toolbar','copy-to-clipboard'], //配置插件
        theme: 'okaidia', //配置主题
        css: true
      }
    ]
  ]
}
