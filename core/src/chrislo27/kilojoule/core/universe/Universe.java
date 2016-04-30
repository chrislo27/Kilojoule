package chrislo27.kilojoule.core.universe;

import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.core.entity.EntityPlayer;
import chrislo27.kilojoule.core.world.DesolateWorld;
import chrislo27.kilojoule.core.world.World;
import ionium.util.noise.SimplexNoise;

/**
 * A collection of Worlds.
 * 
 *
 */
public class Universe {

	public ObjectMap<String, World> worlds = new ObjectMap<>();
	public EntityPlayer player;

	public final long seed;
	public SimplexNoise simplexNoise;

	public Universe(long seed) {
		this.seed = seed;

		simplexNoise = new SimplexNoise(seed);

		addWorlds();
	}

	private void addWorlds() {
		worlds.put("desolate", new DesolateWorld(this, 1280, 720));
	}

	public void spawnPlayer() {
		World defaultWorld = worlds.get("desolate");

		player = new EntityPlayer(defaultWorld, defaultWorld.worldWidth / 2f,
				defaultWorld.generatorSettings.seaLevel
						+ defaultWorld.generatorSettings.hillHeight);

		defaultWorld.addEntity(player);

		defaultWorld.lightingEngine.updateLighting(0, 0, defaultWorld.worldWidth,
				defaultWorld.worldHeight);
	}

	public void tickUpdate() {
		if (player == null) spawnPlayer();

		for (World w : worlds.values()) {
			w.tickUpdate();
		}
	}

	public World getCurrentWorld() {
		if (player == null) spawnPlayer();

		return player.world;
	}

}
