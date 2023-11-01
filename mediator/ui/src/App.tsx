import {useEffect, useState} from 'react'
import './App.css'

import { Icon } from '@chakra-ui/react'
import { Button, Input, Heading, Spinner } from '@chakra-ui/react'
import { CloseIcon } from '@chakra-ui/icons'
import { BiCircle } from 'react-icons/bi'


import { useToast } from '@chakra-ui/react'
import {getAllSession, fetchStartGame, getSession, startSession} from "./api";


const iconsMap = {
  '_': '',
  'o': <div className='circle'><Icon as={BiCircle} /></div>,
  'O': <div className='circle win'><Icon as={BiCircle} /></div>,
  'x': <CloseIcon />,
  'X': <CloseIcon color='red' />,
}

const Char = ({char}) => {
  return <div className='char'>{iconsMap[char]}</div>
}

function App() {
  const toast = useToast()

  const [sessionId, setSessionId] = useState([])

  const [startedSessions, setStartedSessions] = useState([])
  const [startedSessionsLoader, setStartedSessionsLoader] = useState(false)
  const [starting, setStarting] = useState(false)
  const [startingLoader, setStartingLoader] = useState(false)

  const [activeSession, setActiveSession] = useState({})

  const [botForm, setBotForm] = useState({
    bot1Id: 'test-bot-1',
    bot1Container: 'tic-tae-toe-bot:0.0.1',
    bot2Id: 'test-bot-2',
    bot2Container: 'tic-tae-toe-bot:0.0.1'
  })

  const [formError, setFormError] = useState('')


  useEffect(() => {
    setStartedSessionsLoader(true)
    getAllSession()
        .then((data) => {
          setStartedSessions(data)
        })
        .finally(() => {
          setStartedSessionsLoader(false)
        })
  }, [])

  const checkValidateForm = () => {
    for(const key of Object.keys(botForm)) {
      if(botForm[key].length === 0) {
        return false;
      }
    }

    return true;
  }

  const handleStart = async () => {

    if(checkValidateForm()) {

      setStartingLoader(true);

      let sessionId = await startSession([botForm.bot1Id, botForm.bot2Id])
      fetchStartGame(botForm, sessionId)
        .then(({session}) => {
          getSession(session, (data) => {
            console.log(data)
            setActiveSession(data)
          })

        })
        .catch((e) => {
          toast({
            title: 'На сервере произошла ошибка',
            description: e,
            status: 'error',
            duration: 3000,
            isClosable: true,
            position: 'top',
          })
        })
        .finally(() => {
          setStartingLoader(false);
          setStarting(true);
        })

    } else {
      toast({
        description: 'Все поля формы должны быть заполнены',
        status: 'error',
        duration: 1000,
        isClosable: true,
        position: 'top',
      })}
  }

  if(startingLoader) {
    return (<div className='spinner-wrap' >
      <Spinner
        thickness='4px'
        speed='0.65s'
        emptyColor='gray.200'
        color='blue.500'
        size='xl'
      />
    </div>);
  }

  return (
    <>
      {!starting &&
        <div className='start-form'>
          <div className='start-form__form'>
              <div className='start-form__col'>
                  <Heading mb={4} as='h4' size='md' textAlign='center'>Бот 1</Heading>
                  <Input value={botForm.bot1Id} mb={4} placeholder='ID' type="text" onChange={(e) => setBotForm((prevState) => ({
                    ...prevState,
                    bot1Id: e.target.value
                  }))}/>
                  <Input value={botForm.bot1Container} mb={4} placeholder='Image' type="text" onChange={(e) => setBotForm((prevState) => ({
                    ...prevState,
                    bot1Container: e.target.value
                  }))}/>
              </div>
              <div className='vs-separator'>VS.</div>
              <div className='start-form__col'>
                  <Heading mb={4} as='h4' size='md' textAlign='center'>Бот 2</Heading>
                  <Input value={botForm.bot2Id} mb={4} placeholder='ID' type="text" onChange={(e) => setBotForm((prevState) => ({
                    ...prevState,
                    bot2Id: e.target.value
                  }))}/>
                  <Input value={botForm.bot1Container} mb={4} placeholder='Image' type="text" onChange={(e) => setBotForm((prevState) => ({
                    ...prevState,
                    bot2Container: e.target.value
                  }))}/>
              </div>
          </div>


          <Button onClick={handleStart} size='lg' colorScheme='teal' disabled={startingLoader}>Старт!</Button>
        </div>
      }

      {starting &&
          <div className='playground-wrap'>
              <div className='playground-info'>
                  <div className='winner'>Winner: {activeSession?.win_bot || '_'}</div>
                  <div className='turn'>Turn: {activeSession?.last_turn?.turn}</div>
              </div>
              <div className='playground'>
                {activeSession?.last_turn?.game_field.split('').map((char) => {
                  return <Char char={char} />;
                })}
              </div>
          </div>
      }
    </>
  )
}



export default App
