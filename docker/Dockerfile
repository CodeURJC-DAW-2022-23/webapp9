# Build stage for frontend
FROM node:18-alpine AS frontend
WORKDIR /tmp
COPY frontend/angularclient/ .
RUN npm install
RUN npm run build --prod --base-href=/new/

# Build stage for backend
FROM maven:3-openjdk-11 AS backend
WORKDIR /tmp
COPY backend/pom.xml /tmp/
COPY backend/src /tmp/src
COPY --from=frontend /tmp/dist/angularclient /project/src/main/resources/static/new
RUN mvn clean package -DskipTests

# Final stage
FROM openjdk:11
WORKDIR /app
COPY --from=backend /tmp/target/*.jar /app/app.jar
EXPOSE 8
CMD ["java", "-jar", "app.jar"]
