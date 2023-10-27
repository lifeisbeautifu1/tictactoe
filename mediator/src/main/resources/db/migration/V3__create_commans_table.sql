create table commands
(
    command_id varchar(30)
        constraint commands_pk
            primary key,
    password   varchar(100)
);

alter table session_entity
    add constraint attacking_bot_fk
        foreign key (attacking_bot_id) references commands (command_id);

alter table session_entity
    add constraint defending_bot_fk
        foreign key (defending_bot_id) references commands (command_id);

create table command_sessions
(
    command_id varchar(30) references commands (command_id),
    session_id uuid references session_entity (id)
)