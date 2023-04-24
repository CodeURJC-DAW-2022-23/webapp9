# Build stage for frontend
FROM node:18-alpine AS frontend
WORKDIR /ang
COPY frontend/angularclient/ /ang/
RUN npm install
RUN npm run build --prod --base-href="/new/"

# Build stage for backend
FROM maven:3-openjdk-11 AS backend
WORKDIR /tmp
COPY backend/pom.xml /tmp/
COPY backend/src /tmp/src
COPY --from=frontend /ang/dist/angularclient/* /tmp/src/main/resources/static/new/
RUN mvn package -DskipTests

# Final stage
FROM openjdk:11
WORKDIR /usr/src/app
COPY --from=backend /tmp/target/*.jar /usr/src/app
EXPOSE 8443
CMD [ "java", "-jar", "Backend-0.0.1-SNAPSHOT.jar" ]
