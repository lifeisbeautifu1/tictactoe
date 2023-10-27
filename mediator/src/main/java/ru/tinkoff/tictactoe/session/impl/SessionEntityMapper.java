package ru.tinkoff.tictactoe.session.impl;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.tinkoff.tictactoe.commands.persistance.postgres.CommandEntity;
import ru.tinkoff.tictactoe.session.model.Session;
import ru.tinkoff.tictactoe.session.model.SessionWithAllTurns;
import ru.tinkoff.tictactoe.session.model.SessionWithLastTurn;
import ru.tinkoff.tictactoe.session.persistance.postgres.SessionEntity;
import ru.tinkoff.tictactoe.turn.model.Turn;
import ru.tinkoff.tictactoe.turn.persistance.postgres.TurnEntity;

@Mapper(componentModel = "spring")
interface SessionEntityMapper {

    @Mapping(source = "turnEntities", target = "turns")
    SessionWithAllTurns toAllTurnsSession(SessionEntity sessionEntity);

    @Mapping(source = "turnEntities", target = "lastTurn", qualifiedByName = "getLastTurn")
    SessionWithLastTurn toLastTurnSession(SessionEntity sessionEntity);

    @Named("getLastTurn")
    default Turn getLastTurn(List<TurnEntity> turnEntities) {
        if (turnEntities.isEmpty()) {
            return null;
        }
        return this.fromTurnEntity(turnEntities.get(turnEntities.size() - 1));
    }

    Turn fromTurnEntity(TurnEntity turnEntity);

    @Mapping(target = "participantBots", source = "participantBots", qualifiedByName = "fromCommandEntities")
    Session toSession(SessionEntity sessionEntity);

    @Named("fromCommandEntities")
    default Set<String> fromCommandEntities(List<CommandEntity> commandEntityCollection) {
        return commandEntityCollection.stream().map(CommandEntity::getCommandId).collect(toSet());
    }

    List<Session> toListOfSession(List<SessionEntity> sessionEntities);
}
