FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /workspace/target/*.jar /app/app.jar
COPY --from=build /workspace/target/*.war /app/app.war
EXPOSE 8081
CMD ["sh", "-c", "if [ -f /app/app.jar ]; then java -jar /app/app.jar; else java -jar /app/app.war; fi"]