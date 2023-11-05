import express from 'express';
import cors from 'cors';

import { spawn } from 'child_process';

const app = express();

app.use(cors())


const port = 3000;


app.use(express.json());

app.post("/api/start_game", (req, res, next) => {

  const {bot1Id, bot2Id, bot1Container, bot2Container, session_id} = req.body

  const python = spawn('python3', [
    '../../scripts/watch_session.py',
    bot1Id,
    bot1Container,
    bot2Id,
    bot2Container,
    session_id
  ]);

    let sent = false;

    python.stdout.on('data', () => {
        if(!sent) {
            sent = true

            res
                .status(200)
                .json({sent})
        }
    });

    python.stderr.on('data', (data) => {
        console.error(`stderr: ${data}`);
    });
});

app.listen(port, () => {
    console.log(`BFF on port ${port}`)
})
