package chrislo27.kilojoule.core.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.chunk.Chunk;
import chrislo27.kilojoule.core.entity.Entity;
import chrislo27.kilojoule.core.universe.Universe;
import ionium.util.noise.SimplexNoise;
import ionium.util.quadtree.QuadTree;

public class World {

	public final int worldWidth, worldHeight;
	public final int chunksWidth, chunksHeight;
	public final Universe universe;

	private Chunk[][] chunks;
	private Array<Chunk> activeChunks = new Array<>();
	private boolean shouldRebuildActiveChunksArray = true;

	public QuadTree<Entity> quadTree;
	private Array<Entity> allEntities = new Array<>();
	private Array<Entity> activeEntities = new Array<>();
	public boolean shouldRebuildActiveEntitiesArray = true;


	public World(Universe un, int sizex, int sizey) {
		if (sizex % Chunk.SIZE != 0 || sizey % Chunk.SIZE != 0) throw new IllegalArgumentException(
				"Size parameters must evenly divide into chunk boundaries (got " + sizex + "x"
						+ sizey + ", chunk size is " + Chunk.SIZE + "x" + Chunk.SIZE
						+ ", remainders are " + (sizex % Chunk.SIZE) + ", " + (sizey % Chunk.SIZE)
						+ ")");

		worldWidth = sizex;
		worldHeight = sizey;
		chunksWidth = worldWidth / Chunk.SIZE;
		chunksHeight = worldHeight / Chunk.SIZE;

		chunks = new Chunk[chunksWidth][chunksHeight];

		quadTree = new QuadTree<>(new Rectangle(0, 0, sizex, sizey), 0, 8, 8);

		this.universe = un;
	}

	public void tickUpdate() {
		if (shouldRebuildActiveEntitiesArray) {
			shouldRebuildActiveEntitiesArray = false;
			rebuildActiveEntitiesArray();
		}

		if (shouldRebuildActiveChunksArray) {
			shouldRebuildActiveChunksArray = false;
			rebuildActiveChunksArray();
		}

		for (int i = activeChunks.size - 1; i >= 0; i--) {
			Chunk c = activeChunks.get(i);
			if (c.isChunkActive()) {
				c.tickUpdate();
			} else {
				activeChunks.removeIndex(i);
			}
		}

	}

	public float getFoliageColor() {
		return Color.toFloatBits(0, 175, 17, 255);
	}

	public Array<Entity> getActiveEntities() {
		return activeEntities;
	}

	public void addEntity(Entity e) {
		allEntities.add(e);

		if (isEntityInActiveChunk(e)) activeEntities.add(e);
	}

	public void removeEntity(Entity e) {
		allEntities.removeValue(e, true);
		activeEntities.removeValue(e, true);
	}

	public boolean isEntityInActiveChunk(Entity e) {
		return isChunkActive(((int) (e.boundingBox.x)) / Chunk.SIZE,
				((int) (e.boundingBox.y)) / Chunk.SIZE)
				|| isChunkActive(((int) (e.boundingBox.x + e.boundingBox.width)) / Chunk.SIZE,
						((int) (e.boundingBox.y)) / Chunk.SIZE)
				|| isChunkActive(((int) (e.boundingBox.x)) / Chunk.SIZE,
						((int) (e.boundingBox.y + e.boundingBox.height)) / Chunk.SIZE)
				|| isChunkActive(((int) (e.boundingBox.x + e.boundingBox.width)) / Chunk.SIZE,
						((int) (e.boundingBox.y + e.boundingBox.height)) / Chunk.SIZE);
	}

	public Chunk getChunk(int cx, int cy) {
		if (cx < 0 || cy < 0 || cx >= chunksWidth || cy >= chunksHeight)
			throw new IllegalArgumentException("Chunk " + cx + ", " + cy + " is out of bounds");

		if (chunks[cx][cy] == null) {
			setChunk(new Chunk(cx, cy), cx, cy);
		}

		return chunks[cx][cy];
	}

	public void setChunk(Chunk chunk, int cx, int cy) {
		if (cx < 0 || cy < 0 || cx >= chunksWidth || cy >= chunksHeight) return;

		chunks[cx][cy] = chunk;

		if (chunk != null && chunk.isChunkActive()) {
			activeChunks.add(chunk);
		}
	}

	public boolean isChunkActive(int cx, int cy) {
		return getChunk(cx, cy).isChunkActive();
	}

	public void loadChunk(int time, int x, int y) {
		getChunk(x, y).loadedTime = time;
	}

	public Block getBlock(int x, int y) {
		if (x < 0 || y < 0 || x >= worldWidth || y >= worldHeight) return null;

		return getChunk(x / Chunk.SIZE, y / Chunk.SIZE).getBlock(x % Chunk.SIZE, y % Chunk.SIZE);
	}

	public void setBlock(Block block, int x, int y) {
		if (x < 0 || y < 0 || x >= worldWidth || y >= worldHeight) return;

		getChunk(x / Chunk.SIZE, y / Chunk.SIZE).setBlock(block, x % Chunk.SIZE, y % Chunk.SIZE);
	}

	private void rebuildActiveEntitiesArray() {
		activeEntities.clear();

		for (Entity e : allEntities) {
			if (isEntityInActiveChunk(e)) activeEntities.add(e);
		}
	}

	private void rebuildActiveChunksArray() {
		activeChunks.clear();

		for (int x = 0; x < chunksWidth; x++) {
			for (int y = 0; y < chunksHeight; y++) {
				if (getChunk(x, y).isChunkActive()) activeChunks.add(getChunk(x, y));
			}
		}
	}

}
