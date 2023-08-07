package legitish.events.impl;

import legitish.events.Event;
import net.minecraft.entity.EntityLivingBase;

public class RenderNameplateEvent extends Event {
    public final EntityLivingBase entity;
    public final double x, y, z;

    public RenderNameplateEvent(final EntityLivingBase t, final double v, final double v1, final double v2) {
        this.entity = t;
        this.x = v;
        this.y = v1;
        this.z = v2;
    }
}
