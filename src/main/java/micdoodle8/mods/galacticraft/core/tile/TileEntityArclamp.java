package micdoodle8.mods.galacticraft.core.tile;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TileEntityArclamp extends TileEntity
{
	private int ticks = 0;
	private int sideRear = 0;
	public int facing = 0;
	private HashSet<BlockVec3> airToRestore = new HashSet();
	private boolean isActive = false;
	private AxisAlignedBB thisAABB;
	private Vec3 thisPos;
	private int facingSide = 0;
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		boolean firstTick = false;

		if (!this.worldObj.isRemote && this.isActive)
		{
			if (this.thisAABB == null)
			{
				firstTick = true;
				int side = this.getBlockMetadata();
				switch (side)
				{
				case 0:
					this.sideRear = side; //Down
					this.facingSide = this.facing + 2;
					this.thisAABB = AxisAlignedBB.getBoundingBox(this.xCoord - 20, this.yCoord - 8, this.zCoord - 20, this.xCoord + 20, this.yCoord + 20, this.zCoord + 20);
					break;
				case 1:
					this.sideRear = side; //Up
					this.facingSide = this.facing + 2;
					this.thisAABB = AxisAlignedBB.getBoundingBox(this.xCoord - 20, this.yCoord - 20, this.zCoord - 20, this.xCoord + 20, this.yCoord + 8, this.zCoord + 20);
					break;
				case 2:
					this.sideRear = side; //North
					this.facingSide = this.facing;
					if (this.facing > 1) this.facingSide= 7 - this.facing;
					this.thisAABB = AxisAlignedBB.getBoundingBox(this.xCoord - 20, this.yCoord - 20, this.zCoord - 8, this.xCoord + 20, this.yCoord + 20, this.zCoord + 20);
					break;
				case 3:
					this.sideRear = side; //South
					this.facingSide = this.facing;
					if (this.facing > 1) this.facingSide+=2;
					this.thisAABB = AxisAlignedBB.getBoundingBox(this.xCoord - 20, this.yCoord - 20, this.zCoord - 20, this.xCoord + 20, this.yCoord + 20, this.zCoord + 8);
					break;
				case 4:
					this.sideRear = side; //West
					this.facingSide = this.facing;
					this.thisAABB = AxisAlignedBB.getBoundingBox(this.xCoord - 8, this.yCoord - 20, this.zCoord - 20, this.xCoord + 20, this.yCoord + 20, this.zCoord + 20);
					break;
				case 5:
					this.sideRear = side; //East
					this.facingSide = this.facing;
					if (this.facing > 1) this.facingSide= 5 - this.facing;
					this.thisAABB = AxisAlignedBB.getBoundingBox(this.xCoord - 20, this.yCoord - 20, this.zCoord - 20, this.xCoord + 8, this.yCoord + 20, this.zCoord + 20);
					break;
				default:
					return;						
				}
			}
			
			if (firstTick || this.ticks % 100 == 0)
			{
				this.lightArea();
			}

			if (this.worldObj.rand.nextInt(20)==0)
			{
	            List<Entity> moblist = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, this.thisAABB, IMob.mobSelector);
	            
	            if (!moblist.isEmpty())
	            {
	            	for (Entity entry : moblist)
	    	        {
		            	if (!(entry instanceof EntityCreature)) continue;
		            	EntityCreature e = (EntityCreature) entry;
		            	//Check whether the mob can actually *see* the arclamp tile
		            	//if (this.worldObj.func_147447_a(thisPos, this.worldObj.getWorldVec3Pool().getVecFromPool(e.posX, e.posY, e.posZ), true, true, false) != null) continue;
		            			            		
	    	        	Vec3 vecNewTarget = RandomPositionGenerator.findRandomTargetBlockAwayFrom(e, 16, 7, this.thisPos);
	    	        	if (vecNewTarget == null) continue;
	    	        	PathNavigate nav = e.getNavigator();
	    	        	if (nav == null) continue;
	    	        	Vec3 vecOldTarget = null;
	    	        	if (nav.getPath() != null && !nav.getPath().isFinished())
	    	        	{	vecOldTarget = nav.getPath().getPosition(e);	}
	    	        	double distanceNew = vecNewTarget.squareDistanceTo(this.xCoord, this.yCoord, this.zCoord);
	    	        	
		    	        if (distanceNew > e.getDistanceSq(this.xCoord, this.yCoord, this.zCoord))
		    	        {
		    	        	if (vecOldTarget == null || distanceNew > vecOldTarget.squareDistanceTo(this.xCoord, this.yCoord, this.zCoord))
		    	        	{
		    	        		e.getNavigator().tryMoveToXYZ(vecNewTarget.xCoord, vecNewTarget.yCoord, vecNewTarget.zCoord, 0.3D);
			    	        	//System.out.println("Debug: Arclamp repelling entity: "+e.getClass().getSimpleName());
		    	        	}
		    	        }
	    	        }
	            }
	        }
		}

		this.ticks++;
	}
	
	@Override
	public void validate()
	{
		this.thisPos = Vec3.createVectorHelper(this.xCoord+0.5D, this.yCoord+0.5D, this.zCoord+0.5D);
		this.ticks = 0;
		this.thisAABB = null;
		this.isActive = true;
	}
	
	@Override
	public void invalidate()
	{
		if (!this.worldObj.isRemote)
		{
			Block brightAir = GCBlocks.brightAir;
			for (BlockVec3 vec : this.airToRestore)
			{
				if (vec.getBlock(this.worldObj) == brightAir)
					vec.setBlock(this.worldObj, Blocks.air);
			}
			this.airToRestore.clear();
		}
		this.isActive = false;
	}
	
	public void lightArea()
	{
		Block air = Blocks.air;
		Block breatheableAirID = GCBlocks.breatheableAir;
		Block brightAir = GCBlocks.brightAir;
		HashSet<BlockVec3> checked = new HashSet();
		LinkedList<BlockVec3> currentLayer = new LinkedList();
		LinkedList<BlockVec3> nextLayer = new LinkedList();
		BlockVec3 thisvec = new BlockVec3(this);
		currentLayer.add(thisvec);
		World world = this.worldObj;
		int sideskip1 = this.sideRear;
		int sideskip2 = this.facingSide ^ 1;
		for (int i = 0; i < 6; i++)
		{
			if (i!=sideskip1 && i!=sideskip2 && i!=(sideskip1 ^ 1) && i!=(sideskip2 ^ 1))
			{	
				BlockVec3 onEitherSide = thisvec.newVecSide(i); 
				if (onEitherSide.getBlockIDsafe_noChunkLoad(world).getLightOpacity() < 15)
				{
					currentLayer.add(onEitherSide);
				}
			}
		}
		BlockVec3 inFront = new BlockVec3(this);
		System.out.println("Arclamp: facing = "+facingSide+" other = "+(sideskip1 ^ 1));
		for (int i = 0; i< 5; i++)
		{
			inFront = inFront.newVecSide(this.facingSide).newVecSide(sideskip1 ^ 1);
			if (inFront.getBlockIDsafe_noChunkLoad(world).getLightOpacity() < 15)
			{
				currentLayer.add(inFront);
			}
		}

		for (int count = 0; count < 13; count ++)
		{
			int side;
			for (BlockVec3 vec : currentLayer)
			{
				side = 0;
				do
				{
					//Skip the side which this was entered from
					//and never go 'backwards'
					if (side != sideskip1 && side != sideskip2 && !vec.sideDone[side])
					{
						BlockVec3 sideVec = vec.newVecSide(side);

						if (!checked.contains(sideVec))
						{
							checked.add(sideVec);

							Block b = sideVec.getBlockIDsafe_noChunkLoad(world);
							if (b.getLightOpacity(world, sideVec.x, sideVec.y, sideVec.z) == 0)
							{
								nextLayer.add(sideVec);
								if (b == air)
								{	
									world.setBlock(sideVec.x, sideVec.y, sideVec.z, brightAir, 0, 2);
									this.airToRestore.add(sideVec);
								}
								//if (id == breatheableAirID)						
							}
						}
					}
					side++;
				} while (side < 6);
			}
			currentLayer = nextLayer;
			nextLayer = new LinkedList<BlockVec3>();
			if (currentLayer.size() == 0) break;
		}
	}
	
	@Override
    public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
		
		this.facing = nbt.getInteger("Facing");
		
		this.airToRestore.clear();
		NBTTagList airBlocks = nbt.getTagList("AirBlocks", 10);
		if (airBlocks.tagCount() > 0)
		{
			for (int j = airBlocks.tagCount() - 1; j >= 0; j--)
			{
				NBTTagCompound tag1 = airBlocks.getCompoundTagAt(j);
				if (tag1 != null)
				{
					this.airToRestore.add(BlockVec3.readFromNBT(tag1));
				}
			}
		}
	}
    
	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {
		super.writeToNBT(nbt);
		
		nbt.setInteger("Facing", this.facing);
		
		NBTTagList airBlocks = new NBTTagList();

		for (BlockVec3 vec : this.airToRestore)
		{
			NBTTagCompound tag = new NBTTagCompound();
			vec.writeToNBT(tag);
			airBlocks.appendTag(tag);
		}
		nbt.setTag("AirBlocks", airBlocks);
    }
    
	public void facingChanged()
	{
		this.facing -= 2;
		if (this.facing < 0) this.facing = 1 - this.facing;
		//facing sequence: 0 - 3 - 1 - 2

		if (!this.worldObj.isRemote)
		{
			this.thisAABB = null;
			Block brightAir = GCBlocks.brightAir;
			for (BlockVec3 vec : this.airToRestore)
			{
				if (vec.getBlock(this.worldObj) == brightAir)
					vec.setBlock(this.worldObj, Blocks.air);
			}
			this.airToRestore.clear();
		}
	}
}
