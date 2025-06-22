package me.carscupcake.sbremake.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.blocks.Mining;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.SkyblockEntityProjectile;
import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.entity.slayer.PlayerSlayer;
import me.carscupcake.sbremake.entity.slayer.SlayerQuest;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.event.*;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.item.impl.arrows.SkyblockArrow;
import me.carscupcake.sbremake.item.impl.bow.BowItem;
import me.carscupcake.sbremake.item.impl.bow.Shortbow;
import me.carscupcake.sbremake.item.impl.other.*;
import me.carscupcake.sbremake.item.impl.pets.IPet;
import me.carscupcake.sbremake.item.impl.pets.PetItem;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import me.carscupcake.sbremake.item.modifiers.potion.PotionInfo;
import me.carscupcake.sbremake.player.accessories.AccessoryBag;
import me.carscupcake.sbremake.player.hotm.HeartOfTheMountain;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.player.potion.IPotion;
import me.carscupcake.sbremake.player.potion.Potion;
import me.carscupcake.sbremake.player.skill.ISkill;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.xp.SkyblockXpTask;
import me.carscupcake.sbremake.util.*;
import me.carscupcake.sbremake.util.item.Gui;
import me.carscupcake.sbremake.util.item.InventoryBuilder;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import me.carscupcake.sbremake.util.quest.Dialog;
import me.carscupcake.sbremake.worlds.*;
import me.carscupcake.sbremake.worlds.region.Region;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.metadata.other.FishingHookMeta;
import net.minestom.server.entity.metadata.projectile.ProjectileMeta;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithBlockEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.Tool;
import net.minestom.server.network.packet.client.play.*;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Getter
@SuppressWarnings({"unused", "UnstableApiUsage"})
public class SkyblockPlayer extends Player {
    public static final Comparator<StoredPet> PET_COMPARATOR = (o1, o2) -> {
        int rarity = o2.getRarity().ordinal() - o1.getRarity().ordinal();
        if (rarity != 0) return rarity;
        int level = o2.getLevel() - o1.getLevel();
        if (level != 0) return level;
        return (int) (o2.getXp() - o1.getXp());
    };
    public static final ConfigSection.Data<StoredPet> STORED_PET_DATA = new ConfigSection.ClassicGetter<>(jsonElement -> {
        JsonObject o = jsonElement.getAsJsonObject();
        return new StoredPet(IPet.pets.get(o.get("type").getAsString()), o.get("xp").getAsDouble(), ItemRarity.valueOf(o.get("rarity").getAsString()), o.has("petItem") ? ((PetItem) SbItemStack.raw(o.get("petItem").getAsString())) : null, o.get("petCandy").getAsInt(), UUID.fromString(o.get("uuid").getAsString()));
    }, storedPet -> {
        JsonObject o = new JsonObject();
        o.addProperty("type", storedPet.getPet().getId());
        o.addProperty("xp", storedPet.getXp());
        o.addProperty("rarity", storedPet.getRarity().name());
        if (storedPet.getPetItem() != null) o.addProperty("petItem", storedPet.getPetItem().getId());
        o.addProperty("petCandy", storedPet.getPetCandyUsed());
        o.addProperty("uuid", storedPet.getUuid().toString());
        return o;
    });
    public static final ConfigSection.Data<List<StoredPet>> STORED_PET_LIST_DATA = new ConfigSection.ClassicGetter<>(jsonElement -> {
        List<StoredPet> storedPets = new ArrayList<>();
        for (JsonElement e : jsonElement.getAsJsonArray())
            storedPets.add(STORED_PET_DATA.get(e, null));
        return storedPets;

    }, storedPet -> {
        JsonArray array = new JsonArray();
        for (StoredPet pets : storedPet)
            STORED_PET_DATA.set(array, null, pets);
        return array;
    });
    private static final EventNode<Event> PLAYER_NODE = EventNode.all("player.events").addListener(EntityAttackEvent.class, event -> {
        final Entity source = event.getEntity();
        final Entity entity = event.getTarget();

        if (entity instanceof SkyblockPlayer target) {
            if (source.getEntityType() == EntityType.PLAYER) return;
            if (!(source instanceof SkyblockEntity sbEntity)) return;
            target.damage(sbEntity);
        }
    }).addListener(PlayerPacketEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        //Packet Logger
                /*if (!(event.getPacket() instanceof ClientChunkBatchReceivedPacket || event.getPacket() instanceof ClientPlayerPositionPacket || event.getPacket() instanceof ClientPlayerRotationPacket || event.getPacket() instanceof ClientPlayerPositionAndRotationPacket))
                    System.out.println("IN:" + event.getPacket());*/
        if (event.getPacket() instanceof ClientTeleportConfirmPacket(int teleportId)) {
            if (player.spawnTeleportId == teleportId) {
                player.scheduler().buildTask(() -> player.setNoGravity(false)).delay(TaskSchedule.tick(5)).schedule();
                player.spawnTeleportId = -1;
            }
        }
        if (event.getPacket() instanceof ClientAnimationPacket(PlayerHand hand)) {
            if (hand == PlayerHand.MAIN) {
                long time = System.currentTimeMillis();
                long delta = time - player.lastAttack;
                Pos eyePos = player.getPosition().add(0, player.getEyeHeight(), 0);
                Set<Entity> entities = EntityUtils.getEntitiesInLine(eyePos, eyePos.add(player.getPosition().direction().normalize().mul(player.getStat(Stat.SwingRange)).asPosition()), player.getInstance());
                double low = Double.MAX_VALUE;
                SkyblockEntity entity = null;
                for (Entity e : entities) {
                    if (e instanceof SkyblockEntity sbEntity) {
                        double distance = sbEntity.getDistance(eyePos);
                        if (distance < low) {
                            low = distance;
                            entity = sbEntity;
                        }
                    }
                }
                if (entity != null && EntityUtils.blocksInSight(player.getInstance(), eyePos, eyePos.direction(), low)) {
                    entity = null;
                }
                        /*Entity result = player.getLineOfSightEntity(player.getStat(Stat.SwingRange), entity -> entity.getEntityType() != EntityType.PLAYER && entity instanceof LivingEntity);
                        SkyblockEntity entity = (result instanceof SkyblockEntity sb) ? sb : null;*/
                MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, entity, PlayerInteractEvent.Interaction.Left));
                if (delta >= player.attackCooldown()) {
                    if (entity != null && entity.getEntityType() != EntityType.PLAYER) {
                        player.lastAttack = time;
                        entity.damage(player);
                        return;
                    }
                }
                SbItemStack item = player.getSbItemInHand(PlayerHand.MAIN);
                if (item == null) return;
                if (item.sbItem() instanceof Shortbow shortbow && player.shortbowTask == null) {
                    for (Requirement requirement : item.sbItem().requirements())
                        if (!requirement.canUse(player, item.item())) {
                            player.sendMessage("§cYou cannot use this item!");
                            return;
                        }
                    if (delta < shortbow.getShortbowCooldown(player.getStat(Stat.AttackSpeed, true))) return;
                    player.lastAttack = time;
                    SkyblockPlayerArrow.shootBow(player, 1000L, item, (SkyblockArrow) SbItemStack.base(Material.ARROW).sbItem());
                    player.bowStartPull = -1;
                    return;
                }
            }
            return;
        }
        if (event.getPacket() instanceof ClientHeldItemChangePacket) {
            player.lastInteractPotion = false;
            player.bowStartPull = -1;
            if (player.shortbowTask != null) {
                player.shortbowTask.cancel();
                player.shortbowTask = null;
            }
        }
        if (event.getPacket() instanceof ClientPlayerDiggingPacket packet) {
            if (packet.status() == ClientPlayerDiggingPacket.Status.UPDATE_ITEM_STATE) {
                player.lastInteractPotion = false;
                if (player.bowStartPull < 0) return;
                SbItemStack item = player.getSbItemInHand(PlayerHand.MAIN);
                if (!(item.sbItem() instanceof BowItem)) return;
                long chargingTime = System.currentTimeMillis() - player.bowStartPull;
                player.bowStartPull = -1;
                if (chargingTime < 0) return;
                for (Requirement requirement : item.sbItem().requirements())
                    if (!requirement.canUse(player, item.item())) {
                        player.sendMessage("§cYou cannot use this item!");
                        return;
                    }
                SkyblockPlayerArrow.shootBow(player, chargingTime, item, (SkyblockArrow) SbItemStack.base(Material.ARROW).sbItem());
            }
            if (packet.status() == ClientPlayerDiggingPacket.Status.STARTED_DIGGING) {
                MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, packet.blockPosition(), packet.blockFace(), PlayerInteractEvent.Interaction.Left));
                if (!player.getWorldProvider().useCustomMining()) {
                    Block block = player.getInstance().getBlock(packet.blockPosition());
                    double hardness = block.registry().hardness();
                    if (hardness > 0) {
                        SbItemStack sbItem = player.getSbItemInHand(PlayerHand.MAIN);
                        Tool tool = sbItem.item().get(DataComponents.TOOL);
                        if (tool != null) {
                            for (Tool.Rule rule : tool.rules()) {
                                if (rule.blocks().contains(block)) {
                                    if (rule.speed() == null) continue;
                                    double mult = rule.speed();
                                    int lvl = sbItem.getEnchantmentLevel(NormalEnchantments.Efficiency);
                                    if (lvl > 0) mult += Math.pow(lvl, 2) + 1;
                                    me.carscupcake.sbremake.player.potion.PotionEffect effect = player.getPotionEffect(Potion.Haste);
                                    if (effect != null) {
                                        mult *= 0.2 * effect.amplifier() + 1;
                                    }
                                    if (player.getInstance().getBlock(player.getPosition()).isLiquid()) mult /= 5;
                                    if (!player.isOnGround()) mult /= 5;
                                    double damage = mult / hardness;
                                    damage /= 30;
                                    if (damage > 1) {
                                        Block b = player.getInstance().getBlock(packet.blockPosition());
                                        boolean log = false;
                                        for (Log l : Log.logs)
                                            if (Objects.requireNonNull(l.block().registry().material()).equals(b.registry().material())) {
                                                log = true;
                                                break;
                                            }
                                        if (player.getInstance().breakBlock(player, packet.blockPosition(), packet.blockFace(), true)) {
                                            event.setCancelled(true);
                                            player.playSound(log ? SoundType.BLOCK_WOOD_BREAK : SoundType.BLOCK_STONE_BREAK, Sound.Source.BLOCK, 1, 1);
                                        }
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            return;
        }
        if (event.getPacket() instanceof ClientUseItemPacket packet) {
            if (packet.hand() != PlayerHand.MAIN) return;
            SbItemStack item = player.getSbItemInHand(PlayerHand.MAIN);
            if (item.sbItem() instanceof BowItem) {
                if (item.sbItem() instanceof Shortbow shortbow) {
                    for (Requirement requirement : item.sbItem().requirements())
                        if (!requirement.canUse(player, item.item())) {
                            player.sendMessage("§cYou cannot use this item!");
                            return;
                        }
                    long shootCd = shortbow.getShortbowCooldown(player.getStat(Stat.AttackSpeed, true));
                    player.lastShortbowKeepAlive = System.currentTimeMillis();
                    if (player.shortbowTask != null) {
                        return;
                    }
                    player.makeShortbowTask(shootCd, item);
                    return;
                }
                player.bowStartPull = System.currentTimeMillis();
                return;
            }
            long now = System.currentTimeMillis();
            long delta = now - player.lastInteractPacket;
            if (delta < 100) return;
            player.lastInteractPacket = now;
            MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, PlayerInteractEvent.Interaction.Right));
            if (item.sbItem().getType() == ItemType.Potion) {
                if (!player.lastInteractPotion) player.lastInteractPotion = true;
                else {
                    player.lastInteractPotion = false;
                    player.setItemInHand(PlayerHand.MAIN, SbItemStack.base(Material.GLASS_BOTTLE).item());
                    PotionInfo info = item.getModifier(me.carscupcake.sbremake.item.modifiers.Modifier.POTION);
                    if (info != null) {
                        for (PotionInfo.PotionEffect ef : info.effects()) {
                            {
                                me.carscupcake.sbremake.player.potion.PotionEffect effect = new me.carscupcake.sbremake.player.potion.PotionEffect(ef.potion(), System.currentTimeMillis() + (ef.durationTicks() * 50), ef.level());
                                player.startPotionEffect(effect);
                            }
                        }
                    }
                }
            }
            return;
        }

        if (event.getPacket() instanceof ClientPlayerBlockPlacementPacket packet) {
            if (packet.hand() != PlayerHand.MAIN) return;
            long now = System.currentTimeMillis();
            if (now - player.lastInteractPacket < 100) return;
            player.lastInteractPacket = now;
            MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, packet.blockPosition(), packet.blockFace(), PlayerInteractEvent.Interaction.Right));
        }

        if (event.getPacket() instanceof ClientInteractEntityPacket packet) {
            if (packet.type() instanceof ClientInteractEntityPacket.Attack) {
                if (player.getDialog() != null) return;
                AbstractNpc npc = AbstractNpc.npcs.get(packet.targetId());
                if (npc != null) {
                    if (npc.getInteraction() != null)
                        npc.getInteraction().interact(player, PlayerInteractEvent.Interaction.Left);
                }
                return;
            }
            long now = System.currentTimeMillis();
            if (now - player.lastInteractPacket < 100) return;
            player.lastInteractPacket = now;
            AbstractNpc npc = AbstractNpc.npcs.get(packet.targetId());
            if (npc != null) {
                if (player.getDialog() != null) return;
                if (npc.getInteraction() != null)
                    npc.getInteraction().interact(player, PlayerInteractEvent.Interaction.Right);
            } else if (((SkyblockPlayer) event.getPlayer()).getSbItemInMainHand().item().material() != Material.FISHING_ROD)
                MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, player.getInstance().getEntities().stream().filter(entity -> entity.getEntityId() == packet.targetId()).findFirst().orElse(null), PlayerInteractEvent.Interaction.Right));
        }

    }).addListener(ProjectileCollideWithBlockEvent.class, event -> {
        if (event.getEntity() instanceof SkyblockPlayerFishingBobber && event.getBlock() == Block.WATER) return;
        event.getEntity().remove();
        event.setCancelled(true);
    }).addListener(ProjectileCollideWithEntityEvent.class, event -> {
        if (event.getEntity().getEntityMeta() instanceof ProjectileMeta projectileMeta) {

            Entity shooter = projectileMeta.getShooter();
            Entity target = event.getTarget();
            event.getEntity().remove();

            if (!(target instanceof SkyblockEntity) && !(target instanceof SkyblockPlayer)) {
                event.setCancelled(true);
                return;
            }

            if (shooter instanceof SkyblockPlayer && target instanceof SkyblockPlayer) return;

            if (shooter instanceof SkyblockPlayer) {
                if (!(event.getEntity() instanceof SkyblockPlayerArrow projectile)) {
                    Main.LOGGER.warn("Illegal Arrow Found!");
                    event.getEntity().remove();
                    return;
                }
                ((SkyblockEntity) target).damage(projectile);
                return;
            }

            if (target instanceof SkyblockPlayer player) {
                if (!(event.getEntity() instanceof SkyblockEntityProjectile projectile)) {
                    event.setCancelled(true);
                    return;
                }
                player.damage(projectile);
            }
        } else if (event.getEntity() instanceof SkyblockPlayerFishingBobber fishingBobber) {
            var meta = (FishingHookMeta) fishingBobber.getEntityMeta();
            //TODO Fish
        }

    }).addListener(PickupItemEvent.class, event -> {
        final Entity entity = event.getLivingEntity();
        if (entity instanceof SkyblockPlayer player) {
            // Cancel event if player does not have enough inventory space
            final ItemStack itemStack = event.getItemEntity().getItemStack();
            SbItemStack sbItemStack = SbItemStack.from(itemStack);
            event.setCancelled(!player.addItem(sbItemStack.update()));
        }
    }).addListener(InventoryPreClickEvent.class, event -> {
        if (((SkyblockPlayer) event.getPlayer()).getGui() != null) return;
        if (event.getSlot() == 8) {
            event.setCancelled(true);
            ((SkyblockPlayer) event.getPlayer()).openSkyblockMenu();
            return;
        }
        if (event.getInventory() == event.getPlayer().getInventory()) return;
        if (event.getSlot() < 41 || event.getSlot() > 44) {
            if (event.getClick() instanceof Click.LeftShift || event.getClick() instanceof Click.RightShift) {
                SbItemStack clicked = SbItemStack.from(event.getClickedItem());
                if (clicked == SbItemStack.AIR) return;
                int slot = getSlot(clicked.sbItem().getType());
                if (slot < 0) {
                    event.setCancelled(event.getClickedItem().material().isArmor());
                    return;
                }
                if (!event.getPlayer().getInventory().getItemStack(slot).isAir()) {
                    return;
                }
                event.setCancelled(true);
                event.getPlayer().getInventory().setItemStack(slot, clicked.item());
                event.getPlayer().getInventory().setItemStack(event.getSlot(), ItemStack.of(Material.AIR));
                ((SkyblockPlayer) event.getPlayer()).recalculateArmor();
            }
            return;
        }
        SbItemStack cursor = SbItemStack.from(event.getPlayer().getInventory().getCursorItem());
        if (cursor == SbItemStack.AIR) {
            return;
        }
        if (getSlot(cursor.sbItem().getType()) != event.getSlot()) {
            event.setCancelled(true);
        }
    }).addListener(InventoryClickEvent.class, event -> {
        if (event.getInventory() == event.getPlayer().getInventory() && (event.getClickType() == ClickType.SHIFT_CLICK || (event.getSlot() >= 41 && event.getSlot() <= 44))) {
            MinecraftServer.getSchedulerManager().buildTask(() -> ((SkyblockPlayer) event.getPlayer()).recalculateArmor()).delay(1, TimeUnit.SERVER_TICK).schedule();
        }
    }).addListener(PlayerDisconnectEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        player.save();
        if (player.getPet() != null) player.getPet().getPet().despawnPet(player, player.getPet());
        System.gc();
    }).addListener(PlayerRespawnEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        event.setRespawnPosition(player.getWorldProvider().spawn());
        player.setSbHealth(player.getMaxSbHealth());
        player.sendPacket(new PlayerAbilitiesPacket(player.getGameMode() == GameMode.CREATIVE ? PlayerAbilitiesPacket.FLAG_ALLOW_FLYING : (byte) 0, 0.05f, (float) (0.1 * (player.getStat(Stat.Speed) / 100d))));

    }).addListener(ItemDropEvent.class, event -> {
        SbItemStack stack = SbItemStack.from(event.getItemStack());
        if (stack.sbItem() instanceof SkyblockMenu) {
            event.setCancelled(true);
            ((SkyblockPlayer) event.getPlayer()).openSkyblockMenu();
        }
        //Todo make item drop
    }).addListener(PlayerMoveEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        for (Launchpad launchpad : player.getWorldProvider().getLaunchpads())
            if (launchpad.inBox(player)) launchpad.launch(player);

    }).addListener(PlayerGameModeChangeEvent.class, event -> MinecraftServer.getSchedulerManager().buildTask(() -> event.getPlayer().sendPacket(new PlayerAbilitiesPacket(event.getNewGameMode() == GameMode.CREATIVE || event.getNewGameMode() == GameMode.SPECTATOR ? PlayerAbilitiesPacket.FLAG_ALLOW_FLYING : (byte) 0, (float) (0.1 * (((SkyblockPlayer) event.getPlayer()).getStat(Stat.Speed) / 100d)), (float) (0.1 * (((SkyblockPlayer) event.getPlayer()).getStat(Stat.Speed) / 100d))))).delay(TaskSchedule.tick(2)).schedule()).addListener(PlayerMoveEvent.class, event -> {
        if (event.getPlayer().getGameMode().invulnerable()) return;
        if (event.getNewPosition().y() <= -64) ((SkyblockPlayer) event.getPlayer()).setSbHealth(0);
    }).addListener(PlayerInteractEvent.class, event -> {
        if (event.player().isSneaking()) return;
        if (event.block() == null) return;
        if (event.interaction() != PlayerInteractEvent.Interaction.Right) return;
        if (event.player().instance.getBlock(event.block()).registry().material() == Material.CRAFTING_TABLE) {
            Recipe.openCraftingGui(event.player());
        }
    }).addListener(PlayerInteractEvent.class, event -> {
        if (event.interaction() != PlayerInteractEvent.Interaction.Right) return;
        if (event.player().playerFishingBobber != null) {
            event.player().playerFishingBobber.remove();
            event.player().playerFishingBobber = null;
            event.player().playSound(SoundType.ENTITY_FISHING_BOBBER_RETRIEVE.create(1));
            return;
        }
        var item = event.player().getSbItemInHand(PlayerHand.MAIN);
        if (!(item.sbItem() instanceof IFishingRod fishingRod)) return;
        var bobber = new SkyblockPlayerFishingBobber(event.player());
        bobber.setInstance(event.player().instance, event.player().position.add(0, event.player().getEyeHeight(), 0).add(event.player().position.direction().normalize().mul(0.3)));
        bobber.setVelocity(event.player().getPosition().direction().normalize().mul(20));
        event.player().playerFishingBobber = bobber;
        event.player().playSound(SoundType.ENTITY_FISHING_BOBBER_THROW.create(1));
    }).addListener(PlayerDeathEvent.class, event -> {
        event.setChatMessage(Component.text("§c%s §7You Died!".formatted(Characters.Skull)));
        event.setDeathText(Component.text("You Died!"));
    });
    private static final UUID speedUUID = UUID.randomUUID();
    private static final Set<Class<? extends ISbItem>> COIN_ITEMS = Set.of(CoinItem1.class, CoinItem10.class, CoinItem100.class, CoinItem1000.class, CoinItem2000.class, CoinItem5000.class);
    public static Task regenTask;
    @Getter
    public final Sidebar sidebar = new Sidebar(Component.text("§6§lSKYBLOCK"));
    private final ActionBar actionBar = new ActionBar(this);
    private final Map<FullSetBonus, Integer> fullSetBonuses = new HashMap<>();
    @Getter
    private final List<me.carscupcake.sbremake.item.collections.Collection> collections = new LinkedList<>();
    private final Map<Skill, ISkill> skills = new HashMap<>();
    @Getter
    private final List<String> tags;
    private final Map<Powder, Integer> powder = new HashMap<>();
    @Getter
    private final HeartOfTheMountain hotm;
    @Getter
    private final PlayerModifierList temporaryModifiers = new PlayerModifierList();
    @Getter
    private final Deque<Pair<SbItemStack, Integer>> sellHistory = new ArrayDeque<>() {
        @Override
        public void push(@NotNull Pair<SbItemStack, Integer> sbItemStackIntegerPair) {
            super.push(sbItemStackIntegerPair);
            //We track up to 15 items
            //We track up to 15 items
            if (size() > 15) removeLast();
        }
    };
    @Getter
    private final ArrayList<StoredPet> pets = new ArrayList<>();
    @Getter
    private final Map<ISlayer, PlayerSlayer> slayers = new HashMap<>();
    @Getter
    private final CountMap<Essence> essence = new CountMap<>();
    @Getter
    private final UUID configId;
    @Getter
    private final SkyblockPlayerInventory playerInventory = new SkyblockPlayerInventory(this);
    @Getter
    private final AccessoryBag accessoryBag;
    @Setter
    public UpdateHealthPacket lastHealthPacket = null;
    @Getter
    public double mana = 100;
    @Getter
    @Setter
    public Mining blockBreakScheduler = null;
    private boolean lastInteractPotion = false;
    private SkyblockWorld.WorldProvider worldProvider = null;
    @Getter
    private double sbHealth;
    @Getter
    private long bowStartPull = -1;
    private long lastAttack = System.currentTimeMillis();
    private long lastShortbowKeepAlive = System.currentTimeMillis();
    private Task shortbowTask = null;
    @Getter
    private String defenseString = null;
    private int defenseStringTicks = 0;
    @Getter
    private boolean notEnoughMana = false;
    private int notEnoughManaTicks = 0;
    private boolean oftick = false;
    private long lastInteractPacket = 0;
    @Getter
    @Setter
    private Function<SkyblockPlayer, String[]>[] scoreboardDisplay = DefaultScoreboard.values();
    @Getter
    @Setter
    @Range(from = 0, to = Long.MAX_VALUE)
    private double coins;
    @Getter
    private Gui gui = null;
    @Getter
    @Setter
    private Region region = null;
    @Getter
    @Setter
    private Dialog dialog = null;
    @Getter
    @Setter
    private String powderString = null;
    @Getter
    @Setter
    private boolean onLaunchpad = false;
    @Getter
    @Setter
    private SkyblockWorld previous = null;
    @Getter
    @Setter
    private boolean warping;
    @Getter
    @Setter
    private TaskScheduler petTask = null;
    @Getter
    @Setter
    private StoredPet pet;
    @Getter
    @Setter
    private double absorption;
    @Getter
    @Setter
    private SlayerQuest slayerQuest = null;
    @Getter
    private volatile TreeSet<me.carscupcake.sbremake.player.potion.PotionEffect> potionEffects = new TreeSet<>(Comparator.comparingLong(me.carscupcake.sbremake.player.potion.PotionEffect::expiration));
    @Getter
    @Setter
    private int zealotPity;
    @Getter
    @Setter
    private boolean inWorldTransfer;
    @Getter
    @Setter
    private SkyblockPlayerFishingBobber playerFishingBobber = null;
    @Getter
    private long skyblockXp = 0;

    /**
     * This is to set up stuff, when the player gets spawned (respawn or server join)
     */
    private int spawnTeleportId = 0;
    private double lastAbsorbtion = 0;
    private double lastSpeed = 0;
    private boolean noSave = false;

    public SkyblockPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile, UUID configId) {
        super(playerConnection, gameProfile);
        super.callSpawn = false;
        this.inventory = playerInventory;
        this.configId = configId;
        ConfigFile file = new ConfigFile("defaults", this);
        zealotPity = file.get("zealotPity", ConfigSection.INTEGER, 0);
        coins = file.get("coins", ConfigSection.DOUBLE, 0d);
        tags = new ArrayList<>(List.of(file.get("tags", ConfigSection.STRING_ARRAY, new String[0])));
        for (Skill skill : Skill.values()) {
            var skillInstance = skill.instantiate(this);
            skills.put(skill, skillInstance);
            initSkyblockXpTask(skillInstance);
        }
        ConfigSection section = file.get("powder", ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        for (Powder p : Powder.values()) {
            powder.put(p, section.get(p.getId(), ConfigSection.INTEGER, 0));
        }
        ConfigSection potions = file.get("potions", ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        potions.forEntries((s, section1) -> {
            me.carscupcake.sbremake.player.potion.PotionEffect effect = new me.carscupcake.sbremake.player.potion.PotionEffect(s, section1);
            potionEffects.add(effect);
        });
        for (ISlayer s : Slayers.values()) {
            slayers.put(s, new PlayerSlayer(this, s));
        }
        sbHealth = getMaxSbHealth();
        setNoGravity(true);
        Reflections reflections = new Reflections("me.carscupcake.sbremake.item.collections.impl");
        for (Class<? extends me.carscupcake.sbremake.item.collections.Collection> clazz : reflections.getSubTypesOf(me.carscupcake.sbremake.item.collections.Collection.class)) {
            try {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) continue;
                Constructor<? extends me.carscupcake.sbremake.item.collections.Collection> constructor = clazz.getConstructor(SkyblockPlayer.class);
                var collection = constructor.newInstance(this);
                collections.add(collection);
                initSkyblockXpTask(collection);
            } catch (Exception e) {
                kick("§cError while loading");
                throw new RuntimeException(e);
            }
        }
        this.accessoryBag = new AccessoryBag(new ConfigFile("accsessoryBag", this), 3);
        this.hotm = new HeartOfTheMountain(this);
        ConfigFile f = new ConfigFile("pets", this);
        pets.addAll(f.get("stored", STORED_PET_LIST_DATA, new ArrayList<>()));
        if (f.get("equipped", ConfigSection.INTEGER, -1) >= 0) {
            pet = pets.get(f.get("equipped", ConfigSection.INTEGER));
        }
    }

    private void initSkyblockXpTask(SkyblockXpTask xpTask) {
        skyblockXp += xpTask.getTotalXp();
    }

    public void addSkyblockXp(@Range(from = 0, to = Long.MAX_VALUE) long xp) {
        var previous = this.skyblockXp;
        skyblockXp += xp;
        if (previous / 100 < skyblockXp / 100) {
            //TODO: Upgrade Message
        }

    }

    public int getSkyblockLevel() {
        return Math.toIntExact(skyblockXp / 100);
    }

    private static int getSlot(ItemType type) {
        return switch (type) {
            case Helmet -> EquipmentSlot.HELMET.armorSlot();
            case Chestplate -> EquipmentSlot.CHESTPLATE.armorSlot();
            case Leggings -> EquipmentSlot.LEGGINGS.armorSlot();
            case Boots -> EquipmentSlot.BOOTS.armorSlot();
            default -> -1;
        };
    }

    public static void tickLoop() {
        MinecraftServer.getGlobalEventHandler().addChild(PLAYER_NODE);
        regenTask = MinecraftServer.getSchedulerManager().buildTask(() -> Audiences.players().forEachAudience(audience -> {
            SkyblockPlayer player = (SkyblockPlayer) audience;
            if (!player.oftick) {
                player.oftick = true;
                double maxSbHp = player.getMaxSbHealth();
                if (player.getSbHealth() != maxSbHp) {
                    double healthGained = (1.5 + maxSbHp / 100d) * player.getStat(Stat.HealthRegen) / 100;
                    HealthRegenEvent event = new HealthRegenEvent(player, healthGained);
                    MinecraftServer.getGlobalEventHandler().call(event);
                    player.addSbHealth((float) event.getRegenAmount());
                }
                double maxHealth = getMaxHearts(player.getMaxSbHealth());
                if (maxHealth != player.getMaxHealth()) {
                    player.getAttribute(Attribute.MAX_HEALTH).setBaseValue((float) maxHealth);
                    player.updateHpBar();
                } else if (player.lastAbsorbtion != player.absorption) {
                    player.updateHpBar();
                }

            } else player.oftick = false;
            long now = System.currentTimeMillis();
            if (!player.potionEffects.isEmpty()) {
                while (!player.potionEffects.isEmpty() && now > player.potionEffects.getFirst().expiration()) {
                    me.carscupcake.sbremake.player.potion.PotionEffect effect = player.potionEffects.pollFirst();
                    assert effect != null;
                    effect.potion().stop(player, effect.amplifier());
                }
            }
            double speed = player.getStat(Stat.Speed);
            if (speed != player.lastSpeed) {
                player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue((float) (0.1 * (speed / 100d)));
                player.getAttribute(Attribute.FLYING_SPEED).setBaseValue((float) (0.1 * (speed / 100d)));
                player.sendPacket(new PlayerAbilitiesPacket(player.getGameMode() == GameMode.CREATIVE ? PlayerAbilitiesPacket.FLAG_ALLOW_FLYING : (byte) 0, 0.05f, (float) (0.1 * (speed / 100d))));
                player.lastSpeed = speed;
            }


            double maxManaPool = player.getManaPool();
            if (maxManaPool > player.getMana()) {
                double manaGained = maxManaPool * 0.02d;
                ManaRegenEvent e = new ManaRegenEvent(player, manaGained);
                MinecraftServer.getGlobalEventHandler().call(e);
                player.addMana(e.getRegenAmount() * e.getMultiplier());
            }

            ActionBarPacket packet = new ActionBarPacket(Component.text(player.actionBar.build()));
            if (player.defenseString != null) {
                player.defenseStringTicks--;
                if (player.defenseStringTicks == 0) player.defenseString = null;
            }
            if (player.notEnoughMana) {
                player.notEnoughManaTicks--;
                if (player.notEnoughManaTicks == 0) player.notEnoughMana = false;
            }
            player.sendPacket(packet);
            int lines = 0;
            Set<Sidebar.ScoreboardLine> lineCopie = new HashSet<>(player.sidebar.getLines());
            for (Function<SkyblockPlayer, String[]> display : player.scoreboardDisplay) {
                String[] l = display.apply(player);
                for (String s : l) {
                    Component text = Component.text(s);
                    String id = Integer.toString(15 - lines);
                    if (lines >= lineCopie.size())
                        player.sidebar.createLine(new Sidebar.ScoreboardLine(id, text, 15 - lines));
                    else {
                        Sidebar.ScoreboardLine line = player.sidebar.getLine(id);
                        if (line == null) {
                            System.err.println("Warning! Line " + (id) + " is null!");
                            continue;
                        }
                        if (!line.getContent().equals(text)) player.sidebar.updateLineContent(id, text);
                    }
                    lines++;
                }
            }
            for (int i = lines; i < lineCopie.size(); i++) {
                String id = Integer.toString(15 - i);
                player.sidebar.removeLine(id);
            }
        })).repeat(TaskSchedule.seconds(1)).schedule();
    }

    private static float getMaxHearts(double maxHealth) {
        float health = 0;
        if (maxHealth < 125) {
            health = 20;
        } else if (maxHealth < 165) {
            health = 22;
        } else if (maxHealth < 230) {
            health = 24;
        } else if (maxHealth < 300) {
            health = 26;
        } else if (maxHealth < 400) {
            health = 28;
        } else if (maxHealth < 500) {
            health = 30;
        } else if (maxHealth < 650) {
            health = 32;
        } else if (maxHealth < 800) {
            health = 34;
        } else if (maxHealth < 1000) {
            health = 36;
        } else if (maxHealth < 1250) {
            health = 38;
        } else if (maxHealth >= 1250) {
            health = 40;
        }
        return health;
    }

    private static float getAbsorptionHearts(double absorption) {
        if (absorption == 0) return 0;
        if (absorption <= 10) return 1;
        if (absorption <= 20) return 2;
        if (absorption <= 30) return 3;
        if (absorption <= 40) return 4;
        if (absorption <= 50) return 5;
        if (absorption <= 60) return 6;
        if (absorption <= 70) return 7;
        if (absorption <= 80) return 8;
        if (absorption <= 90) return 9;
        if (absorption <= 100) return 10;
        if (absorption <= 125) return 11;
        if (absorption <= 150) return 12;
        if (absorption <= 175) return 13;
        if (absorption <= 200) return 14;
        if (absorption <= 250) return 15;
        return 16;
    }

    /**
     * Gets the player inventory
     *
     * @return the legacy inventory
     * @deprecated Use SkyblockPlayer#getPlayerInventory()
     */
    @Override
    @Deprecated
    public @NotNull PlayerInventory getInventory() {
        return getPlayerInventory();
    }

    public boolean hasPotionEffect(IPotion potion) {
        return getPotionEffect(potion) != null;
    }

    public me.carscupcake.sbremake.player.potion.PotionEffect getPotionEffect(IPotion potion) {
        for (me.carscupcake.sbremake.player.potion.PotionEffect effect : potionEffects)
            if (effect.potion() == potion) return effect;
        return null;
    }

    public void startPotionEffect(me.carscupcake.sbremake.player.potion.PotionEffect effect) {
        if (effect.potion().isInstant()) {
            effect.potion().start(this, effect.amplifier(), 0);
            return;
        }
        me.carscupcake.sbremake.player.potion.PotionEffect e = getPotionEffect(effect.potion());
        if (e != null) {
            if (effect.amplifier() >= e.amplifier()) {
                e.potion().stop(this, e.amplifier());
                potionEffects.remove(e);
                potionEffects.add(effect);
                effect.potion().start(this, effect.amplifier(), (long) ((effect.expiration() - System.currentTimeMillis()) / 50d));
                initPotion(effect);
            }
            return;
        }
        potionEffects.add(effect);
        effect.potion().start(this, effect.amplifier(), (long) ((effect.expiration() - System.currentTimeMillis()) / 50d));
        initPotion(effect);
    }

    public void initPotion(me.carscupcake.sbremake.player.potion.PotionEffect effect) {
        Map<Stat, PlayerStatEvent.PlayerStatModifier> modifierMap = effect.potion().getStatModifiers(effect.amplifier());
        if (modifierMap != null) {
            for (Map.Entry<Stat, PlayerStatEvent.PlayerStatModifier> entry : modifierMap.entrySet()) {
                temporaryModifiers.add(entry.getKey(), entry.getValue(), Duration.ofMillis(effect.expiration() - System.currentTimeMillis()));
            }
        }
        if (effect.potion().getVanillaEffect() != null) {
            sendPacket(new EntityEffectPacket(getEntityId(), new net.minestom.server.potion.Potion(effect.potion().getVanillaEffect(), effect.amplifier() - 1, (int) ((effect.expiration() - System.currentTimeMillis()) / 50d), (byte) 0)));
        }
    }

    public void addAbsorption(double d) {
        absorption += d;
    }

    public void openPetsMenu() {
        InventoryBuilder builder = new InventoryBuilder(6, "Pets").fill(TemplateItems.EmptySlot.getItem(), 0, 9).verticalFill(0, 5, TemplateItems.EmptySlot.getItem()).fill(TemplateItems.EmptySlot.getItem(), 45, 53).verticalFill(8, 5, TemplateItems.EmptySlot.getItem());
        ArrayList<StoredPet> pets = new ArrayList<>(this.pets);
        pets.sort(PET_COMPARATOR);
        int i = 10;
        for (StoredPet pet : pets) {
            builder.setItem(i, new ItemBuilder(pet.toItem().update(this).item()).addLoreIf(() -> pet == SkyblockPlayer.this.pet, "§3 ", "§cClick to despawn.").addLoreIf(() -> pet != SkyblockPlayer.this.pet, "§3 ", "§aClick to spawn.").build());
            i++;
        }
        Gui gui = new Gui(builder.build());
        gui.setCancelled(true);
        gui.setGeneralClickEvent(event -> {
            if (event.getInventory() == event.getPlayer().getInventory()) return true;
            int row = (int) (event.getSlot() / 9d);
            if (row < 1 || row > 5) return true;
            int slotFromRow = event.getSlot() - (row * 9);
            if (slotFromRow == 0 || slotFromRow == 8) return true;
            int index = ((row - 1) * 7) - 1 + slotFromRow;
            if (index >= pets.size()) return true;
            StoredPet clickedPet = pets.get(index);
            if (event.getClick() instanceof Click.Right) {
                if (clickedPet == SkyblockPlayer.this.pet) {
                    SkyblockPlayer.this.pet.getPet().despawnPet(SkyblockPlayer.this, pet);
                    pet = null;
                }
                SkyblockPlayer.this.pets.remove(clickedPet);
                addItem(clickedPet.toItem().update(this), false);
                closeGui();
                return true;
            }
            if (SkyblockPlayer.this.pet == clickedPet) {
                SkyblockPlayer.this.pet = null;
                clickedPet.getPet().despawnPet(SkyblockPlayer.this, clickedPet);
                sendMessage("§cYou despawned your " + (clickedPet.getPet().getName()));
                closeGui();
                return true;
            }
            if (SkyblockPlayer.this.pet != null) {
                SkyblockPlayer.this.pet.getPet().despawnPet(SkyblockPlayer.this, SkyblockPlayer.this.pet);
            }
            SkyblockPlayer.this.pet = clickedPet;
            clickedPet.getPet().spawnPet(SkyblockPlayer.this, clickedPet);
            sendMessage("§aYou spawned your " + (clickedPet.getPet().getName()));
            closeGui();
            return true;
        });
        gui.showGui(this);
    }

    public void openSkyblockMenu() {
        InventoryBuilder inventoryBuilder = new InventoryBuilder(6, "Skyblock Menu").fill(TemplateItems.EmptySlot.getItem());
        PlayerSkin s = getSkin();
        ItemBuilder profileItem = new ItemBuilder(Material.PLAYER_HEAD);
        if (s != null) profileItem.setHeadTexture(s.textures());
        profileItem.setName("§aYour Skyblock Profile").addAllLore("§7View your equipment, stats,").addAllLore("§7and more!").addAllLore("§7 ");
        for (Stat stat : Stat.values())
            profileItem.addLoreRow((stat) + " §f" + (getStat(stat)) + (stat.isPercentValue() ? "%" : ""));
        inventoryBuilder.setItem(profileItem.addAllLore("§7  ").addLoreRow("§eClick to view!").build(), 13).setItem(new ItemBuilder(Material.DIAMOND_SWORD).setName("§aYour Skills").addAllLore("§7View your skills progression", "§7and rewards.", "§7 ", "§6" + (StringUtils.cleanDouble(skillsAverage())) + " Skill Avearage", "§e ", "§eClick to view!").build(), 19).setItem(new ItemBuilder(Material.PAINTING).setName("§aCollections §c§lWIP").build(), 20).setItem(new ItemBuilder(Material.BOOK).setName("§aRecipe Book §c§lWIP").build(), 21).setItem(new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdkODg1YjMyYjBkZDJkNmI3ZjFiNTgyYTM0MTg2ZjhhNTM3M2M0NjU4OWEyNzM0MjMxMzJiNDQ4YjgwMzQ2MiJ9fX0=").setName("§aSkyblock Leveling §c§lWIP").build(), 22).setItem(new ItemBuilder(Material.WRITABLE_BOOK).setName("§aQuest Log §c§lWIP").build(), 23).setItem(new ItemBuilder(Material.CLOCK).setName("§aCalendar and Events §c§lWIP").build(), 24).setItem(new ItemBuilder(Material.CHEST).setName("§aStorage §c§lWIP").build(), 25).setItem(new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYxYTkxOGMwYzQ5YmE4ZDA1M2U1MjJjYjkxYWJjNzQ2ODkzNjdiNGQ4YWEwNmJmYzFiYTkxNTQ3MzA5ODVmZiJ9fX0=").setName("§aYour Bags §c§lWIP").build(), 29).setItem(new ItemBuilder(Material.BONE).setName("§aPets §c§lWIP").build(), 30).setItem(new ItemBuilder(Material.CRAFTING_TABLE).setName("§aCrafting Table §c§lWIP").build(), 31).setItem(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherColor(new Color(0x3e05af)).setName("§aWardrobe §c§lWIP").build(), 32).setItem(new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=").setName("§aPersonal Bank §c§lWIP").build(), 33).setItem(new ItemBuilder(Material.BARRIER).setName("§cClose").build(), 49);
        Gui gui = new Gui(inventoryBuilder.build());
        gui.setCancelled(true);
        gui.getClickEvents().add(31, clickType -> {
            Recipe.openCraftingGui(SkyblockPlayer.this);
            return true;
        });
        gui.showGui(this);
    }

    public double skillsAverage() {
        int i = 0;
        double total = 0;
        for (ISkill skill : skills.values()) {
            if (skill.isCosmetic()) continue;
            i++;
            total += skill.getLevel();
        }
        return total / i;
    }

    public void save() {
        if (noSave) return;
        ConfigFile configFile = new ConfigFile("inventory", this);
        configFile.setRawElement(new JsonObject());
        for (int i = 0; i < this.getInventory().getSize(); i++) {
            SbItemStack item = getPlayerInventory().getSbItemStack(i);
            if (item == SbItemStack.AIR) continue;
            configFile.set(i + "", item, ConfigSection.ITEM);
        }
        configFile.save();
        ConfigFile defaults = new ConfigFile("defaults", this);
        defaults.set("world", this.getWorldProvider().type().getId(), ConfigSection.STRING);
        defaults.set("coins", this.coins, ConfigSection.DOUBLE);
        defaults.set("tags", tags.toArray(new String[0]), ConfigSection.STRING_ARRAY);
        defaults.set("zealotPity", zealotPity, ConfigSection.INTEGER);
        ConfigSection powders = defaults.get("powder", ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        for (Map.Entry<Powder, Integer> powderIntegerEntry : powder.entrySet()) {
            powders.set(powderIntegerEntry.getKey().getId(), powderIntegerEntry.getValue(), ConfigSection.INTEGER);
        }
        defaults.set("powder", powders, ConfigSection.SECTION);
        ConfigSection potions = new ConfigSection(new JsonObject());
        for (me.carscupcake.sbremake.player.potion.PotionEffect potionEffect : potionEffects)
            potionEffect.store(potions);
        defaults.set("potions", potions, ConfigSection.SECTION);
        defaults.save();
        for (ISkill skill : this.skills.values()) skill.save();
        collections.forEach(me.carscupcake.sbremake.item.collections.Collection::save);
        hotm.save();
        ConfigFile petsFile = new ConfigFile("pets", this);
        petsFile.set("stored", pets, STORED_PET_LIST_DATA);
        petsFile.set("equipped", (pet == null) ? -1 : pets.indexOf(pet), ConfigSection.INTEGER);
        petsFile.save();
        Main.LOGGER.info("Saved profile from {}", ((TextComponent) this.getName()).content());

        ConfigFile file = new ConfigFile("slayer", this);
        for (PlayerSlayer s : slayers.values())
            s.save(file);
        file.save();
    }

    public ISkill getSkill(Skill skill) {
        return skills.get(skill);
    }

    public void closeGui() {
        setGui(null);
    }

    public int getPowder(Powder type) {
        return powder.get(type);
    }

    public void addPowder(Powder type, int amount) {
        int newAmount = powder.get(type) + amount;
        setPowder(type, newAmount);
        powderString = (type.getColor()) + "᠅ §f" + (type.getName()) + ": " + (type.getColor()) + (StringUtils.toFormatedNumber(newAmount));
    }

    public void setPowder(Powder type, @Range(from = 0, to = Integer.MAX_VALUE) int amount) {
        powder.put(type, amount);
    }

    public void removePowder(Powder type, int amount) {
        int current = powder.get(type);
        setPowder(type, current - amount);
    }

    public void setGui(Gui gui) {
        if (gui == null) {
            if (this.gui == null) return;
            this.gui.getPlayers().remove(this);
            this.gui = null;
            closeInventory();
            return;
        }
        openInventory(gui.getInventory());
        this.gui = gui;
    }

    public void setDefenseString(String s) {
        defenseString = s;
        defenseStringTicks = 2;
    }

    public void setNotEnoughMana() {
        notEnoughMana = true;
        notEnoughManaTicks = 2;
    }

    //For some reason the default implementation does not work :/
    @Override
    public void sendMessage(@NotNull String message) {
        sendMessage(Component.text(message));
    }

    @Override
    public void sendMessage(final @NotNull Component message) {
        SystemChatPacket chatMessage = new SystemChatPacket(message, false);
        sendPacket(chatMessage);
    }

    public void spawn() {
        spawn((previous == null) ? worldProvider.spawn() : worldProvider.getCustomEntry().getOrDefault(previous, worldProvider.spawn()));
    }

    public void spawn(Pos spawn) {
        super.spawn();
        if (Main.IS_DEBUG) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            Main.LOGGER.debug("Spawn Caller: {}", stackTraceElements[2].getMethodName().equals("spawn") ? stackTraceElements[3].toString() : stackTraceElements[2].toString());
        }

        absorption = 0;
        recalculateArmor();
        if (pet != null) {
            pet.getPet().despawnPet(this, pet);
            pet.getPet().spawnPet(this, pet);
        }
        setHealth(getMaxHealth());
        setSbHealth(getMaxSbHealth());
        setMana(getStat(Stat.Intelligence));
        instance.loadChunk(spawn.chunkX(), spawn.chunkZ());
        setNoGravity(true);
        spawnTeleportId = getNextTeleportId();
        PlayerPositionAndLookPacket packet = new PlayerPositionAndLookPacket(spawnTeleportId, spawn, getVelocity(), spawn.yaw(), spawn.pitch(), (byte) 0);
        sendPacket(packet);
        sendPacket(new ClearTitlesPacket(true));
        getInventory().setItemStack(8, ISbItem.get(SkyblockMenu.class).create().item());
        SbItemStack item = getSbItemInHand(PlayerHand.MAIN);
        warping = false;
        if (item != null) setItemInHand(PlayerHand.MAIN, item.update().item());
        clearEffects();
        if (worldProvider.useCustomMining()) {
            sendPacket(new EntityEffectPacket(getEntityId(), new net.minestom.server.potion.Potion(PotionEffect.MINING_FATIGUE, 255, -1, (byte) 0)));
            sendPacket(new EntityEffectPacket(getEntityId(), new net.minestom.server.potion.Potion(PotionEffect.HASTE, 0, -1, (byte) 0)));
        } else {
            sendPacket(new RemoveEntityEffectPacket(getEntityId(), PotionEffect.MINING_FATIGUE));
            sendPacket(new RemoveEntityEffectPacket(getEntityId(), PotionEffect.HASTE));
        }
        if (!sidebar.isViewer(this)) sidebar.addViewer(this);
        for (me.carscupcake.sbremake.player.potion.PotionEffect effect : potionEffects)
            effect.potion().start(this, effect.amplifier(), (long) ((effect.expiration() - System.currentTimeMillis()) / 50d));
        sendPacket(new EntityMetaDataPacket(getEntityId(), Map.of(11, Metadata.Boolean(true))));
        sendPacket(new TimeUpdatePacket(0, Time.tick, false));
        if (!worldProvider.isRelight()) worldProvider.relight();
        sendMessage("§bYour Skyblock Level is: §3" + getSkyblockLevel());
    }

    public float getMaxHealth() {
        return (float) getAttribute(Attribute.MAX_HEALTH).getValue();
    }

    public void setWorldProvider(SkyblockWorld.WorldProvider provider) {
        if (worldProvider != null && provider != worldProvider) {
            worldProvider.removePlayer(this);
            previous = worldProvider.type();
            this.worldProvider = provider;
            provider.addPlayer(this, previous);
        }
        this.worldProvider = provider;
        onLaunchpad = false;
    }

    public void setWorldProvider(SkyblockWorld.WorldProvider provider, WarpLocation location) {
        if (worldProvider != null && provider != worldProvider) {
            worldProvider.removePlayer(this);
            previous = location.getWorld();
            this.worldProvider = provider;
            provider.addPlayer(this, location.getSpawn());
        }
        this.worldProvider = provider;
        onLaunchpad = false;
        inWorldTransfer = false;
    }

    public void addCoins(int i) {
        coins += i;
    }

    public void removeCoins(int i) {
        if (coins - i < 0) throw new IllegalStateException("Coins are not allowed to be negative");
        coins -= i;
    }

    private void makeShortbowTask(long shortbowCd, SbItemStack item) {
        long now = System.currentTimeMillis();
        Shortbow sb = (Shortbow) item.sbItem();
        this.shortbowTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (System.currentTimeMillis() - lastShortbowKeepAlive > 249 || isRemoved() || isDead()) {
                shortbowTask.cancel();
                shortbowTask = null;
                return;
            }
            long cd = sb.getShortbowCooldown(getStat(Stat.AttackSpeed, true));
            if (cd != shortbowCd) {
                shortbowTask.cancel();
                makeShortbowTask(cd, item);
                return;
            }
            long n = System.currentTimeMillis();
            if (n - lastAttack < shortbowCd) {
                Main.LOGGER.warn("Unusual Shortbow behaviour!");
                return;
            }
            lastAttack = System.currentTimeMillis();
            SkyblockPlayerArrow.shootBow(SkyblockPlayer.this, 1000L, item, (SkyblockArrow) SbItemStack.base(Material.ARROW).sbItem());
        }).delay(TaskSchedule.millis((now - lastAttack < shortbowCd) ? shortbowCd - (now - lastAttack) : 0)).repeat(TaskSchedule.millis(shortbowCd)).schedule();
    }

    public double getStat(Stat stat) {
        return getStat(stat, false);
    }

    public double getStat(Stat stat, boolean isBow) {
        double value = getStatModifiers(stat, isBow).calculate();
        if (stat.getMaxValue() > 0 && stat.getMaxValue() < value) value = stat.getMaxValue();
        if (value < 0) value = 0;
        return value;
    }

    @Deprecated
    public @NotNull ItemStack getEquipment(@NotNull EquipmentSlot slot) {
        return getSbEquipment(slot).item();
    }

    public SbItemStack getSbEquipment(EquipmentSlot slot) {
        return getPlayerInventory().getSbEquipment(slot);
    }

    public SbItemStack getSbItemInMainHand() {
        return getPlayerInventory().getSbEquipment(EquipmentSlot.MAIN_HAND);
    }

    @Deprecated
    @Override
    public @NotNull ItemStack getItemInMainHand() {
        return getSbItemInMainHand().item();
    }

    @Deprecated
    public void setItemInMainHand(@NotNull ItemStack itemStack) {
        setItemInMainHand(SbItemStack.from(itemStack));
    }

    public void setItemInMainHand(@NotNull SbItemStack itemStack) {
        getPlayerInventory().setEquipment(EquipmentSlot.MAIN_HAND, itemStack);
    }

    public SbItemStack getSbItemInOffHand() {
        return getPlayerInventory().getSbEquipment(EquipmentSlot.OFF_HAND);
    }

    @Deprecated
    @Override
    public @NotNull ItemStack getItemInOffHand() {
        return getSbItemInOffHand().item();
    }

    @Deprecated
    public void setItemInOffHand(@NotNull ItemStack itemStack) {
        setItemInOffHand(SbItemStack.from(itemStack));
    }

    public void setItemInOffHand(@NotNull SbItemStack itemStack) {
        getPlayerInventory().setEquipment(EquipmentSlot.OFF_HAND, itemStack);
    }

    public SbItemStack getSbItemInHand(PlayerHand hand) {
        SbItemStack var10000;
        switch (hand) {
            case MAIN -> var10000 = this.getSbItemInMainHand();
            case OFF -> var10000 = this.getSbItemInOffHand();
            default -> throw new MatchException(null, null);
        }
        return var10000;
    }

    @Override
    @Deprecated
    public @NotNull ItemStack getItemInHand(@NotNull PlayerHand hand) {
        return getSbItemInHand(hand).item();
    }

    @Deprecated
    @Override
    public void setItemInHand(@NotNull PlayerHand hand, @NotNull ItemStack itemStack) {
        setItemInHand(PlayerHand.MAIN, SbItemStack.from(itemStack));
    }

    public void setItemInHand(@NotNull PlayerHand hand, SbItemStack itemStack) {
        switch (hand) {
            case MAIN -> this.setItemInMainHand(itemStack);
            case OFF -> this.setItemInOffHand(itemStack);
        }
    }

    public PlayerStatEvent getStatModifiers(Stat stat, boolean isBow) {
        PlayerStatEvent event = new PlayerStatEvent(this, new ArrayList<>(), stat);
        event.modifiers().add(new PlayerStatEvent.BasicModifier("Base Value", stat.getBaseValue(), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Innate));
        for (EquipmentSlot slot : EquipmentSlot.armors()) {
            SbItemStack item = getSbEquipment(slot);
            if (item == null) continue;
            double value = item.getStat(stat, this);
            if (value == 0) continue;
            boolean canUse = true;
            for (Requirement requirement : item.sbItem().requirements())
                if (!requirement.canUse(this, item.item())) {
                    canUse = false;
                    break;
                }
            if (canUse)
                event.modifiers().add(new PlayerStatEvent.BasicModifier((item.displayName()), value, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Armor));
        }
        if (pet != null) {
            double bonus = pet.getPet().getStat(stat, pet.toPetInfo());
            if (bonus != 0)
                event.modifiers().add(new PlayerStatEvent.BasicModifier(pet.getPet().getName(), bonus, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.PetStats));
        }
        temporaryModifiers.forEachModifier(stat, modifier -> event.modifiers().add(modifier));
        SbItemStack item = getSbItemInHand(PlayerHand.MAIN);
        if (item != SbItemStack.AIR && (item.sbItem().getType().isStatsInMainhand() || (isBow && item.sbItem() instanceof BowItem))) {
            boolean canUse = true;
            for (Requirement requirement : item.sbItem().requirements())
                if (!requirement.canUse(this, item.item())) {
                    canUse = false;
                    break;
                }
            if (canUse)
                event.modifiers().add(new PlayerStatEvent.BasicModifier((item.displayName()), item.getStat(stat, this), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.ItemHeld));
        }
        MinecraftServer.getGlobalEventHandler().call(event);
        return event;
    }

    public int getFullSetBonusPieceAmount(FullSetBonus bonus) {
        return fullSetBonuses.getOrDefault(bonus, 0);
    }

    public double getMaxSbHealth() {
        double val = getStat(Stat.Health);
        return (val >= Float.MAX_VALUE) ? Float.MAX_VALUE : (float) val;
    }

    public double getManaPool() {
        return getStat(Stat.Intelligence) + 100d;
    }

    public void addSbHealth(double health, boolean ignoreVitality) {
        if (ignoreVitality) {
            setSbHealth(health + getHealth());
            return;
        }
        double vitalityPers = getStat(Stat.Vitality) / 100;
        setSbHealth((getSbHealth() + (health * vitalityPers)));
    }

    public void addSbHealth(double health) {
        addSbHealth(health, false);
    }

    public boolean addItem(ItemStack item) {
        return addItem(SbItemStack.from(item), true);
    }

    public boolean addItem(SbItemStack item) {
        return addItem(item, true);
    }

    public boolean addItem(SbItemStack item, boolean isCollection) {
        if (COIN_ITEMS.contains(item.sbItem().getClass())) {
            if (!item.item().hasTag(Tag.Double("coinamount"))) return true;
            double amount = item.item().getTag(Tag.Double("coinamount"));
            setCoins(getCoins() + amount);
            return true;
        }
        if (!getInventory().addItemStack(item.item())) return false;
        if (isCollection) {
            for (me.carscupcake.sbremake.item.collections.Collection collection : collections) {
                int amount = collection.progress(item.sbItem());
                if (amount > 0) {
                    collection.addProgress(amount * item.item().amount());
                    break;
                }
            }
        }
        return true;
    }

    public void setSbHealth(double health) {
        double newHealth = Math.max(0, Math.min(health, getMaxSbHealth()));
        if (newHealth == getSbHealth()) return;
        sbHealth = newHealth;
        updateHpBar();
    }

    public void updateHpBar() {
        double health = getMaxHealth() * (sbHealth / getMaxSbHealth());
        if (health != getHealth()) setHealth((float) health);
        if (absorption > 0 || lastAbsorbtion != absorption) {
            EntityMetaDataPacket packet = new EntityMetaDataPacket(getEntityId(), Map.of(15, Metadata.Float(getAbsorptionHearts(absorption))));
            sendPacket(packet);
            lastAbsorbtion = absorption;
        }
    }

    public void addMana(double value) {
        setMana(value + getMana());
    }

    public void setMana(double value) {
        mana = Math.max(0, Math.min(value, getManaPool()));
    }

    public long attackCooldown() {
        double attackSpeedMultiplier = 1d - (0.5d * (getStat(Stat.AttackSpeed) / 100d));
        return (long) (attackSpeedMultiplier * 500d);
    }

    public void damage(SkyblockEntity entity) {
        damage(entity, true);
    }

    public void damage(SkyblockEntity entity, boolean withKnockback) {
        EntityMeleeDamagePlayerEvent event = new EntityMeleeDamagePlayerEvent(entity, this);
        MinecraftServer.getGlobalEventHandler().call(event);
        if (event.isCancelled()) return;
        dealDamage(event);
        if (event.getCachedDamage() >= 1) {
            sendPacket(new DamageEventPacket(getEntityId(), 0, entity.getEntityId(), 0, getPosition()));
            if (hasKb() && withKnockback) {
                this.takeKnockback(0.4f, Math.sin(entity.getPosition().yaw() * 0.017453292), -Math.cos(entity.getPosition().yaw() * 0.017453292));
            }
        }
    }

    public void damage(SkyblockEntityProjectile entity) {
        ProjectileDamagePlayerEvent event = new ProjectileDamagePlayerEvent(entity, this);
        MinecraftServer.getGlobalEventHandler().call(event);
        if (event.isCancelled()) return;
        sendPacket(new DamageEventPacket(getEntityId(), 0, entity.getEntityId(), 0, getPosition()));
        dealDamage(event);
        if (hasKb()) {
            this.takeKnockback(0.4f, Math.sin(entity.getPosition().yaw() * 0.017453292), -Math.cos(entity.getPosition().yaw() * 0.017453292));
        }
    }

    public void damage(double damage, double trueDamage) {
        PlayerSelfDamageEvent event = new PlayerSelfDamageEvent(this, damage, trueDamage);
        MinecraftServer.getGlobalEventHandler().call(event);
        if (event.isCancelled()) return;
        sendPacket(new DamageEventPacket(getEntityId(), 0, getEntityId(), 0, getPosition()));
        dealDamage(event);
    }

    public void forceDamage(double amount) {
        sendPacket(new DamageEventPacket(getEntityId(), 0, getEntityId(), 0, getPosition()));
        dealDamage(new IDamageEvent() {
            @Override
            public double getCachedDamage() {
                return amount;
            }

            @Override
            public void setCachedDamage(double damage) {

            }

            @Override
            public Entity getTarget() {
                return SkyblockPlayer.this;
            }

            @Override
            public Entity getSource() {
                return SkyblockPlayer.this;
            }

            @Override
            public double getTrueDamage() {
                return 0;
            }

            @Override
            public double getNormalDamage() {
                return 0;
            }

            @Override
            public double getTargetDefense() {
                return 0;
            }

            @Override
            public double getTargetTrueDefense() {
                return 0;
            }
        });
    }

    private void dealDamage(IDamageEvent event) {
        double finalDamage = event.calculateDamage();
        event.spawnDamageTag();
        if (absorption > 0) {
            if (absorption - finalDamage <= 0) {
                finalDamage -= absorption;
                absorption = 0;
            } else {
                absorption -= finalDamage;
                finalDamage = 0;
            }
            updateHpBar();
        }
        setSbHealth(getSbHealth() - finalDamage);
    }

    public double calculateDamage(double damage, double trueDamage) {
        PlayerSelfDamageEvent event = new PlayerSelfDamageEvent(this, damage, trueDamage);
        return event.calculateDamage();
    }

    public boolean hasKb() {
        return true;
    }

    public void recalculateArmor() {
        recalculateArmor(getPlayerInventory());
    }

    public void recalculateArmor(SkyblockPlayerInventory inventory) {
        Main.LOGGER.debug("Recalculate Armor");
        Map<FullSetBonus, Integer> copy = new HashMap<>(fullSetBonuses);
        fullSetBonuses.clear();
        for (EquipmentSlot slot : EquipmentSlot.armors()) {
            SbItemStack stack = inventory.getSbItemStack(slot.armorSlot());
            if (stack == null) continue;
            for (Ability ability : stack.getAbilities(this))
                if (ability instanceof FullSetBonus fullSetBonus)
                    fullSetBonuses.put(fullSetBonus, fullSetBonuses.getOrDefault(fullSetBonus, 0) + 1);
        }
        Set<FullSetBonus> bonuses = new HashSet<>(copy.keySet());
        bonuses.addAll(fullSetBonuses.keySet());
        for (FullSetBonus bonus : bonuses) {
            int old = copy.getOrDefault(bonus, 0);
            int neu = fullSetBonuses.getOrDefault(bonus, 0); // neu (ger. new)
            if (neu == old) continue;
            if (neu < bonus.getMinPieces() && old >= bonus.getMinPieces()) {
                bonus.stop(this);
                Main.LOGGER.debug("Stopping {}", bonus.toString());
                continue;
            }
            if (old < bonus.getMinPieces() && neu >= bonus.getMinPieces()) {
                bonus.start(this);
                Main.LOGGER.debug("Starting {}", bonus.toString());
                continue;
            }
            if (neu >= bonus.getMinPieces()) {
                bonus.stop(this);
                bonus.start(this);
                Main.LOGGER.debug("Restarting {}", bonus.toString());
            }
        }
        for (EquipmentSlot slot : EquipmentSlot.armors()) {
            SbItemStack stack = inventory.getSbItemStack(slot.armorSlot());
            if (stack == null) continue;
            inventory.setItemStack(slot.armorSlot(), stack.update(this));
        }
    }

    public void playSound(SoundType type, Sound.Source source, float volume, float pitch) {
        playSound(Sound.sound(type.getKey(), source, volume, pitch));
    }

    public void playSound(SoundType type, Sound.Source source, float volume, float pitch, Pos location) {
        playSound(Sound.sound(type.getKey(), source, volume, pitch), location);
    }

    public void kick(String s, boolean noSave) {
        this.noSave = noSave;
        kick(s);
    }

    public void playSound(SoundType soundType, float volume, float pitch) {
        playSound(soundType.create(volume, pitch));
    }
}
