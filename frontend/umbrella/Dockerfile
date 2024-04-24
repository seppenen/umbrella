
# Билд стейдж
FROM node:16-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . ./
RUN npm run build

# Рантайм стейдж
FROM node:16-alpine
WORKDIR /app
# Копируем файлы сборки и package.json
COPY --from=build /app/build ./build
COPY --from=build /app/package*.json ./
# Устанавливаем глобально пакет serve
RUN npm install -g serve
# Запускаем сервис на порту 3000
CMD ["serve", "-s", "build", "-l", "3000"]
