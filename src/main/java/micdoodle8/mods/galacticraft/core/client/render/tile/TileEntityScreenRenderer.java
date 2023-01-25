package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockScreen;
import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.nio.FloatBuffer;

@OnlyIn(Dist.CLIENT)
public class TileEntityScreenRenderer extends TileEntityRenderer<TileEntityScreen>
{
    public static final ResourceLocation blockTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/screen_side.png");
    private final TextureManager textureManager = Minecraft.getInstance().textureManager;
    private static final FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);

    private final float yPlane = 0.91F;
    float frame = 0.098F;

    public TileEntityScreenRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityScreen screen, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        matrixStackIn.push();
        // Texture file
        this.textureManager.bindTexture(TileEntityScreenRenderer.blockTexture);

        Direction dir = screen.getBlockState().get(BlockScreen.FACING);

        switch (dir)
        {
        case DOWN:
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180));
            matrixStackIn.translate(0, -1.0F, -1.0F);
            break;
        case UP:
            break;
        case NORTH:
            matrixStackIn.translate(0.0F, 0.0F, -0.87F);
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
            matrixStackIn.translate(0.0F, 0.0F, -1.0F);
            break;
        case SOUTH:
            matrixStackIn.translate(0.0F, 0.0F, 0.87F);
            matrixStackIn.rotate(Vector3f.XN.rotationDegrees(90));
            matrixStackIn.translate(1.0F, -1.0F, 1.0F);
            matrixStackIn.rotate(Vector3f.YN.rotationDegrees(180));
            break;
        case WEST:
            matrixStackIn.translate(-0.87F, 0.0F, 0.0F);
            matrixStackIn.rotate(Vector3f.ZN.rotationDegrees(90));
            matrixStackIn.translate(-1.0F, 0.0F, 1.0F);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
            break;
        case EAST:
            matrixStackIn.translate(0.87F, 0.0F, 0.0F);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90));
            matrixStackIn.translate(1.0F, -1.0F, 0.0F);
            matrixStackIn.rotate(Vector3f.YN.rotationDegrees(90));
            break;
        default:
            break;
        }

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        matrixStackIn.translate(-screen.screenOffsetx, this.yPlane, -screen.screenOffsetz);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
        boolean cornerblock = false;
        if (screen.connectionsLeft == 0 || screen.connectionsRight == 0)
        {
            cornerblock = (screen.connectionsUp == 0 || screen.connectionsDown == 0);
        }
        int totalLR = screen.connectionsLeft + screen.connectionsRight;
        int totalUD = screen.connectionsUp + screen.connectionsDown;
        if (totalLR > 1 && totalUD > 1 && !cornerblock)
        {
            //centre block
            if (screen.connectionsLeft == screen.connectionsRight - (totalLR | 1))
            {
                if (screen.connectionsUp == screen.connectionsDown - (totalUD | 1))
                {
                    cornerblock = true;
                }
            }
        }
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));;
        matrixStackIn.translate(-screen.screen.getScaleX(), 0.0F, 0.0F);
        screen.screen.drawScreen(matrixStackIn, screen.imageType, partialTicks + screen.getWorld().getDayTime(), cornerblock);

        matrixStackIn.pop();
    }

    @Override
    public boolean isGlobalRenderer(TileEntityScreen te) {
        return true;
    }
}
