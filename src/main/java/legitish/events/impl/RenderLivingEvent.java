package legitish.events.impl;

import legitish.events.Event;
import net.minecraft.entity.EntityLivingBase;

public class RenderLivingEvent extends Event {
    public final Type type;
    public final EntityLivingBase entity;
    public final double x, y, z;
    public final float entityYaw, partialTicks;

    public RenderLivingEvent(final EntityLivingBase t, final double v, final double v1, final double v2, final float v3, final float v4, Type type) {
        this.type = type;
        this.entity = t;
        this.x = v;
        this.y = v1;
        this.z = v2;
        this.entityYaw = v3;
        this.partialTicks = v4;
    }

    public enum Type {
        PRE,
        POST
    }
}
