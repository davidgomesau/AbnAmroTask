FROM node:20.8.0

WORKDIR /app

#ENV PATH /app/node_modules/.bin:$PATH

COPY package.json /app/package.json
RUN npm install
RUN npm install -g @angular/cli@16.2.7

COPY . /app

# start app
CMD ng serve --host 0.0.0.0
