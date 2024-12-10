package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.item.impl.other.GodPotion;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MaxProfile extends Command {
    public MaxProfile() {
        super("maxprofile");
        addSyntax((e, _) -> {
            SkyblockPlayer player = (SkyblockPlayer) e;
            for (Skill skill : Skill.values())
            {
                player.getSkill(skill).addXp(112_000_000);
            }
            GodPotionCommand.startPotion(player, 604_800_000);
            //More stuff later
        });
    }
}
