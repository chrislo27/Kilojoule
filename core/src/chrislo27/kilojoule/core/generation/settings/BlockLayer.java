package chrislo27.kilojoule.core.generation.settings;

import chrislo27.kilojoule.core.block.Block;

public class BlockLayer {

	public Block block;
	public int amount = 1;

	public BlockLayer(Block b, int amt) {
		block = b;
		amount = amt;
	}

}