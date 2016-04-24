package chrislo27.kilojoule.core.nbt;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;

public interface NBTSaveable {

	public void writeToNBT(TagCompound compound);

	public void readFromNBT(TagCompound compound)
			throws TagNotFoundException, UnexpectedTagTypeException;

}
