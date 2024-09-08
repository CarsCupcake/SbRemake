package me.carscupcake.sbremake.entity.slayer;

public interface ISlayer {
    String getName();

    String getId();

    SlayerEntity spawnEntity(int tier);

    int requiredXp(int currentLevel);

    String getTitle(int level);

    default int getMaxLevel() {
        return 9;
    }
}
