package chrislo27.kilojoule.core.registry;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import chrislo27.kilojoule.core.entity.Entity;
import chrislo27.kilojoule.core.entity.EntityPlayer;
import chrislo27.kilojoule.core.world.World;

public class Entities {

	private static Entities instance;

	private Entities() {
	}

	public static Entities instance() {
		if (instance == null) {
			instance = new Entities();
			instance.loadResources();
		}
		return instance;
	}

	private HashMap<String, Class<? extends Entity>> allEntities = new HashMap<>();
	private HashMap<Class<? extends Entity>, String> reverseMap = new HashMap<>();

	private void loadResources() {
		addEntity("player", EntityPlayer.class);
	}

	public static Class<? extends Entity> getEntityClass(String id) {
		return instance().allEntities.get(id);
	}

	public static String getEntityID(Class<? extends Entity> clazz) {
		return instance().reverseMap.get(clazz);
	}

	public static Entity getNewInstance(String id, World world, float x, float y) {
		try {
			return getEntityClass(id).getConstructor(World.class, float.class, float.class)
					.newInstance(world, x, y);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void addEntity(String id, Class<? extends Entity> clazz) {
		instance().allEntities.put(id, clazz);
		instance().reverseMap.put(clazz, id);
	}

}
