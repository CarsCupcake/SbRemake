package me.carscupcake.sbremake.util.block;

import lombok.*;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@SuppressWarnings("unused")
public class FakeBlock {
    private final Instance instance;
    private final BlockVec position;
    private final Block originalBlock;
    @NotNull private Block newType;
    private boolean active = false;

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            instance.setBlock(position, newType);
        } else {
            instance.setBlock(position, originalBlock);
        }
    }

    public void release() {
        setActive(false);
    }

    public void setNewType(Block newType) {
        this.newType = newType;
        if (active) {
            instance.setBlock(position, newType);
        }
    }
}
