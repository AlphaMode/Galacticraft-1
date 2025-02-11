package micdoodle8.mods.galacticraft.core.fluid;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;

import java.util.function.UnaryOperator;

public class GCFluids
{
    public static final GCFluidRegistry FLUIDS = new GCFluidRegistry();

    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> OIL = registerLiquid("oil", fluidAttributes -> fluidAttributes.color(0xFF111111).density(800).viscosity(1500));
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> FUEL = registerLiquid("fuel", fluidAttributes -> fluidAttributes.color(0xFFDBDF16).density(400).viscosity(900));
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> OXYGEN = registerLiquid("oxygen", fluidAttributes -> fluidAttributes.color(0xFF6CE2FF).temperature(1).density(13).viscosity(295).gaseous());
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> HYDROGEN = registerLiquid("hydrogen", fluidAttributes -> fluidAttributes.color(0xFFFFFFFF).temperature(1).density(1).viscosity(295).gaseous());

    private static FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> registerLiquid(String name, UnaryOperator<FluidAttributes.Builder> fluidAttributes)
    {
        return FLUIDS.register(name, fluidAttributes.apply(FluidAttributes.builder(new ResourceLocation(Constants.MOD_ID_CORE, "block/liquid/liquid"),
                new ResourceLocation(Constants.MOD_ID_CORE, "block/liquid/liquid_flow"))), Material.WATER);
    }
}
