package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedEnderman;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerEvolvedEndermanEyes extends AbstractEyesLayer<EntityEvolvedEnderman, ModelEvolvedEnderman>
{
    private static final RenderType EYES = RenderType.getEyes(new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/evolved_enderman_eyes.png"));

    public LayerEvolvedEndermanEyes(RenderEvolvedEnderman render)
    {
        super(render);
    }

    @Override
    public RenderType getRenderType() {
        return EYES;
    }
}