//package micdoodle8.mods.galacticraft.planets.mars.world.gen;
//
//import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeDecoratorSpace;
//import micdoodle8.mods.galacticraft.core.world.gen.WorldGenMinableMeta;
//import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerPlanets;
//import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
//import net.minecraft.block.Blocks;
//import net.minecraft.world.World;
//import net.minecraft.world.gen.feature.Feature;
//
//public class BiomeDecoratorMars extends BiomeDecoratorSpace
//{
//    private Feature dirtGen;
//    private Feature deshGen;
//    private Feature tinGen;
//    private Feature copperGen;
//    private Feature ironGen;
//    private Feature iceGen;
//    private World currentWorld;
//
//    public BiomeDecoratorMars()
//    {
//        this.copperGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 4, 0, true, MarsBlocks.marsBlock, 9);
//        this.tinGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 4, 1, true, MarsBlocks.marsBlock, 9);
//        this.deshGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 6, 2, true, MarsBlocks.marsBlock, 9);
//        this.ironGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 8, 3, true, MarsBlocks.marsBlock, 9);
//        this.dirtGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 32, 6, true, MarsBlocks.marsBlock, 9);
//        this.iceGen = new WorldGenMinableMeta(Blocks.ICE, 18, 0, true, MarsBlocks.marsBlock, 6);
//    }
//
//    @Override
//    protected void decorate()
//    {
//        this.generateOre(4, this.iceGen, 60, 120);
//        this.generateOre(20, this.dirtGen, 0, 200);
//        if (!ConfigManagerPlanets.disableDeshGenMars.get())
//        {
//            this.generateOre(15, this.deshGen, 20, 64);
//        }
//        if (!ConfigManagerPlanets.disableCopperGenMars.get())
//        {
//            this.generateOre(26, this.copperGen, 0, 60);
//        }
//        if (!ConfigManagerPlanets.disableTinGenMars.get())
//        {
//            this.generateOre(23, this.tinGen, 0, 60);
//        }
//        if (!ConfigManagerPlanets.disableIronGenMars.get())
//        {
//            this.generateOre(20, this.ironGen, 0, 64);
//        }
//    }
//
//    @Override
//    protected void setCurrentWorld(World world)
//    {
//        this.currentWorld = world;
//    }
//
//    @Override
//    protected World getCurrentWorld()
//    {
//        return this.currentWorld;
//    }
//}
