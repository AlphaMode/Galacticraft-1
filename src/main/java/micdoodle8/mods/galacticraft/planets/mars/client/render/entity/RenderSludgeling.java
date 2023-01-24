package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelSludgeling;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySludgeling;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderSludgeling extends MobRenderer<EntitySludgeling, ModelSludgeling>
{
    private static final ResourceLocation sludgelingTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/model/sludgeling.png");
    private boolean texSwitch;

    public RenderSludgeling(EntityRendererManager renderManager)
    {
        super(renderManager, new ModelSludgeling(), 0.3F);
    }

    @Override
    protected float getDeathMaxRotation(EntitySludgeling par1EntityLiving)
    {
        return 180.0F;
    }

    @Override
    public void render(EntitySludgeling sludgeling, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(sludgeling, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.render(sludgeling, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySludgeling entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : sludgelingTexture;
    }

    @Override
    protected void preRenderCallback(EntitySludgeling entity, MatrixStack matrixStackIn, float par2)
    {
        super.preRenderCallback(entity, matrixStackIn, par2);
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs(matrixStackIn);
        }
    }
}
