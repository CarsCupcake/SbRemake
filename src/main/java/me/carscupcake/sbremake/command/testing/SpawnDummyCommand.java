package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.entity.impl.test.DummyEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentEntityType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.EntityType;

public class SpawnDummyCommand extends Command {
    public SpawnDummyCommand() {
        super("dummy");
        addSyntax(this::execute);
        ArgumentEntityType entityTypeArg = new ArgumentEntityType("entityType");
        addSyntax((sender, commandContext) -> {
            EntityType type = commandContext.get(entityTypeArg);
            SkyblockPlayer player = (SkyblockPlayer) sender;
            DummyEntity entity = new DummyEntity(type);
            entity.setInstance(player.getInstance(), player.getPosition());
        }, entityTypeArg);
        setCondition(Conditions::playerOnly);
    }

    public void execute(CommandSender sender, CommandContext context) {
        SkyblockPlayer player = (SkyblockPlayer) sender;
        DummyEntity entity = new DummyEntity();
        entity.setInstance(player.getInstance(), player.getPosition());
    }
}
