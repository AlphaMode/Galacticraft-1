package micdoodle8.mods.galacticraft.core.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FootprintRenderer
{
    public static Map<Long, List<Footprint>> footprints = new ConcurrentHashMap<Long, List<Footprint>>();
    private static final ResourceLocation footprintTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/footprint.png");

    public static void renderFootprints(MatrixStack matrixStackIn, PlayerEntity player, float partialTicks)
    {
        DimensionType dimActive = GCCoreUtil.getDimensionType(player.world);
        List<Footprint> footprintsToDraw = new LinkedList<>();

        for (List<Footprint> footprintList : footprints.values())
        {
            for (Footprint footprint : footprintList)
            {
                if (footprint.dimension == dimActive)
                {
                    footprintsToDraw.add(footprint);
                }
            }
        }

        if (footprintsToDraw.isEmpty())
        {
            return;
        }

        matrixStackIn.push();
        Minecraft.getInstance().textureManager.bindTexture(FootprintRenderer.footprintTexture);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.disableCull();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tessellator tessellator = Tessellator.getInstance();
        float f7 = 1.0F;
        float f6 = 0.0F;
        float f8 = 0.0F;
        float f9 = 1.0F;

        float f10 = 0.4F;
        RenderSystem.defaultAlphaFunc();
        float lightMapSaveX = GlStateManager.lastBrightnessX;
        float lightMapSaveY = GlStateManager.lastBrightnessY;
        boolean sensorGlasses = OverlaySensorGlasses.overrideMobTexture();
        if (sensorGlasses)
        {
            RenderSystem.glMultiTexCoord2f(GL13.GL_TEXTURE1, 240.0F, 240.0F);
        }

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (Footprint footprint : footprintsToDraw)
        {
            matrixStackIn.push();

            if (!sensorGlasses)
            {
                int j = footprint.lightmapVal % 65536;
                int k = footprint.lightmapVal / 65536;
                RenderSystem.glMultiTexCoord2f(GL13.GL_TEXTURE1, (float) j, (float) k);
            }

            float ageScale = footprint.age / (float) Footprint.MAX_AGE;
            BufferBuilder worldRenderer = tessellator.getBuffer();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            Vec3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
            double x = (footprint.position.x - projectedView.getX());
            double y = (footprint.position.y - projectedView.getY()) + 0.001F;
            double z = (footprint.position.z - projectedView.getZ());

            matrixStackIn.translate(x, y, z);

            int brightness = (int) (100 + ageScale * 155);
            //                    worldRenderer.putBrightness4(brightness, brightness, brightness, brightness);
            RenderSystem.color4f(1F - ageScale, 1F - ageScale, 1F - ageScale, 1F - ageScale);
            float footprintScale = 0.5F;
            Matrix4f last = matrixStackIn.getLast().getMatrix();
            worldRenderer.pos(last, MathHelper.sin((45F - footprint.rotation) / Constants.RADIANS_TO_DEGREES) * footprintScale, 0, MathHelper.cos((45 - footprint.rotation) / Constants.RADIANS_TO_DEGREES) * footprintScale).tex(f7, f9).endVertex();
            worldRenderer.pos(last, MathHelper.sin((135 - footprint.rotation) / Constants.RADIANS_TO_DEGREES) * footprintScale, 0, MathHelper.cos((135 - footprint.rotation) / Constants.RADIANS_TO_DEGREES) * footprintScale).tex(f7, f8).endVertex();
            worldRenderer.pos(last, MathHelper.sin((225 - footprint.rotation) / Constants.RADIANS_TO_DEGREES) * footprintScale, 0, MathHelper.cos((225 - footprint.rotation) / Constants.RADIANS_TO_DEGREES) * footprintScale).tex(f6, f8).endVertex();
            worldRenderer.pos(last, MathHelper.sin((315 - footprint.rotation) / Constants.RADIANS_TO_DEGREES) * footprintScale, 0, MathHelper.cos((315 - footprint.rotation) / Constants.RADIANS_TO_DEGREES) * footprintScale).tex(f6, f9).endVertex();

            tessellator.draw();
            matrixStackIn.pop();
        }

//        if (sensorGlasses)
//        {
//            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
//        }

        matrixStackIn.pop();
    }

    public static void addFootprint(long chunkKey, Footprint footprint)
    {
        List<Footprint> footprintList = footprints.get(chunkKey);

        if (footprintList == null)
        {
            footprintList = new ArrayList<Footprint>();
        }

        footprintList.add(new Footprint(footprint.dimension, footprint.position, footprint.rotation, footprint.owner, footprint.lightmapVal));
        footprints.put(chunkKey, footprintList);
    }

    public static void addFootprint(long chunkKey, DimensionType dimension, Vector3 position, float rotation, String owner, int lightmapVal)
    {
        addFootprint(chunkKey, new Footprint(dimension, position, rotation, owner, lightmapVal));
    }

    public static void setFootprints(long chunkKey, List<Footprint> prints)
    {
        List<Footprint> footprintList = footprints.get(chunkKey);

        if (footprintList == null)
        {
            footprintList = new ArrayList<Footprint>();
        }

        Iterator<Footprint> i = footprintList.iterator();
        while (i.hasNext())
        {
            Footprint print = i.next();
            if (!print.owner.equals(Minecraft.getInstance().player.getName()))
            {
                i.remove();
            }
        }

        footprintList.addAll(prints);
        footprints.put(chunkKey, footprintList);
    }
}
