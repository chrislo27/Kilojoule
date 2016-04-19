package chrislo27.kilojoule.core.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.chunk.Chunk;
import chrislo27.kilojoule.core.entity.Entity;
import chrislo27.kilojoule.core.generation.Step;
import ionium.util.noise.SimplexNoise;
import ionium.util.quadtree.QuadTree;

public class World {

	public final int worldWidth, worldHeight;
	public final int chunksWidth, chunksHeight;

	private Chunk[][] chunks;
	private Array<Chunk> activeChunks;

	public QuadTree<Entity> quadTree;
	private Array<Entity> allEntities = new Array<>();
	private Array<Entity> activeEntities = new Array<>();
	private boolean shouldRebuildActiveEntitiesArray = true;

	public SimplexNoise simplexNoise;
	public final long generationSeed;

	public World(long seed, int sizex, int sizey) {
		if (sizex % Chunk.SIZE != 0 || sizey % Chunk.SIZE != 0) throw new IllegalArgumentException(
				"Size parameters must evenly divide into chunk boundaries (got " + sizex + "x"
						+ sizey + ", chunk size is " + Chunk.SIZE + "x" + Chunk.SIZE + ")");

		worldWidth = sizex;
		worldHeight = sizey;
		chunksWidth = worldWidth / Chunk.SIZE;
		chunksHeight = worldHeight / Chunk.SIZE;

		chunks = new Chunk[chunksWidth][chunksHeight];

		quadTree = new QuadTree<>(new Rectangle(0, 0, sizex, sizey), 0, 8, 8);

		generationSeed = seed;
		simplexNoise = new SimplexNoise(seed);
	}

	public void tickUpdate() {
		if (shouldRebuildActiveEntitiesArray) {
			shouldRebuildActiveEntitiesArray = false;
			rebuildActiveEntitiesArray();
		}

		for (int y = 0; y < chunksHeight; y++) {
			for (int x = 0; x < chunksWidth; x++) {
				if (getChunk(x, y).isChunkActive()) getChunk(x, y).tickUpdate();
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

		if (chunks[cx][cy] == null) {
			// generate
			chunks[cx][cy] = new Chunk(cx, cy);
		}

		return chunks[cx][cy];
	}

	public void setChunk(Chunk chunk, int cx, int cy) {
		if (cx < 0 || cy < 0 || cx >= chunksWidth || cy >= chunksHeight) return;
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

}
