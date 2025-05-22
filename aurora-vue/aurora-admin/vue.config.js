const { defineConfig } = require('@vue/cli-service')
const path = require('path')
function resolve(dir) {
  return path.join(__dirname, dir)
}
module.exports = defineConfig({
  transpileDependencies: true,
  productionSourceMap: false,
  devServer: {
    proxy: {
      '/api': {
        target: 'https://aurora.cloudorz.com/api',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },
  chainWebpack: (config) => {
    config.resolve.alias.set('@', resolve('src'))
  }
})
