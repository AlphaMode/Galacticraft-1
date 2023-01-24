package micdoodle8.mods.galacticraft.core.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.api.client.IScreenManager;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.render.RenderPlanet;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GameScreenBasic implements IGameScreen
{
    private TextureManager textureManager;

    private float frameA;
    private float frameBx;
    private float frameBy;
    private float textureAx = 0F;
    private float textureAy = 0F;
    private float textureBx = 1.0F;
    private float textureBy = 1.0F;

    public GameScreenBasic()
    {
        //This can be called from either server or client, so don't include
        //client-LogicalSide only code on the server.
        if (GCCoreUtil.getEffectiveSide().isClient())
        {
            textureManager = Minecraft.getInstance().textureManager;
        }
    }

    @Override
    public void setFrameSize(float frameSize)
    {
        this.frameA = frameSize;
    }

    @Override
    public void render(MatrixStack matrixStackIn, int type, float ticks, float scaleX, float scaleY, IScreenManager scr)
    {
        frameBx = scaleX - frameA;
        frameBy = scaleY - frameA;

        if (scaleX == scaleY)
        {
            textureAx = 0F;
            textureAy = 0F;
            textureBx = 1.0F;
            textureBy = 1.0F;
        }
        else if (scaleX < scaleY)
        {
            textureAx = (1.0F - (scaleX / scaleY)) / 2F;
            textureAy = 0F;
            textureBx = 1.0F - textureAx;
            textureBy = 1.0F;
        }
        else if (scaleY < scaleX)
        {
            textureAx = 0F;
            textureAy = (1.0F - (scaleY / scaleX)) / 2F;
            textureBx = 1.0F;
            textureBy = 1.0F - textureAy;
        }

        switch (type)
        {
        case 0:
            drawBlackBackground(0.09F);
            break;
        case 1:
            if (scr instanceof DrawGameScreen && ((DrawGameScreen) scr).mapDone)
            {
                RenderSystem.bindTexture(DrawGameScreen.reusableMap.getGlTextureId());
                draw2DTexture();
            }
            else if (ClientProxyCore.overworldTexturesValid)
            {
                matrixStackIn.push();
                float centreX = scaleX / 2;
                float centreY = scaleY / 2;
                matrixStackIn.translate(centreX, centreY, 0F);
                RenderPlanet.renderPlanet(matrixStackIn, ClientProxyCore.overworldTextureWide.getGlTextureId(), Math.min(scaleX, scaleY) - 0.2F, ticks, 45F);
                matrixStackIn.pop();
            }
            else
            {
                this.textureManager.bindTexture(new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/earth.png"));
                if (!ClientProxyCore.overworldTextureRequestSent)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, GCCoreUtil.getDimensionType(Minecraft.getInstance().world), new Object[]{}));
                    ClientProxyCore.overworldTextureRequestSent = true;
                }
////                 Overworld texture is 48x48 in a 64x64 .png file
                draw2DTexture();
            }
            break;
        }
    }

    private void draw2DTexture()
    {
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        worldRenderer.pos(frameA, frameBy, 0F).tex(textureAx, textureBy).endVertex();
        worldRenderer.pos(frameBx, frameBy, 0F).tex(textureBx, textureBy).endVertex();
        worldRenderer.pos(frameBx, frameA, 0F).tex(textureBx, textureAy).endVertex();
        worldRenderer.pos(frameA, frameA, 0F).tex(textureAx, textureAy).endVertex();
        tess.draw();
    }

    private void drawBlackBackground(float greyLevel)
    {
        RenderSystem.disableLighting();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
        RenderSystem.disableTexture();
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        RenderSystem.color4f(greyLevel, greyLevel, greyLevel, 1.0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        worldRenderer.pos(frameA, frameBy, 0.005F).endVertex();
        worldRenderer.pos(frameBx, frameBy, 0.005F).endVertex();
        worldRenderer.pos(frameBx, frameA, 0.005F).endVertex();
        worldRenderer.pos(frameA, frameA, 0.005F).endVertex();
        tess.draw();

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableTexture();
        RenderSystem.enableLighting();
    }
}
