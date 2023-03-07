require('dotenv').config({path: __dirname + '/.env.local'})
const client = require('scp2')
const ora = require('ora');
const chalk = require('chalk')
const spinner = ora(chalk.green('正在部署项目到服务器...'));
const Client = require('ssh2').Client
const conn = new Client()
conn.on('ready', () => {
    conn.exec("rm -rf /usr/local/aurora-springboot/aurora-springboot-0.0.1.jar", (err, stream) => {
        if (err) throw err
        stream.on('close', () => {
            spinner.start();
            client.scp('./target/aurora-springboot-0.0.1.jar', {
                host: process.env.VUE_APP_HOST,
                port: process.env.VUE_APP_PORT,
                username: process.env.VUE_APP_USERNAME,
                password: process.env.VUE_APP_PASSWORD,
                path: process.env.VUE_APP_PATH
            }, (err) => {
                if (!err) {
                    conn.exec("sh /usr/local/aurora-springboot/aurora-start.sh", (err, stream) => {
                        if (err) throw err
                        stream.on("close", () => {
                            conn.end()
                            spinner.stop()
                            console.log(chalk.green("项目部署完成！"))
                        }).on('data', () => {
                        }).stderr.on('data', () => {
                        })
                    })
                } else {
                    console.log(chalk.red(err))
                }
            })
        }).on('data', () => {
        }).stderr.on('data', () => {
        })
    })
}).on('error', async err => {
    console.log(chalk.red('Client 链接错误' + err.toString()))
}).connect({
    host: process.env.VUE_APP_HOST,
    port: process.env.VUE_APP_PORT,
    username: process.env.VUE_APP_USERNAME,
    password: process.env.VUE_APP_PASSWORD
})
