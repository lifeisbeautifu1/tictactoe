package ru.tinkoff.tictactoe.commands.persistance.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "commands")
public class CommandEntity {

    @Id
    @Column(name = "command_id")
    private String commandId;

    @Column(name = "password")
    private String password;
}
