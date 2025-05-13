# Stage 1: Build Angular App
FROM node:18 as build
WORKDIR /app
COPY ./package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

# Stage 2: Serve Angular App with Nginx
FROM nginx:alpine
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
COPY nginx-custom.conf /etc/nginx/conf.d/default.conf