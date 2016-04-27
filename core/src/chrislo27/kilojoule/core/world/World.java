package chrislo27.kilojoule.core.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import chrislo27.kilojoule.core.biome.Biome;
import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.chunk.Chunk;
import chrislo27.kilojoule.core.chunk.IChunkLoader;
import chrislo27.kilojoule.core.entity.Entity;
import chrislo27.kilojoule.core.generation.WorldGeneratorSettings;
import chrislo27.kilojoule.core.generation.step.BiomeStep.BiomeRange;
import chrislo27.kilojoule.core.universe.Universe;
import ionium.aabbcollision.CollisionResolver;
import ionium.aabbcollision.PhysicsBody;
import ionium.registry.GlobalVariables;
import ionium.util.CoordPool;
import ionium.util.Coordinate;
import ionium.util.MathHelper;
import ionium.util.quadtree.QuadTree;

public abstract class World {

	public final int worldWidth, worldHeight;
	public final int chunksWidth, chunksHeight;
	public final Universe universe;

	private Chunk[][] chunks;
	private Biome[] biomes;
	private Array<Chunk> activeChunks = new Array<>();
	private boolean shouldRebuildActiveChunksArray = true;

	public QuadTree<Entity> quadTree;
	private Array<Entity> allEntities = new Array<>();
	private Array<Entity> activeEntities = new Array<>();
	public boolean shouldRebuildActiveEntitiesArray = true;
	public CollisionResolver collisionResolver;
	public Pool<PhysicsBody> physicsBodyPool = new Pool<PhysicsBody>() {

		@Override
		protected PhysicsBody newObject() {
			return new PhysicsBody();
		}

	};
	private Array<Coordinate> tempCoords = new Array<>();

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
		biomes = new Biome[worldWidth];

		quadTree = new QuadTree<>(new Rectangle(0, 0, sizex, sizey), 0, 8, 8);

		collisionResolver = new CollisionResolver(1f / GlobalVariables.getInt("TICKS"));

		this.universe = un;
	}

	public abstract void assignBiomes(Array<BiomeRange> rangeArray);

	public abstract WorldGeneratorSettings getGeneratorSettings();

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

		for (int i = activeEntities.size - 1; i >= 0; i--) {
			Entity e = activeEntities.get(i);

			e.movementUpdate();
			e.tickUpdate();

			if (e.shouldBeRemoved()) {
				removeEntity(e);
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

		if (isEntityInActiveChunk(e) || e instanceof IChunkLoader) activeEntities.add(e);
	}

	public void removeEntity(Entity e) {
		allEntities.removeValue(e, true);
		activeEntities.removeValue(e, true);
	}

	public boolean isEntityInActiveChunk(Entity e) {
		tempCoords.clear();
		getAllChunksInArea(tempCoords, e.physicsBody.bounds.x, e.physicsBody.bounds.y,
				e.physicsBody.bounds.width, e.physicsBody.bounds.height);

		boolean allClear = true;
		for (Coordinate c : tempCoords) {
			if (!getChunk(c.getX(), c.getY()).isChunkActive()) {
				allClear = false;
				break;
			}
		}

		CoordPool.freeAll(tempCoords);

		return allClear;
	}

	public void getAllChunksInArea(Array<Coordinate> array, Rectangle rect) {
		getAllChunksInArea(array, rect.x, rect.y, rect.width, rect.height);
	}

	public void getAllChunksInArea(Array<Coordinate> array, float x, float y, float w, float h) {
		Coordinate topLeft = CoordPool.obtain().setPosition((int) (x) / Chunk.SIZE,
				(int) (y + h) / Chunk.SIZE);
		Coordinate topRight = CoordPool.obtain().setPosition((int) (x + w) / Chunk.SIZE,
				(int) (y + h) / Chunk.SIZE);
		Coordinate bottomLeft = CoordPool.obtain().setPosition((int) (x) / Chunk.SIZE,
				(int) (y) / Chunk.SIZE);

		for (int cx = topLeft.getX(); cx <= topRight.getX(); cx++) {
			for (int cy = bottomLeft.getY(); cy <= topLeft.getY(); cy++) {
				array.add(CoordPool.obtain().setPosition(cx, cy));
			}
		}

		CoordPool.free(topLeft);
		CoordPool.free(topRight);
		CoordPool.free(bottomLeft);
	}

	public Chunk getChunk(int cx, int cy) {
		if (cx < 0 || cy < 0 || cx >= chunksWidth || cy >= chunksHeight) return null;

		if (chunks[cx][cy] == null) {
			setChunk(new Chunk(cx, cy), cx, cy);
		}

		return chunks[cx][cy];
	}

	public void setChunk(Chunk chunk, int cx, int cy) {
		if (cx < 0 || cy < 0 || cx >= chunksWidth || cy >= chunksHeight) return;

		chunks[cx][cy] = chunk;

		if (chunk != null && chunk.isChunkActive()) {
			loadChunk(chunk.getChunkLoadedTime(), cx, cy);
		}
	}

	public boolean isChunkActive(int cx, int cy) {
		return getChunk(cx, cy) != null && getChunk(cx, cy).isChunkActive();
	}

	public void loadChunk(int time, int x, int y) {
		time = Math.max(0, time);

		Chunk chunk = getChunk(x, y);

		chunk.loadChunk(time);

		if (time <= 0) {
			boolean shouldUnload = true;
			for (Entity e : allEntities) {
				if (e instanceof IChunkLoader && MathHelper.intersects(e.physicsBody.bounds.x,
						e.physicsBody.bounds.y, e.physicsBody.bounds.width,
						e.physicsBody.bounds.height, x, y, 16, 16)) {
					shouldUnload = false;

					break;
				}
			}

			if (shouldUnload) {
				for (int bx = 0; bx < Chunk.SIZE; bx++) {
					for (int by = 0; by < Chunk.SIZE; by++) {
						if (chunk.getBlock(bx, by) != null
								&& chunk.getBlock(bx, by) instanceof IChunkLoader) {
							shouldUnload = false;

							break;
						}
					}
				}
			}

			if (shouldUnload) {
				activeChunks.removeValue(chunk, true);
			} else {
				chunk.loadChunk(
						(int) (IChunkLoader.CHUNK_LOAD_TIME * GlobalVariables.getFloat("TICKS")));
			}
		} else {
			if (!activeChunks.contains(chunk, true)) {
				activeChunks.add(chunk);
			}
		}
	}

	public Block getBlock(int x, int y) {
		if (x < 0 || y < 0 || x >= worldWidth || y >= worldHeight) return null;

		return getChunk(x / Chunk.SIZE, y / Chunk.SIZE).getBlock(x % Chunk.SIZE, y % Chunk.SIZE);
	}

	public void setBlock(Block block, int x, int y) {
		if (x < 0 || y < 0 || x >= worldWidth || y >= worldHeight) return;

		getChunk(x / Chunk.SIZE, y / Chunk.SIZE).setBlock(block, x % Chunk.SIZE, y % Chunk.SIZE);
	}

	public Biome getBiome(int x) {
		if (x < 0 || x >= worldWidth) return null;

		return biomes[x];
	}

	public void setBiome(Biome b, int x) {
		if (x < 0 || x >= worldWidth) return;

		biomes[x] = b;
	}

	private void rebuildActiveEntitiesArray() {
		activeEntities.clear();

		for (Entity e : allEntities) {
			if (isEntityInActiveChunk(e) || e instanceof IChunkLoader) activeEntities.add(e);
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
