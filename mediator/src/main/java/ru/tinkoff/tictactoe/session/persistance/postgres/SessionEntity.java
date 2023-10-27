package ru.tinkoff.tictactoe.session.persistance.postgres;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.tinkoff.tictactoe.commands.persistance.postgres.CommandEntity;
import ru.tinkoff.tictactoe.turn.persistance.postgres.TurnEntity;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "session_entity")
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "attacking_bot_url")
    private String attackingBotUrl;
    @Column(name = "attacking_bot_id")
    private String attackingBotId;

    @Column(name = "defending_bot_id")
    private String defendingBotId;
    @Column(name = "defending_bot_url")
    private String defendingBotUrl;

    @OneToMany(mappedBy = "sessionEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TurnEntity> turnEntities = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "command_sessions",
        joinColumns = {@JoinColumn(name = "session_id")},
        inverseJoinColumns = {@JoinColumn(name = "command_id")}
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<CommandEntity> participantBots;

    @Column(name = "status")
    private String status;

    @Column(name = "win_bot")
    private String winBot;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
}
