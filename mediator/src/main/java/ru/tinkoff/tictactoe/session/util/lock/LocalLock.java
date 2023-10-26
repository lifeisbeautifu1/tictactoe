package ru.tinkoff.tictactoe.session.util.lock;

import static java.util.Objects.isNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import ru.tinkoff.tictactoe.ApiException;

@Slf4j
@Component
public class LocalLock {

    private static final int PERMITS = 1;
    private static final Duration SEMAPHORE_TTL = Duration.ofMinutes(10);
    private final Map<UUID, SemaphoreItem> semaphores = new ConcurrentHashMap<>();
    private final Function<UUID, Semaphore> semaphoreProvider = id -> new Semaphore(PERMITS, true);
    private final Clock clock = Clock.systemDefaultZone();

    {
        final var taskScheduler = getSemaphoresCleanScheduler();
        taskScheduler.scheduleWithFixedDelay(this::cleanSemaphores, Duration.ofMinutes(1));
    }

    public <T> T lockRegistration(UUID semaphoreId, Duration timeout, Callable<T> call) {
        final var timeoutMillis = timeout.toMillis();
        log.trace("Start acquire session {} lock", semaphoreId);
        final var semaphoreItem = semaphores.compute(semaphoreId, (key, existedSemaphore) -> {
            final var instant = clock.instant();
            if (!isNull(existedSemaphore)) {
                return new SemaphoreItem(existedSemaphore.semaphore(), instant);
            } else {
                final var newSemaphore = semaphoreProvider.apply(key);
                return new SemaphoreItem(newSemaphore, instant);
            }
        });
        log.trace("Session {} lock is here", semaphoreId);
        final var semaphore = semaphoreItem.semaphore();
        try {
            log.trace("Trying acquire session {} lock", semaphoreId);
            final var acquired = semaphore.tryAcquire(PERMITS, timeoutMillis, MILLISECONDS);
            if (!acquired) {
                log.debug("Session lock {} is not acquired", semaphoreId);
                throw new LockException();
            }
            log.trace("Session lock {} is acquired", semaphoreId);
            final var result = call.call();
            log.trace("Session lock {} is released", semaphoreId);
            semaphore.release(PERMITS);
            return result;
        } catch (ApiException apiException) {
            log.trace("Session lock {} is released", semaphoreId);
            semaphore.release(PERMITS);
            throw apiException;
        } catch (Exception e) {
            log.trace("Session lock {} is released", semaphoreId);
            semaphore.release(PERMITS);
            throw new LockException(e);
        }
    }

    private void cleanSemaphores() {
        semaphores.replaceAll((key, semaphoreItem) -> {
            final var timeAfterLastAccessElapsed = Duration.between(semaphoreItem.lastAccess(), clock.instant());
            if (timeAfterLastAccessElapsed.compareTo(SEMAPHORE_TTL) > 0) {
                return null;
            } else {
                return semaphoreItem;
            }
        });
    }

    private TaskScheduler getSemaphoresCleanScheduler() {
        final var taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setDaemon(true);
        taskScheduler.setThreadGroupName("locks-cleaner");
        taskScheduler.initialize();
        return taskScheduler;
    }

    private record SemaphoreItem(
        Semaphore semaphore,
        Instant lastAccess
    ) {}
}
