package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.client.model.ModelJuicer;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityJuicer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderJuicer extends MobRenderer<EntityJuicer, ModelJuicer>
{
    private static final ResourceLocation juicerTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/model/juicer.png");
    private boolean texSwitch;

    public RenderJuicer(EntityRendererManager renderManager)
    {
        super(renderManager, new ModelJuicer(), 0.5F);
    }

    @Override
    protected void preRenderCallback(EntityJuicer entity, MatrixStack matrixStackIn, float partialTickTime)
    {
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entity.isHanging() ? 180.0F : 0.0F));
        matrixStackIn.translate(0.0F, entity.isHanging() ? 1.8F : 1.3F, 0.0F);
        super.preRenderCallback(entity, matrixStackIn, partialTickTime);
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs(matrixStackIn);
        }
    }

    @Override
    public void render(EntityJuicer entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }

    @Override
    public ResourceLocation getEntityTexture(EntityJuicer juicer)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : juicerTexture;
    }
}
