export const getSession = async (session, cb) => {
  setTimeout(async () => {
    let data;
    try {
      const response = await fetch(`/sessions/${session}`)
      data = await response.json();

      cb(data)

    } catch (e) {
      console.log(e)
    } finally {
      if(data.status !== "FINISHED") {
        await getSession(session, cb)
      }
    }

  }, 1000)
}

export const startSession = async (partitipant_bots) => {
  return await fetch('/sessions', {
    method: 'POST',
    body: JSON.stringify({
      "participant_bots": partitipant_bots
    }),
    headers: {
      'Content-Type': 'application/json'
    }
  })
  // .then((data) => {
  //   let resp = data.json();
  //   console.log(resp)
  //   return resp
  // })
}

export const getAllSession = () => {
  return fetch('/sessions', {
    method: 'GET'
  }).then((data) => data.json())
}

export const fetchStartGame = (form, sessionId) => {
  return fetch('/api/start_game', {
    method: 'POST',
    body: JSON.stringify({
      ...form,
      'session_id': sessionId
    }),
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    },
  })
  .then((data) => data.json())
}
