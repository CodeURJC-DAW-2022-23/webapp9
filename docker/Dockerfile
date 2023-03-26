FROM maven:3-openjdk-11 as builder

WORKDIR /tmp

COPY pom.xml /tmp/

COPY /src /tmp/src

RUN mvn package -DskipTests=true

FROM openjdk:11

WORKDIR /usr/src/app

COPY --from=builder /tmp/target/*.jar /usr/src/app/

EXPOSE 8443

CMD ["java", "-jar", "Backend-0.0.1-SNAPSHOT.jar" ]
