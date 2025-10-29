package me.carscupcake.sbremake.item.minion;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.ICoinItem;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.gui.Gui;
import me.carscupcake.sbremake.util.gui.InventoryBuilder;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.worlds.impl.PrivateIsle;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import java.util.*;

/**
 * Basic implementation of the {@link Minion} interface, here are all
 * usefull and general methods implemented
 */

public abstract class AbstractMinion implements Minion {
    private int level;
    protected final IMinionData base;
    protected MinionArmorStand stand;
    protected final Pos location;
    protected final String minionId;
    protected final UUID player;
    protected final ArrayList<SbItemStack> inventory = new ArrayList<>();
    protected int inventorySpace;
    protected int timeBetweenActions;
    protected TaskScheduler breakingRunnable;
    protected boolean isFull;
    protected boolean noSpace;
    protected LivingEntity message;
    protected final Instance instance;
    @Getter
    @Setter
    protected boolean isRunning = true;

    /**
     * This constructor provides the basic values
     *
     * @param level            is the level of the minion
     * @param base             is the base item of the minion
     * @param location         is the location where the minion is placed
     * @param minionIdentifier is a string for the minion. This is a random UUID from the method {@link UUID#randomUUID()} and is also used to load the minion from the file
     * @param placer           is the player who owns the isle
     */
    public AbstractMinion(int level, IMinionData base, Instance instance, Pos location, String minionIdentifier, UUID placer) {
        Assert.assertTrue(level <= base.getLevels());
        this.instance = instance;
        inventorySpace = getMinionInventorySpace(level);
        this.level = level;
        this.base = base;
        this.location = location;
        minionId = minionIdentifier;
        this.player = placer;
        timeBetweenActions = base.timeBetweenActions()[level - 1];
        placeMinion();
        loadInventory();
        checkHasSpace();
        startWorking();
    }

    @Override
    public UUID getOwner() {
        return player;
    }

    protected BlockVec lookAtTarget(List<BlockVec> missing) {
        if (missing.isEmpty())
            return null;
        Collections.shuffle(missing);
        var target = missing.getFirst();
        missing.remove(target);
        if (instance.isChunkLoaded(stand.getPosition())){
            var lo = stand.getPosition().withDirection(target.middle().sub(stand.getPosition()));
            var angle = new Vec(lo.pitch(), 0, 0);
            stand.teleport(lo);
            var armorStandMeta = (ArmorStandMeta) stand.getEntityMeta();
            armorStandMeta.setHeadRotation(angle);
        }
        return target;
    }

    public void placeMinion() {
        stand = new MinionArmorStand(this);
        stand.setInstance(instance, location);
        stand.setEquipment(EquipmentSlot.MAIN_HAND, base.getItemInHand());
        HashMap<EquipmentSlot, ItemStack> equipment = base.getEquipment();
        for (EquipmentSlot slot : equipment.keySet())
            stand.setEquipment(slot, equipment.get(slot));
        stand.setEquipment(EquipmentSlot.HELMET, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(base.getHeadStrings()[level - 1]).build());
    }

    public void loadInventory() {
        inventory.clear();
        ConfigFile config = new ConfigFile("minions", player);
        ConfigSection minionSection = config.get(minionId, ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        if (!minionSection.has("items")) return;
        SbItemStack[] values = minionSection.get("items", ConfigSection.ITEM_ARRAY, new SbItemStack[0]);
        Arrays.sort(values, Comparator.comparingInt(o -> o.item().amount()));
        boolean oneItemStackHasSpace = false;
        for (var b : values) {
            inventory.add(b);
            if (b.item().amount() < b.sbItem().getMaxStackSize()) oneItemStackHasSpace = true;
        }
        isFull = inventory.size() == inventorySpace && !oneItemStackHasSpace;
        if (isFull) {
            setFull();
            return;
        }
        for (int i = 0; i < getSteps(minionSection.get("lastDate", ConfigSection.LONG, new Date().getTime()), timeBetweenActions); i++) {
            generateLoot();
            if (isFull) return;
        }
    }



    protected void resetPos() {

        new TaskScheduler() {
            @Override
            public void run() {
                if (!isRunning) {
                    return;
                }
                Pos l = stand.getPosition();
                stand.teleport(l.withYaw(0).withPitch(0));
                var armorStandMeta = (ArmorStandMeta) stand.getEntityMeta();
                armorStandMeta.setHeadRotation(Vec.ZERO);
                armorStandMeta.setRightArmRotation(Vec.ZERO);
            }
        }.delayTask(10);
    }

    protected long getSteps(long oldMs, long timeBetweenActions){
        long timeInBetween = new Date().getTime() - oldMs;
        long seconds = timeInBetween/1000;
        long ticks = seconds * 20;
        return (ticks/timeBetweenActions)/2;
    }

    public void isFull() {
        boolean oneItemStackHasSpace = false;
        for (var b : inventory)
            if (b.item().amount() < b.sbItem().getMaxStackSize()) oneItemStackHasSpace = true;
        boolean b = isFull;
        isFull = inventory.size() == inventorySpace && !oneItemStackHasSpace;
        if(!isFull && b){
            if(message != null && !message.isDead())
                message.remove();
        }
    }

    public void saveMinion() {
        ConfigFile config = new ConfigFile("minions", player);
        var section = new ConfigSection(new JsonObject());
        section.set("lastDate", new Date().getTime(), ConfigSection.LONG);
        section.set("id", base.id(), ConfigSection.STRING);
        section.set("level", level, ConfigSection.INTEGER);
        section.set("pos", location, ConfigSection.POSITION);
        section.set("items", inventory.toArray(SbItemStack[]::new), ConfigSection.ITEM_ARRAY);
        config.set(minionId, section, ConfigSection.SECTION);
        config.save();
    }

    public void removeMinionFromFile() {
        ConfigFile config = new ConfigFile("minions", player);
        config.set(minionId, null, ConfigSection.SECTION);
        config.save();
    }

    /**
     * starts the get animation, and after finishing the animation runs the {@link AbstractMinion#generateLoot()} method
     */
    abstract void startGetAnimation();

    /**
     * is used to regenerate the resource
     */
    abstract void startGenerateAnimation();

    /**
     * is a check if all resourses has been respawned
     *
     * @return true if all resources are regenerated false if not
     */
    abstract boolean isMaxGenerated();

    /**
     * check for block space
     *
     * @return how many blocks are able to be set or the right type
     */
    abstract int settableSpace();

    public void generateLoot() {
        for (var drop : base.drops().loot(null)) {
            if (drop.sbItem() instanceof ICoinItem) continue;
            if (!addItemToInventory(drop)) {
                setFull();
            }
        }
    }

    public void setFull() {
        isFull = true;
        if (breakingRunnable != null && breakingRunnable.isRunning())
            breakingRunnable.cancel();
        setMinionMessage("§c/!\\ Minion is full!", -1);
    }

    public void setNoSpace() {
        noSpace = true;
        if (breakingRunnable != null && breakingRunnable.isRunning())
            breakingRunnable.cancel();
        setMinionMessage("§c/!\\ Minion has no space!", -1);
    }

    /**
     * adds an item to the inventory
     *
     * @param item   is the item thats getting added
     * @return true if the adding was possible (also returns false if not the full amount of items was possible to add to the inventory)
     */
    public boolean addItemToInventory(SbItemStack item) {
        var amount = item.item().amount();
        for (int i = 0; i < inventory.size(); i++) {
            var s = inventory.get(i);
            if (s.sbItem().getMaxStackSize() == s.item().amount()) continue;

            if (s.item().isSimilar(item.item())) {
                if (amount + s.item().amount() > s.sbItem().getMaxStackSize()) {
                    amount -= (s.sbItem().getMaxStackSize() - s.item().amount());
                    inventory.set(i, s.withAmount(s.sbItem().getMaxStackSize()));
                } else {
                    inventory.set(i, s.withAmount(amount + s.item().amount()));
                    return true;
                }
            }
        }
        if (amount <= 0) return true;

        if (inventory.size() < inventorySpace) {
            inventory.add(item);
            return true;
        }

        return false;
    }

    @Override
    public void remove(@Nullable SkyblockPlayer player, MinionRemoveReason removeReason) {
        setRunning(false);
        if (stand != null && !stand.isDead()) stand.remove();
        if(message != null && !message.isDead()) message.remove();

        if (removeReason == MinionRemoveReason.QUIT) {
            saveMinion();
        } else {
            if (removeReason == MinionRemoveReason.PICKUP_MINION) {
                if (player == null) throw new IllegalArgumentException("Player cannot be null!");
                for (var item : inventory)
                    player.addItem(item);
                inventory.clear();
                player.addItem(SbItemStack.from(base.id() + "_GENERATOR_" + level));
            }
            removeMinionFromFile();
        }
    }

    public void setLevel(int i) {
        Assert.assertTrue("New Minion level has to be larger than the level before!", level <= i);
        level = i;
        stand.setEquipment(EquipmentSlot.HELMET, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(base.getHeadStrings()[level - 1]).build());
        inventorySpace = getMinionInventorySpace(level);
        startWorking();
    }

    public void checkHasSpace() {
        if (settableSpace() > 0) {
            noSpace = false;
            if (breakingRunnable == null || breakingRunnable.cancelled.get()) startWorking();
        } else {
            setNoSpace();
        }
    }


    @Override
    public void startWorking() {
        if (isFull) return;
        if (noSpace) return;

        if (message != null) {
            message.remove();
            message = null;
        }

        if (breakingRunnable != null && !breakingRunnable.cancelled.get()) try {
            breakingRunnable.cancel();
        } catch (Exception ignored) {
        }
        breakingRunnable = new TaskScheduler() {

            @Override
            public void run() {
                if (isMaxGenerated()) {
                    startGetAnimation();
                } else {
                   startGenerateAnimation();
                }
            }
        };
        breakingRunnable.repeatTask(timeBetweenActions, timeBetweenActions);
    }

    @Override
    public void showInventory(SkyblockPlayer player) {
        InventoryBuilder builder = new InventoryBuilder(6, base.name() + " " + StringUtils.toRoman(level));
        builder.fill(TemplateItems.EmptySlot.getItem());
        builder.fill(ItemStack.AIR, 21, 25).fill(ItemStack.AIR, 30, 34).fill(ItemStack.AIR, 39, 43);
        List<Integer> slots = new ArrayList<>(List.of(21));
        if (level < 2)
            builder.setItems(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§eStorage unlocks at tier II").build(), 22, 23);
        else slots.addAll(List.of(22, 23));
        if (level < 4)
            builder.setItems(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§eStorage unlocks at tier IV").build(), 24, 25, 30);
        else slots.addAll(List.of(24, 25, 30));
        if (level < 6)
            builder.setItems(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§eStorage unlocks at tier VI").build(), 31, 32, 33);
        else slots.addAll(List.of(31, 32, 33));
        if (level < 8)
            builder.setItems(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§eStorage unlocks at tier VIII").build(), 34, 39, 40);
        else slots.addAll(List.of(34, 39, 40));
        if (level < 10)
            builder.setItems(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§eStorage unlocks at tier X").build(), 41, 42, 43);
        else slots.addAll(List.of(41, 42, 43));
        int i = 0;
        ArrayList<Integer> usedSlots = new ArrayList<>();
        for (var item : inventory) {
            builder.setItems(item.item(), slots.get(i));
            usedSlots.add(slots.get(i));
            i++;
        }
        builder.setItem(new ItemBuilder(Material.CHEST).setName("§aCollect All").addLoreRow("§eClick to collect all items!").build(), 48);
        builder.setItem(new ItemBuilder(Material.BEDROCK).setName("§ePickup Minion!").build(), 53);
        Gui gui = new Gui(builder.build());
        gui.setCancelled(true);
        gui.setGeneralClickEvent(event -> {
            var slot = event.getClick().slot();
            if (usedSlots.contains(slot)) {
                int indexOf = usedSlots.indexOf(slot);
                var item = inventory.get(indexOf);
                inventory.remove(indexOf);
                player.addItem(item);
                showInventory(player);
                isFull();
                startWorking();
            }
            return true;
        });
        gui.getClickEvents().add(48, ignored -> {
            for (int j = 0; j < inventory.size(); j++) {
                var item = inventory.get(j);
                if (player.addItem(item)) {
                    inventory.set(j, SbItemStack.AIR);
                } else {
                    player.sendMessage("§cYou dont have enough space to claim all items!");
                    break;
                }
            }
            var remaining = inventory.stream().filter(x -> x != SbItemStack.AIR);
            inventory.clear();
            inventory.addAll(remaining.toList());
            showInventory(player);
            isFull();
            startWorking();
            return true;
        });
        gui.getClickEvents().add(53, ignored -> {
            var isle = (PrivateIsle) player.getWorldProvider();
            isle.pickupMinion(player, this);
            player.closeInventory();
            return true;
        });
        gui.showGui(player);
    }

    @Override
    public void setMinionMessage(String message, long duration) {
        if (this.message == null) {
            this.message = new LivingEntity(EntityType.ARMOR_STAND);
            var meta = (ArmorStandMeta) this.message.getEntityMeta();
            meta.setMarker(true);
            meta.setInvisible(true);
            meta.setCustomNameVisible(true);
            meta.setHasNoGravity(true);
            this.message.setInstance(instance, stand.getPosition().add(0, 1, 0));
        }

        this.message.set(DataComponents.CUSTOM_NAME, Component.text(message));

        if (duration != -1) {
            this.message.scheduleRemove(duration, TimeUnit.SERVER_TICK);
        }

    }

    @Override
    public MinionArmorStand getArmorStand() {
        return stand;
    }

    @Override
    public boolean isInventoryFull() {
        return isFull;
    }

    @Override
    public UUID getId() {
        return UUID.fromString(minionId);
    }

    public static int getMinionInventorySpace(int level) {
        switch (level) {
            case 1 -> {
                return 1;
            }
            case 2, 3 -> {
                return 3;
            }
            case 4, 5 -> {
                return 6;
            }
            case 6, 7 -> {
                return 9;
            }
            case 8, 9 -> {
                return 12;
            }
            case 10, 11, 12 -> {
                return 15;
            }
        }
        return 0;
    }



    protected Iterable<BlockVec> getBlockGrit() {
        return new Iterable<>() {
            @Override
            public @NotNull Iterator<BlockVec> iterator() {
                return new  Iterator<>() {
                    private int x = -2;
                    private int z = -2;
                    @Override
                    public boolean hasNext() {
                        return z < 2 || x < 2;
                    }

                    @Override
                    public BlockVec next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        if (z == 0 && x == 0) {
                            x++;
                        }
                        if (x >= 2) {
                            x = -2;
                            z++;
                        }
                        return new BlockVec(location.add(x++, -1, z));
                    }
                };
            }
        };
    }
}
