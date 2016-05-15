package chrislo27.kilojoule.core.registry.reflection;

import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.core.world.DesolateWorld;
import chrislo27.kilojoule.core.world.World;

public class Worlds {

	private static Worlds instance;

	private Worlds() {
	}

	public static Worlds instance() {
		if (instance == null) {
			instance = new Worlds();
			instance.loadResources();
		}
		return instance;
	}

	private ObjectMap<String, Class<? extends World>> worlds = new ObjectMap<>();
	private ObjectMap<Class<? extends World>, String> reverse = new ObjectMap<>();

	private void loadResources() {
		add("desolate", DesolateWorld.class);
	}

	public void add(String key, Class<? extends World> clazz) {
		worlds.put(key, clazz);
		reverse.put(clazz, key);
	}

	public static Class<? extends World> getWorldClass(String key) {
		return instance().worlds.get(key);
	}

	public static String getWorldKey(Class<? extends World> c) {
		return instance().reverse.get(c);
	}

}
