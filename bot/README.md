# Тестовый бот для участия в игре tic-tac-toe

## Как собрать образ с ботом

Запустить в терминале из [корневой директории проекта](../)

```shell
docker build . -t tic-tac-toe-bot:0.0.1 -f bot/Dockerfile
```

При запуске тестового бота важно знать, что ему в env переменной `BOT_ID` передается идентификатор команды, в
`BOT_PASSWORD` передается пароль. Это важно учесть при регистрации ботов, когда настраивается окружение по
[инструкции](../README.md).
