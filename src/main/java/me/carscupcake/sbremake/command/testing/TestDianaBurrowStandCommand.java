package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.color.Color;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;

import java.util.LinkedList;

@DebugCommand
public class TestDianaBurrowStandCommand extends Command {
    public TestDianaBurrowStandCommand() {
        super("testdiana");
        addSyntax((sender, context) -> {
            var player = (SkyblockPlayer) sender;
            var stand = new LivingEntity(EntityType.ARMOR_STAND);
            stand.setEquipment(EquipmentSlot.MAIN_HAND, ItemStack.of(Material.ARROW));
            var meta = (ArmorStandMeta) stand.getEntityMeta();
            meta.setRightArmRotation(new Vec(90, 0, 45));
            stand.setInstance(player.getInstance(), player.getPosition().add(player.getPosition().withPitch(0).direction().mul(4)).withYaw(player.getPosition().yaw()));
            stand.setNoGravity(true);
            stand.setInvisible(true);

            new TaskScheduler()

            {
                @Override
                public void run() {
                    stand.teleport(stand.getPosition().withYaw(stand.getPosition().yaw() + 2));
                    var base = new Vec(-0.6, 1.2, -0.55);
                    var poss = new LinkedList<Point>();
                    for (double i = -1; i < 1.1; i += 0.2) {
                        poss.add(stand.getPosition().add(base.add(i, 0.5, 0).rotateAroundY(Math.toRadians(stand.getPosition().yaw()) * -1)));
                        poss.add(stand.getPosition().add(base.add(i, -0.5, 0).rotateAroundY(Math.toRadians(stand.getPosition().yaw()) * -1)));
                    }
                    var particle = Particle.DUST.withColor(new Color(0xFF0000));
                    for (Point arrowPos : poss) {
                        ParticleUtils.spawnParticle(stand.getInstance(), arrowPos, particle, 1);
                    }
                }

            }.repeatTask(1);


        });
    }
}
