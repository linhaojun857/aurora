#!/bin/bash

echo "Start installing aurora blog"
echo "installing yum-utils..."
sudo yum install -y yum-utils
if [ $? -ne 0 ]; then
  echo "yum-utils install failed"
  exit 1
fi
echo "Configuring docker Alibaba Cloud image... "
sudo yum-config-manager \
  --add-repo \
  https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
if [ $? -ne 0 ]; then
  echo "Docker image warehouse configuration failed"
  exit 1
fi
echo "Updating yum package index..."
sudo yum makecache fast
if [ $? -ne 0 ]; then
  echo "yum package index update failed"
  exit 1
fi
echo "Installing docker..."
sudo yum install docker-ce docker-ce-cli containerd.io
if [ $? -ne 0 ]; then
  echo "Docker installation failed"
  exit 1
fi
echo "Starting docker..."
sudo systemctl start docker
if [ $? -ne 0 ]; then
  echo "Docker failed to start "
  exit 1
else
  echo "Docker launched successfully"
fi
echo "Setting docker to boot automatically..."
sudo systemctl enable docker
if [ $? -ne 0 ]; then
  echo "Docker failed to set up self-boot"
  exit 1
fi
sudo mkdir -p /etc/docker
if [ $? -ne 0 ]; then
  echo "mkdir -p /etc/docker failed"
  exit 1
fi
sudo tee /etc/docker/daemon.json <<-'EOF'
{
    "registry-mirrors": [
        "https://registry.docker-cn.com",
        "http://hub-mirror.c.163.com",
        "https://dockerhub.azk8s.cn",
        "https://mirror.ccs.tencentyun.com",
        "https://registry.cn-hangzhou.aliyuncs.com",
        "https://docker.mirrors.ustc.edu.cn",
        "https://docker.m.daocloud.io",
        "https://noohub.ru",
        "https://huecker.io",
        "https://dockerhub.timeweb.cloud",
        "https://hub.atomgit.com/repos",
        "https://docker.1panel.live",
        "https://hub.rat.dev"
    ]
}
EOF
if [ $? -ne 0 ]; then
  echo "tee /etc/docker/daemon.json failed"
  exit 1
fi
sudo systemctl daemon-reload
if [ $? -ne 0 ]; then
  echo "systemctl daemon-reload failed"
  exit 1
fi
sudo systemctl restart docker
if [ $? -ne 0 ]; then
  echo "systemctl restart docker failed"
  exit 1
fi
echo "Creating docker network bridge..."
sudo docker network create aurora
if [ $? -ne 0 ]; then
  echo "Docker network bridge creation failed"
  exit 1
fi
mysql_version="8.0.39-debian"
echo "Installing mysql..."
sudo docker pull mysql:$mysql_version
if [ $? -ne 0 ]; then
  echo "MySQL image download failed"
  exit 1
fi
sudo mkdir -p /opt/data/mysql/data
if [ $? -ne 0 ]; then
  echo "mkdir -p /opt/data/mysql/data failed"
  exit 1
fi
sudo mkdir -p /opt/data/mysql/conf
if [ $? -ne 0 ]; then
  echo "mkdir -p /opt/data/mysql/conf failed"
  exit 1
fi
sudo mkdir -p /opt/data/mysql/mysqld
if [ $? -ne 0 ]; then
  echo "mkdir -p /opt/data/mysql/mysqld failed"
  exit 1
fi
sudo chmod 777 /opt/data/mysql/data
if [ $? -ne 0 ]; then
  echo "chmod 777 /opt/data/mysql/data failed"
  exit 1
fi
sudo chmod 777 /opt/data/mysql/conf
if [ $? -ne 0 ]; then
  echo "chmod 777 /opt/data/mysql/conf failed"
  exit 1
fi
sudo chmod 777 /opt/data/mysql/mysqld
if [ $? -ne 0 ]; then
  echo "chmod 777 /opt/data/mysql/mysqld failed"
  exit 1
fi
config_file="my.cnf"
sudo cp $config_file /opt/data/mysql/conf/
if [ $? -eq 0 ]; then
  echo "mysql configuration file copied successfully"
else
  echo "Copying mysql configuration file failed"
  exit 1
fi
MYSQL_PASSWORD="Aurora_123456"
echo "Starting mysql..."
sudo docker run \
  --name mysql \
  --restart=always \
  -p 3306:3306 \
  -v /opt/data/mysql/mysqld:/var/run/mysqld -v /opt/data/mysql/data:/var/lib/mysql -v /opt/data/mysql/conf:/etc/mysql/conf.d \
  -e MYSQL_ROOT_PASSWORD=$MYSQL_PASSWORD -d mysql:$mysql_version
if [ $? -ne 0 ]; then
  echo "mysql failed to start"
  exit 1
else
  echo "mysql started successfully"
fi
sudo docker network connect aurora mysql
if [ $? -ne 0 ]; then
  echo "mysql failed to join docker network"
  exit 1
else
  echo "mysql successfully joined docker network"
fi
machine_physics_net=$(ls /sys/class/net/ | grep -v "$(ls /sys/devices/virtual/net/)")
local_ip=$(ip addr | grep "$machine_physics_net" | awk '/^[0-9]+: / {}; /inet.*global/ {print gensub(/(.*)\/(.*)/, "\\1", "g", $2)}')
public_ip=$(curl ifconfig.me)
echo "Creating database..."
sleep 20
sudo docker exec -i mysql mysql -h $local_ip -uroot -p$MYSQL_PASSWORD -e "CREATE DATABASE aurora CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
if [ $? -ne 0 ]; then
  echo "Database creation failed"
  exit 1
else
  echo "Database created successfully"
fi
echo "Initializing database..."
sudo docker exec -i mysql mysql -h $local_ip -uroot -p$MYSQL_PASSWORD aurora <$(pwd)/aurora.sql
if [ $? -ne 0 ]; then
  echo "Database initialization failed"
  exit 1
else
  echo "Database initialization successful"
fi
redis_version="7.0.13"
echo "Installing redis..."
sudo docker pull redis:$redis_version
if [ $? -ne 0 ]; then
  echo "Redis image download failed"
  exit 1
fi
REDIS_PASSWORD="123456"
echo "Starting redis..."
sudo docker run --name redis \
  --restart=always \
  -p 6379:6379 -d redis:$redis_version \
  --requirepass $REDIS_PASSWORD
if [ $? -ne 0 ]; then
  echo "redis failed to start"
  exit 1
else
  echo "redis launched successfully"
fi
sudo docker network connect aurora redis
if [ $? -ne 0 ]; then
  echo "redis failed to join docker network"
  exit 1
else
  echo "redis successfully joined docker network"
fi
rabbitmq_version="3.12.14"
echo "Installing rabbitmq..."
sudo docker pull rabbitmq:$rabbitmq_version-management
if [ $? -ne 0 ]; then
  echo "rabbitmq image download failed"
  exit 1
fi
echo "Starting rabbitmq..."
sudo docker run --name rabbitmq \
  --restart=always -p 5672:5672 -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest \
  -d rabbitmq:$rabbitmq_version-management
if [ $? -ne 0 ]; then
  echo "rabbitmq failed to start"
  exit 1
else
  echo "rabbitmq launched successfully"
fi
sudo docker network connect aurora rabbitmq
if [ $? -ne 0 ]; then
  echo "rabbitmq failed to join docker network"
  exit 1
else
  echo "rabbitmq successfully joined docker network"
fi
echo "Installing minio..."
sudo docker pull minio/minio
if [ $? -ne 0 ]; then
  echo "Minio image download failed"
  exit 1
fi
sudo mkdir -p /opt/data/minio/config
if [ $? -ne 0 ]; then
  echo "minio profile directory creation failed"
  exit 1
fi
sudo mkdir -p /opt/data/minio/data
if [ $? -ne 0 ]; then
  echo "minio data directory creation failed"
  exit 1
fi
echo "Starting minio..."
sudo docker run \
  -p 9000:9000 \
  -p 9090:9090 \
  --name minio \
  -d --restart=always \
  -e "MINIO_ACCESS_KEY=minioadmin" \
  -e "MINIO_SECRET_KEY=minioadmin" \
  -v /opt/data/minio/data:/data \
  -v /opt/data/minio/config:/root/.minio \
  minio/minio server \
  /data --console-address ":9090" -address ":9000"
if [ $? -ne 0 ]; then
  echo "minio failed to start"
  exit 1
else
  echo "Minio launched successfully"
fi
sudo docker network connect aurora minio
if [ $? -ne 0 ]; then
  echo "Minio failed to join docker network"
  exit 1
else
  echo "Minio joins docker network successfully"
fi
echo "Creating minio default bucket..."
minio_bucket="aurora"
minio_myname="myminio"
sudo docker exec -it minio mc alias set $minio_myname http://minio:9000 minioadmin minioadmin
if [ $? -ne 0 ]; then
  echo "minio alias setting failed"
  exit 1
fi
sudo docker exec -it minio mc mb $minio_myname/$minio_bucket
if [ $? -ne 0 ]; then
  echo "minio default bucket creation failed"
  exit 1
else
  echo "minio default bucket created successfully"
fi
sudo docker exec -it minio mc anonymous set public $minio_myname/$minio_bucket
if [ $? -ne 0 ]; then
  echo "Minio default bucket permissions setting failed"
  exit 1
fi
echo "Installing openjdk 8..."
sudo docker pull openjdk:8-jdk
if [ $? -ne 0 ]; then
  echo "Openjdk 8 image download failed"
  exit 1
fi
echo "Starting backend services..."
minio_url=http://$public_ip:8066/minio/
sudo docker run -d \
  -p 8080:8080 \
  --name aurora_server --restart=always \
  -v $(pwd):$(pwd) \
  openjdk:8-jdk java -jar -Dupload.minio.bucketName=$minio_bucket -Dupload.minio.url=$minio_url $(pwd)/aurora-springboot-0.0.1.jar
if [ $? -ne 0 ]; then
  echo "Back-end service startup failed"
  exit 1
else
  echo "Back-end service started successfully"
fi
sudo docker network connect aurora aurora_server
if [ $? -ne 0 ]; then
  echo "Back-end service failed to join docker network"
  exit 1
else
  echo "Back-end service successfully joined docker network"
fi
echo "Restarting backend services..."
sudo docker restart aurora_server
if [ $? -ne 0 ]; then
  echo "Back-end service restart failed"
  exit 1
else
  echo "Back-end service restarted successfully"
fi
echo "Installing nginx..."
sudo docker pull nginx
if [ $? -ne 0 ]; then
  echo "nginx image download failed"
  exit 1
fi
echo "Starting nginx..."
sudo docker run -d \
  --name aurora_nginx \
  --restart=always \
  -p 8066:8066 \
  -v $(pwd)/nginx.conf:/etc/nginx/nginx.conf \
  -v $(pwd)/blog_web:/usr/local/aurora-vue/blog \
  -v $(pwd)/admin_web:/usr/local/aurora-vue/admin \
  nginx
if [ $? -ne 0 ]; then
  echo "nginx failed to start"
  exit 1
else
  echo "nginx started successfully"
fi
sudo docker network connect aurora aurora_nginx
if [ $? -ne 0 ]; then
  echo "nginx failed to join docker network"
  exit 1
else
  echo "nginx successfully joins docker network"
fi
echo "Restarting nginx..."
sudo docker restart aurora_nginx
if [ $? -ne 0 ]; then
  echo "nginx restart failed"
  exit 1
else
  echo "nginx restarted successfully"
fi
echo "All services started successfully"
echo "服务器安全组需要开放：8066端口，才能访问博客"
echo 访问 http://$public_ip:8066/ 即可访问博客
echo 访问 http://$public_ip:8066/admin/ 即可访问后台管理
echo "后台管理默认账号密码为：admin@163.com / 123456"
echo "作者：花未眠（http://www.linhaojun.top/）"
echo "部署脚本作者：karl（https://kangxianghui.top/）"
echo "感谢您选择使用Aurora博客系统，祝您使用愉快！"
