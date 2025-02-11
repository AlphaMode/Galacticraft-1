//package micdoodle8.mods.galacticraft.core.client.gui.screen;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.ScaledResolution;
//import net.minecraft.client.renderer.Matrix4f;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.BufferBuilder;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.ResourceLocation;
//import org.lwjgl.input.Mouse;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.util.glu.GLU;
//
//public abstract class GuiStarBackground extends Screen
//{
//    private static final ResourceLocation backgroundTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/stars.png");
//    private static final ResourceLocation blackTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/black.png");
//
//    public void drawBlackBackground()
//    {
//        final ScaledResolution var5 = ClientUtil.getScaledRes(this.minecraft, this.minecraft.displayWidth, this.minecraft.displayHeight);
//        final int var6 = var5.getScaledWidth();
//        final int var7 = var5.getScaledHeight();
//        RenderSystem.disableDepthTest();
//        RenderSystem.depthMask(false);
//        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.disableAlphaTest();
//        this.minecraft.getTextureManager().bindTexture(GuiStarBackground.blackTexture);
//        final Tessellator tess = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tess.getBuffer();
//        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer.pos(0.0D, var7, -90.0D).tex(0.0F, 1.0F).endVertex();
//        worldRenderer.pos(var6, var7, -90.0D).tex(1.0F, 1.0F).endVertex();
//        worldRenderer.pos(var6, 0.0D, -90.0D).tex(1.0F, 0.0F).endVertex();
//        worldRenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0F, 0.0F).endVertex();
//        tess.draw();
//        RenderSystem.depthMask(true);
//        RenderSystem.enableDepthTest();
//        RenderSystem.enableAlphaTest();
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    private void drawPanorama2(float par1)
//    {
//        final Tessellator tess = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tess.getBuffer();
//        RenderSystem.matrixMode(GL11.GL_PROJECTION);
//        RenderSystem.pushMatrix();
//        RenderSystem.loadIdentity();
//        RenderSystem.multMatrix(Matrix4f.perspective(120.0F, 1.0F, 0.05F, 10.0F));
//        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
//        RenderSystem.pushMatrix();
//        RenderSystem.loadIdentity();
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
//        RenderSystem.enableBlend();
//        RenderSystem.disableAlphaTest();
//        RenderSystem.disableCull();
//        RenderSystem.depthMask(false);
//        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
//        final byte var5 = 1;
//
//        for (int var6 = 0; var6 < var5 * var5; ++var6)
//        {
//            RenderSystem.pushMatrix();
//            final float var7 = ((float) (var6 % var5) / (float) var5 - 0.5F) / 128.0F;
//            final float var8 = ((float) (var6 / var5) / (float) var5 - 0.5F) / 128.0F;
//            final float var9 = 0.0F;
//
//            double mY;
//            double mX;
//
//            if (minecraft.mouseHelper.getMouseY() < this.height)
//            {
//                mY = (-this.height + minecraft.mouseHelper.getMouseY()) / 100F;
//            }
//            else
//            {
//                mY = (-this.height + minecraft.mouseHelper.getMouseY()) / 100F;
//            }
//
//            mX = (this.width - minecraft.mouseHelper.getMouseX()) / 100F;
//
//            this.doCustomTranslation(0, var7, var8, var9, mX, mY);
//
//            for (int var10 = 0; var10 < 9; ++var10)
//            {
//                RenderSystem.pushMatrix();
//
//                if (var10 == 1)
//                {
//                    RenderSystem.translatef(1.96F, 0.0F, 0.0F);
//                }
//
//                if (var10 == 2)
//                {
//                    RenderSystem.translatef(-1.96F, 0.0F, 0.0F);
//                }
//
//                if (var10 == 3)
//                {
//                    RenderSystem.translatef(0.0F, 1.96F, 0.0F);
//                }
//
//                if (var10 == 4)
//                {
//                    RenderSystem.translatef(0.0F, -1.96F, 0.0F);
//                }
//
//                if (var10 == 5)
//                {
//                    RenderSystem.translatef(-1.96F, -1.96F, 0.0F);
//                }
//
//                if (var10 == 6)
//                {
//                    RenderSystem.translatef(-1.96F, 1.96F, 0.0F);
//                }
//
//                if (var10 == 7)
//                {
//                    RenderSystem.translatef(1.96F, -1.96F, 0.0F);
//                }
//
//                if (var10 == 8)
//                {
//                    RenderSystem.translatef(1.96F, 1.96F, 0.0F);
//                }
//
//                this.minecraft.getTextureManager().bindTexture(GuiStarBackground.backgroundTexture);
//                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
//                worldRenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0F + 1, 0.0F + 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
//                worldRenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0F - 1, 0.0F + 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
//                worldRenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0F - 1, 1.0F - 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
//                worldRenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0F + 1, 1.0F - 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
//                tess.draw();
//                RenderSystem.popMatrix();
//            }
//
//            RenderSystem.popMatrix();
//        }
//
//        worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
//        RenderSystem.matrixMode(GL11.GL_PROJECTION);
//        RenderSystem.popMatrix();
//        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
//        RenderSystem.popMatrix();
//        RenderSystem.depthMask(true);
//        RenderSystem.enableCull();
//        RenderSystem.enableAlphaTest();
//        RenderSystem.enableDepthTest();
//    }
//
//    private void drawPanorama(float par1)
//    {
//        final Tessellator tess = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tess.getBuffer();
//        RenderSystem.matrixMode(GL11.GL_PROJECTION);
//        RenderSystem.pushMatrix();
//        RenderSystem.loadIdentity();
//        RenderSystem.multMatrix(Matrix4f.perspective(120.0F, 1.0F, 0.05F, 10.0F));
//        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
//        RenderSystem.pushMatrix();
//        RenderSystem.loadIdentity();
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
//        RenderSystem.enableBlend();
//        RenderSystem.disableAlphaTest();
//        RenderSystem.disableCull();
//        RenderSystem.depthMask(false);
//        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
//        final byte var5 = 1;
//
//        for (int var6 = 0; var6 < var5 * var5; ++var6)
//        {
//            RenderSystem.pushMatrix();
//            final float var7 = ((float) (var6 % var5) / (float) var5 - 0.5F) / 64.0F;
//            final float var8 = ((float) (var6 / var5) / (float) var5 - 0.5F) / 64.0F;
//            final float var9 = 0.0F;
//
//            double mY;
//            double mX;
//
//            if (minecraft.mouseHelper.getMouseY() < this.height)
//            {
//                mY = (-this.height + minecraft.mouseHelper.getMouseY()) / 100F;
//            }
//            else
//            {
//                mY = (-this.height + minecraft.mouseHelper.getMouseY()) / 100F;
//            }
//
//            mX = (this.width - minecraft.mouseHelper.getMouseX()) / 100F;
//
//            this.doCustomTranslation(1, var7, var8, var9, mX, mY);
//
//            RenderSystem.rotatef(MathHelper.sin(par1 / 1000.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
//            RenderSystem.rotatef(-par1 * 0.005F, 0.0F, 1.0F, 0.0F);
//            RenderSystem.rotatef(41, 0, 0, 1);
//
//            for (int var10 = 0; var10 < 6; ++var10)
//            {
//                RenderSystem.pushMatrix();
//
//                if (var10 == 1)
//                {
//                    RenderSystem.rotatef(90.0F, 0.0F, 1.0F, 0.0F);
//                }
//
//                if (var10 == 2)
//                {
//                    RenderSystem.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
//                }
//
//                if (var10 == 3)
//                {
//                    RenderSystem.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//                }
//
//                if (var10 == 4)
//                {
//                    RenderSystem.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
//                }
//
//                if (var10 == 5)
//                {
//                    RenderSystem.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
//                }
//
//                this.minecraft.getTextureManager().bindTexture(GuiStarBackground.backgroundTexture);
//                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
//                worldRenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0F + 1, 0.0F + 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
//                worldRenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0F - 1, 0.0F + 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
//                worldRenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0F - 1, 1.0F - 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
//                worldRenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0F + 1, 1.0F - 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
//                tess.draw();
//                RenderSystem.popMatrix();
//            }
//
//            RenderSystem.popMatrix();
//        }
//
//        worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
//        RenderSystem.matrixMode(GL11.GL_PROJECTION);
//        RenderSystem.popMatrix();
//        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
//        RenderSystem.popMatrix();
//        RenderSystem.depthMask(true);
//        RenderSystem.enableCull();
//        RenderSystem.enableAlphaTest();
//        RenderSystem.enableDepthTest();
//    }
//
//    private void rotateAndBlurSkybox()
//    {
//        this.minecraft.getTextureManager().bindTexture(GuiStarBackground.backgroundTexture);
//        RenderSystem.enableBlend();
//        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//        RenderSystem.colorMask(true, true, true, false);
//        RenderSystem.pushMatrix();
//        RenderSystem.popMatrix();
//        RenderSystem.colorMask(true, true, true, true);
//    }
//
//    public void renderSkybox()
//    {
//        RenderSystem.viewport(0, 0, this.minecraft.displayWidth, this.minecraft.displayHeight);
//        RenderSystem.pushMatrix();
//        RenderSystem.scalef(1.0F, 0.0F, 1.0F);
//        this.drawPanorama(1);
//        this.drawPanorama2(1);
//        RenderSystem.disableTexture();
//        RenderSystem.enableTexture();
//        this.rotateAndBlurSkybox();
//        final Tessellator tess = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tess.getBuffer();
//        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
//        final float var5 = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
//        final float var6 = this.height * var5 / 256.0F;
//        final float var7 = this.width * var5 / 256.0F;
//        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//        final int var8 = this.width;
//        final int var9 = this.height;
//        worldRenderer.pos(0.0D, var9, this.getBlitOffset()).tex(0.5F - var6, 0.5F + var7).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
//        worldRenderer.pos(var8, var9, this.getBlitOffset()).tex(0.5F - var6, 0.5F - var7).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
//        worldRenderer.pos(var8, 0.0D, this.getBlitOffset()).tex(0.5F + var6, 0.5F - var7).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
//        worldRenderer.pos(0.0D, 0.0D, this.getBlitOffset()).tex(0.5F + var6, 0.5F + var7).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
//        tess.draw();
//        RenderSystem.popMatrix();
//    }
//
//    public abstract void doCustomTranslation(int type, float coord1, float coord2, float coord3, double mX, double mY);
//}
