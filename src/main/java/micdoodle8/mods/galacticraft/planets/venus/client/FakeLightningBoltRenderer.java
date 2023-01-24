package micdoodle8.mods.galacticraft.planets.venus.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class FakeLightningBoltRenderer
{
    public static void renderBolt(MatrixStack matrixStackIn, long boltVertex, float x, float y, float z)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        RenderSystem.disableTexture();
        RenderSystem.disableLighting();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        float[] adouble = new float[8];
        float[] adouble1 = new float[8];
        float d0 = 0.0F;
        float d1 = 0.0F;
        Random random = new Random(boltVertex);

        for (int i = 7; i >= 0; --i)
        {
            adouble[i] = d0;
            adouble1[i] = d1;
            d0 += random.nextInt(11) - 5;
            d1 += random.nextInt(11) - 5;
        }

        Matrix4f last = matrixStackIn.getLast().getMatrix();
        for (int k1 = 0; k1 < 4; ++k1)
        {
            Random random1 = new Random(boltVertex);

            for (int j = 0; j < 3; ++j)
            {
                int k = 7;
                int l = 0;

                if (j > 0)
                {
                    k = 7 - j;
                }

                if (j > 0)
                {
                    l = k - 2;
                }

                float d2 = adouble[k] - d0;
                float d3 = adouble1[k] - d1;

                for (int i1 = k; i1 >= l; --i1)
                {
                    float d4 = d2;
                    float d5 = d3;

                    if (j == 0)
                    {
                        d2 += random1.nextInt(11) - 5;
                        d3 += random1.nextInt(11) - 5;
                    }
                    else
                    {
                        d2 += random1.nextInt(31) - 15;
                        d3 += random1.nextInt(31) - 15;
                    }

                    worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
                    float d6 = 0.1F + (float) k1 * 0.2F;

                    if (j == 0)
                    {
                        d6 *= (float) i1 * 0.1D + 1.0D;
                    }

                    float d7 = 0.1F + (float) k1 * 0.2F;

                    if (j == 0)
                    {
                        d7 *= (float) (i1 - 1) * 0.1D + 1.0D;
                    }

                    for (int j1 = 0; j1 < 5; ++j1)
                    {
                        float d8 = x + 0.5F - d6;
                        float d9 = z + 0.5F - d6;

                        if (j1 == 1 || j1 == 2)
                        {
                            d8 += d6 * 2.0D;
                        }

                        if (j1 == 2 || j1 == 3)
                        {
                            d9 += d6 * 2.0D;
                        }

                        float d10 = x + 0.5F - d7;
                        float d11 = z + 0.5F - d7;

                        if (j1 == 1 || j1 == 2)
                        {
                            d10 += d7 * 2.0D;
                        }

                        if (j1 == 2 || j1 == 3)
                        {
                            d11 += d7 * 2.0D;
                        }

                        worldrenderer.pos(last, d10 + d2, y + (i1 * 16), d11 + d3).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
                        worldrenderer.pos(last, d8 + d4, y + ((i1 + 1) * 16), d9 + d5).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
                    }

                    tessellator.draw();
                }
            }
        }

        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableBlend();
        RenderSystem.enableLighting();
        RenderSystem.enableTexture();
    }
}
