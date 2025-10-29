package me.carscupcake.sbremake.worlds.impl.hub;

import lombok.Getter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.player.potion.IPotion;
import me.carscupcake.sbremake.player.potion.Potion;
import me.carscupcake.sbremake.player.potion.PotionEffect;
import me.carscupcake.sbremake.player.skill.ISkill;
import me.carscupcake.sbremake.util.gui.InputGui;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public enum CentauriToyBox {
    CookieJar(new ItemBuilder(Material.COOKIE)
            .setName("§6Cookie Jar")
            .addLore("""
                    §8I'd give you a cooki, but i ate it.
                    
                    §7Reach into the depths of the mysterious jar to claim free infinite §6Booster Cookies§7!
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            player.sendMessage("§cBooster cookies are not implemented yet!");
        }
    },
    PotionSeller(new ItemBuilder(Material.POTION)
            .setName("§6Potion Seller")
            .addLore("""
                    §8Why respect Knights when my potions can do anything they can?
                    
                    §7Brave the eponymous Potion Seller's barrage of beat-down to earn infinite §cGood Potion §7effects!
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            long ms = (long) (14 * 24 * 60 * 60 * 1000) + System.currentTimeMillis();
            giveGodPotionEffect(player, ms);
        }
    },
    MaxItems(new ItemBuilder(Material.DIAMOND_PICKAXE)
            .setName("§6Max Items")
            .addLore("""
                    §8The Great Sword begotten in the dragon's breath...
                    
                    §7Utilise channeled magic to imbue your items with §5maximum §7power!
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {

        }
    },
    DuplicateItem(new ItemBuilder(Material.GOLD_BLOCK)
            .setName("§6Duplicate Item")
            .addLore("""
                    §8 The original impossible to discern amongst them.
                    
                    §7Apply the Gemino Curse to the item you are holding causing it to duplicate!
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            var holding = player.getSbItemInHand(PlayerHand.MAIN);
            player.addItem(holding);
        }
    },
    MaxCollections(new ItemBuilder(Material.PAINTING)
            .setName("§6Max Collections")
            .addLore("""
                    §8Gotta collect em all.
                    
                    §7Cheat. Pure and simple, cheating!
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            for (var collection : player.getCollections().values()) {
                var total = 0;
                for (int i = collection.getLevel() ; i < collection.getMaxLevel() ; i++) {
                    total += collection.getLevelProgress()[i];
                }
                collection.addProgress(total);
            }
        }
    },
    MaxSkills(new ItemBuilder(Material.DIAMOND_SWORD)
            .setName("§6Max Skills")
            .addLore("""
                    §8Gotta collect em all.
                    
                    §7Cheat. Pure and simple, cheating!
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            for (var skill : player.getSkills().values()) {
                var total = 0;
                for (int i = skill.getLevel() ; i < skill.getMaxLevel() ; i++) {
                    total += ISkill.nextLevelXp[i];
                }
                skill.addXp(total);
            }
        }
    },
    MaxSlayers(new ItemBuilder(Material.ROTTEN_FLESH)
            .setName("§6Max Slayers")
            .addLore("""
                    §8Gotta collect em all.
                    
                    §7Impress Maddox!
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            for (var slayer : player.getSlayers().values()) {
                var total = 0;
                for (int i = slayer.getLevel() ; i < slayer.getSlayer().getMaxLevel() ; i++) {
                    total += slayer.getSlayer().requiredXp(i);
                }
                slayer.addXp(total);
            }
        }
    },
    AddHotmExp(new ItemBuilder(Material.PRISMARINE_CRYSTALS)
            .setName("§50k HOTM Exp")
            .addLore("""
                    §8The power of a pickaxe.
                    
                    §7Impress the dwarves!
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            player.getHotm().addXp(50_000);
        }
    },
    AddPowders(new ItemBuilder(Material.GUNPOWDER)
            .setName("§aBig ol' chunk o' Powder!")
            .addLore("""
                    §8Mindcraft
                    
                    §ePowder powder powder! Get 2,000,000 of each powder.
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            for (var powder : Powder.values())
                player.getPowder().put(powder, 2_000_000 + player.getPowder().getOrDefault(powder, 0));
        }
    },
    AddGems(new ItemBuilder(Material.EMERALD)
            .setName("§a5k Gems")
            .addLore("""
                    §8Get rich quick.
                    
                    §7Set your gem purse to 5,000.
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            player.sendMessage("§cGems are not implemented yet!");
        }
    },
    SetRngMeter(new ItemBuilder(Material.EXPERIENCE_BOTTLE)
            .setName("§aSet RNG Meter Progress")
            .addLore("""
                    §8The power of the RNG in the palm of my hand.
                    
                    §7Set the progress of the RNG Meter to a custom one.
                    
                    §eClick to claim!
                    """)
            .build()) {
        private final InputGui input = new InputGui(List.of("", "^^^^^^", "Enter your", "amount!"));
        @Override
        public void executeToy(SkyblockPlayer player) {
            input.show(player, InputGui.SIMPLE_INTEGER_FORMAT, i -> {
                if (i == null) {
                    player.sendMessage("Not a valid number");
                    return;
                }
                if (i < 0) {
                    player.sendMessage("Number must be positive");
                    return;
                }
                for (var slayer : player.getSlayers().values()) {
                    slayer.getMeter().setRngMeterXp(i);
                }
            });
        }
    },
    ClearRngMeter(new ItemBuilder(Material.GLASS_BOTTLE)
            .setName("§aClear RNG Meter Progress")
            .addLore("""
                    §8The power of the RNG in the palm of my hand.
                    
                    §7Clears the progress of every RNG Meter.
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {

            for (var slayer : player.getSlayers().values()) {
                slayer.getMeter().setRngMeterXp(0);
            }
        }
    },
    FillAgathaRewards(new ItemBuilder(Material.OAK_LOG)
            .setName("§aFill Agatha's Rewards")
            .addLore("""
                    §8Leave Agatha in awe.
                    
                    §7Gives you rewards for Agatha as if you've participated in 25 §dStarlyn Sister§7's contents.
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            player.sendMessage("§cAgatha's rewards are not implemented yet!");
        }
    },
    ForestWhisper(new ItemBuilder(Material.SEA_LANTERN)
            .setName("§aBig ol' chunk o' Whisper!")
            .addLore("""
                    §8To turn your tears to roses.
                    
                    §7Get 1,000,000 of Forest Whispers.
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            player.sendMessage("§cWhisper is not implemented yet!");
        }
    },
    Shards(new ItemBuilder(Material.PRISMARINE_SHARD)
            .setName("§aShards!")
            .addLore("""
                    §8A little bit of this, a little bit of that.
                    
                    §7Get 10 of each available Shard.
                    
                    §eClick to claim!
                    """)
            .build()) {
        @Override
        public void executeToy(SkyblockPlayer player) {
            player.sendMessage("§cShards are not implemented yet!");
        }
    };

    public static void giveGodPotionEffect(SkyblockPlayer player, long ms) {
        synchronized (player.getPotionEffects()) {
            for (IPotion potion : Potion.values()) {
                if (potion.isBuff() && !potion.isInstant()) {
                    player.startPotionEffect(new PotionEffect(potion, ms++, potion.getMaxLevel()));
                }
            }
            player.startPotionEffect(new PotionEffect(Potion.JumpBoost, ms, (byte) 6));
        }
    }

    @Getter
    private final ItemStack showItem;
    CentauriToyBox(ItemStack showItem) {
        this.showItem = showItem;
    }
    public abstract void executeToy(SkyblockPlayer player);
}
