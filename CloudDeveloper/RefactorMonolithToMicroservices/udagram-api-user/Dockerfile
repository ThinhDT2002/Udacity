FROM node:12
## Create app directory
WORKDIR /usr/src/app
## Install app dependencies
## A wildcard is used to ensure both package.json AND package-lock.json are copied where available (npm@5+)
COPY udagram-api-user/package*.json ./
RUN npm ci 
## Bundle app source
COPY udagram-api-user/. .
EXPOSE 8080
CMD [ "npm", "run", "prod" ]