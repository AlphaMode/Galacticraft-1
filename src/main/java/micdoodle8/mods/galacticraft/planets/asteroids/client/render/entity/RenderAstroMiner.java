package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;
import java.util.Random;

public class RenderAstroMiner extends EntityRenderer<EntityAstroMiner>
{
    private static final float LSIZE = 0.12F;
    private static final float RETRACTIONSPEED = 0.02F;
    private float lastPartTime;

    public static ResourceLocation scanTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/misc/gradient.png");
    private static IBakedModel mainModel;
    private static IBakedModel hoverPadMain;
    private static IBakedModel hoverPadGlow;
    private static IBakedModel mainModelInactive;
    private static IBakedModel modellaser1;
    private static IBakedModel modellaser3;
    private static IBakedModel modellasergl;

    private final NoiseModule wobbleX;
    private final NoiseModule wobbleY;
    private final NoiseModule wobbleZ;
    private final NoiseModule wobbleXX;
    private final NoiseModule wobbleYY;
    private final NoiseModule wobbleZZ;

    public static void updateModels()
    {
        mainModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/astro_miner_full.obj"), ImmutableList.of("Hull", "Lasers"));
        hoverPadMain = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/astro_miner_full.obj"), ImmutableList.of("HoverPad"));
        hoverPadGlow = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/astro_miner_full.obj"), ImmutableList.of("Glow"));
        modellaser1 = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/astro_miner_full.obj"), ImmutableList.of("Main_Laser_Front"));
        modellaser3 = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/astro_miner_full.obj"), ImmutableList.of("Main_Laser_Center"));
        modellasergl = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/astro_miner_full.obj"), ImmutableList.of("Main_Laser_Left_Guard"));
        mainModelInactive = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/astro_miner_full.obj"), ImmutableList.of("Hull", "Lasers", "HoverPad"));
    }

    public RenderAstroMiner(EntityRendererManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 2F;

        Random rand = new Random();
        this.wobbleX = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleX.amplitude = 0.5F;
        this.wobbleX.frequencyX = 0.025F;

        this.wobbleY = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleY.amplitude = 0.6F;
        this.wobbleY.frequencyX = 0.025F;

        this.wobbleZ = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleZ.amplitude = 0.1F;
        this.wobbleZ.frequencyX = 0.025F;

        this.wobbleXX = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleXX.amplitude = 0.1F;
        this.wobbleXX.frequencyX = 0.8F;

        this.wobbleYY = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleYY.amplitude = 0.15F;
        this.wobbleYY.frequencyX = 0.8F;

        this.wobbleZZ = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleZZ.amplitude = 0.04F;
        this.wobbleZZ.frequencyX = 0.8F;
        GCModelCache.INSTANCE.reloadCallback(RenderAstroMiner::updateModels);
    }

    @Override
    public void render(EntityAstroMiner astroMiner, float entityYaw, float partialTickTime, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        int ais = astroMiner.AIstate;
        boolean active = ais > EntityAstroMiner.AISTATE_ATBASE;
        float time = astroMiner.ticksExisted + partialTickTime;
        float sinOfTheTime = (MathHelper.sin(time / 4) + 1F) / 4F + 0.5F;
        float wx = active ? this.wobbleX.getNoise(time) + this.wobbleXX.getNoise(time) : 0F;
        float wy = active ? this.wobbleY.getNoise(time) + this.wobbleYY.getNoise(time) : 0F;
        float wz = active ? this.wobbleZ.getNoise(time) + this.wobbleZZ.getNoise(time) : 0F;
        float partTime = partialTickTime - this.lastPartTime;
        this.lastPartTime = partialTickTime;

        while (partTime < 0)
        {
            partTime += 1F;
        }

        this.renderManager.textureManager.bindTexture(getEntityTexture(astroMiner));

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        RenderSystem.enableRescaleNormal();
        matrixStackIn.push();

        final float rotPitch = astroMiner.prevRotationPitch + (astroMiner.rotationPitch - astroMiner.prevRotationPitch) * partialTickTime;
        final float rotYaw = astroMiner.prevRotationYaw + (astroMiner.rotationYaw - astroMiner.prevRotationYaw) * partialTickTime;

        matrixStackIn.translate(0F, 1.4F, 0F);
        float partBlock;

        switch (astroMiner.facing)
        {
        case DOWN:
            partBlock = (float) (astroMiner.getPosY() % 2D);
            break;
        case UP:
            partBlock = 1F - (float) (astroMiner.getPosY() % 2D);
            break;
        case NORTH:
            partBlock = (float) (astroMiner.getPosZ() % 2D);
            break;
        case SOUTH:
            partBlock = 1F - (float) (astroMiner.getPosZ() % 2D);
            break;
        case WEST:
            partBlock = (float) (astroMiner.getPosX() % 2D);
            break;
        case EAST:
            partBlock = 1F - (float) (astroMiner.getPosX() % 2D);
            break;
        default:
            partBlock = 0F;
        }
        partBlock /= 0.06F;

        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotYaw + 180F));

        if (rotPitch != 0F)
        {
            matrixStackIn.translate(-0.65F, -0.65F, 0);
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(rotPitch / 4F));
            matrixStackIn.translate(0.65F, 0.65F, 0);
        }

        matrixStackIn.translate(0F, -0.42F, 0.28F);
        matrixStackIn.scale(0.0495F, 0.0495F, 0.0495F);
        matrixStackIn.translate(wx, wy, wz);

        if (active)
        {
            ClientUtil.drawBakedModel(mainModel, bufferIn, matrixStackIn, packedLightIn);
            this.renderLaserModel(matrixStackIn, bufferIn, packedLightIn, astroMiner.retraction);

            float lightMapSaveX = GlStateManager.lastBrightnessX;
            float lightMapSaveY = GlStateManager.lastBrightnessY;
            RenderSystem.glMultiTexCoord2f(GL13.GL_TEXTURE1, 240.0F, 240.0F);
            RenderSystem.disableLighting();
            RenderSystem.color4f(sinOfTheTime, sinOfTheTime, sinOfTheTime, 1.0F);
            ClientUtil.drawBakedModel(hoverPadMain, bufferIn, matrixStackIn, packedLightIn);

            RenderSystem.disableCull();
            RenderSystem.disableAlphaTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            RenderSystem.enableBlend();
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            RenderSystem.color4f(sinOfTheTime, sinOfTheTime, sinOfTheTime, 0.6F);
            ClientUtil.drawBakedModel(hoverPadGlow, bufferIn, matrixStackIn, packedLightIn);
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            if (ais < EntityAstroMiner.AISTATE_DOCKING)
            {
                //This is the scanning lasers:
                Minecraft.getInstance().textureManager.bindTexture(scanTexture);
                final Tessellator tess = Tessellator.getInstance();
                RenderSystem.color4f(0, 0.6F, 1.0F, 0.2F);
                BufferBuilder worldRenderer = tess.getBuffer();
                float scanProgress = (MathHelper.cos(partBlock * 0.012F * 6.283F)) * 0.747F;
                float scanAngle = 0.69866F - scanProgress * scanProgress;
                float scanEndX = 38.77F * MathHelper.sin(scanAngle);
                float scanEndY = 32F;
                float scanEndZ = 38.77F * MathHelper.cos(scanAngle);
                scanEndZ += 20F;
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                worldRenderer.pos(15.6F, -0.6F, -20F).tex(0F, 0F).endVertex();
                worldRenderer.pos(15.6F + scanEndX, scanEndY - 0.6F, -scanEndZ).tex(1F, 0F).endVertex();
                worldRenderer.pos(15.6F + scanEndX, -0.6F - scanEndY, -scanEndZ).tex(1F, 1F).endVertex();
                worldRenderer.pos(15.6F, -0.7F, -20F).tex(0F, 1F).endVertex();
                tess.draw();
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                worldRenderer.pos(-15.6F, -0.6F, -20F).tex(0F, 0F).endVertex();
                worldRenderer.pos(-15.6F - scanEndX, scanEndY - 0.6F, -scanEndZ).tex(1F, 0F).endVertex();
                worldRenderer.pos(-15.6F - scanEndX, -0.6F - scanEndY, -scanEndZ).tex(1F, 1F).endVertex();
                worldRenderer.pos(-15.6F, -0.7F, -20F).tex(0F, 1F).endVertex();
                tess.draw();

                int removeCount = 0;
                int afterglowCount = 0;
                matrixStackIn.pop();
                matrixStackIn.push();
                matrixStackIn.translate((float) astroMiner.getPosX(), (float) astroMiner.getPosY(), (float) astroMiner.getPosZ());

                for (Integer blockTime : new ArrayList<Integer>(astroMiner.laserTimes))
                {
                    if (blockTime < astroMiner.ticksExisted - 19)
                    {
                        removeCount++;
                    }
                    else if (blockTime < astroMiner.ticksExisted - 3)
                    {
                        afterglowCount++;
                    }
                }
                if (removeCount > 0)
                {
                    astroMiner.removeLaserBlocks(removeCount);
                }
                int count = 0;
                for (BlockVec3 blockLaser : new ArrayList<BlockVec3>(astroMiner.laserBlocks))
                {
                    if (count < afterglowCount)
                    {
                        int fade = astroMiner.ticksExisted - astroMiner.laserTimes.get(count) - 8;
                        if (fade < 0)
                        {
                            fade = 0;
                        }
                        this.doAfterGlow(matrixStackIn, blockLaser, fade);
                    }
                    else
                    {
                        this.doLaser(matrixStackIn, astroMiner, blockLaser);
                    }
                    count++;
                }
                if (astroMiner.retraction > 0F)
                {
                    astroMiner.retraction -= RETRACTIONSPEED * partTime;
                    if (astroMiner.retraction < 0F)
                    {
                        astroMiner.retraction = 0F;
                    }
                }
                matrixStackIn.pop();
            }
            else
            {
                if (astroMiner.retraction < 1F)
                {
                    astroMiner.retraction += RETRACTIONSPEED * partTime;
                    if (astroMiner.retraction > 1F)
                    {
                        astroMiner.retraction = 1F;
                    }
                }
                matrixStackIn.pop();
            }
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.enableCull();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableLighting();
            RenderSystem.depthMask(true);
            RenderSystem.glMultiTexCoord2f(GL13.GL_TEXTURE1, lightMapSaveX, lightMapSaveY);
        }
        else
        {
            this.renderManager.textureManager.bindTexture(getEntityTexture(astroMiner));
            ClientUtil.drawBakedModel(mainModelInactive, bufferIn, matrixStackIn, packedLightIn);
            this.renderLaserModel(matrixStackIn, bufferIn, packedLightIn, astroMiner.retraction);
            if (astroMiner.retraction < 1F)
            {
                astroMiner.retraction += RETRACTIONSPEED * partTime;
                if (astroMiner.retraction > 1F)
                {
                    astroMiner.retraction = 1F;
                }
            }
            matrixStackIn.pop();
        }
    }

    private void doAfterGlow(MatrixStack matrixStackIn, BlockVec3 blockLaser, int level)
    {
        matrixStackIn.push();
        matrixStackIn.translate(blockLaser.x, blockLaser.y, blockLaser.z);
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        RenderSystem.color4f(1.0F, 0.7F, 0.7F, 0.016667F * (12 - level));
        float cA = -0.01F;
        float cB = 1.01F;
        Matrix4f last = matrixStackIn.getLast().getMatrix();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, cA, cB, cA).tex(0F, 1F).endVertex();
        worldRenderer.pos(last, cB, cB, cA).tex(1F, 1F).endVertex();
        worldRenderer.pos(last, cB, cB, cB).tex(1F, 0F).endVertex();
        worldRenderer.pos(last, cA, cB, cB).tex(0F, 0F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, cA, cA, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(last, cA, cA, cB).tex(0F, 1F).endVertex();
        worldRenderer.pos(last, cB, cA, cB).tex(1F, 1F).endVertex();
        worldRenderer.pos(last, cB, cA, cA).tex(1F, 0F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, cA, cA, cA).tex(1F, 0F).endVertex();
        worldRenderer.pos(last, cA, cB, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(last, cA, cB, cB).tex(0F, 1F).endVertex();
        worldRenderer.pos(last, cA, cA, cB).tex(1F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, cB, cA, cA).tex(1F, 1F).endVertex();
        worldRenderer.pos(last, cB, cA, cB).tex(1F, 0F).endVertex();
        worldRenderer.pos(last, cB, cB, cB).tex(0F, 0F).endVertex();
        worldRenderer.pos(last, cB, cB, cA).tex(0F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, cA, cA, cA).tex(1F, 0F).endVertex();
        worldRenderer.pos(last, 1F, cA, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(last, 1F, 1F, cA).tex(0F, 1F).endVertex();
        worldRenderer.pos(last, cA, 1F, cA).tex(1F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, 1F, cA, 1F).tex(1F, 1F).endVertex();
        worldRenderer.pos(last, cA, cA, 1F).tex(1F, 0F).endVertex();
        worldRenderer.pos(last, cA, 1F, 1F).tex(0F, 0F).endVertex();
        worldRenderer.pos(last, 1F, 1F, 1F).tex(0F, 1F).endVertex();
        tess.draw();
        matrixStackIn.pop();
    }

    private void doLaser(MatrixStack matrixStackIn, EntityAstroMiner entity, BlockVec3 blockLaser)
    {
        matrixStackIn.push();
        matrixStackIn.translate(blockLaser.x, blockLaser.y, blockLaser.z);
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        RenderSystem.color4f(1.0F, 0.7F, 0.7F, 0.2F);
        float cA = -0.01F;
        float cB = 1.01F;
        Matrix4f last = matrixStackIn.getLast().getMatrix();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, cA, cB, cA).tex(0F, 1F).endVertex();
        worldRenderer.pos(last, cB, cB, cA).tex(1F, 1F).endVertex();
        worldRenderer.pos(last, cB, cB, cB).tex(1F, 0F).endVertex();
        worldRenderer.pos(last, cA, cB, cB).tex(0F, 0F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, cA, cA, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(last, cA, cA, cB).tex(0F, 1F).endVertex();
        worldRenderer.pos(last, cB, cA, cB).tex(1F, 1F).endVertex();
        worldRenderer.pos(last, cB, cA, cA).tex(1F, 0F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, cA, cA, cA).tex(1F, 0F).endVertex();
        worldRenderer.pos(last, cA, cB, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(last, cA, cB, cB).tex(0F, 1F).endVertex();
        worldRenderer.pos(last, cA, cA, cB).tex(1F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, cB, cA, cA).tex(1F, 1F).endVertex();
        worldRenderer.pos(last, cB, cA, cB).tex(1F, 0F).endVertex();
        worldRenderer.pos(last, cB, cB, cB).tex(0F, 0F).endVertex();
        worldRenderer.pos(last, cB, cB, cA).tex(0F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, cA, cA, cA).tex(1F, 0F).endVertex();
        worldRenderer.pos(last, 1F, cA, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(last, 1F, 1F, cA).tex(0F, 1F).endVertex();
        worldRenderer.pos(last, cA, 1F, cA).tex(1F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(last, 1F, cA, 1F).tex(1F, 1F).endVertex();
        worldRenderer.pos(last, cA, cA, 1F).tex(1F, 0F).endVertex();
        worldRenderer.pos(last, cA, 1F, 1F).tex(0F, 0F).endVertex();
        worldRenderer.pos(last, 1F, 1F, 1F).tex(0F, 1F).endVertex();
        tess.draw();

        RenderSystem.color4f(1.0F, 0.79F, 0.79F, 0.17F);
        float bb = 1.7F;
        float cc = 0.4F;
        float radiansYaw = entity.rotationYaw / Constants.RADIANS_TO_DEGREES;
        float radiansPitch = entity.rotationPitch / Constants.RADIANS_TO_DEGREES / 4F;
        float mainLaserX = bb * MathHelper.sin(radiansYaw) * MathHelper.cos(radiansPitch);
        float mainLaserY = cc + bb * MathHelper.sin(radiansPitch);
        float mainLaserZ = bb * MathHelper.cos(radiansYaw) * MathHelper.cos(radiansPitch);

        mainLaserX += entity.getPosX() - blockLaser.x;
        mainLaserY += entity.getPosY() - blockLaser.y;
        mainLaserZ += entity.getPosZ() - blockLaser.z;

        float xD = mainLaserX - 0.5F;
        float yD = mainLaserY - 0.5F;
        float zD = mainLaserZ - 0.5F;
        float xx, yy, zz;

        if (entity.facing.getIndex() > Direction.SOUTH.getIndex())
        {
            xx = xD < 0 ? cA : cB;
            this.drawLaserX(matrixStackIn, mainLaserX, mainLaserY, mainLaserZ, xx, 0.5F, 0.5F);
        }
        else if (entity.facing.getIndex() <= Direction.UP.getIndex())
        {
            yy = yD < 0 ? cA : cB;
            this.drawLaserY(matrixStackIn, mainLaserX, mainLaserY, mainLaserZ, 0.5F, yy, 0.5F);
        }
        else
        {
            zz = zD < 0 ? cA : cB;
            this.drawLaserZ(matrixStackIn, mainLaserX, mainLaserY, mainLaserZ, 0.5F, 0.5F, zz);
        }

        matrixStackIn.pop();
    }

    private void drawLaserX(MatrixStack matrixStackIn, float x1, float y1, float z1, float x2, float y2, float z2)
    {
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        Matrix4f last = matrixStackIn.getLast().getMatrix();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, x1, y1 - 0.01F, z1 - 0.01F).endVertex();
        worldRenderer.pos(last, x2, y2 - LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(last, x2, y2 + LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(last, x1, y1 + 0.01F, z1 - 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, x1, y1 - 0.01F, z1 + 0.01F).endVertex();
        worldRenderer.pos(last, x2, y2 - LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(last, x2, y2 + LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(last, x1, y1 + 0.01F, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, x1, y1 - 0.01F, z1 - 0.01F).endVertex();
        worldRenderer.pos(last, x2, y2 - LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(last, x2, y2 - LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(last, x1, y1 - 0.01F, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, x1, y1 + 0.01F, z1 + 0.01F).endVertex();
        worldRenderer.pos(last, x2, y2 + LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(last, x2, y2 + LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(last, x1, y1 + 0.01F, z1 - 0.01F).endVertex();
        tess.draw();
    }

    private void drawLaserY(MatrixStack matrixStackIn, float x1, float y1, float z1, float x2, float y2, float z2)
    {
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        Matrix4f last = matrixStackIn.getLast().getMatrix();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, x1 - 0.01F, y1, z1 - 0.01F).endVertex();
        worldRenderer.pos(last, x2 - LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(last, x2 + LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(last, x1 + 0.01F, y1, z1 - 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, x1 - 0.01F, y1, z1 + 0.01F).endVertex();
        worldRenderer.pos(last, x2 - LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(last, x2 + LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(last, x1 + 0.01F, y1, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, x1 - 0.01F, y1, z1 - 0.01F).endVertex();
        worldRenderer.pos(last, x2 - LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(last, x2 - LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(last, x1 - 0.01F, y1, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, x1 + 0.01F, y1, z1 + 0.01F).endVertex();
        worldRenderer.pos(last, x2 + LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(last, x2 + LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(last, x1 + 0.01F, y1, z1 - 0.01F).endVertex();
        tess.draw();
    }

    private void drawLaserZ(MatrixStack matrixStackIn, float x1, float y1, float z1, float x2, float y2, float z2)
    {
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        Matrix4f last = matrixStackIn.getLast().getMatrix();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 - 0.01F, y1 - 0.01F, z1).endVertex();
        worldRenderer.pos(last, x2 - LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(last, x2 - LSIZE, y2 + LSIZE, z2).endVertex();
        worldRenderer.pos(last, x1 - 0.01F, y1 + 0.01F, z1).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, x1 + 0.01F, y1 - 0.01F, z1).endVertex();
        worldRenderer.pos(last, x2 + LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(last, x2 + LSIZE, y2 + LSIZE, z2).endVertex();
        worldRenderer.pos(last, x1 + 0.01F, y1 + 0.01F, z1).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, x1 - 0.01F, y1 - 0.01F, z1).endVertex();
        worldRenderer.pos(last, x2 - LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(last, x2 + LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(last, x1 + 0.01F, y1 - 0.01F, z1).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(last, x1, y1 + 0.01F, z1 + 0.01F).endVertex();
        worldRenderer.pos(last, x2, y2 + LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(last, x2, y2 + LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(last, x1, y1 + 0.01F, z1 - 0.01F).endVertex();
        tess.draw();
    }

    private void renderLaserModel(MatrixStack matrixStackIn, IRenderTypeBuffer buffer, int light, float retraction)
    {
        float laserretraction = retraction / 0.8F;
        if (laserretraction > 1F)
        {
            laserretraction = 1F;
        }
        float guardmovement = (retraction - 0.6F) / 0.4F * 1.875F;
        if (guardmovement < 0F)
        {
            guardmovement = 0F;
        }
        matrixStackIn.push();
        float zadjust = laserretraction * 5F;
        float yadjust = zadjust;

        if (yadjust > 0.938F)
        {
            yadjust = 0.938F;
            zadjust = (zadjust - yadjust) * 2.5F + yadjust;
        }
        matrixStackIn.translate(0F, yadjust, zadjust);
        ClientUtil.drawBakedModel(modellaser1, buffer, matrixStackIn, light);
        if (yadjust == 0.938F)
        {
            //Do not move laser centre into body
            matrixStackIn.translate(0F, 0F, -zadjust + 0.938F);
        }
        ClientUtil.drawBakedModel(modellaser3, buffer, matrixStackIn, light);
        matrixStackIn.pop();
        matrixStackIn.push();
        matrixStackIn.translate(guardmovement, 0F, 0F);
        ClientUtil.drawBakedModel(modellasergl, buffer, matrixStackIn, light);
        matrixStackIn.translate(-2 * guardmovement + 8.75F, 0F, 0F);
        ClientUtil.drawBakedModel(modellasergl, buffer, matrixStackIn, light);
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityAstroMiner entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public boolean shouldRender(EntityAstroMiner miner, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        return miner.isInRangeToRender3d(camX, camY, camZ);
    }
}
