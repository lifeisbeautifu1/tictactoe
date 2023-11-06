package ru.tinkoff.tictactoe.commands.persistance.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CommandEntityRepository extends JpaRepository<CommandEntity, String> {}
