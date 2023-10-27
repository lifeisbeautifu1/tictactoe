package ru.tinkoff.tictactoe.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.tictactoe.commands.exception.InvalidCommandCredentials;
import ru.tinkoff.tictactoe.commands.model.Command;
import ru.tinkoff.tictactoe.commands.persistance.postgres.CommandDao;

@Service
@RequiredArgsConstructor
public class CommandService {

    private final CommandDao commandDao;

    public void saveCommand(Command command) {
        commandDao.upsertCommand(command);
    }

    public void validateCommandCredentials(String commandId, String password) {
        commandDao.findById(commandId)
            .filter(command -> command.password().equals(password))
            .orElseThrow(() -> new InvalidCommandCredentials(commandId));
    }
}
