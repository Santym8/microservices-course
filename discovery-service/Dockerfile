FROM maven as build
WORKDIR /app
COPY pom.xml pom.xml
RUN mvn de.qaware.maven:go-offline-maven-plugin:resolve-dependencies
COPY src src
RUN mvn clean package -DskipTests -q && cp target/*.jar app.jar

FROM eclipse-temurin:17-jdk-alpine

COPY --from=build /root/.m2 /root/.m2

WORKDIR /app
COPY --from=build /app/app.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]