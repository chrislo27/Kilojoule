package chrislo27.kilojoule.core.universe;

import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.core.world.World;
import ionium.util.noise.SimplexNoise;

/**
 * A collection of Worlds.
 * 
 *
 */
public class Universe {

	public ObjectMap<String, World> worlds = new ObjectMap<>();

	private String currentWorld;

	public final long seed;
	public SimplexNoise simplexNoise;

	public Universe(long seed) {
		this.seed = seed;

		simplexNoise = new SimplexNoise(seed);

		addWorlds();
	}

	private void addWorlds() {
		worlds.put("desoret", new World(this, 4000, 2000));
		worlds.put("desoret2", new World(this, 4000, 2000));
		worlds.put("desoret3", new World(this, 4000, 2000));
		worlds.put("desoret4", new World(this, 4000, 2000));
	}

	public void tickUpdate() {
		for (World w : worlds.values()) {
			w.tickUpdate();
		}
	}

	public World getCurrentWorld() {
		return worlds.get(currentWorld);
	}

}