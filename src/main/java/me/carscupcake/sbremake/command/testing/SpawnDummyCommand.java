package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.entity.impl.test.DummyEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.condition.Conditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Condition;

public class SpawnDummyCommand extends Command {
    public SpawnDummyCommand() {
        super("dummy");
        addSyntax(this::execute);
        setCondition(Conditions::playerOnly);
    }

    public void execute(CommandSender sender, CommandContext context) {
        SkyblockPlayer player = (SkyblockPlayer) sender;
        DummyEntity entity = new DummyEntity();
        entity.setInstance(player.getInstance(), player.getPosition());
    }
}
