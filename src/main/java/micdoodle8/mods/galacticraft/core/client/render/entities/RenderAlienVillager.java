package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelAlienVillager;
import micdoodle8.mods.galacticraft.core.entities.EntityAlienVillager;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderAlienVillager extends MobRenderer<EntityAlienVillager, ModelAlienVillager>
{
    private static final ResourceLocation villagerTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/villager.png");
    private boolean texSwitch;

    protected ModelAlienVillager villagerModel;

    public RenderAlienVillager(EntityRendererManager manager)
    {
        super(manager, new ModelAlienVillager(0.0F), 0.5F);
        this.villagerModel = this.entityModel;
    }

    @Override
    protected void preRenderCallback(EntityAlienVillager villager, MatrixStack matrixStackIn, float partialTickTime)
    {
        float f1 = 0.9375F;

        if (villager.getGrowingAge() < 0)
        {
            f1 = (float) (f1 * 0.5D);
            this.shadowSize = 0.25F;
        }
        else
        {
            this.shadowSize = 0.5F;
        }

        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs(matrixStackIn);
        }

        matrixStackIn.scale(f1, f1, f1);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityAlienVillager par1Entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderAlienVillager.villagerTexture;
    }

    @Override
    public void render(EntityAlienVillager entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }
}
