package me.carscupcake.sbremake.util;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.junit.Assert;

public abstract class TaskScheduler implements Runnable {
    private Task task;

    public void repeatTask(int delay, int repeatDelay) {
        task = MinecraftServer.getSchedulerManager().buildTask(this).delay(TaskSchedule.tick(delay)).repeat(TaskSchedule.tick(repeatDelay)).schedule();
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
        task.cancel();
        task = null;
    }
}
