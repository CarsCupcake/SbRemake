package me.carscupcake.sbremake.item.impl.sword.slayer.enderman;

import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.ability.Requirement;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.event.eventBinding.PlayerDamageEntityBinding;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public record SoulcryAbility(int ferocity) implements Consumer<PlayerInteractEvent>, Requirement<PlayerInteractEvent> {
    public static final Map<SkyblockPlayer, Integer> items = new HashMap<>();
    public static final EventNode<Event> LISTENER =
            EventNode.all("ability.soulcry").register(new PlayerDamageEntityBinding(event -> {
                if (event.getTarget().getEntityType() != EntityType.ENDERMAN) return;
                var ferocity = items.get(event.getPlayer());
                if (ferocity == null) return;
                event.setFerocity(event.getFerocity() + ferocity);
            }));

    @Override
    public void accept(PlayerInteractEvent event) {
        var player = (SkyblockPlayer) event.getPlayer();
        items.put(player, ferocity);
        var id = event.player().getPlayerInventory().getSbItemStack(event.player().getHeldSlot()).getModifier(Modifier.ITEM_ID);
        int i = 0;
        for (var item : player.getPlayerInventory()) {
            i++;
            if (!item.sbItem().isUnstackable()) continue;
            if (item.getModifier(Modifier.ITEM_ID).equals(id)) {
                player.getPlayerInventory().setItemStack(i - 1, SbItemStack.from(item.item().withMaterial(Material.GOLDEN_SWORD)));
            }
        }
        new TaskScheduler() {
            @Override
            public void run() {
                items.remove(player);
                int i = 0;
                for (var item : player.getPlayerInventory()) {
                    i++;
                    if (!item.sbItem().isUnstackable()) continue;
                    if (item.getModifier(Modifier.ITEM_ID).equals(id)) {
                        player.getPlayerInventory().setItemStack(i - 1, SbItemStack.from(item.item().withMaterial(Material.DIAMOND_SWORD)));
                    }
                }
                new TaskScheduler() {
                    int i = 0;
                    @Override
                    public void run() {
                        switch (i){
                            case 0 -> {
                                player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.5F, 0.9F);
                                player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.5F, 1.9F);
                            }
                            case 1 -> {
                                player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.5F, 0.8F);
                                player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.5F, 1.8F);
                            }
                            default -> cancel();
                        }

                        i++;
                    }
                }.repeatTask(0, 1);

            }
        }.delayTask(4 * 20);

        new TaskScheduler() {
            int i = 1;
            @Override
            public void run() {
                switch (i){
                    case 1 -> {
                        player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.1F, 0.5F);
                        player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.5F, 1.5F);
                    }
                    case 2 -> {
                        player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.1F, 0.6F);
                        player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.5F, 1.6F);
                    }
                    case 3 -> {
                        player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.1F, 0.7F);
                        player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.5F, 1.7F);
                    }
                    case 4 -> {
                        player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.1F, 0.8F);
                        player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.5F, 1.8F);
                    }
                    case 5 -> {
                        player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.1F, 0.9F);
                        player.playSound(player.getPosition(), SoundType.ENTITY_GHAST_SCREAM, 0.5F, 1.9F);
                    }
                    default -> cancel();

                }
                i++;
            }
        }.repeatTask(0, 1);

    }

    @Override
    public boolean requirement(PlayerInteractEvent event) {
        var b = items.containsKey(event.player());
        if (b) event.player().sendMessage("§cYou already have an ability running!");
        return !b;
    }
}
