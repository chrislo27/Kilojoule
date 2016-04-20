package chrislo27.kilojoule.core.chunk;

import com.badlogic.gdx.math.MathUtils;

import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.registry.Blocks;
import ionium.templates.Main;

public class Chunk {

	public static final int SIZE = 16;

	public int chunkX, chunkY;
	private Block[][] blocks = new Block[SIZE][SIZE];
	public int loadedTime = 0;

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

	public boolean isChunkActive() {
		return loadedTime > 0;
	}

}
