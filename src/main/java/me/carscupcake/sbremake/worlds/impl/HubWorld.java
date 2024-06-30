package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.entity.impl.hub.GraveyardZombie;
import me.carscupcake.sbremake.worlds.EntitySpawner;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.coordinate.Pos;

import java.util.HashSet;
import java.util.Set;

public class HubWorld extends SkyblockWorld.WorldProvider {
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.Hub;
    }

    @Override
    public Pos spawn() {
        return new Pos(-2.5, 70.5, -70.5, 180, 0);
    }

    @Override
    public boolean useCustomMining() {
        return false;
    }

    private final Set<EntitySpawner> spawners = new HashSet<>();

    @Override
    protected void register() {
        spawners.add(new EntitySpawner(new Pos[]{new Pos(-105.5, 71.0, -61.5), new Pos(-112.5, 71.0, -61.5), new Pos(-114.5, 71.0, -66.5), new Pos(-117.5, 71.0, -73.5), new Pos(-124.5, 71.0, -75.5), new Pos(-121.5, 71.0, -82.5), new Pos(-117.5, 71.0, -89.5), new Pos(-110.5, 72.0, -103.5), new Pos(-101.5, 72.0, -111.5), new Pos(-99.5, 72.0, -127.5), new Pos(-93.5, 72.0, -145.5), new Pos(-119.5, 72.0, -141.5), new Pos(-145.5, 72.0, -122.5), new Pos(-158.5, 72.0, -93.5), new Pos(-173.5, 74.0, -86.5), new Pos(-163.5, 72.0, -136.5), new Pos(-68.5, 79.0, -184.5), new Pos(-43.5, 80.0, -173.5)}, 200,
                new EntitySpawner.BasicConstructor(GraveyardZombie::new), container));
    }

    @Override
    protected void unregister() {
        spawners.forEach(EntitySpawner::stop);
    }
}
