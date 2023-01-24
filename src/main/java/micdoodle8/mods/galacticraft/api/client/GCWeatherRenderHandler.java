package micdoodle8.mods.galacticraft.api.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.client.WeatherRenderHandler;

public interface GCWeatherRenderHandler extends WeatherRenderHandler {
    @Override
    default void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc) {
        RenderSystem.pushMatrix();
        MatrixStack stack = new MatrixStack();
        render(ticks, partialTicks, stack, world, mc);
        RenderSystem.multMatrix(stack.getLast().getMatrix());
        RenderSystem.popMatrix();
    }

    void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc);
}
