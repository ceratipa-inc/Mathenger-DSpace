#build
FROM node:12.4.0-alpine as build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . ./

#args
ARG REACT_APP_API_BASE_URL
ARG REACT_APP_WEB_SOCKET_CONNECTION_URL

#env
ENV REACT_APP_API_BASE_URL=${REACT_APP_API_BASE_URL}
ENV REACT_APP_WEB_SOCKET_CONNECTION_URL=${REACT_APP_WEB_SOCKET_CONNECTION_URL}

RUN npm run build

#run
FROM nginx:1.17.0-alpine


COPY --from=build /app/build /var/www


COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

ENTRYPOINT ["nginx","-g","daemon off;"]
