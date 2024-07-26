FROM maven:3-eclipse-temurin-17 as build
WORKDIR /jooby-api-template
COPY pom.xml pom.xml
COPY src src
COPY conf conf
RUN mvn package

FROM eclipse-temurin:17-jdk
WORKDIR /jooby-api-template
COPY --from=build /jooby-api-template/target/jooby-api-template-1.0.0.jar app.jar
COPY conf conf
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
