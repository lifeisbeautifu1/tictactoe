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

export const startSession = () => {
  return fetch('/sessions', {
    method: 'POST'
  }).then((data) => data.json())
}

export const getAllSession = () => {
  return fetch('/sessions', {
    method: 'GET'
  }).then((data) => data.json())
}

export const fetchStartGame = (form) => {
  return fetch('/api/start_game', {
    method: 'POST',
    body: JSON.stringify(form),
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    },
  })
      .then((data) => data.json())
}
