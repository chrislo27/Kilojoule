package chrislo27.kilojoule.core.registry;

import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.core.block.Block;

public class Blocks {

	private static Blocks instance;

	private Blocks() {
	}

	public static Blocks instance() {
		if (instance == null) {
			instance = new Blocks();
			instance.loadResources();
		}
		return instance;
	}

	private ObjectMap<String, Block> blocks = new ObjectMap<>();
	private ObjectMap<Block, String> reverseMap = new ObjectMap<>();

	private void loadResources() {

	}

	public static Block getBlock(String key) {
		return instance().blocks.get(key);
	}

	public static String getKey(Block block) {
		return instance().reverseMap.get(block);
	}

	public static void addBlock(String key, Block block) {
		instance().blocks.put(key, block);
		instance().reverseMap.put(block, key);
	}

}
