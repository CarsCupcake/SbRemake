package me.carscupcake.sbremake.item.impl.sword;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.*;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.EntitySinusMovement;
import me.carscupcake.sbremake.util.EntityUtils;
import me.carscupcake.sbremake.util.ParticleUtils;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;

import java.util.List;
import java.util.Map;

public class InkWand implements ISbItem {
    private static final EntitySinusMovement movement = new EntitySinusMovement(0.5, 1, 0);
    private static final List<Ability> abilities = List.of(new ItemAbility<>("Ink Bomb", AbilityType.RIGHT_CLICK, event -> {
        ItemEntity item = new ItemEntity(ItemStack.of(Material.INK_SAC));
        item.setPickable(false);
        item.setNoGravity(true);
        item.setInstance(event.getPlayer().getInstance(), event.getPlayer().getPosition().add(0, 1.5, 0));
        Vec finish = event.getPlayer().getPosition().direction().normalize().mul(30);
        final Vec dir = Vec.fromPoint(finish.sub(item.getPosition())).normalize().mul(item.getPosition().distance(finish) / 40);
        movement.move(item, item::remove, 40, 0, item.getPosition(), event.getPlayer().getPosition().add(finish), new Runnable() {
            int i = 0;
            @Override
            public void run() {
                i++;
                Pos old = item.getPosition().sub(dir).sub(0, movement.calculateOffset(i, 40) - movement.calculateOffset(i - 1, 40), 0);
                List<Entity> list = EntityUtils.getEntitiesInLine(old, item.getPosition(), item.getInstance())
                        .stream().filter(entity -> entity instanceof SkyblockEntity).toList();
                ParticleUtils.spawnParticle(item.getInstance(),item.getPosition(), Particle.SQUID_INK, 1);
                if (list.isEmpty()) return;
                item.remove();
                for (Entity e : list) {
                    SkyblockEntity entity = (SkyblockEntity) e;
                    entity.mageDamage(event.player(), 10_000d, 1d);
                }
            }
        });
    }, new Lore("§7Shoot an ink bomb in front of you dealing§a %dmg% §7damage and giving blindness!", Map.of("%dmg%", new Lore.AbilityDamagePlaceholder(10_000, 1d))), new ManaRequirement<>(60), new CooldownRequirement<>(30)));

    @Override
    public String getId() {
        return "INK_WAND";
    }

    @Override
    public String getName() {
        return "Ink Wand";
    }

    @Override
    public Material getMaterial() {
        return Material.STICK;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return abilities;
    }

    @Override
    public double getStat(Stat stat) {
        return switch (stat) {
            case Damage -> 130;
            case Strength -> 90;
            default -> 0;
        };
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }
}
