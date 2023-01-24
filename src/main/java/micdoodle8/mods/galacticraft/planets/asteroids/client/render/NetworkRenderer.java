package micdoodle8.mods.galacticraft.planets.asteroids.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class NetworkRenderer
{
    public static void renderNetworks(MatrixStack matrixStackIn, World world, float partialTicks)
    {
        List<TileEntityBeamOutput> nodes = new ArrayList<TileEntityBeamOutput>();

        for (Object o : new ArrayList<TileEntity>(world.loadedTileEntityList))
        {
            if (o instanceof TileEntityBeamOutput)
            {
                nodes.add((TileEntityBeamOutput) o);
            }
        }

        if (nodes.isEmpty())
        {
            return;
        }

        Tessellator tess = Tessellator.getInstance();
        ClientPlayerEntity player = Minecraft.getInstance().player;
        double interpPosX = player.lastTickPosX + (player.getPosX() - player.lastTickPosX) * partialTicks;
        double interpPosY = player.lastTickPosY + (player.getPosY() - player.lastTickPosY) * partialTicks;
        double interpPosZ = player.lastTickPosZ + (player.getPosZ() - player.lastTickPosZ) * partialTicks;

        RenderSystem.disableTexture();

        for (TileEntityBeamOutput tileEntity : nodes)
        {
            if (tileEntity.getTarget() == null)
            {
                continue;
            }

            matrixStackIn.push();

            Vector3 outputPoint = tileEntity.getOutputPoint(true);
            Vector3 targetInputPoint = tileEntity.getTarget().getInputPoint();

            Vector3 direction = Vector3.subtract(outputPoint, targetInputPoint);
            float directionLength = direction.getMagnitude();

            float posX = (float) (tileEntity.getPos().getX() - interpPosX);
            float posY = (float) (tileEntity.getPos().getY() - interpPosY);
            float posZ = (float) (tileEntity.getPos().getZ() - interpPosZ);
            matrixStackIn.translate(posX, posY, posZ);

            matrixStackIn.translate(outputPoint.floatX() - tileEntity.getPos().getX(), outputPoint.floatY() - tileEntity.getPos().getY(), outputPoint.floatZ() - tileEntity.getPos().getZ());
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntity.yaw + 180));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-tileEntity.pitch));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(tileEntity.ticks * 10));

            tess.getBuffer().begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

            Matrix4f last = matrixStackIn.getLast().getMatrix();
            for (Direction dir : Direction.values())
            {
                tess.getBuffer().pos(last, dir.getXOffset() / 40.0F, dir.getYOffset() / 40.0F, dir.getZOffset() / 40.0F).color(tileEntity.getColor().floatX(), tileEntity.getColor().floatY(), tileEntity.getColor().floatZ(), 1.0F).endVertex();
                tess.getBuffer().pos(last, dir.getXOffset() / 40.0F, dir.getYOffset() / 40.0F, directionLength + dir.getZOffset() / 40.0F).color(tileEntity.getColor().floatX(), tileEntity.getColor().floatY(), tileEntity.getColor().floatZ(), 1.0F).endVertex();
            }

            tess.draw();

            matrixStackIn.pop();
        }

        RenderSystem.enableTexture();

        RenderSystem.color4f(1, 1, 1, 1);
    }
}
