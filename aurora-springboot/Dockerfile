FROM maven:3-openjdk-8-slim AS builder
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:resolve -DskipTests -B

COPY src ./src
RUN mvn clean package -DskipTests -B -T 1C

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/aurora-springboot-0.0.1.jar /app/aurora-springboot-0.0.1.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/aurora-springboot-0.0.1.jar","--spring.config.location=/config/application-prod.yml"]
EXPOSE 8080