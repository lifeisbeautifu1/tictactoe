from json import loads, dumps
from http.client import HTTPConnection
from time import sleep


class MediatorClient:
    def __init__(self):
        self.connection = HTTPConnection(host='localhost', port=8080)
        self.mediator_url = "http://mediator:8080"
        self.game_field_size = 19

    def create_new_session(self, participant_bots):
        self.connection.request(
            'POST',
            '/sessions',
            body=dumps({
                'participant_bots': participant_bots
            }),
            headers={
                'Content-Type': 'application/json'
            }
        )
        session_id = loads(self.connection.getresponse().read().decode(encoding='utf-8'))['session_id']
        self.session_id = session_id
        return session_id

    def watch_game(self):
        while True:
            self.connection.request('GET', f'/sessions/{self.session_id}')
            response = loads(self.connection.getresponse().read().decode(encoding='utf-8'))
            status = response['status']
            last_turn = response['last_turn']
            last_turn_num = last_turn['turn']
            print(f'Status {status}, last turn {last_turn_num}')
            counter = 0
            game_field = last_turn['game_field']
            while counter < self.game_field_size * self.game_field_size:
                print(game_field[counter:(counter + self.game_field_size)])
                counter += self.game_field_size
            print(f"============================================================")
            if status == 'FINISHED':
                self.winner = response['win_bot']
                break
            sleep(1)
