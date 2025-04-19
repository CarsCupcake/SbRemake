package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.entity.impl.crimsonIsle.WitherSpectre;
import me.carscupcake.sbremake.worlds.EntitySpawner;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

import java.util.HashSet;
import java.util.Set;

public class CrimsonIsle extends SkyblockWorld.WorldProvider {
    public static final Pos SPAWN_POS = new Pos(-360.5, 80.1, -430.5, 180f, 0f);
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.CrimsonIsle;
    }

    @Override
    public Pos spawn() {
        return SPAWN_POS;
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }

    private final Set<EntitySpawner> entitySpawners = new HashSet<>();

    @Override
    protected void register() {
        super.register();
        entitySpawners.add(new EntitySpawner(new Pos[]{new Pos(-432.0, 86.0, -497.0),new Pos(-423.0, 86.0, -497.0),new Pos(-404.0, 86.0, -497.0),new Pos(-396.0, 86.0, -497.0),new Pos(-390.0, 86.0, -498.0),new Pos(-387.0, 86.0, -503.0),new Pos(-389.0, 86.0, -518.0),new Pos(-414.0, 100.0, -520.0),new Pos(-423.0, 100.0, -529.0),new Pos(-430.0, 100.0, -527.0)},
                200, new EntitySpawner.BasicConstructor(WitherSpectre::new), container));
    }

    @Override
    protected void unregister() {
        super.unregister();
        entitySpawners.forEach(EntitySpawner::stop);
    }
}
