package chrislo27.kilojoule.core.registry;

import com.badlogic.gdx.utils.Array;
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

	private Array<Block> allBlocks = new Array<>();
	private Array<String> allKeys = new Array<>();

	private void loadResources() {
		addBlock("dirt", new Block() {

		});
	}

	public static Block getBlock(String key) {
		return instance().blocks.get(key);
	}

	public static String getKey(Block block) {
		return instance().reverseMap.get(block);
	}

	public static Array<Block> getAllBlocks() {
		return instance().allBlocks;
	}

	public static Array<String> getAllKeys() {
		return instance().allKeys;
	}

	public static void addBlock(String key, Block block) {
		instance().blocks.put(key, block);
		instance().reverseMap.put(block, key);
		instance().allBlocks.add(block);
		instance().allKeys.add(key);
	}

}
