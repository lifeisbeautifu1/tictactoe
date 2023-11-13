docker run -t -i -p 8081:5000 --name bot \
-e SESSION_ID="64d32f88-a78a-42d1-9e84-797a4bf0d5e8" \
-e BOT_URL="http://localhost:5000" \
-e MEDIATOR_URL="http:localhost:3000" \
-e SERVER_PORT=5000 \
"tic-tac-toe-bot:0.0.1"
