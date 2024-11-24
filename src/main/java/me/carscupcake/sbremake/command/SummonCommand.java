package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.impl.deepCaverns.SneakyCreeper;
import me.carscupcake.sbremake.entity.impl.hub.GraveyardZombie;
import me.carscupcake.sbremake.entity.impl.spidersDen.*;
import me.carscupcake.sbremake.entity.slayer.zombie.RevenantHorrorI;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.utils.location.RelativeVec;
import org.apache.cassandra.repair.PreviewRepairConflictWithIncrementalRepairException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class SummonCommand extends Command {

    private final ArgumentEnum<EntityClass> entity;
    private final Argument<RelativeVec> pos;

    public SummonCommand() {
        super("summon");
        setCondition(Conditions::playerOnly);

        entity = ArgumentType.Enum("entity type", EntityClass.class);
        pos = ArgumentType.RelativeVec3("block").setDefaultValue(() -> new RelativeVec(
                new Vec(0, 0, 0),
                RelativeVec.CoordinateType.RELATIVE,
                true, true, true
        ));
        addSyntax(this::execute, entity, pos);
        setCondition(Conditions::playerOnly);
        setDefaultExecutor((sender, _) -> sender.sendMessage("Usage: /summon <type> <x> <y> <z>"));
    }

    private void execute(@NotNull CommandSender commandSender, @NotNull CommandContext commandContext) {
        try {
            final Entity entity = commandContext.get(this.entity).newInstance();
            //noinspection ConstantConditions - One couldn't possibly execute a command without being in an instance
            entity.setInstance(((Player) commandSender).getInstance(), commandContext.get(pos).fromSender(commandSender));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            commandSender.sendMessage(STR."§cAn Error occurred while executing the command! §7(\{e.getClass().getSimpleName()})");
        }
    }

    enum EntityClass implements EntityFactory{
        GraveyardZombie(GraveyardZombie.class),
        GravelSkeleton(GravelSkeleton.class),
        SneakyCreeper(me.carscupcake.sbremake.entity.impl.deepCaverns.SneakyCreeper.class),
        SplitterSpider(me.carscupcake.sbremake.entity.impl.spidersDen.SplitterSpider.class) {
            @Override
            public SkyblockEntity newInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
                return new SplitterSpider(4);
            }
        },
        SpiderJockey(me.carscupcake.sbremake.entity.impl.spidersDen.SpiderJockey.class) {
            @Override
            public SkyblockEntity newInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
                return new SpiderJockey(false);
            }
        },
        VoraciousSpider(me.carscupcake.sbremake.entity.impl.spidersDen.VoraciousSpider.class) {
            @Override
            public SkyblockEntity newInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
                return new VoraciousSpider(4);
            }
        },
        DasherSpider(me.carscupcake.sbremake.entity.impl.spidersDen.DasherSpider.class) {
            @Override
            public SkyblockEntity newInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
                return new DasherSpider(4);
            }
        };
        private final Class<? extends SkyblockEntity> entityClazz;

        EntityClass(Class<? extends SkyblockEntity> entityClazz) {
            this.entityClazz = entityClazz;
        }

        @Override
        public SkyblockEntity newInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            if (entityClazz == null)
                throw new NullPointerException("Illegal!");
            return entityClazz.getConstructor().newInstance();
        }
    }

    interface EntityFactory {
        SkyblockEntity newInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
    }
}
