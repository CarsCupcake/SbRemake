package me.carscupcake.sbremake.item.minion;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.util.Cloneable;

public interface MinionEntity extends Cloneable<SkyblockEntity> {
    String getId();
    String minionId();
    void setMinionId(String minionId);
}
