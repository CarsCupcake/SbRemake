package me.carscupcake.sbremake.item.modifiers.gemstone;

import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.modifiers.RarityStat;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record GemstoneItem(Gemstone.Type type, Gemstone.Quality quality, RarityStat statBonus, String headTexture) implements ISbItem, HeadWithValue, Gemstone {
    static List<GemstoneItem> items = new ArrayList<>();
    public GemstoneItem{
        items.add(this);
        Map<Quality, Gemstone> map = gemstones.getOrDefault(type, new HashMap<>());
        map.put(quality, this);
        gemstones.put(type, map);
    }
    @Override
    public String value() {
        return headTexture;
    }

    @Override
    public String getId() {
        return  (quality.name().toUpperCase()) + "_" + (type.name().toUpperCase()) ;
    }

    @Override
    public String getName() {
        return  (type.getStat().getSymbol()) + " " + (quality.name()) + " " + (type.name()) + " Gemstone";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return quality.getRarity();
    }

    @Override
    public double value(SbItemStack item) {
        return statBonus.fromRarity(item.getRarity());
    }

    @Override
    public ISbItem asItem() {
        return this;
    }

    @Override
    public boolean isUnstackable() {
        return quality == Quality.Flawless ||  quality == Quality.Perfect;
    }

    public static void init() {
        new GemstoneItem(Type.Ruby, Quality.Rough, new RarityStat(1, 2, 3, 4, 5, 7), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDE1OWIwMzI0M2JlMThhMTRmM2VhZTc2M2M0NTY1Yzc4ZjFmMzM5YTg3NDJkMjZmZGU1NDFiZTU5YjdkZTA3In19fQ==");
        new GemstoneItem(Type.Amber, Quality.Rough, new RarityStat(4, 8, 12, 16, 20, 24, 28), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGExOTQzNmY2MTUxYTdiNjZkNjVlZDc2MjRhZGQ0MzI1Y2ZiYmMyZWVlODE1ZmNmNzZmNGMyOWRkZjA4Zjc1YiJ9fX0=");
        new GemstoneItem(Type.Topaz, Quality.Rough, new RarityStat(0.4, 0.4, 0.4, 0.4, 0.4, 0.5), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ZkOTYwNzIyZWMyOWM2NjcxNmFlNWNhOTdiOWI2YjI2Mjg5ODRlMWQ2ZjlkMjU5MmNkMDg5OTE0MjA2YTFiIn19fQ==");
        new GemstoneItem(Type.Jade, Quality.Rough, new RarityStat(2, 4, 6, 8, 10, 12, 14), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I0YzJhZmQ1NDRkMGE2MTM5ZTZhZThlZjhmMGJmYzA5YTlmZDgzN2QwY2FkNGY1Y2QwZmU3ZjYwN2I3ZDFhMCJ9fX0=");
        new GemstoneItem(Type.Sapphire, Quality.Rough, new RarityStat(2, 3, 4, 5, 6, 7), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2ZjZWJlNTRkYmMzNDVlYTdlMjIyMDZmNzAzZTZiMzNiZWZiZTk1YjZhOTE4YmQxNzU0Yjc2MTg4YmM2NWJiNSJ9fX0=");
        new GemstoneItem(Type.Amethyst, Quality.Rough, new RarityStat(1, 2, 3, 4, 5, 6, 7), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ5M2M2ZjU0MGM3MDAxZmVkOTdiMDdmNmI0Yzg5MTI4ZTNhN2MzNzU2M2FhMjIzZjBhY2NhMzE0ZjE3NTUxNSJ9fX0=");
        new GemstoneItem(Type.Jasper, Quality.Rough, new RarityStat(1, 1, 1, 2, 3, 4), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjNkMDY0ZWMxNTAxNzJkMDU4NDRjMTFhMTg2MTljMTQyMWJiZmIyZGRkMWRiYjg3Y2RjMTBlMjIyNTJiNzczYiJ9fX0=");
        new GemstoneItem(Type.Opal, Quality.Rough, new RarityStat(1, 1, 1, 2, 2, 3), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0ZDNjNTdmODA4MjRiMzgzOWI4YjIyMGYyMTU4YmNhNTA1ZDQ5N2ZkMWM5ZTNmMjlmNDIyYjFlNjIwNmE0NSJ9fX0=");
        new GemstoneItem(Type.Aquamarine, Quality.Rough, new RarityStat(0.1, 0.1, 0.2, 0.2, 0.3, 0.4), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjI2MGE1NjEwNmM5YWU1NTU3YTEyZDVjYzI5MTU5MWYyZWFmMjY3NzAxMmQzYmVlYzU4ZDQ1OGRkYjkyYWViOSJ9fX0=");
        new GemstoneItem(Type.Citrine, Quality.Rough, new RarityStat(0.5, 1, 1.5, 2, 2.5, 3), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBlODUxN2IzMDE0ZTc4MzU1NmRhNDdiMTdiZTkzMTUwMWIwOWRhNWJmODg4OWI3OTdlYzA4N2E4NzQ5YTg5In19fQ==");
        new GemstoneItem(Type.Onyx, Quality.Rough, new RarityStat(1, 1, 2, 2, 3, 4), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzA0ZDdiM2QxMTE0M2RhNzc2YzI1Yjg1YzBkM2Y0YzJiYTFjOTlmZjU3YWVhNGUzNGI4MDgwMDJkYjdmMzllOSJ9fX0=");
        new GemstoneItem(Type.Peridot, Quality.Rough, new RarityStat(0.5, 1, 1.5, 2, 2.5, 3), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjNjMmIxOTRkMzg5MjJlNDkzY2ZhZjI3NmQ2OGY2ZjliYTBjMGNkM2E2NDViZjJhMDZkYzUxNDI0ZWE2MjJkNyJ9fX0=");

        new GemstoneItem(Type.Ruby, Quality.Flawed, new RarityStat(3, 4, 5, 6, 8, 10), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZkODEwNjhjYmRmNGEzNjQyMzFhMjY0NTNkNmNkNjYwYTAwOTVmOWNkODc5NTMwN2M1YmU2Njc0Mjc3MTJlIn19fQ==");
        new GemstoneItem(Type.Amber, Quality.Flawed, new RarityStat(6, 10, 14, 18, 24, 30, 36), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTczYmNmYzM5ZWI4NWRmMTg0ODUzNTk4NTIxNDA2MGExYmQxYjNiYjQ3ZGVmZTQyMDE0NzZlZGMzMTY3MTc0NCJ9fX0=");
        new GemstoneItem(Type.Topaz, Quality.Flawed, new RarityStat(0.8, 0.8, 0.8, 0.8, 0.8, 0.8, 0.9), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjYzOTI3NzNkMTE0YmUzMGFlYjNjMDljOTBjYmU2OTFmZmVhY2ViMzk5YjUzMGZlNmZiNTNkZGMwY2VkMzcxNCJ9fX0=");
        new GemstoneItem(Type.Jade, Quality.Flawed, new RarityStat(3, 5, 7, 10, 14, 18, 22), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIyODJjNmJiODM0M2UwZjBkNjFlZTA3NDdkYWRhNzUzNDRmMzMyZTlmZjBhY2FhM2FkY2RmMDkzMjFkM2RkIn19fQ==");
        new GemstoneItem(Type.Sapphire, Quality.Flawed, new RarityStat(5, 5, 6, 7, 8, 10), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGEwYWY5OWU4ZDg3MDMxOTRhODQ3YTU1MjY4Y2Y1ZWY0YWM0ZWIzYjI0YzBlZDg2NTUxMzM5ZDEwYjY0NjUyOSJ9fX0=");
        new GemstoneItem(Type.Amethyst, Quality.Flawed, new RarityStat(3, 4, 5, 6, 8, 10), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFkYjU5MjYwODk1NTc4ZDM3ZTU5NTA1ODgwNjAyZGU5NDBiMDg4ZTVmZmY4ZGEzZTY1MjAxZDczOWM4NmU4NCJ9fX0=");
        new GemstoneItem(Type.Jasper, Quality.Flawed, new RarityStat(2, 2, 2, 3, 4, 5), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTczNTExZTUwNGMzMTZiMTM5ZWRiMzVmZWJlNzNlZjU5MWMwZjQ1NWU4Y2FmOWVlMzUzYmMxMmI2YzE0YTkyMiJ9fX0=");
        new GemstoneItem(Type.Opal, Quality.Flawed, new RarityStat(2, 2, 2, 3, 3, 4), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWFkYzNiY2RkN2M3MDFiNjNmOGI4YjRhOTZlNDI5MzE2YTA4Mzg4NjY5ZDlhOThjMWE5ODc5MTcyOWI5NjFkZiJ9fX0=");
        new GemstoneItem(Type.Aquamarine, Quality.Flawed, new RarityStat(0.2, 0.2, 0.3, 0.4, 0.6, 0.8), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQxNjYyNTRhYzhiMzI0YzFiNzhlYWRjN2MyNDk4NjZjNjc0MmMxNzU4NmEzM2FlYTZhODlmZTM0NmIyYWNmMCJ9fX0=");
        new GemstoneItem(Type.Citrine, Quality.Flawed, new RarityStat(1, 1.5, 2, 2.5, 3, 4), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGMwYjg0MzM1NzlmNDgwYWRjNjg1NzQ4YjJiZjJlY2NjNjkyMjExMzdmNzA0YmJiOTIyNjE0MTkxZjgwMWIyNiJ9fX0=");
        new GemstoneItem(Type.Onyx, Quality.Flawed, new RarityStat(2, 2, 3, 3, 4, 6), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUyYTkxZjQ0YmNiYzc4ZDRjOWQwNDVlOTQxYTIzZjdiZGI0ZDk5NjA5ZTYwYmRjZWM4NDIzOTZmZWIzMTVhNyJ9fX0=");
        new GemstoneItem(Type.Peridot, Quality.Flawed, new RarityStat(1, 1.5, 2, 2.5, 3, 4), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzVkYTczNGU2M2FhMjIxMTZlNjQ3MDNiYjMzMjcxYzRmYmMzZDEzMzJhNmE2MGMyNGFmNDliODA0MTkzYmY0NiJ9fX0=");

        new GemstoneItem(Type.Ruby, Quality.Fine, new RarityStat(4, 5, 6, 8, 10, 14), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY3Mjk1OTAyOGYyNzRiMzc5ZDQzMGYwNjhmMGYxNWE0Zjc5M2VhYzEyYWZiOTRhZTBiNGU1MGNmODk1ZGYwZiJ9fX0=");
        new GemstoneItem(Type.Amber, Quality.Fine, new RarityStat(10, 14, 20, 28, 36, 45, 54), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGIxY2NlMjJkZTE5ZWQ2NzI3YWJjNWU2YzJkNTc4NjRjODcxYTQ0Yzk1NmJiZTJlYjM5NjAyNjliNjg2YjhiMyJ9fX0=");
        new GemstoneItem(Type.Topaz, Quality.Fine, new RarityStat(1.2, 1.2, 1.2, 1.2, 1.2, 1.2, 1.3), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTJjYjZlNTFjNDYxZTczNTk1MjZiZWE1ZTA2MjA5Y2RkZGU3YzY0NjlhODE5ZjM0MDVjZjBhMDM4YzE1OTUwMiJ9fX0=");
        new GemstoneItem(Type.Jade, Quality.Fine, new RarityStat(5, 7, 10, 15, 20, 25, 30), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjI4ZjFjMGM1MDkyZTEyZDMzNzcwZGY0NWM1ODQ1YTk2MTA4ODYwMzliMzRhYmU5M2ExNmM1ZTk0MmRmYzhlNCJ9fX0=");
        new GemstoneItem(Type.Sapphire, Quality.Fine, new RarityStat(7, 8, 9, 10, 11, 12), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzYxNjFkYWEzNTg5ZWM5YzgxODc0NTlhYzM2ZmQ0ZGQyNjQ2YzA0MDY3OGQzYmZhY2I3MmEyMjEwYzZjODAxYyJ9fX0=");
        new GemstoneItem(Type.Amethyst, Quality.Fine, new RarityStat(4, 5, 6, 8, 10, 14), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ExZWU1ZmZjZTA0ZWI3ZGE1OTJkNDI0MTRmZjM1ZTFiZjM4MTk0ZDZiODJlMzEwZGJjNjI2MWI0N2ZiOWM5MSJ9fX0=");
        new GemstoneItem(Type.Jasper, Quality.Fine, new RarityStat(3, 3, 4, 5, 6, 7), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWFjMTVmNmZjZjJjZTk2M2VmNGNhNzFmMWE4Njg1YWRiOTdlYjc2OWUxZDExMTk0Y2JiZDJlOTY0YTg4OTc4YyJ9fX0=");
        new GemstoneItem(Type.Opal, Quality.Fine, new RarityStat(3, 3, 3, 4, 4, 5), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWQ3OThlMjBhNDdkMjUxYTllMzNkNDAzMzI5NzNjNzE4OWFjMTU1MDc2MGJhMjVjNGI5NTZjOTE1OTM2NDU2OCJ9fX0=");
        new GemstoneItem(Type.Aquamarine, Quality.Fine, new RarityStat(0.5, 0.6, 0.7, 0.8, 1, 1.2), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmI4MjJiOWRjNzBiOWNhMjAwMDhjMzMyZjQ4MWNiZWJiNTJiZDUwNjY5Y2E5OGE4OWZkMzNkMTM0NWZhMTBmMiJ9fX0=");
        new GemstoneItem(Type.Citrine, Quality.Fine, new RarityStat(1.5, 2, 3, 4, 5, 6), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWMwYWI5YzlmMzVhMWJmNDRkZTE4YTBkYjJhYTBlZDJlYmU4YjlmYWE4MjAzMDdkYTM2MzhmMzZhODMwNjUzNiJ9fX0=");
        new GemstoneItem(Type.Onyx, Quality.Fine, new RarityStat(3, 3, 4, 5, 6, 8), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg3YTMyNjMzMWZlMjdmN2VlMDc0Zjk3NzI3NjA0YzQ5NWY5NWMzNzgxMjEzMDJlODc5ZTFmNDBiYTRkMjBhOCJ9fX0=");
        new GemstoneItem(Type.Peridot, Quality.Fine, new RarityStat(1.5, 2, 3, 4, 5, 6), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNiYTdhZWZjMWM0ZjQxMjg2ODczOWUyZjJjMmU2NTFlZGViMWJlMjVkM2M3MTc1ZTY4YTA1YjY3NTA2YTFlZCJ9fX0=");

        new GemstoneItem(Type.Ruby, Quality.Flawless, new RarityStat(5, 7, 10, 14, 18, 22), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI2YTI0OGZiYmMwNmNmMDZlMmM5MjBlY2ExY2FjOGEyYzk2MTY0ZDMyNjA0OTRiZWQxNDJkNTUzMDI2Y2M2In19fQ==");
        new GemstoneItem(Type.Amber, Quality.Flawless, new RarityStat(14, 20, 30, 44, 58, 75, 92), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWRjZTYyZjcwYWMwNDZiODgxMTEzYzZjZjg2Mjk4NzcyNzc3NGUyNjU4ODU1MDFjOWEyNDViMTgwZGIwOGMwZCJ9fX0=");
        new GemstoneItem(Type.Topaz, Quality.Flawless, new RarityStat(1.6, 1.6, 1.6, 1.6, 1.6, 1.6, 1.8), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDEwOTY0ZjNjNDc5YWQ3ZDlhZmFmNjhhNDJjYWI3YzEwN2QyZDg4NGY1NzVjYWUyZjA3MGVjNmY5MzViM2JlIn19fQ==");
        new GemstoneItem(Type.Jade, Quality.Flawless, new RarityStat(7, 10, 15, 20, 27, 35, 44), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg5Zjc1ZTBiMDAzNzhhNTgzZGJiYTcyOGRjZGM2ZTkzNDZmMzFkZDYwMWQ0NDhmM2Q2MDYxNWM3NDY1Y2MzZSJ9fX0=");
        new GemstoneItem(Type.Sapphire, Quality.Flawless, new RarityStat(19, 11, 12, 14, 17, 20), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU3Y2ZhOWM3NWJhNTg0NjQ1ZWUyYWY2ZDk4NjdkNzY3ZGRlYTQ2NjdjZGZjNzJkYzEwNjFkZDE5NzVjYTdkMCJ9fX0=");
        new GemstoneItem(Type.Amethyst, Quality.Flawless, new RarityStat(5, 7, 10, 14, 18, 22), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM2MjM1MjFjODExMWFkMjllOWRjZjdhY2M1NjA4NWE5YWIwN2RhNzMyZDE1MTg5NzZhZWU2MWQwYjNlM2JkNiJ9fX0=");
        new GemstoneItem(Type.Jasper, Quality.Flawless, new RarityStat(5, 6, 7, 8, 10, 12), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmY5OTNkM2E0M2Q0MDU5N2I0NzQ0ODU5NzYxNjBkMGNmNTJhYzY0ZDE1NzMwN2QzYjFjOTQxZGIyMjRkMGFjNiJ9fX0=");
        new GemstoneItem(Type.Opal, Quality.Flawless, new RarityStat(4, 4, 5, 6, 8, 9), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQxNWVkNzBlNzIwMDQwYWQ3MzExZTY5MzU5ZGZkZjVlMTE0ZWFkZDJhNGMxZjk3MWE5NTAxMzQxYTQ1MjY0YiJ9fX0=");
        new GemstoneItem(Type.Aquamarine, Quality.Flawless, new RarityStat(0.9, 1, 1.1, 1.2, 1.4, 1.6), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM3Njk5YmRmOGNiNDNlZmYxNDdkOWNlMTliMTQ4MDJmOWJjODhhMjFlNjNhZDI4YjY2OTUzMGU4YTNjMGI1NyJ9fX0=");
        new GemstoneItem(Type.Citrine, Quality.Flawless, new RarityStat(2, 3, 4, 5, 6, 8), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjA2YjRmNjNkM2QzYTM5Yzk4NTY1YWY0ODU4YTUxMzViZTc3NGFkNjcyZWIyMzZiYjY1YWRmYzhjYjM0MjVlOCJ9fX0=");
        new GemstoneItem(Type.Onyx, Quality.Flawless, new RarityStat(4, 5, 6, 7, 8, 10), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzYzYmI0YTRjZmY0ZDU3ZjFiNjdhZTczZDU2ZTQ1OGRlZTU5YmQ2NDMxNWU5MjE0ODU5MmI2MTRiMzA3NGViMSJ9fX0=");
        new GemstoneItem(Type.Peridot, Quality.Fine, new RarityStat(2, 3, 4, 5, 6, 8), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg4NGE4ZGNiNzEyODM0MWNlMDljYjM2MTZjMTFjM2U0NmY4MGQwODNhNmYyZGJiNGRiZTBiYjkzMjMxOGIwOSJ9fX0=");

        new GemstoneItem(Type.Ruby, Quality.Perfect, new RarityStat(6, 9, 13, 18, 24, 30), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzliNmUwNDdkM2IyYmNhODVlOGNjNDllNTQ4MGY5Nzc0ZDhhMGVhZmU2ZGZhOTU1OTUzMDU5MDI4MzcxNTE0MiJ9fX0=");
        new GemstoneItem(Type.Amber, Quality.Perfect, new RarityStat(20, 28, 40, 60, 80, 100, 120), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZTIzNmNkZWMzZjJhNmY1MWVhZTE1ZTJjOGY2MjI4YjM0ZjEzN2RhMTU2OWZlYzllODAzZjljZDgxNzU5ZCJ9fX0=");
        new GemstoneItem(Type.Topaz, Quality.Perfect, new RarityStat(2, 2, 2, 2, 2, 2, 2.2), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RhNmVjZGNiYzNmZTM1NWNhMDYxMTE5MmEzZmJkMzVkZDU2MzVkNWZjZGYzZmJjNzllZDJiYzFmNGEwMTdmZSJ9fX0=");
        new GemstoneItem(Type.Jade, Quality.Perfect, new RarityStat(10, 14, 20, 30, 40, 50, 60), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ZjZWQ3OTc3MzgyYmY3MWQ0ZWUxN2ZmNWI5MTllMGViNzk3MjA4M2M0Y2NjZmExNzVjODc1M2FlNDBiYTAwNiJ9fX0=");
        new GemstoneItem(Type.Sapphire, Quality.Perfect, new RarityStat(12, 14, 17, 20, 24, 30), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5M2ViYWNiNjBiNzE3OTMzNTVmZGUwZDRiYmE0M2E5YzVlYzA5YzNmMzg4OTdjNDhjMWY4NTc1MjNhMGEyOSJ9fX0=");
        new GemstoneItem(Type.Amethyst, Quality.Perfect, new RarityStat(6, 9, 13, 18, 24, 30), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg4NmUwZjQxMTg1YjE4YTNhZmQ4OTQ4OGQyZWU0Y2FhMDczNTAwOTI0N2NjY2YwMzljZWQ2YWVkNzUyZmYxYSJ9fX0=");
        new GemstoneItem(Type.Jasper, Quality.Perfect, new RarityStat(6, 7, 9, 11, 13, 16), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjYzZjk5MWI4ZTAzOGU0NmI4ZWQ3NjMyZjQ0Y2EyZTMwYzE1ZjQyOTc3MDcwYThjOGQ4NzI4ZTNmYzA0ZmM3YyJ9fX0=");
        new GemstoneItem(Type.Opal, Quality.Perfect, new RarityStat(5, 6, 7, 9, 11, 13), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U1MDlmMDZiOGRjMzg0MzU4ZmYyNDcyYWI2MmNlNmZkYzJmNjQ2ZTMzOGVmZGMzYzlmYjA1ZGRjNDMxZjY0In19fQ==");
        new GemstoneItem(Type.Aquamarine, Quality.Perfect, new RarityStat(1.3, 1.4, 1.5, 1.6, 1.8, 2), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU1YzhhYWM4OTkwZTQ1NmFiZjM1MDMwMTE1ZTM2YzdjNTY2NzgyZTEzZDgxYjFhNzcwOTk1ZmE1ZjM3YzgyMiJ9fX0=");
        new GemstoneItem(Type.Citrine, Quality.Perfect, new RarityStat(3, 4, 5, 6, 8, 10), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWMzY2EwZGIwYjlhZjhlOThhY2RlMWM3ZDY5NmRjZjc5ZWIzMDc5NzAyZTViNTU0MTQxZGJhNjYzYjQzZGQ2NSJ9fX0=");
        new GemstoneItem(Type.Onyx, Quality.Perfect, new RarityStat(5, 6, 7, 8, 10, 12), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU5NmFkYjE0Y2FkNTIzZmE2ZmQzNzNmYzlhNTk5ZjkyYmFmMDNjZjg4MjVmM2E2ZDZmMzIyMzZhZjIxYmE1NSJ9fX0=");
        new GemstoneItem(Type.Peridot, Quality.Perfect, new RarityStat(3, 4, 5, 6, 7, 10), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWEyNTg4ODMxY2Q0YjI4MmFhYTE2MzU2Yjc4N2Q1NmU0ZWIxM2YwZDYyZjQxNGRhZmQxMzE4ZWEwOWY4MDE5YyJ9fX0=");

        items.forEach(SbItemStack::initSbItem);
        items.clear();

    }
}
