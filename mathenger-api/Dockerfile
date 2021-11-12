FROM openjdk:11
VOLUME /home/mathenger-api
WORKDIR /home/mathenger-api
COPY ./ /home/mathenger-api/
EXPOSE 8081
CMD chmod +x ./gradlew && ./gradlew bootRun --args='--spring.profiles.active=docker'



