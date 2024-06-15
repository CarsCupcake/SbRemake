package me.carscupcake.sbremake.util;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.util.HashMap;

public class EntitySinusMovement {
    private final double strech;
    private final double maxHeight;
    private final double add;

    public EntitySinusMovement(double strech, double maxHeight, double add) {
        this.strech = strech;
        this.maxHeight = maxHeight;
        this.add = add;
    }

    private final HashMap<Entity, Task> tasks = new HashMap<>();

    public void move(Entity e, Runnable end, int ticks, int staringTick, Point finish) {
        move(e, end, ticks, staringTick, e.getPosition(), finish, null);
    }

    public void move(Entity e, int ticks, int staringTick, Point start, Point finish, Runnable tick) {
        move(e, null, ticks, staringTick, start, finish, tick);
    }

    public void move(Entity e, int ticks, int staringTick, Point start, Point finish) {
        move(e, null, ticks, staringTick, start, finish, null);
    }

    public void move(Entity e, Runnable end, int ticks, int staringTick, Point start, Point finish) {
        move(e, end, ticks, staringTick, start, finish, null);
    }

    public void move(Entity e, Runnable end, int ticks, int staringTick, Point start, Point finish, Runnable tick) {
        if (tasks.containsKey(e)) return;
        final Vec dir = Vec.fromPoint(finish.sub(start)).normalize().mul(start.distance(finish) / (ticks - staringTick));

        tasks.put(e, e.scheduler().buildTask(
                new Runnable() {
                    int i = staringTick;

                    @Override
                    public void run() {
                        if (e.isRemoved()) {
                            tasks.get(e).cancel();
                            return;
                        }
                        if (ticks < i) {
                            if (end != null) {
                                end.run();
                            }
                            tasks.get(e).cancel();
                            return;
                        }
                        i++;
                        e.teleport(e.getPosition().add(dir).add(0, calculateOffset(i, ticks) - calculateOffset(i - 1, ticks), 0));
                        if (tick != null) tick.run();
                    }
                }).repeat(TaskSchedule.tick(1)).schedule());
    }

    public double calculateOffset(int tick, int animationLenght) {
        return useFunction(((((double) tick) / ((double) animationLenght))) * Math.PI);
    }

    private double useFunction(double val) {
        return maxHeight * Math.sin(val * strech) + add;
    }
}
