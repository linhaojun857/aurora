require('dotenv').config({ path: __dirname + '/.env.local' })
let client = require('scp2')
const ora = require('ora')
const chalk = require('chalk')
const spinner = ora(chalk.green('正在发布到服务器...'))
spinner.start()

client.scp(
  './dist/',
  {
    host: process.env.VUE_APP_HOST,
    port: process.env.VUE_APP_PORT,
    username: process.env.VUE_APP_USERNAME,
    password: process.env.VUE_APP_PASSWORD,
    path: process.env.VUE_APP_PATH
  },
  (err) => {
    spinner.stop()
    if (!err) {
      console.log(chalk.green('项目发布完毕!'))
    } else {
      console.log('err', err)
    }
  }
)
