package chrislo27.kilojoule.core.nbt;

import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.core.registry.Blocks;

public class BlockIDMap extends IDMap {

	public BlockIDMap(String tagName) {
		super(tagName);

		this.ticker = 1;

		Array<String> blockIds = Blocks.instance().getAllKeys();

		for (String key : blockIds) {
			this.add(key);
		}
	}

}
