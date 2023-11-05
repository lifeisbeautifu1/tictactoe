export const getSession = async (session, cb) => {
  setTimeout(async () => {
    let data;
    try {
      const response = await fetch(`/api/sessions/${session}`)
      data = await response.json();

      cb(data)

    } catch (e) {
      console.log(e)
    } finally {
      console.log(data)
      if(data.status !== "FINISHED" && data.status !== 404) {
        await getSession(session, cb)
      }
    }

  }, 1000)
}

export const startSession = async (partitipant_bots) => {
  return fetch('/api/sessions', {
    method: 'POST',
    body: JSON.stringify({
      "participant_bots": partitipant_bots
    }),
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    }
  }).then((response) => {
    if (response.ok) {
      return response.json();
    }
    throw new Error('Something went wrong');
  })
}

export const getAllSession = () => {
  return fetch('/api/sessions', {
    method: 'GET'
  })
  .then((response) => {
    if (response.ok) {
      return response.json();
    }
    throw new Error('Something went wrong');
  })
}

export const fetchStartGame = (form, sessionId) => {
  return fetch('http://localhost:3000/api/start_game', {
    method: 'POST',
    body: JSON.stringify({
      ...form,
      'session_id': sessionId
    }),
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    },
  })
  .then((response) => {
    if (response.ok) {
      return response.json();
    }
    throw new Error('Something went wrong');
  })
}
