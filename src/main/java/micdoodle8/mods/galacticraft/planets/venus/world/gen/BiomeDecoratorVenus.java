//package micdoodle8.mods.galacticraft.planets.venus.world.gen;
//
//import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
//import micdoodle8.mods.galacticraft.core.world.gen.WorldGenMinableMeta;
//import micdoodle8.mods.galacticraft.planets.venus.ConfigManagerVenus;
//import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraft.world.biome.BiomeDecorator;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.gen.feature.Feature;
//import net.minecraftforge.common.MinecraftForge;
//
//import java.util.Random;
//
//public class BiomeDecoratorVenus extends BiomeDecorator
//{
//    private Feature aluminumGen;
//    private Feature copperGen;
//    private Feature galenaGen;
//    private Feature quartzGen;
//    private Feature siliconGen;
//    private Feature tinGen;
//    private Feature solarGen;
//    private World world;
//
//    public BiomeDecoratorVenus()
//    {
//        this.aluminumGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 6, true, VenusBlocks.venusBlock, 1);
//        this.copperGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 7, true, VenusBlocks.venusBlock, 1);
//        this.galenaGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 8, true, VenusBlocks.venusBlock, 1);
//        this.quartzGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 9, true, VenusBlocks.venusBlock, 1);
//        this.siliconGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 10, true, VenusBlocks.venusBlock, 1);
//        this.tinGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 11, true, VenusBlocks.venusBlock, 1);
//        this.solarGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 5, 13, true, VenusBlocks.venusBlock, 1);
//    }
//
//    @Override
//    public void decorate(World worldIn, Random random, Biome biome, BlockPos blockPos)
//    {
//        if (this.world != null)
//        {
//            throw new RuntimeException("Already decorating!!");
//        }
//        else
//        {
//            this.world = worldIn;
//            this.chunkPos = blockPos;
//            this.generateVenus(random);
//            this.world = null;
//        }
//    }
//
//    private void genStandardOre(int amountPerChunk, Feature worldGenerator, int minY, int maxY, Random random)
//    {
//        for (int var5 = 0; var5 < amountPerChunk; ++var5)
//        {
//            BlockPos blockpos = this.chunkPos.add(random.nextInt(16), random.nextInt(maxY - minY) + minY, random.nextInt(16));
//            worldGenerator.generate(this.world, random, blockpos);
//        }
//    }
//
//    private void generateVenus(Random random)
//    {
//        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Pre(this.world, random, chunkPos));
//        if (!ConfigManagerVenus.disableAluminumGenVenus) this.genStandardOre(18, this.aluminumGen, 0, 60, random);
//        if (!ConfigManagerVenus.disableCopperGenVenus) this.genStandardOre(24, this.copperGen, 0, 60, random);
//        if (!ConfigManagerVenus.disableGalenaGenVenus) this.genStandardOre(18, this.galenaGen, 0, 60, random);
//        if (!ConfigManagerVenus.disableQuartzGenVenus) this.genStandardOre(26, this.quartzGen, 0, 60, random);
//        if (!ConfigManagerVenus.disableSiliconGenVenus) this.genStandardOre(4, this.siliconGen, 0, 60, random);
//        if (!ConfigManagerVenus.disableTinGenVenus) this.genStandardOre(22, this.tinGen, 0, 60, random);
//        if (!ConfigManagerVenus.disableSolarGenVenus) this.genStandardOre(6, this.solarGen, 0, 50, random);
//        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Post(this.world, random, chunkPos));
//    }
//}
