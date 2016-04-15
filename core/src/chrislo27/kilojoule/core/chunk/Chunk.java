package chrislo27.kilojoule.core.chunk;

import chrislo27.kilojoule.core.block.Block;

public class Chunk {

	public static final int SIZE = 16;

	public int chunkX, chunkY;
	private Block[][] blocks = new Block[SIZE][SIZE];

	public Chunk(int x, int y) {
		chunkX = x;
		chunkY = y;
	}

	public void setBlock(Block block, int x, int y) {
		if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) return;

		blocks[x][y] = block;
	}

	public Block getBlock(int x, int y) {
		if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) return null;

		return blocks[x][y];
	}

}
