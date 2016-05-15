package chrislo27.kilojoule.core.tileentity;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagInteger;

import chrislo27.kilojoule.core.nbt.NBTSaveable;
import chrislo27.kilojoule.core.world.World;

public class TileEntity implements NBTSaveable {

	private int x, y;
	protected final World world;

	public TileEntity(World world, int x, int y) {
		this.x = x;
		this.y = y;
		this.world = world;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public void writeToNBT(TagCompound compound) {
		compound.setTag(new TagInteger("PosX", x));
		compound.setTag(new TagInteger("PosY", y));
	}

	@Override
	public void readFromNBT(TagCompound compound)
			throws TagNotFoundException, UnexpectedTagTypeException {
		setPosition(compound.getInteger("PosX"), compound.getInteger("PosY"));
	}

}
