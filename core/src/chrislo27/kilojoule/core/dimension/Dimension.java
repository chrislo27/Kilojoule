package chrislo27.kilojoule.core.dimension;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.chunk.Chunk;
import chrislo27.kilojoule.core.entity.Entity;
import ionium.util.quadtree.QuadTree;

public class Dimension {

	public final int dimWidth, dimHeight;
	public final int chunksWidth, chunksHeight;

	private Chunk[][] chunks;

	public QuadTree<Entity> quadTree;
	private Array<Entity> allEntities = new Array<>();
	private Array<Entity> activeEntities = new Array<>();
	private boolean shouldRebuildActiveEntitiesArray = true;
	private Rectangle tempChunkActiveEntityRect = new Rectangle();

	public Dimension(int sizex, int sizey) {
		if (sizex % Chunk.SIZE != 0 || sizey % Chunk.SIZE != 0) throw new IllegalArgumentException(
				"Size parameters must evenly divide into chunk boundaries (got " + sizex + "x"
						+ sizey + ", chunk size is " + Chunk.SIZE + "x" + Chunk.SIZE + ")");

		dimWidth = sizex;
		dimHeight = sizey;
		chunksWidth = dimWidth / Chunk.SIZE;
		chunksHeight = dimHeight / Chunk.SIZE;

		chunks = new Chunk[chunksWidth][chunksHeight];

		quadTree = new QuadTree<>(new Rectangle(0, 0, sizex, sizey), 0, 8, 8);
	}

	public void tickUpdate() {
		if (shouldRebuildActiveEntitiesArray) {
			shouldRebuildActiveEntitiesArray = false;
			rebuildActiveEntitiesArray();
		}

	}

	public Array<Entity> getActiveEntities() {
		return activeEntities;
	}

	public void addEntity(Entity e) {
		allEntities.add(e);

	}

	public boolean isEntityInActiveChunk(Entity e) {
		return isChunkActive(((int) (e.boundingBox.x)) / Chunk.SIZE,
				((int) (e.boundingBox.y)) / Chunk.SIZE)
				&& isChunkActive(((int) (e.boundingBox.x + e.boundingBox.width)) / Chunk.SIZE,
						((int) (e.boundingBox.y)) / Chunk.SIZE)
				&& isChunkActive(((int) (e.boundingBox.x)) / Chunk.SIZE,
						((int) (e.boundingBox.y + e.boundingBox.height)) / Chunk.SIZE)
				&& isChunkActive(((int) (e.boundingBox.x + e.boundingBox.width)) / Chunk.SIZE,
						((int) (e.boundingBox.y + e.boundingBox.height)) / Chunk.SIZE);
	}

	public Chunk getChunk(int cx, int cy) {
		if (cx < 0 || cy < 0 || cx >= chunksWidth || cy >= chunksHeight)
			throw new IllegalArgumentException("Chunk " + cx + ", " + cy + " is out of bounds");

		return chunks[cx][cy];
	}

	public void setChunk(Chunk chunk, int cx, int cy) {
		if (cx < 0 || cy < 0 || cx >= chunksWidth || cy >= chunksHeight) return;
	}

	public boolean isChunkActive(int cx, int cy) {
		return getChunk(cx, cy).isChunkActive();
	}

	public Block getBlock(int x, int y) {
		if (x < 0 || y < 0 || x >= dimWidth || y >= dimHeight) return null;

		return getChunk(x / Chunk.SIZE, y / Chunk.SIZE).getBlock(x % Chunk.SIZE, y % Chunk.SIZE);
	}

	public void setBlock(Block block, int x, int y) {
		if (x < 0 || y < 0 || x >= dimWidth || y >= dimHeight) return;

		getChunk(x / Chunk.SIZE, y / Chunk.SIZE).setBlock(block, x % Chunk.SIZE, y % Chunk.SIZE);
	}

	private void rebuildActiveEntitiesArray() {
		activeEntities.clear();
	}

}
