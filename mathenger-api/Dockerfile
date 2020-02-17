FROM openjdk:11
ENV IMAGE_MAGICK_VERSION=8:6.9.10.23+dfsg-2.1
RUN apt-get update && apt-get install -y \
    texlive-latex-extra \
    imagemagick=${IMAGE_MAGICK_VERSION}
VOLUME /home/mathenger-api
VOLUME /home/storage/mathenger
WORKDIR /home/mathenger-api
COPY ./ /home/mathenger-api/
EXPOSE 8080
CMD chmod +x ./gradlew && ./gradlew bootRun --args='--spring.profiles.active=prod'



