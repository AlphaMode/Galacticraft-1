package micdoodle8.mods.galacticraft.core.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.api.client.IScreenManager;
import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.render.RenderPlanet;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.dimension.Dimension;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

public class GameScreenCelestial implements IGameScreen
{
    private TextureManager textureManager;

    private float frameA;
    private float frameBx;
    private float frameBy;
    private float centreX;
    private float centreY;
    private float scale;

    private final int lineSegments = 90;
    private final float cos = MathHelper.cos(Constants.twoPI / lineSegments);
    private final float sin = MathHelper.sin(Constants.twoPI / lineSegments);

    private DoubleBuffer planes;


    public GameScreenCelestial()
    {
        if (GCCoreUtil.getEffectiveSide().isClient())
        {
            textureManager = Minecraft.getInstance().textureManager;
            planes = BufferUtils.createDoubleBuffer(4 * Double.SIZE);
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
        centreX = scaleX / 2;
        centreY = scaleY / 2;
        frameBx = scaleX - frameA;
        frameBy = scaleY - frameA;
        this.scale = Math.max(scaleX, scaleY) - 0.2F;

        drawBlackBackground(matrixStackIn, 0.0F);

        planeEquation(frameA, frameA, 0, frameA, frameBy, 0, frameA, frameBy, 1);
        GL11.glClipPlane(GL11.GL_CLIP_PLANE0, planes);
        GL11.glEnable(GL11.GL_CLIP_PLANE0);
        planeEquation(frameBx, frameBy, 0, frameBx, frameA, 0, frameBx, frameA, 1);
        GL11.glClipPlane(GL11.GL_CLIP_PLANE1, planes);
        GL11.glEnable(GL11.GL_CLIP_PLANE1);
        planeEquation(frameA, frameBy, 0, frameBx, frameBy, 0, frameBx, frameBy, 1);
        GL11.glClipPlane(GL11.GL_CLIP_PLANE2, planes);
        GL11.glEnable(GL11.GL_CLIP_PLANE2);
        planeEquation(frameBx, frameA, 0, frameA, frameA, 0, frameA, frameA, 1);
        GL11.glClipPlane(GL11.GL_CLIP_PLANE3, planes);
        GL11.glEnable(GL11.GL_CLIP_PLANE3);

        switch (type)
        {
        case 2:
            Dimension wp = scr.getWorldProvider();
            CelestialBody body = null;
            if (wp instanceof IGalacticraftDimension)
            {
                body = ((IGalacticraftDimension) wp).getCelestialBody();
            }
            if (body == null)
            {
                body = GalacticraftCore.planetOverworld;
            }
            drawCelestialBodies(matrixStackIn, body, ticks);
            break;
        case 3:
            drawCelestialBodiesZ(matrixStackIn, GalacticraftCore.planetOverworld, ticks);
            break;
        case 4:
            drawPlanetsTest(matrixStackIn, ticks);
            break;
        }

        GL11.glDisable(GL11.GL_CLIP_PLANE3);
        GL11.glDisable(GL11.GL_CLIP_PLANE2);
        GL11.glDisable(GL11.GL_CLIP_PLANE1);
        GL11.glDisable(GL11.GL_CLIP_PLANE0);
    }

    private void drawBlackBackground(MatrixStack matrixStackIn, float greyLevel)
    {
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableTexture();
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        RenderSystem.color4f(greyLevel, greyLevel, greyLevel, 1.0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        Matrix4f last = matrixStackIn.getLast().getMatrix();
        worldRenderer.pos(last, frameA, frameBy, 0.005F).endVertex();
        worldRenderer.pos(last, frameBx, frameBy, 0.005F).endVertex();
        worldRenderer.pos(last, frameBx, frameA, 0.005F).endVertex();
        worldRenderer.pos(last, frameA, frameA, 0.005F).endVertex();
        tess.draw();

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableTexture();
    }

    private void drawCelestialBodies(MatrixStack matrixStackIn, CelestialBody body, float ticks)
    {
        Star star = null;
        SolarSystem solarSystem = null;
        if (body instanceof Planet)
        {
            solarSystem = ((Planet) body).getParentSolarSystem();
        }
        else if (body instanceof Moon)
        {
            solarSystem = ((Moon) body).getParentPlanet().getParentSolarSystem();
        }
        else if (body instanceof Satellite)
        {
            solarSystem = ((Satellite) body).getParentPlanet().getParentSolarSystem();
        }

        if (solarSystem == null)
        {
            solarSystem = GalacticraftCore.solarSystemSol;
        }
        star = solarSystem.getMainStar();

        if (star != null && star.getBodyIcon() != null)
        {
            this.drawCelestialBody(matrixStackIn, star, 0F, 0F, ticks, 6F);
        }

        String mainSolarSystem = solarSystem.getUnlocalizedName();
        for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
        {
            if (planet.getParentSolarSystem() != null && planet.getBodyIcon() != null)
            {
                if (planet.getParentSolarSystem().getUnlocalizedName().equalsIgnoreCase(mainSolarSystem))
                {
                    Vector3 pos = this.getCelestialBodyPosition(planet, ticks);
                    this.drawCircle(matrixStackIn, planet);
                    this.drawCelestialBody(matrixStackIn, planet, pos.x, pos.y, ticks, (planet.getRelativeDistanceFromCenter().unScaledDistance < 1.5F) ? 2F : 2.8F);
                }
            }
        }
    }

    private void drawCelestialBodiesZ(MatrixStack matrixStackIn, CelestialBody planet, float ticks)
    {
        this.drawCelestialBody(matrixStackIn, planet, 0F, 0F, ticks, 11F);

        for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
        {
            if (moon.getParentPlanet() == planet && moon.getBodyIcon() != null)
            {
                Vector3 pos = this.getCelestialBodyPosition(moon, ticks);
                this.drawCircle(matrixStackIn, moon);
                this.drawCelestialBody(matrixStackIn, moon, pos.x, pos.y, ticks, 4F);
            }
        }

        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
        {
            if (satellite.getParentPlanet() == planet)
            {
                Vector3 pos = this.getCelestialBodyPosition(satellite, ticks);
                this.drawCircle(matrixStackIn, satellite);
                this.drawCelestialBody(matrixStackIn, satellite, pos.x, pos.y, ticks, 3F);
            }
        }
    }

    private void drawTexturedRectCBody(MatrixStack matrixStackIn, float x, float y, float width, float height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        Matrix4f last = matrixStackIn.getLast().getMatrix();
        worldRenderer.pos(last, x, y + height, 0F).tex(0F, 1.0F).endVertex();
        worldRenderer.pos(last, x + width, y + height, 0F).tex(1.0F, 1.0F).endVertex();
        worldRenderer.pos(last, x + width, y, 0F).tex(1.0F, 0F).endVertex();
        worldRenderer.pos(last, x, y, 0F).tex(0F, 0F).endVertex();
        tessellator.draw();
    }

    private void drawCelestialBody(MatrixStack matrixStackIn, CelestialBody planet, float xPos, float yPos, float ticks, float relSize)
    {
        if (xPos + centreX > frameBx || xPos + centreX < frameA)
        {
            return;
        }
        if (yPos + centreY > frameBy || yPos + centreY < frameA)
        {
            return;
        }

        matrixStackIn.push();
        matrixStackIn.translate(xPos + centreX, yPos + centreY, 0F);

        float alpha = 1.0F;

        CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(planet, planet.getBodyIcon(), 12, matrixStackIn);
        MinecraftForge.EVENT_BUS.post(preEvent);

        RenderSystem.color4f(1, 1, 1, alpha);
        if (preEvent.celestialBodyTexture != null)
        {
            this.textureManager.bindTexture(preEvent.celestialBodyTexture);
        }

        if (!preEvent.isCanceled())
        {
            float size = relSize / 70 * scale;
            this.drawTexturedRectCBody(matrixStackIn, -size / 2, -size / 2, size, size);
        }

        CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(planet, matrixStackIn);
        MinecraftForge.EVENT_BUS.post(postEvent);

        matrixStackIn.pop();
    }

    private void drawCircle(MatrixStack matrixStackIn, CelestialBody cBody)
    {
        matrixStackIn.push();
        matrixStackIn.translate(centreX, centreY, 0.002F);
        RenderSystem.disableTexture();

        float sd = 0.002514F * scale;
        float x = this.getScale(cBody);
        float y = 0;
        float grey = 0.1F + 0.65F * Math.max(0F, (0.5F - x));
        x = x * scale / sd;

        RenderSystem.color4f(grey, grey, grey, 1.0F);
        RenderSystem.lineWidth(0.002F);

        matrixStackIn.scale(sd, sd, sd);
        CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre(cBody, new Vector3(0.0F, 0.0F, 0.0F), matrixStackIn);
        MinecraftForge.EVENT_BUS.post(preEvent);

        if (!preEvent.isCanceled())
        {
            GL11.glBegin(GL11.GL_LINE_LOOP);

            float temp;
            for (int i = 0; i < lineSegments; i++)
            {
                GL11.glVertex2f(x, y);

                temp = x;
                x = cos * x - sin * y;
                y = sin * temp + cos * y;
            }

            GL11.glEnd();
        }

        CelestialBodyRenderEvent.CelestialRingRenderEvent.Post postEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Post(cBody, matrixStackIn);
        MinecraftForge.EVENT_BUS.post(postEvent);

        RenderSystem.enableTexture();
        matrixStackIn.pop();
    }

    private Vector3 getCelestialBodyPosition(CelestialBody cBody, float ticks)
    {
        float timeScale = cBody instanceof Planet ? 200.0F : 2.0F;
        float distanceFromCenter = this.getScale(cBody) * scale;
        return new Vector3((float) Math.sin(ticks / (timeScale * cBody.getRelativeOrbitTime()) + cBody.getPhaseShift()) * distanceFromCenter, (float) Math.cos(ticks / (timeScale * cBody.getRelativeOrbitTime()) + cBody.getPhaseShift()) * distanceFromCenter, 0);
    }

    private float getScale(CelestialBody celestialBody)
    {
        float distance = celestialBody.getRelativeDistanceFromCenter().unScaledDistance;
        if (distance >= 1.375F)
        {
            if (distance >= 1.5F)
            {
                distance *= 1.15F;
            }
            else
            {
                distance += 0.075F;
            }
        }
        return 1 / 140.0F * distance * (celestialBody instanceof Planet ? 25.0F : 3.5F);
    }

    private void planeEquation(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3)
    {
        double[] result = new double[4];
        result[0] = y1 * (z2 - z3) + y2 * (z3 - z1) + y3 * (z1 - z2);
        result[1] = z1 * (x2 - x3) + z2 * (x3 - x1) + z3 * (x1 - x2);
        result[2] = x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2);
        result[3] = -(x1 * (y2 * z3 - y3 * z2) + x2 * (y3 * z1 - y1 * z3) + x3 * (y1 * z2 - y2 * z1));
        planes.put(result, 0, 4);
        planes.position(0);
    }

    private void drawPlanetsTest(MatrixStack matrixStackIn, float ticks)
    {
        matrixStackIn.push();
        matrixStackIn.translate(centreX, centreY, 0F);

        int id = (int) (ticks / 600F) % 5;
        RenderPlanet.renderID(matrixStackIn, id, scale, ticks);
        matrixStackIn.pop();
    }

    private void drawTexturedRectUV(MatrixStack matrixStackIn, float x, float y, float width, float height, float ticks)
    {
        for (int ysect = 0; ysect < 6; ysect++)
        {
//    		drawTexturedRectUVSixth(x, y, width, height, (ticks / 600F) % 1F, ysect / 6F);
            float angle = 7.5F + 15F * ysect;
            drawTexturedRectUVSixth(matrixStackIn, x, y, width, height, (ticks / (900F - 80F * MathHelper.cos(angle))) % 1F, ysect / 6F);
        }
    }

    private void drawTexturedRectUVSixth(MatrixStack matrixStackIn, float x, float y, float width, float height, float prog, float y0)
    {
        y0 /= 2;
        prog = 1.0F - prog;
        float y1 = y0 + 1 / 12F;
        float y2 = 1F - y1;
        float y3 = 1F - y0;
        float yaa = y + height * y0;
        float yab = y + height * y1;
        float yba = y + height * y2;
        float ybb = y + height * y3;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        Matrix4f last = matrixStackIn.getLast().getMatrix();
        if (prog <= 0.75F)
        {
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(last, x, yab, 0F).tex(prog, y1).endVertex();
            worldRenderer.pos(last, x + width, yab, 0F).tex(prog + 0.25F, y1).endVertex();
            worldRenderer.pos(last, x + width, yaa, 0F).tex(prog + 0.25F, y0).endVertex();
            worldRenderer.pos(last, x, yaa, 0F).tex(prog, y0).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(last, x, ybb, 0F).tex(prog, y3).endVertex();
            worldRenderer.pos(last, x + width, ybb, 0F).tex(prog + 0.25F, y3).endVertex();
            worldRenderer.pos(last, x + width, yba, 0F).tex(prog + 0.25F, y2).endVertex();
            worldRenderer.pos(last, x, yba, 0F).tex(prog, y2).endVertex();
            tessellator.draw();
        }
        else
        {
            float xp = x + width * (1F - prog) / 0.25F;
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(last, x, yab, 0F).tex(prog, y1).endVertex();
            worldRenderer.pos(last, xp, yab, 0F).tex(1.0F, y1).endVertex();
            worldRenderer.pos(last, xp, yaa, 0F).tex(1.0F, y0).endVertex();
            worldRenderer.pos(last, x, yaa, 0F).tex(prog, y0).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(last, x, ybb, 0F).tex(prog, y3).endVertex();
            worldRenderer.pos(last, xp, ybb, 0F).tex(1.0F, y3).endVertex();
            worldRenderer.pos(last, xp, yba, 0F).tex(1.0F, y2).endVertex();
            worldRenderer.pos(last, x, yba, 0F).tex(prog, y2).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(last, xp, yab, 0F).tex(0F, y1).endVertex();
            worldRenderer.pos(last, x + width, yab, 0F).tex(prog - 0.75F, y1).endVertex();
            worldRenderer.pos(last, x + width, yaa, 0F).tex(prog - 0.75F, y0).endVertex();
            worldRenderer.pos(last, xp, yaa, 0F).tex(0F, y0).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(last, xp, ybb, 0F).tex(0F, y3).endVertex();
            worldRenderer.pos(last, x + width, ybb, 0F).tex(prog - 0.75F, y3).endVertex();
            worldRenderer.pos(last, x + width, yba, 0F).tex(prog - 0.75F, y2).endVertex();
            worldRenderer.pos(last, xp, yba, 0F).tex(0F, y2).endVertex();
            tessellator.draw();
        }
    }
}
