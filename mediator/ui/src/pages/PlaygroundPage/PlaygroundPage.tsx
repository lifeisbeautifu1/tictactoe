import { useParams } from "react-router-dom";
import {useEffect, useState} from "react";

import {Icon, Spinner} from "@chakra-ui/react";
import {BiCircle} from "react-icons/bi";
import {CloseIcon} from "@chakra-ui/icons";

import {getSession} from "../../api";

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

const PlaygroundPage = () => {
  const { sessionId } = useParams();

  const [activeSession, setActiveSession] = useState({})
  const [loader, setLoader] = useState(false)

  useEffect(() => {

    setLoader(true);
    getSession(sessionId, (data) => {
      setActiveSession(data)
    }).finally(() => {
      setLoader(false);
    })

  }, [sessionId])

  if(loader) {
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
  )
}

export default PlaygroundPage;
