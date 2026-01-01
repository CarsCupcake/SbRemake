package me.carscupcake.sbremake.item.impl.shard;

import me.carscupcake.sbremake.item.ItemRarity;

import java.util.List;

public record FusionIngredient(int amount, List<IFusionConstrain> constrains) {
    public interface IFusionConstrain {
        boolean test(Shard shard);
    }

    public record RarityConstrain(List<ItemRarity> rarity) implements IFusionConstrain{

        @Override
        public boolean test(Shard shard) {
            return rarity.contains(shard.getRarity());
        }
    }

    public record FamilyConstrain(List<ShardFamily> families) implements IFusionConstrain{

        @Override
        public boolean test(Shard shard) {
            for (var shardFamily : shard.getFamilies())
                if (families.contains(shardFamily)) return true;
            return false;
        }
    }

    public record CategoryConstrain(List<ShardCategory> categories) implements IFusionConstrain{
        @Override
        public boolean test(Shard shard) {
            return categories.contains(shard.getCategory());
        }
    }

    public record ShardConstrain(List<Shard> shards) implements IFusionConstrain {
        @Override
        public boolean test(Shard shard) {
            return shards.contains(shard);
        }
    }
}
