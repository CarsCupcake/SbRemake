package me.carscupcake.sbremake.util;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.junit.Assert;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TaskScheduler implements Runnable {
    private Task task;
    @Getter
    @Setter
    private SkyblockEntity entity;
    public AtomicBoolean cancelled = new AtomicBoolean(false);

    public void repeatTask(int delay, int repeatDelay) {
        Task.Builder builder = MinecraftServer.getSchedulerManager().buildTask(this);
        if (delay != 0)
            builder.delay(TaskSchedule.tick(delay));
        task = builder.repeat(TaskSchedule.tick(repeatDelay)).schedule();
    }

    public void repeatTaskAsync(int delay, int repeatDelay) {
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(delay * 50L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                try {
                    Thread.sleep(repeatDelay * 50L);
                    if (cancelled.get()) {
                        break;
                    }
                    run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void repeatTask(int repeatDelay) {
        repeatTask(0, repeatDelay);
    }

    public void delayTask(int delay) {
        Assert.assertNull("Task is already running", task);
        task = MinecraftServer.getSchedulerManager().buildTask(() -> {
            run();
            task = null;
        }).delay(TaskSchedule.tick(delay)).schedule();
    }

    public synchronized void cancel() {
        cancelled.set(true);
        if (task == null || !task.isAlive()) return;
        task.cancel();
        task = null;
        if (entity != null) {
            entity.unassignTask(this);
        }
    }

    public boolean isRunning() {
        return task != null && task.isAlive() && !cancelled.get();
    }
}
