FROM openjdk:17-slim

ADD target/SpringBootJdbcApi-0.0.1-SNAPSHOT.jar app.jar

CMD ["sh", "-c", "java -jar /app.jar"]


