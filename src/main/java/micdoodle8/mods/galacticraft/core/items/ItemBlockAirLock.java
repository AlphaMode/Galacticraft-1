//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import net.minecraft.block.Block;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemBlockAirLock extends ItemBlockDesc
//{
//    public ItemBlockAirLock(Block block)
//    {
//        super(block);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//    }
//
//    @Override
//    public int getMetadata(int meta)
//    {
//        return meta;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public Rarity getRarity(ItemStack par1ItemStack)
//    {
//        return ClientProxyCore.galacticraftItem;
//    }
//
//    @Override
//    public String getUnlocalizedName(ItemStack itemstack)
//    {
//        String name = "";
//
//        switch (itemstack.getDamage())
//        {
//        case 0:
//        {
//            name = "air_lock_frame";
//            break;
//        }
//        case 1:
//        {
//            name = "air_lock_controller";
//            break;
//        }
//        default:
//            name = "null";
//        }
//
//        return "tile" + "." + name;
//    }
//
//    @Override
//    public String getUnlocalizedName()
//    {
//        return this.getBlock().getUnlocalizedName() + ".0";
//    }
//}
