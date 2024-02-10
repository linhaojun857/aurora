FROM openjdk:8
RUN rm -f /etc/localtime \
&& ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone
COPY target/aurora-springboot-0.0.1.jar /app/aurora-springboot-0.0.1.jar
COPY src/main/resources/application.yml /config/application.yml
COPY src/main/resources/application-prod.yml /config/application-prod.yml
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/aurora-springboot-0.0.1.jar","--spring.config.location=/config/application.yml"]
EXPOSE 8080
