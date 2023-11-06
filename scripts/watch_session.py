import sys
from random import randint
import docker
from start_session import MediatorClient

bot_1_container = None
bot_2_container = None
try:
    bot_1_id = sys.argv[1]
    bot_1_image = sys.argv[2]

    bot_2_id = sys.argv[3]
    bot_2_image = sys.argv[4]

    session_id = sys.argv[5]

    docker_container = docker.from_env()
    mediator_client = MediatorClient()
    mediator_client.session_id = session_id


    class BotContainerParams:
        def __init__(
                self,
                id,
                image,
                port
        ):
            self.id = id
            self.image = image
            self.port = port
            self.url = f"http://{id}:{port}"


    def start_bot_container(bot_container_params: BotContainerParams):
        return docker_container.containers.run(
            bot_container_params.image,
            detach=True,
            network='internal',
            name=bot_container_params.id,
            environment={
                'SESSION_ID': session_id,
                'BOT_URL': bot_container_params.url,
                'MEDIATOR_URL': mediator_client.mediator_url,
                'BOT_ID': bot_container_params.id,
                'BOT_PASSWORD': bot_container_params.id,
                'SERVER_PORT': bot_container_params.port,
            }
        )


    print(f"================STARTING SESSION {session_id}===============")
    bot_1_container = start_bot_container(BotContainerParams(
        bot_1_id,
        bot_1_image,
        randint(10_000, 20_000)
    ))
    bot_2_container = start_bot_container(BotContainerParams(
        bot_2_id,
        bot_2_image,
        randint(10_000, 20_000)
    ))
    mediator_client.watch_game()
    print(f"========SESSION {session_id} OVER, bot {mediator_client.winner} wins========")

except Exception as e:
    print(f"Error {e}")
finally:
    if bot_1_container:
        bot_1_container.stop()
        bot_1_container.remove()
    if bot_2_container:
        bot_2_container.stop()
        bot_2_container.remove()
