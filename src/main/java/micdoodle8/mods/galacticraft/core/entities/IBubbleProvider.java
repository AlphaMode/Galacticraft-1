package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;

public interface IBubbleProvider
{
    float getBubbleSize();

    void setBubbleVisible(boolean shouldRender);

    boolean getBubbleVisible();

    Vector3 getColor();
}
