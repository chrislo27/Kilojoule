package chrislo27.kilojoule.core.chunk;

import chrislo27.kilojoule.core.block.Block;

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

}
