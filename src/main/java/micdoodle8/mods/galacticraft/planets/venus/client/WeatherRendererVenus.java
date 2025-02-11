package micdoodle8.mods.galacticraft.planets.venus.client;

import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.IRenderHandler;

import java.lang.reflect.Field;
import java.util.Random;

public class WeatherRendererVenus implements IRenderHandler
{
    private final Random random = new Random();
    private final float[] rainXCoords = new float[1024];
    private final float[] rainYCoords = new float[1024];
    private static final ResourceLocation RAIN_TEXTURES = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/misc/rain_venus.png");

    public WeatherRendererVenus()
    {
        for (int i = 0; i < 32; ++i)
        {
            float f1 = (float) (i - 16);
            for (int j = 0; j < 32; ++j)
            {
                float f = (float) (j - 16);
                float f2 = MathHelper.sqrt(f * f + f1 * f1);
                this.rainXCoords[i << 5 | j] = -f1 / f2;
                this.rainYCoords[i << 5 | j] = f / f2;
            }
        }
    }

    @Override
    public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc)
    {
        float strength = world.getRainStrength(partialTicks);

        if (strength > 0.0F)
        {
            int rendererUpdateCount = 0;
            try
            {
                Field fieldRUC = mc.gameRenderer.getClass().getDeclaredField(GCCoreUtil.isDeobfuscated() ? "rendererUpdateCount" : "field_78529_t");
                fieldRUC.setAccessible(true);
                rendererUpdateCount = fieldRUC.getInt(mc.gameRenderer);
            }
            catch (Exception e)
            {
            }
            mc.gameRenderer.getLightTexture().enableLightmap();
            Entity entity = mc.getRenderViewEntity();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder worldrenderer = tessellator.getBuffer();
            RenderSystem.disableCull();
            RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.defaultAlphaFunc();
            double d0 = entity.lastTickPosX + (entity.getPosX() - entity.lastTickPosX) * (double) partialTicks;
            double d1 = entity.lastTickPosY + (entity.getPosY() - entity.lastTickPosY) * (double) partialTicks;
            double d2 = entity.lastTickPosZ + (entity.getPosZ() - entity.lastTickPosZ) * (double) partialTicks;
            int l = MathHelper.floor(d1);

            int r = 4;
            if (mc.gameSettings.fancyGraphics)
            {
                r = 8;
            }

            int drawFlag = -1;
//            worldrenderer.setTranslation(-d0, -d1, -d2);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            int px = MathHelper.floor(entity.getPosX());
            int py = MathHelper.floor(entity.getPosY());
            int pz = MathHelper.floor(entity.getPosZ());

            for (int z = pz - r; z <= pz + r; ++z)
            {
                int indexZ = (z - pz + 16) * 32;
                for (int x = px - r; x <= px + r; ++x)
                {
                    mutablePos.setPos(x, 0, z);
                    int yHeight = world.getHeight(Heightmap.Type.MOTION_BLOCKING, mutablePos).getY() + 4 - (int) (4.8F * strength);
                    int y = py - r;
                    int ymax = py + r;

                    if (y < yHeight)
                    {
                        y = yHeight;
                    }

                    if (ymax < yHeight)
                    {
                        ymax = yHeight;
                    }

                    int yBase = yHeight;

                    if (yHeight < l)
                    {
                        yBase = l;
                    }

                    if (y != ymax)
                    {
                        this.random.setSeed(x * x * 3121 + x * 45238971 ^ z * z * 418711 + z * 13761);

                        if (drawFlag != 0)
                        {
                            if (drawFlag >= 0)
                            {
                                tessellator.draw();
                            }

                            drawFlag = 0;
                            mc.getTextureManager().bindTexture(RAIN_TEXTURES);
                            worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        int index = indexZ + x - px + 16;
                        double dx = (double) this.rainXCoords[index] * 0.5D;
                        double dz = (double) this.rainYCoords[index] * 0.5D;
                        float dy = -((rendererUpdateCount + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31) + partialTicks) / 80.0F * (3.0F + this.random.nextFloat());
                        double yo = this.random.nextDouble() / 1.8D;
                        double xx = x + 0.5D - entity.getPosX();
                        double zz = z + 0.5D - entity.getPosZ();
                        float rr = MathHelper.sqrt(xx * xx + zz * zz) / r;
                        float alpha = ((1.0F - rr * rr) * 0.5F + 0.5F) * strength / 0.6F;  //0.6F is the max rainstrength on Venus
                        mutablePos.setPos(x, yBase, z);
                        int light = WorldRenderer.getCombinedLight(world, mutablePos);
                        int lx = light >> 16 & 65535;
                        int ly = light & 65535;
                        double xc = x + 0.5D;
                        double zc = z + 0.5D;
                        worldrenderer.pos(xc - dx, (double) ymax - yo, zc - dz).tex(0.0F, y * 0.25F + dy).color(1.0F, 1.0F, 1.0F, alpha).lightmap(lx, ly).endVertex();
                        worldrenderer.pos(xc + dx, (double) ymax - yo, zc + dz).tex(1.0F, y * 0.25F + dy).color(1.0F, 1.0F, 1.0F, alpha).lightmap(lx, ly).endVertex();
                        worldrenderer.pos(xc + dx, (double) y - yo, zc + dz).tex(1.0F, ymax * 0.25F + dy).color(1.0F, 1.0F, 1.0F, alpha).lightmap(lx, ly).endVertex();
                        worldrenderer.pos(xc - dx, (double) y - yo, zc - dz).tex(0.0F, ymax * 0.25F + dy).color(1.0F, 1.0F, 1.0F, alpha).lightmap(lx, ly).endVertex();
                    }
                }
            }

            if (drawFlag >= 0)
            {
                tessellator.draw();
            }

//            worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.defaultAlphaFunc();
            mc.gameRenderer.getLightTexture().disableLightmap();
        }
    }
}
