package chrislo27.kilojoule.core.universe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.client.Keybinds;
import chrislo27.kilojoule.core.entity.EntityPlayer;
import chrislo27.kilojoule.core.world.DesolateWorld;
import chrislo27.kilojoule.core.world.World;
import ionium.util.input.AnyKeyPressed;
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
				defaultWorld.worldHeight, true);
	}

	public void tickUpdate() {
		if (player == null) spawnPlayer();

		for (World w : worlds.values()) {
			w.tickUpdate();
		}
	}

	public void inputUpdate() {
		if (player == null) return;

		if (AnyKeyPressed.isAKeyPressed(Keybinds.LEFT)) {
			player.move(-Gdx.graphics.getDeltaTime(), 0);
		}

		if (AnyKeyPressed.isAKeyPressed(Keybinds.RIGHT)) {
			player.move(Gdx.graphics.getDeltaTime(), 0);
		}

		if (AnyKeyPressed.isAKeyPressed(Keybinds.UP)) {
			player.move(0, Gdx.graphics.getDeltaTime());
		}
		if (AnyKeyPressed.isAKeyPressed(Keybinds.DOWN)) {
			player.move(0, -Gdx.graphics.getDeltaTime());
		}

		if (AnyKeyPressed.isAKeyJustPressed(Keybinds.JUMP)) {
			player.jump();
		}
	}

	public World getCurrentWorld() {
		if (player == null) spawnPlayer();

		return player.world;
	}

}
