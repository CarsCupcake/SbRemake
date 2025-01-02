package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.blocks.impl.EndStone;
import me.carscupcake.sbremake.blocks.impl.Obsidian;
import me.carscupcake.sbremake.entity.impl.end.EndermanT1;
import me.carscupcake.sbremake.entity.impl.end.EndermanT2;
import me.carscupcake.sbremake.entity.impl.end.EndermanT3;
import me.carscupcake.sbremake.worlds.EntitySpawner;
import me.carscupcake.sbremake.worlds.Launchpad;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class End extends SkyblockWorld.WorldProvider {
    public End() {
        super(Collections.singletonList(new Launchpad(-496, -277, -495, -274, 100, SkyblockWorld.SpidersDen, new Pos(-378, 130, -261))));
    }
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.End;
    }

    @Override
    public Pos spawn() {
        return new Pos(-503, 101, -275.5, 90, 0);
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }

    private final Set<EntitySpawner> entitySpawners = new HashSet<>();

    @Override
    protected void register() {
        super.register();
        entitySpawners.add(new EntitySpawner(new Pos[]{new Pos(-520.0, 100.0, -272.0),new Pos(-528.0, 100.0, -264.0),new Pos(-547.0, 96.0, -247.0),new Pos(-563.0, 97.0, -247.0),new Pos(-572.0, 96.0, -251.0),new Pos(-570.0, 100.0, -265.0),new Pos(-567.0, 99.0, -280.0),new Pos(-571.0, 93.0, -295.0),new Pos(-580.0, 87.0, -303.0),new Pos(-571.0, 80.0, -316.0),new Pos(-556.0, 97.0, -305.0),new Pos(-536.0, 86.0, -306.0),new Pos(-547.0, 100.0, -289.0),new Pos(-538.0, 101.0, -276.0),new Pos(-549.0, 87.0, -226.0),new Pos(-569.0, 82.0, -221.0)},
                200, new EntitySpawner.BasicConstructor(EndermanT1::new), container));
        entitySpawners.add(new EntitySpawner(new Pos[]{new Pos(-571.0, 81.0, -224.0),new Pos(-553.0, 78.0, -237.0),new Pos(-552.0, 73.0, -250.0),new Pos(-536.0, 68.0, -256.0),new Pos(-516.0, 70.0, -267.0),new Pos(-517.0, 67.0, -284.0),new Pos(-534.0, 69.0, -300.0),new Pos(-545.0, 70.0, -317.0),new Pos(-553.0, 77.0, -296.0),new Pos(-560.0, 71.0, -288.0),new Pos(-562.0, 70.0, -275.0),new Pos(-539.0, 66.0, -247.0),new Pos(-537.0, 62.0, -235.0),new Pos(-541.0, 54.0, -217.0),new Pos(-559.0, 45.0, -202.0),new Pos(-581.0, 73.0, -305.0),new Pos(-524.0, 66.0, -287.0)},
                200, new EntitySpawner.BasicConstructor(EndermanT2::new), container));
        entitySpawners.add(new EntitySpawner(new Pos[]{new Pos(-564.0, 16.0, -266.0),new Pos(-568.0, 16.0, -272.0),new Pos(-560.0, 14.0, -280.0),new Pos(-557.0, 9.0, -290.0),new Pos(-561.0, 6.0, -309.0),new Pos(-549.0, 4.0, -310.0),new Pos(-542.0, 5.0, -299.0),new Pos(-542.0, 5.0, -287.0),new Pos(-533.0, 5.0, -278.0),new Pos(-529.0, 6.0, -268.0),new Pos(-537.0, 2.0, -258.0),new Pos(-554.0, 1.0, -248.0),new Pos(-566.0, 4.0, -264.0),new Pos(-579.0, 4.0, -262.0),new Pos(-583.0, 4.0, -249.0),new Pos(-575.0, 4.0, -238.0),new Pos(-580.0, 12.0, -218.0),new Pos(-561.0, 20.0, -222.0),new Pos(-561.0, 19.0, -242.0),new Pos(-542.0, 21.0, -269.0),new Pos(-532.0, 22.0, -270.0),new Pos(-527.0, 16.0, -287.0),new Pos(-523.0, 17.0, -299.0),new Pos(-525.0, 12.0, -314.0),new Pos(-536.0, 7.0, -325.0),new Pos(-547.0, 4.0, -314.0)},
                200, new EntitySpawner.BasicConstructor(EndermanT3::new), container));
    }

    @Override
    protected void unregister() {
        super.unregister();
        entitySpawners.forEach(EntitySpawner::stop);
        entitySpawners.clear();
    }

    private static final MiningBlock[] ores = {new EndStone(), new Obsidian()};
    @Override
    public MiningBlock[] ores(Pos pos) {
        return ores;
    }
}
