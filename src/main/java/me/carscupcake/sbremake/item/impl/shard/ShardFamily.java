package me.carscupcake.sbremake.item.impl.shard;

import lombok.Getter;

public enum ShardFamily {
    Elemental,
    Reptile,
    Croco,
    Dragon,
    Turtle,
    Eel,
    Amphibian,
    Bug,
    Demon,
    Bird,
    Lizard,
    Panda,
    Shulker,
    Scaled,
    Serpent,
    Lapis,
    Fish,
    Spider,
    Treasure,
    Squid,
    Phantom,
    Drowned,
    TropicalFish( "Tropical Fish"),
    TreasureFish( "Treasure Fish"),
    CaveDweller( "Cave Dweller"),
    Frog,
    Axolotl;
    private final String name;
    ShardFamily() {
        this.name = null;
    }

    ShardFamily(String name) {
        this.name = name;
    }

    public String getName() {
        return name == null ? name() : name;
    }
}
