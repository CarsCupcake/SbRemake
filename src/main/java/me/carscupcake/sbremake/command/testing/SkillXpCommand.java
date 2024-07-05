package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;

import java.util.function.Consumer;
import java.util.function.Function;

public class SkillXpCommand extends Command {
    public SkillXpCommand() {
        super("skillxp");
        ArgumentEnum<Operation> operationEnum = new ArgumentEnum<>("operation", Operation.class);
        ArgumentEnum<Skill> skillArgumentEnum = new ArgumentEnum<>("skill", Skill.class);
        ArgumentNumber<Double> xp = ArgumentType.Double("xp").min(0d);
        addSyntax((commandSender, commandContext) -> {
            commandContext.get(operationEnum).run((SkyblockPlayer) commandSender, commandContext.get(xp), commandContext.get(skillArgumentEnum));
        }, skillArgumentEnum, operationEnum, xp);
    }

    public enum Operation implements XpOperation {
        Add {
            @Override
            public void run(SkyblockPlayer player, double xp, Skill skill) {
                player.getSkill(skill).addXp(xp);
            }
        }
    }

    public interface XpOperation {
        void run(SkyblockPlayer player, double xp, Skill skill);
    }
}
