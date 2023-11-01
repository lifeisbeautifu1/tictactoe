import sys

from start_session import MediatorClient

mediator_client = MediatorClient()

bot_1_id = sys.argv[1]
bot_2_id = sys.argv[3]
session_id = mediator_client.create_new_session([bot_1_id, bot_2_id])

sys.argv.append(session_id)
exec(open('./watch_session.py').read())
