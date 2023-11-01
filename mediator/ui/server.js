import express from 'express';

import { spawn } from 'child_process';


const app = express();
app.use(express.json());

app.post("/api/start_game", (req, res, next) => {

  const {bot1Id, bot2Id, bot1Container, bot2Container, session_id} = req.body
  const python = spawn('python3', [
    '../scripts/watch_session.py',
    bot1Id,
    bot1Container,
    bot2Id,
    bot2Container,
    session_id
  ]);
  let sent = false;

    python.stdout.on('data', (data) => {
        const session = data.toString().split('%')[0]
        console.log(sent)
        if(!sent) {
            sent = true

            res
                .status(200)
                .json({session})
        }
    });

    python.stderr.on('data', (data) => {
        console.error(`stderr: ${data}`);
    });
});

export const handler = app;
