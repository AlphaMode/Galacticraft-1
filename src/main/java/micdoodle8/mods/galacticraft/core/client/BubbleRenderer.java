package micdoodle8.mods.galacticraft.core.client;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.List;

public class BubbleRenderer
{
    public static IBakedModel sphere;
    private static final List<IBubbleProvider> bubbleProviders = Lists.newArrayList();

    public static void clearBubbles()
    {
        bubbleProviders.clear();
    }

    public static void addBubble(IBubbleProvider tile)
    {
        bubbleProviders.add(tile);
    }

    public static void renderBubbles(MatrixStack matrixStackIn)
    {
        if (bubbleProviders.isEmpty())
        {
            return;
        }

        Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

        matrixStackIn.push();

        RenderSystem.enableRescaleNormal();
        RenderSystem.enableBlend();
        RenderSystem.disableLighting();
        RenderSystem.disableCull();

        RenderSystem.defaultAlphaFunc();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.matrixMode(GL11.GL_TEXTURE);
        RenderSystem.loadIdentity();
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        RenderSystem.depthMask(false);
        float lightMapSaveX = GlStateManager.lastBrightnessX;
        float lightMapSaveY = GlStateManager.lastBrightnessY;
        RenderSystem.glMultiTexCoord2f(GL13.GL_TEXTURE1, 240.0F, 240.0F);

        for (IBubbleProvider dimension : bubbleProviders)
        {
            TileEntity tile = (TileEntity) dimension;
            if (!dimension.getBubbleVisible())
            {
                continue;
            }

            matrixStackIn.push();

            Vec3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
            double x = tile.getPos().getX() - projectedView.getX();
            double y = tile.getPos().getY() - projectedView.getY();
            double z = tile.getPos().getZ() - projectedView.getZ();

            matrixStackIn.translate(x + 0.5F, y + 1.0F, z + 0.5F);
            matrixStackIn.scale(dimension.getBubbleSize(), dimension.getBubbleSize(), dimension.getBubbleSize());

            Vector3 colorVec = dimension.getColor();
            ClientUtil.drawBakedModelColored(sphere, Minecraft.getInstance().getRenderTypeBuffers().getBufferSource(), matrixStackIn, 15728880, colorVec.z, colorVec.y, colorVec.x, 30 / 255F);

            matrixStackIn.pop();
        }

        RenderSystem.glMultiTexCoord2f(GL13.GL_TEXTURE1, lightMapSaveX, lightMapSaveY);
        RenderSystem.depthMask(true);
        matrixStackIn.pop();
    }
}
