package micdoodle8.mods.galacticraft.planets.asteroids.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.SkyRenderHandler;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class SkyProviderAsteroids implements SkyRenderHandler
{
    private static final ResourceLocation overworldTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/earth.png");
    private static final ResourceLocation galaxyTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/planets/galaxy.png");
    private static final ResourceLocation sunTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/planets/orbitalsun.png");

    public VertexBuffer starVBO;
    public VertexBuffer skyVBO;
    public VertexBuffer sky2VBO;

    private final float sunSize;

    public SkyProviderAsteroids(IGalacticraftDimension asteroidsProvider)
    {
        this.sunSize = 17.5F * asteroidsProvider.getSolarSize();

        final Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        this.starVBO = new VertexBuffer(DefaultVertexFormats.POSITION);
        this.renderStars(worldRenderer);
        this.starVBO.upload(worldRenderer);
        this.skyVBO = new VertexBuffer(DefaultVertexFormats.POSITION);
        final byte byte2 = 64;
        final int i = 256 / byte2 + 2;
        float f = 16F;

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        for (int j = -byte2 * i; j <= byte2 * i; j += byte2)
        {
            for (int l = -byte2 * i; l <= byte2 * i; l += byte2)
            {

                worldRenderer.pos(j + 0, f, l + 0).endVertex();
                worldRenderer.pos(j + byte2, f, l + 0).endVertex();
                worldRenderer.pos(j + byte2, f, l + byte2).endVertex();
                worldRenderer.pos(j + 0, f, l + byte2).endVertex();
            }
        }

        worldRenderer.finishDrawing();
        this.skyVBO.upload(worldRenderer);
        this.sky2VBO = new VertexBuffer(DefaultVertexFormats.POSITION);
        f = -16F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        for (int k = -byte2 * i; k <= byte2 * i; k += byte2)
        {
            for (int i1 = -byte2 * i; i1 <= byte2 * i; i1 += byte2)
            {
                worldRenderer.pos(k + byte2, f, i1 + 0).endVertex();
                worldRenderer.pos(k + 0, f, i1 + 0).endVertex();
                worldRenderer.pos(k + 0, f, i1 + byte2).endVertex();
                worldRenderer.pos(k + byte2, f, i1 + byte2).endVertex();
            }
        }

        worldRenderer.finishDrawing();
        this.sky2VBO.upload(worldRenderer);
    }

    @Override
    public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc)
    {
        float var10;
        float var11;
        float var12;
        final Tessellator var23 = Tessellator.getInstance();
        BufferBuilder worldRenderer = var23.getBuffer();

        RenderSystem.disableTexture();
        RenderSystem.disableRescaleNormal();
        RenderSystem.color3f(1F, 1F, 1F);
        RenderSystem.depthMask(false);
        RenderSystem.enableFog();
        RenderSystem.color3f(0, 0, 0);
        this.skyVBO.bindBuffer();
        DefaultVertexFormats.POSITION.setupBufferState(0L);
        this.skyVBO.draw(matrixStack.getLast().getMatrix(), GL11.GL_QUADS);
        VertexBuffer.unbindBuffer();
        DefaultVertexFormats.POSITION.clearBufferState();
        RenderSystem.disableFog();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();

        RenderSystem.disableTexture();
        RenderSystem.color4f(0.7F, 0.7F, 0.7F, 0.7F);
        this.starVBO.bindBuffer();
        DefaultVertexFormats.POSITION.setupBufferState(0L);
        this.starVBO.draw(matrixStack.getLast().getMatrix(), GL11.GL_QUADS);
        VertexBuffer.unbindBuffer();
        DefaultVertexFormats.POSITION.clearBufferState();

        matrixStack.push();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        // Sun:
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(world.getCelestialAngle(partialTicks) * 360.0F));
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableTexture();
        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
        var12 = this.sunSize / 4.2F;
        Matrix4f last = matrixStack.getLast().getMatrix();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, -var12, 90.0F, -var12).endVertex();
        worldRenderer.pos(last, var12, 90.0F, -var12).endVertex();
        worldRenderer.pos(last, var12, 90.0F, var12).endVertex();
        worldRenderer.pos(last, -var12, 90.0F, var12).endVertex();
        var23.draw();
        RenderSystem.enableTexture();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        var12 = this.sunSize / 1.2F;
        //110 distance instead of the normal 100, because there is no atmosphere to make the disk seem larger
        Minecraft.getInstance().textureManager.bindTexture(SkyProviderAsteroids.sunTexture);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, -var12, 90.0F, -var12).tex(0.0F, 0.0F).endVertex();
        worldRenderer.pos(last, var12, 90.0F, -var12).tex(1.0F, 0.0F).endVertex();
        worldRenderer.pos(last, var12, 90.0F, var12).tex(1.0F, 1.0F).endVertex();
        worldRenderer.pos(last, -var12, 90.0F, var12).tex(0.0F, 1.0F).endVertex();
        var23.draw();

        matrixStack.pop();

        matrixStack.push();

        // HOME:
        var12 = 0.5F;
        matrixStack.scale(0.6F, 0.6F, 0.6F);
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(40.0F));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(200F));
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
        Minecraft.getInstance().textureManager.bindTexture(SkyProviderAsteroids.overworldTexture);
        Matrix4f last2 = matrixStack.getLast().getMatrix();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last2, -var12, -100.0F, var12).tex(0, 1.0F).endVertex();
        worldRenderer.pos(last2, var12, -100.0F, var12).tex(1.0F, 1.0F).endVertex();
        worldRenderer.pos(last2, var12, -100.0F, -var12).tex(1.0F, 0).endVertex();
        worldRenderer.pos(last2, -var12, -100.0F, -var12).tex(0, 0).endVertex();
        var23.draw();

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableFog();
        matrixStack.pop();

        RenderSystem.disableTexture();
        RenderSystem.color3f(0.0F, 0.0F, 0.0F);
        final double var25 = mc.player.getPosition().getY() - world.getHorizonHeight();

        //		if (var25 < 0.0D)
        //		{
        //			GL11.glPushMatrix();
        //			GL11.glTranslatef(0.0F, 12.0F, 0.0F);
        //			GL11.glCallList(this.glSkyList2);
        //			GL11.glPopMatrix();
        //			var10 = 1.0F;
        //			var11 = -((float) (var25 + 65.0D));
        //			var12 = -var10;
        //			var23.startDrawingQuads();
        //			var23.setColorRGBA_I(0, 255);
        //			var23.addVertex(-var10, var11, var10);
        //			var23.addVertex(var10, var11, var10);
        //			var23.addVertex(var10, var12, var10);
        //			var23.addVertex(-var10, var12, var10);
        //			var23.addVertex(-var10, var12, -var10);
        //			var23.addVertex(var10, var12, -var10);
        //			var23.addVertex(var10, var11, -var10);
        //			var23.addVertex(-var10, var11, -var10);
        //			var23.addVertex(var10, var12, -var10);
        //			var23.addVertex(var10, var12, var10);
        //			var23.addVertex(var10, var11, var10);
        //			var23.addVertex(var10, var11, -var10);
        //			var23.addVertex(-var10, var11, -var10);
        //			var23.addVertex(-var10, var11, var10);
        //			var23.addVertex(-var10, var12, var10);
        //			var23.addVertex(-var10, var12, -var10);
        //			var23.addVertex(-var10, var12, -var10);
        //			var23.addVertex(-var10, var12, var10);
        //			var23.addVertex(var10, var12, var10);
        //			var23.addVertex(var10, var12, -var10);
        //			var23.draw();
        //		}

        RenderSystem.color3f(70F / 256F, 70F / 256F, 70F / 256F);

        //		GL11.glPushMatrix();
        //		GL11.glTranslatef(0.0F, -((float) (var25 - 16.0D)), 0.0F);
        //		GL11.glCallList(this.glSkyList2);
        //		GL11.glPopMatrix();
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);

        RenderSystem.enableColorMaterial();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableBlend();
    }

    private void renderStars(BufferBuilder worldRenderer)
    {
        final Random var1 = new Random(10842L);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        for (int var3 = 0; var3 < (ConfigManagerCore.moreStars.get() ? 35000 : 6000); ++var3)
        {
            double var4 = var1.nextFloat() * 2.0F - 1.0F;
            double var6 = var1.nextFloat() * 2.0F - 1.0F;
            double var8 = var1.nextFloat() * 2.0F - 1.0F;
            final double var10 = 0.08F + var1.nextFloat() * 0.07F;
            double var12 = var4 * var4 + var6 * var6 + var8 * var8;

            if (var12 < 1.0D && var12 > 0.01D)
            {
                var12 = 1.0D / Math.sqrt(var12);
                var4 *= var12;
                var6 *= var12;
                var8 *= var12;
                final double pX = var4 * (ConfigManagerCore.moreStars.get() ? var1.nextDouble() * 75D + 65D : 80.0D);
                final double pY = var6 * (ConfigManagerCore.moreStars.get() ? var1.nextDouble() * 75D + 65D : 80.0D);
                final double pZ = var8 * (ConfigManagerCore.moreStars.get() ? var1.nextDouble() * 75D + 65D : 80.0D);
                final double var20 = Math.atan2(var4, var8);
                final double var22 = Math.sin(var20);
                final double var24 = Math.cos(var20);
                final double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
                final double var28 = Math.sin(var26);
                final double var30 = Math.cos(var26);
                final double var32 = var1.nextDouble() * Math.PI * 2.0D;
                final double var34 = Math.sin(var32);
                final double var36 = Math.cos(var32);

                for (int i = 0; i < 4; ++i)
                {
                    final double i1 = ((i & 2) - 1) * var10;
                    final double i2 = ((i + 1 & 2) - 1) * var10;
                    final double var47 = i1 * var36 - i2 * var34;
                    final double var49 = i2 * var36 + i1 * var34;
                    final double var55 = -var47 * var30;
                    final double dX = var55 * var22 - var49 * var24;
                    final double dZ = var49 * var22 + var55 * var24;
                    final double dY = var47 * var28;
                    worldRenderer.pos(pX + dX, pY + dY, pZ + dZ).endVertex();
                }
            }
        }

        worldRenderer.finishDrawing();
    }

    private Vec3d getCustomSkyColor()
    {
        return new Vec3d(0.26796875D, 0.1796875D, 0.0D);
    }

    public float getSkyBrightness(float par1)
    {
        final float var2 = Minecraft.getInstance().world.getCelestialAngle(par1);
        float var3 = 1.0F - (MathHelper.sin(var2 * Constants.twoPI) * 2.0F + 0.25F);

        if (var3 < 0.0F)
        {
            var3 = 0.0F;
        }

        if (var3 > 1.0F)
        {
            var3 = 1.0F;
        }

        return var3 * var3 * 1F;
    }
}
