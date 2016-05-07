package chrislo27.kilojoule.core.chunk;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagInteger;
import com.evilco.mc.nbt.tag.TagIntegerArray;

import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.nbt.BlockIDMap;
import chrislo27.kilojoule.core.registry.Blocks;

public class Chunk {

	public static final int SIZE = 16;

	public int chunkX, chunkY;
	private Block[][] blocks = new Block[SIZE][SIZE];
	private int[][] lighting = new int[SIZE][SIZE];

	private int loadedTime = 0;

	public Chunk(int x, int y) {
		chunkX = x;
		chunkY = y;
	}

	public void tickUpdate() {
		if (!isChunkActive()) {
			loadedTime = 0;
			return;
		}
	}

	public void setBlock(Block block, int x, int y) {
		if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) return;

		blocks[x][y] = block;
	}

	public Block getBlock(int x, int y) {
		if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) return null;

		return blocks[x][y];
	}

	public void setLighting(int lighting, int x, int y) {
		if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) return;

		this.lighting[x][y] = lighting;
	}

	public int getLighting(int x, int y) {
		if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) return 0;

		return lighting[x][y];
	}

	public boolean isChunkActive() {
		return loadedTime > 0;
	}

	public int getChunkLoadedTime() {
		return loadedTime;
	}

	public void loadChunk(int time) {
		loadedTime = Math.max(0, loadedTime);
	}

	public static int getBlockIndex(int x, int y) {
		return y * SIZE + x;
	}

	public static int getXFromIndex(int index) {
		return index % SIZE;
	}

	public static int getYFromIndex(int index) {
		return index / SIZE;
	}

	public void writeToNBT(TagCompound compound, BlockIDMap idMap) {
		compound.setTag(new TagInteger("PositionX", chunkX));
		compound.setTag(new TagInteger("PositionY", chunkY));
		compound.setTag(new TagInteger("LoadTime", loadedTime));

		// blocks, lighting
		{
			int[] allBlocks = new int[SIZE * SIZE];
			int[] allLighting = new int[SIZE * SIZE];

			for (int x = 0; x < SIZE; x++) {
				for (int y = 0; y < SIZE; y++) {
					int index = getBlockIndex(x, y);

					allBlocks[index] = blocks[x][y] == null ? 0
							: idMap.keyToValue.get(Blocks.getKey(blocks[x][y]));
					allLighting[index] = lighting[x][y];
				}
			}

			compound.setTag(new TagIntegerArray("Blocks", allBlocks));
			compound.setTag(new TagIntegerArray("Lighting", allLighting));

		}
	}

	public void readFromNBT(TagCompound compound, BlockIDMap idMap)
			throws TagNotFoundException, UnexpectedTagTypeException {
		chunkX = compound.getInteger("PositionX");
		chunkY = compound.getInteger("PositionY");
		loadedTime = compound.getInteger("LoadTime");

		int[] allBlocks = compound.getIntegerArray("Blocks");
		int[] allLighting = compound.getIntegerArray("Lighting");

		for (int i = 0; i < SIZE * SIZE; i++) {
			int x = getXFromIndex(i);
			int y = getYFromIndex(i);

			setBlock(allBlocks[i] == 0 ? null : Blocks.getBlock(idMap.valueToKey.get(allBlocks[i])),
					x, y);
			setLighting(allLighting[i], x, y);
		}
	}

}
