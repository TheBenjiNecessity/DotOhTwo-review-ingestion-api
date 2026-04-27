FROM public.ecr.aws/amazoncorretto/amazoncorretto:21 AS build
RUN yum install -y tar gzip
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
ARG CODEARTIFACT_AUTH_TOKEN
ENV CODEARTIFACT_AUTH_TOKEN=$CODEARTIFACT_AUTH_TOKEN
RUN ./mvnw -s .mvn/settings.xml dependency:go-offline
COPY src/ src/
RUN ./mvnw -s .mvn/settings.xml package -DskipTests

FROM public.ecr.aws/amazoncorretto/amazoncorretto:21
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]