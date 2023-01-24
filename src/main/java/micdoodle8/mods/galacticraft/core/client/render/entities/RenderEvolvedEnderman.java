package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedEndermanEyes;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedEndermanHeldBlock;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedEnderman;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class RenderEvolvedEnderman extends MobRenderer<EntityEvolvedEnderman, ModelEvolvedEnderman>
{
    private static final ResourceLocation endermanTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/evolved_enderman.png");
    private final ModelEvolvedEnderman endermanModel;
    private final Random rnd = new Random();
    private boolean texSwitch;

    public RenderEvolvedEnderman(EntityRendererManager manager)
    {
        super(manager, new ModelEvolvedEnderman(), 0.5F);
        this.endermanModel = super.entityModel;
        this.addLayer(new LayerEvolvedEndermanEyes(this));
        this.addLayer(new LayerEvolvedEndermanHeldBlock(this));
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEvolvedEnderman entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedEnderman.endermanTexture;
    }

    @Override
    public void render(EntityEvolvedEnderman entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        BlockState iblockstate = entity.getHeldBlockState();
        this.endermanModel.isCarrying = iblockstate != null;
        this.endermanModel.isAttacking = entity.isScreaming();

        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }

    public Vec3d getRenderOffset(EntityEvolvedEnderman entityIn, float partialTicks) {
        if (entityIn.isScreaming()) {
            return new Vec3d(this.rnd.nextGaussian() * 0.02D, 0.0D, this.rnd.nextGaussian() * 0.02D);
        } else {
            return super.getRenderOffset(entityIn, partialTicks);
        }
    }

    @Override
    protected void preRenderCallback(EntityEvolvedEnderman entity, MatrixStack matrixStack, float partialTickTime)
    {
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs(matrixStack);
        }
    }
}