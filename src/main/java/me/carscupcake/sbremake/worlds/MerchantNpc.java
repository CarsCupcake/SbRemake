package me.carscupcake.sbremake.worlds;

import kotlin.Pair;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.item.ShopGui;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;

import java.util.List;

public class MerchantNpc extends Npc{
    public MerchantNpc(Pos pos, Instance instance, String name, PlayerSkin playerSkin, List<Pair<SbItemStack, Cost>> items) {
        super(pos, instance, name, playerSkin);
        super.withInteraction((player, _) -> {
            ShopGui shopGui = new ShopGui(items, name, player);
            shopGui.showGui(player);
        });
    }
}
