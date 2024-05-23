FROM node:10.15-alpine as builder

WORKDIR /usr/src
COPY package.* /usr/src
RUN npm install
#RUN npm i gzipper -g
COPY . /usr/src
RUN node ./node_modules/.bin/ng build --prod
# RUN node --max_old_space_size=8192 ./node_modules/.bin/ng build --prod 
# RUN gzipper --verbose ./dist
#RUN npm update
FROM nginx:latest

# copy artifact build from the 'build environment'
COPY ./compose/* /etc/nginx/conf.d/
COPY --from=builder /usr/src/dist/payment-client /usr/share/nginx/html
EXPOSE 80
EXPOSE 443
#CMD ["nginx", "-g", "daemon off;"]
CMD ["/bin/sh",  "-c",  "envsubst < /usr/share/nginx/html/assets/env.template.js > /usr/share/nginx/html/assets/env.js && exec nginx -g 'daemon off;'"]
