package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RenderSmallAsteroid extends EntityRenderer<EntitySmallAsteroid>
{
    public RenderSmallAsteroid(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void render(EntitySmallAsteroid asteroid, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        RenderSystem.disableRescaleNormal();

        matrixStackIn.push();
        matrixStackIn.translate(0F, 0.5F, 0F);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(asteroid.rotationPitch));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(asteroid.rotationYaw));

        this.renderManager.textureManager.bindTexture(getEntityTexture(asteroid));
        dispatcher.renderBlock(AsteroidBlocks.rock0.getDefaultState(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);

        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySmallAsteroid entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
