package legitish.events.impl;

import legitish.events.Event;
import net.minecraft.util.BlockPos;

public class DrawBlockHighlightEvent extends Event {
    public final BlockPos blockPos;

    public DrawBlockHighlightEvent(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
