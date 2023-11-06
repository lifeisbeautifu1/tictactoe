package ru.tinkoff.tictactoe.commands.persistance.postgres;

import org.mapstruct.Mapper;
import ru.tinkoff.tictactoe.commands.model.Command;

@Mapper(componentModel = "spring")
interface CommandDaoMapper {

    CommandEntity toEntity(Command command);

    Command fromEntity(CommandEntity commandEntity);
}
