package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.entity.impl.test.DummyEntity;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentLoop;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentEntityType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.EntityType;

public class SpawnDummyCommand extends Command {
    public SpawnDummyCommand() {
        super("dummy");
        addSyntax(this::execute);
        ArgumentEntityType entityTypeArg = new ArgumentEntityType("entityType");
        ArgumentLoop<MobType> mobTypeArgumentLoop = new ArgumentLoop<>("entityTypes", new ArgumentEnum<>("mobType", MobType.class));
        addSyntax((sender, commandContext) -> {
            EntityType type = commandContext.get(entityTypeArg);
            SkyblockPlayer player = (SkyblockPlayer) sender;
            DummyEntity entity = new DummyEntity(type);
            entity.setInstance(player.getInstance(), player.getPosition());
        }, entityTypeArg);
        addSyntax((sender, commandContext) -> {
            EntityType type = commandContext.get(entityTypeArg);
            var mobTypes = commandContext.get(mobTypeArgumentLoop).toArray(MobType[]::new);
            SkyblockPlayer player = (SkyblockPlayer) sender;
            DummyEntity entity = new DummyEntity(type, mobTypes);
            entity.setInstance(player.getInstance(), player.getPosition());
        }, entityTypeArg, mobTypeArgumentLoop);
        setCondition(Conditions::playerOnly);
    }

    public void execute(CommandSender sender, CommandContext context) {
        SkyblockPlayer player = (SkyblockPlayer) sender;
        DummyEntity entity = new DummyEntity();
        entity.setInstance(player.getInstance(), player.getPosition());
    }
}
