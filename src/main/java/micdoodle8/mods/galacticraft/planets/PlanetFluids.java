package micdoodle8.mods.galacticraft.planets;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.fluid.FluidRegistrationEntry;
import micdoodle8.mods.galacticraft.core.fluid.GCFluidRegistry;
import micdoodle8.mods.galacticraft.planets.venus.blocks.SulphuricAcidFluid;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import static net.minecraftforge.fluids.ForgeFlowingFluid.Source;

public class PlanetFluids
{
    public static final GCFluidRegistry FLUIDS = new GCFluidRegistry();

    public static final Material ACID_MATERIAL = new Material(MaterialColor.EMERALD, true, false, false, false, true, false, true, PushReaction.DESTROY);

    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> GAS_METHANE = registerLiquid("methane", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1).viscosity(11).temperature(295).gaseous());
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> GAS_ATMOSPHERIC = registerLiquid("atmosphericgases", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1).viscosity(13).temperature(295).gaseous());
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> GAS_NITROGEN = registerLiquid("nitrogen", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1).viscosity(12).temperature(295).gaseous());
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> GAS_CARBON_DIOXIDE = registerLiquid("carbondioxide", fluidAttributes -> fluidAttributes.color(0xFF111111).density(2).viscosity(20).temperature(295).gaseous());
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> GAS_ARGON = registerLiquid("argon", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1).viscosity(4).temperature(295).gaseous());
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> GAS_HELIUM = registerLiquid("helium", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1).viscosity(1).temperature(295).gaseous());

    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> LIQUID_METHANE = registerLiquid("liquidmethane", fluidAttributes -> fluidAttributes.color(0xFF111111).density(450).viscosity(120).temperature(109));
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> LIQUID_OXYGEN = registerLiquid("liquidoxygen", fluidAttributes -> fluidAttributes.color(0xFF111111).density(1141).viscosity(140).temperature(90));
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> LIQUID_NITROGEN = registerLiquid("liquidnitrogen", fluidAttributes -> fluidAttributes.color(0xFF111111).density(808).viscosity(130).temperature(90));
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> LIQUID_ARGON = registerLiquid("liquidargon", fluidAttributes -> fluidAttributes.color(0xFF111111).density(900).viscosity(100).temperature(87));
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> LIQUID_SULPHURIC_ACID = registerLiquid("sulphuricacid", fluidAttributes -> fluidAttributes.color(0xFF111111).density(6229).viscosity(1400), SulphuricAcidFluid.Source::new, SulphuricAcidFluid.Flowing::new);
    public static final FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> LIQUID_BACTERIAL_SLUDGE = registerLiquid("bacterialsludge", fluidAttributes -> fluidAttributes.color(0xFF111111).density(800).viscosity(1500), ACID_MATERIAL);

    private static FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> registerLiquid(String name, UnaryOperator<FluidAttributes.Builder> fluidAttributes)
    {
        return FLUIDS.register(name, fluidAttributes.apply(FluidAttributes.builder(new ResourceLocation(Constants.MOD_ID_PLANETS, "block/liquid/liquid"),
                new ResourceLocation(Constants.MOD_ID_PLANETS, "block/liquid/liquid_flow"))), Material.WATER);
    }

    private static FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> registerLiquid(String name, UnaryOperator<FluidAttributes.Builder> fluidAttributes, Function<ForgeFlowingFluid.Properties, FlowingFluid> source, Function<ForgeFlowingFluid.Properties, FlowingFluid> flowing)
    {
        return FLUIDS.register(name, fluidAttributes.apply(FluidAttributes.builder(new ResourceLocation(Constants.MOD_ID_PLANETS, "block/liquid/liquid"),
                new ResourceLocation(Constants.MOD_ID_PLANETS, "block/liquid/liquid_flow"))), Material.WATER, source, flowing);
    }

    private static FluidRegistrationEntry<FlowingFluid, FlowingFluid, FlowingFluidBlock, BucketItem> registerLiquid(String name, UnaryOperator<FluidAttributes.Builder> fluidAttributes, Material blockMaterial)
    {
        return FLUIDS.register(name, fluidAttributes.apply(FluidAttributes.builder(new ResourceLocation(Constants.MOD_ID_PLANETS, "block/liquid/liquid"),
                new ResourceLocation(Constants.MOD_ID_PLANETS, "block/liquid/liquid_flow"))), blockMaterial);
    }
}
