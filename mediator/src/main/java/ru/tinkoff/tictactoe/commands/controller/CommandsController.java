package ru.tinkoff.tictactoe.commands.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.tictactoe.commands.CommandService;
import ru.tinkoff.tictactoe.commands.model.Command;

@RestController
@RequestMapping("/commands")
@RequiredArgsConstructor
class CommandsController {

    private final CommandService commandService;

    @PutMapping("/{commandId}")
    void saveCommand(
        @PathVariable String commandId,
        @RequestBody @NotBlank String password
    ) {
        commandService.saveCommand(new Command(commandId, password));
    }
}
