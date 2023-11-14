package ru.tinkoff.tictactoebottinkoffsummerpractice.bot.impl;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import ru.tinkoff.tictactoebottinkoffsummerpractice.bot.BotService;
import ru.tinkoff.tictactoebottinkoffsummerpractice.bot.Figure;
import ru.tinkoff.tictactoebottinkoffsummerpractice.bot.registration.BotRegistrationService;
import ru.tinkoff.tictactoebottinkoffsummerpractice.config.BotConfig;
import ru.tinkoff.tictactoebottinkoffsummerpractice.bot.AppModel;


@Service
public class StupidBotService implements BotService {

	private final AppModel appModel;

    public StupidBotService(
        BotRegistrationService registrationService,
        BotConfig botConfig
    ) {
        Figure figure = registrationService.getFigure();
		appModel = new AppModel(figure.getName());
    }

    @Override
    public String makeTurnByGameField(String gameField) {
		this.appModel.sync(gameField);
		this.appModel.show();
		this.appModel.makeTurn();
		return this.appModel.getGameField();
    }
}
