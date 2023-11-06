package ru.tinkoff.tictactoe.commands.persistance.postgres;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.tictactoe.commands.model.Command;

@Component
@RequiredArgsConstructor
public class CommandDao {

    private final CommandEntityRepository commandEntityRepository;
    private final CommandDaoMapper commandDaoMapper;

    public void upsertCommand(Command command) {
        commandEntityRepository.saveAndFlush(commandDaoMapper.toEntity(command));
    }

    public Optional<Command> findById(String commandId) {
        return commandEntityRepository.findById(commandId)
            .map(commandDaoMapper::fromEntity);
    }
}
