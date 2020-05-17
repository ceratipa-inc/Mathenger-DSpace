FROM openjdk:11
VOLUME /home/mathenger-api
VOLUME /home/storage/mathenger
WORKDIR /home/mathenger-api
COPY ./ /home/mathenger-api/
EXPOSE 8080
CMD chmod +x ./gradlew && ./gradlew bootRun --args='--spring.profiles.active=prod'



