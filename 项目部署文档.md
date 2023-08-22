## 1.服务器要求

> 下面的建议不包含minio的内存占用，如需使用minio，请另外考虑

1. 核心数和内存大小有两种方案：
   - 2核4G（使用ES搜索策略）
   - 2核2G（使用MySQL搜素策略）

2. 带宽大小有两种方案：
   - 使用oss对带宽没有要求（本人亲测：1M带宽+oss 访问速度很快）
   - 使用minio需要大一点的带宽（小带宽也可以，但需要使用cdn加速）

## 2.docker的安装

> 以下命令基于CentOS环境。

1. 下载工具

   ```shell
   yum install -y yum-utils
   ```

2. 设置镜像的仓库

   ```shell
   yum-config-manager \
       --add-repo \
       https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo  #配置阿里云的镜像
   ```

3. 更新yum软件包索引

   ```shell
   yum makecache fast
   ```

4. 安装docker相关配置

   ```shell
   yum install docker-ce docker-ce-cli containerd.io
   ```

5. 启动docker

   ```shell
   systemctl start docker
   # 查看当前版本号，是否启动成功
   docker version
   # 设置开机自启动
   systemctl enable docker
   ```

****

## 3.安装mysql

### 方式1: 无挂载模式

> 这种方式直接运行mysql之后，所有关于mysql的内容都在容器中，后续如果需要修改mysql的内容，需要手动进入容器内进行操作。且在宿主机上无备份，一旦容器被删除，数据也会被删除。

```shell
docker pull mysql //下载MySQL镜像
docker run --name mysql --restart=always -p 3306:3306 -e MYSQL_ROOT_PASSWORD=密码 -d mysql //启动MySQL
```

### 方式2: 数据卷挂载模式

> 和**无挂载模式相对**，通过数据卷挂载的方式运行容器，将容器内的部分重要文件映射到宿主机上。直接操作宿主机对应的映射文件就能和容器内作同步，方便操作的同时还能保证容器内的数据在宿主机上有一个备份。
>
> 下面的命令分别对mysql的日志文件、配置文件、数据文件进行了映射，你也可以自己修改。

```shell
docker run --name mysql --restart=always -p 3306:3306 -v /mnt/aurora/mysql/log:/var/log/mysql -v /mnt/aurora/mysql/data:/var/lib/mysql -v /mnt/aurora/mysql/conf:/etc/mysql/conf.d  -e MYSQL_ROOT_PASSWORD=密码 -d mysql
```

****

## 4.安装redis

```shell
docker pull redis # 下载Redis镜像
docker run --name redis  --restart=always -p 6379:6379 -d redis --requirepass "密码" # 启动Redis
```

可能出现的问题：

Redis 容器一直重启，并且使用`docker logs 容器ID`查看容器日志发现有如下报错：

```
Fatal: Can't initialize Background Jobs. Error message: Operation not permitted
```

有可能是因为当前服务器环境与Redis最新版本不兼容，此时可以换成7.0.10版本，命令如下：

```shell
docker pull redis:7.0.10 # 下载Redis镜像
docker run --name redis  --restart=always -p 6379:6379 -d redis:7.0.10 --requirepass "密码" # 启动Redis，执行这条命令之前记得先删除旧的Redis容器，否则会造成容器名称冲突
```

## 5.安装rabbitmq

```shell
docker pull rabbitmq:management //下载RabbitMQ镜像
docker run --name rabbit --restart=always -p 15672:15672 -p 5672:5672  -d  rabbitmq:management   //启动RabbitMQ,默认guest用户，密码也是guest。
```

## 6.安装elasticsearch

```shell
docker pull elasticsearch:7.9.2
```

```shell
mkdir -p  /home/elasticsearch/data/ 
mkdir -p  /home/elasticsearch/config/
```

1. 编写配置文件

   ```shell
   echo 'http.host: 0.0.0.0
   http.cors.enabled: true
   http.cors.allow-origin: "*" '>>/home/elasticsearch/config/elasticsearch.yml
   ```

2. 修改文件夹权限

   ```shell
   chmod -R 777 /home/elasticsearch/
   ls -l # 查看文件权限
   ```

3. 启动elasticseach镜像

   ```shell
   docker run --name elasticsearch -p 9200:9200 \
    -p 9300:9300 \
    -e "discovery.type=single-node" \
    -e ES_JAVA_OPTS="-Xms64m -Xmx128m" \
     -v /home/elasticsearch/config/elasticsearch.yml:/usr/shellare/elasticsearch/config/elasticsearch.yml \
    -v /home/elasticsearch/data:/usr/shellare/elasticsearch/data \
    -v /home/elasticsearch/plugins:/usr/shellare/elasticsearch/plugins \
    -d elasticsearch:7.9.2
   ```

接下来我们就是拿浏览器访问啦。

在浏览器上输入：服务器IP地址:9200

可能出现的问题：

1. 启动成功，但是访问失败的话，查看一下安全组中映射端口是否已经打开。如果是本地虚拟机，可能与防火墙相关（我用的云服务器，没有测试，如果有问题，还是需要大家自行去百度||谷歌一下）。

2. -e ES_JAVA_OPTS="-Xms64m -Xmx128m"：配置内存大小出现了问题。

3. 最后就还有可能是配置文件中出现了问题。

   ```shell
   echo 'http.host: 0.0.0.0
   http.cors.enabled: true
   http.cors.allow-origin: "*" '>>/home/elasticsearch/config/elasticsearch.yml
   ```

   `http.host: 0.0.0.0` 在冒号后面有一个空格的，这里的配置文件是`yml`格式,所以一定要严格按照`yml`格式来书写。

安装ik分词器

进入已经启动成功的elasticsearch容器。

```shell
docker exec -it elasticsearch /bin/bash
```

```shell
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.9.2/elasticsearch-analysis-ik-7.9.2.zip
```

ps: `安装的ik分词器的版本一定要与elasticsearch的版本保持一致`

下载过程中如果遇到选择，直接输入 y回车就可以了。

下载完成之后，退出去，重新启动elasticsearch容器。

```shell
docker restart elasticsearch
```

测试ik分词器是否安装成功

安装完成后，我们再来进行测试遍，看看有什么样的变化。

postman post请求分词测试：`http://服务器IP地址:9200/_analyze`

```json
{
	"tokenizer":"ik_smart",
	"text":"我爱技术"
}
```

结果

```shell
{
    "tokens": [
        {
            "token": "我",
            "start_offset": 0,
            "end_offset": 1,
            "type": "CN_CHAR",
            "position": 0
        },
        {
            "token": "爱",
            "start_offset": 1,
            "end_offset": 2,
            "type": "CN_CHAR",
            "position": 1
        },
        {
            "token": "技术",
            "start_offset": 2,
            "end_offset": 4,
            "type": "CN_WORD",
            "position": 2
        }
    ]
}
```

## 7.安装MaxWell

```sh
docker pull zendesk/maxwell //下载MaxWell镜像
docker run --name maxwell --restart=always  -d  zendesk/maxwell bin/maxwell  --user='数据库用户名' --password='数据库密码'  --host='IP地址'  --producer=rabbitmq --rabbitmq_user='MQ用户名' --rabbitmq_pass='MQ密码' --rabbitmq_host='IP地址' --rabbitmq_port='5672' --rabbitmq_exchange='maxwell_exchange'  --rabbitmq_exchange_type='fanout' --rabbitmq_exchange_durable='true' --filter='exclude: *.*, include: aurora.t_article.article_title = *, include: aurora.t_article.article_content = *, include: aurora.t_article.is_delete = *, include: aurora.t_article.status = *' //运行MaxWell
```

注意：上述命令中aurora为数据库名称。

****

## 8.填写aurora-springboot中的yml文件

此处省略，操作比较简单，就是配置一些简单的ip+端口+用户名+密码，前提是服务器上对应的端口已经打开

阿里云oss具体情况详见百度

QQ互联具体情况详见百度

这里就不赘述了

## 9.打包运行后端项目

1. 使用idea右侧Maven的package命令

2. 在服务器的/usr/local/下面创建名为aurora-springboot的文件夹

3. 将打包好的jar包传到服务器上面的/usr/local/aurora-springboot下面

4. 编写Dockerfile,同时将编写好的Dockerfile传输到服务器的/usr/local/aurora-springboot下面

```dockerfile
FROM openjdk:8
VOLUME /tmp
ADD aurora-springboot-0.0.1.jar blog.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/blog.jar"]
```

> 注意：Dockerfile文件只需要上传到指定目录下即可，不需要手动执行其他操作。

5. 编写aurora-start.sh,同时将编写好的aurora-start.sh上传到服务器的/usr/local/docker下面

```shell
SOURCE_PATH=/usr/local/aurora-springboot
SERVER_NAME=aurora-springboot-0.0.1.jar
TAG=latest
SERVER_PORT=8080
CID=$(docker ps | grep "$SERVER_NAME" | awk '{print $1}')
IID=$(docker images | grep "$SERVER_NAME" | awk '{print $3}')
if [ -n "$CID" ]; then
  echo "存在容器$SERVER_NAME,CID-$CID"
  docker stop $CID
  echo "成功停止容器$SERVER_NAME,CID-$CID"
  docker rm $CID
  echo "成功删除容器$SERVER_NAME,CID-$CID"
fi
if [ -n "$IID" ]; then
  echo "存在镜像$SERVER_NAME:$TAG,IID=$IID"
  docker rmi $IID
  echo "成功删除镜像$SERVER_NAME:$TAG,IID=$IID"
fi
echo "开始构建镜像$SERVER_NAME:$TAG"
cd $SOURCE_PATH
docker build -t $SERVER_NAME:$TAG .
echo "成功构建镜像$SERVER_NAME:$TAG"
docker run --restart=always --name aurora-springboot-0.0.1.jar -d -p 8080:8080 aurora-springboot-0.0.1.jar:latest
echo "成功创建并运行容器$SERVER_NAME"
```

**注意事项：sh文件需要用notepad++转为Unix格式**

具体操作：点开notepad++中的编辑 ---> 文档格式转换 --->转为Unix 

6. cd到/usr/local/docker下面 ，执行如下命令打包docker镜像文件

```shell
sh aurora-start.sh
```

7. 启动容器

```sh
docker run  --name aurora-springboot-0.0.1.jar -d -p 8080:8080 aurora-springboot-0.0.1.jar:latest
```

8. 这样后端项目就可以在服务器上跑起来了

9. 访问测试(浏览器上访问)

   ```shell
   ip:8080
   ```

****

## 10.打包运行前端项目

1. aurora-blog项目中，更改src/config/config.ts

   ```typescript
     captcha: {
       TENCENT_CAPTCHA: '你自己的天御验证码'
     },
     qqLogin: {
       QQ_APP_ID: '你自己的APP_ID',
       QQ_REDIRECT_URI: 'https://你的前台域名/oauth/login/qq'
     },
   ```

2. aurora-blog项目中，更改public/index.html

   ```html
   <script
         src="http://connect.qq.com/qc_jssdk.js"
         data-appid="你自己的APP_ID"
         data-redirecturi="https://你的前台域名/oauth/login/qq"></script>
   ```

3. 如果你的网站没有打算使用https,将aurora-blog/public/index.html和aurora-admin/public/index.html中下面这一行代码给注释掉

   ```html
   <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests" />
   ```

4. 分别到aurora-blog和aurora-admin下面执行如下命令 (推荐关闭vscode的Eslint,本项目没有遵循Eslint的规范)

   如果下列命令执行报错，可以尝试替换版本

   参考版本：npm版本为：8.3.1    vue-cli的版本为：5.0.6

   ```shell
   npm install
   npm run build
   ```

   如果想在本地 debug，可以执行下面这个命令：

   ```shell
   npm run serve
   ```

5. 在服务器的/usr/local/下面创建名为aurora-vue的文件夹，

6. 将打包好的前台代码重命名为blog, 并传输到服务器的/usr/local/aurora-vue下面

7. 将打包好的后台代码重命名为admin, 并传输到服务器的/usr/local/aurora-vue下面

后台管理系统的初始用户名密码分别是：

```
用户名：admin@163.com
密码：123456
```

==第一次登录后一定要修改密码！！！==

## 11.安装并启动nginx

### 方式1: http

1. 拉取nginx镜像

   ```shell
   docker pull nginx
   ```

2. 在/usr/local/nginx下创建nginx.conf文件，格式如下

   ```shell
   events {
       worker_connections  1024;
   }
   
   http {
       include       mime.types;
       default_type  application/octet-stream;
       sendfile        on;
       keepalive_timeout  65;
   
       client_max_body_size     50m;
       client_body_buffer_size  10m; 
       client_header_timeout    1m;
       client_body_timeout      1m;
   
       gzip on;
       gzip_min_length  1k;
       gzip_buffers     4 16k;
       gzip_comp_level  4;
       gzip_types text/plain application/javascript application/x-javascript text/css application/xml text/javascript application/x-httpd-php image/jpeg image/gif image/png;
       gzip_vary on;
   
   server {
           listen       80;
           server_name  前台域名;
        
           location / {		
               root   /usr/local/aurora-vue/blog;
               index  index.html index.htm; 
               try_files $uri $uri/ /index.html;	
           }
   			
   	location ^~ /api/ {		
               proxy_pass http://服务器ip:8080/;
   	       proxy_set_header   Host             $host;
               proxy_set_header   X-Real-IP        $remote_addr;						
               proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
           }
   		
       }
   	
   server {
           listen       80;
           server_name  后台子域名;
        
           location / {		
               root   /usr/local/aurora-vue/admin;
               index  index.html index.htm; 
               try_files $uri $uri/ /index.html;	
           }
   			
   	location ^~ /api/ {		
               proxy_pass http://服务器ip:8080/;
   	    proxy_set_header   Host             $host;
               proxy_set_header   X-Real-IP        $remote_addr;						
               proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
           }
   		
       }
    }
   ```

3. 启动nginx

   ```shell
   docker run --name nginx --restart=always -p 80:80 -d -v /usr/local/nginx/nginx.conf:/etc/nginx/nginx.conf -v /usr/local/aurora-vue:/usr/local/aurora-vue nginx 
   ```

### 方式2: https

1. 拉取nginx镜像

   ```shell
   docker pull nginx
   ```

2. 修改配置文件

   ```shell
   events {
       worker_connections  1024;
   }
   
   http {
       include       mime.types;
       default_type  application/octet-stream;
       sendfile        on;
       keepalive_timeout  65;
   
       client_max_body_size     50m;
       client_body_buffer_size  10m; 	  
       client_header_timeout    1m;
       client_body_timeout      1m;
   
       gzip on;
       gzip_min_length  1k;
       gzip_buffers     4 16k;
       gzip_comp_level  4;
       gzip_types text/plain application/javascript application/x-javascript text/css application/xml text/javascript application/x-httpd-php image/jpeg image/gif image/png;
       gzip_vary on;
   	
   server {
   
           listen  443 ssl;
           server_name  前台域名;
   
           ssl on;
           ssl_certificate ssl证书文件位置; 
   	   ssl_certificate_key ssl证书文件位置; 
           ssl_session_timeout 5m;
           ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
           ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
           ssl_prefer_server_ciphers on;
   
          location / {
               root   /usr/local/vue/blog;
               index  index.html index.htm;
               try_files $uri $uri/ /index.html;
           }
   		
           location ^~ /api/ {
               proxy_pass http://服务器ip:8080/;
               proxy_set_header   Host             $host;
               proxy_set_header   X-Real-IP        $remote_addr;
               proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
           }
   
       }
   
   server {
   
           listen  443 ssl;
           server_name 后台域名;
   
           ssl on;
           ssl_certificate ssl证书文件位置; 
           ssl_certificate_key ssl文件位置; 
           ssl_session_timeout 5m;
           ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
           ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
           ssl_prefer_server_ciphers on;
   
          location / {
               root   /usr/local/vue/admin;
               index  index.html index.htm;
               try_files $uri $uri/ /index.html;
           }
   		
           location ^~ /api/ {
               proxy_pass http://服务器ip:8080/;
               proxy_set_header   Host             $host;
               proxy_set_header   X-Real-IP        $remote_addr;
               proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
           }
   	
       }
   
   
   
   server {
           listen       80;
           server_name  前台域名;
           rewrite ^(.*)$	https://$host$1	permanent;
   		
       }
   	
   server {
           listen       80;
           server_name  后台域名;
           rewrite ^(.*)$	https://$host$1	permanent;
   		
       }	
   }
   ```

3. 启动nginx

   ```shell
   docker run --name nginx --restart=always -p 443:443 -p 80:80 -d -v /usr/local/nginx/nginx.conf:/etc/nginx/nginx.conf -v /usr/local/nginx/cert:/etc/nginx/cert -v /usr/local/aurora-vue:/usr/local/aurora-vue --privileged=true nginx
   ```


## 12. 如何修改网站基本信息
打开后台管理系统，找到【系统管理-->网站管理】，在此修改。
