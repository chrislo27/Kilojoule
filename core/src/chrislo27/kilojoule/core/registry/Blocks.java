package chrislo27.kilojoule.core.registry;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.block.BlockDirt;
import chrislo27.kilojoule.core.block.BlockGrass;
import chrislo27.kilojoule.core.block.BlockStone;
import ionium.util.render.TextureRegionDebleeder;

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

	private TextureAtlas atlas;
	private ObjectMap<String, AtlasRegion> regions = new ObjectMap<>();

	private void loadResources() {
		addBlock("dirt", new BlockDirt());
		addBlock("grass", new BlockGrass());
		addBlock("stone", new BlockStone());
	}

	public void setAtlas(TextureAtlas a) {
		atlas = a;

		Array<AtlasRegion> allRegions = atlas.getRegions();
		regions.clear();

		TextureRegionDebleeder.fixAmountPx = 0.25f;

		for (AtlasRegion ar : allRegions) {
			//TextureRegionDebleeder.fixBleeding(ar);
			regions.put(ar.name + (ar.name.endsWith("_") ? ar.index : ""), ar);
		}
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	public static AtlasRegion getRegion(String id) {
		return instance().regions.get(id);
	}

	public static Block getBlock(String key) {
		if (key == null) return null;

		return instance().blocks.get(key);
	}

	public static String getKey(Block block) {
		if (block == null) return null;

		return instance().reverseMap.get(block);
	}

	public Array<Block> getAllBlocks() {
		return allBlocks;
	}

	public Array<String> getAllKeys() {
		return allKeys;
	}

	public static void addBlock(String key, Block block) {
		instance().blocks.put(key, block);
		instance().reverseMap.put(block, key);
		instance().allBlocks.add(block);
		instance().allKeys.add(key);
	}

}
