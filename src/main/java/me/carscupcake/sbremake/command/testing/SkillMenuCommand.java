package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;

public class SkillMenuCommand extends Command {
    public SkillMenuCommand() {
        super("skillmenu");
        ArgumentEnum<Skill> skillArgumentEnum = new ArgumentEnum<>("skill", Skill.class);
        addSyntax((commandSender, commandContext) -> {
            ((SkyblockPlayer) commandSender).getSkill(commandContext.get(skillArgumentEnum)).openInventory(1, (SkyblockPlayer) commandSender);
        }, skillArgumentEnum);
    }
}
