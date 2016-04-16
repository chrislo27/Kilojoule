package chrislo27.kilojoule.core.registry;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import chrislo27.kilojoule.client.packer.TilePackedTextureAtlas;

public class BlockTextures {

	private static BlockTextures instance;

	private BlockTextures() {
	}

	public static BlockTextures instance() {
		if (instance == null) {
			instance = new BlockTextures();
			instance.loadResources();
		}
		return instance;
	}

	private TilePackedTextureAtlas atlas;
	private ObjectMap<String, AtlasRegion> regions = new ObjectMap<>();

	private void loadResources() {

	}

	public void setAtlas(TilePackedTextureAtlas a) {
		atlas = a;

		Array<AtlasRegion> allRegions = atlas.getRegions();
		regions.clear();

		for (AtlasRegion ar : allRegions) {
			regions.put(ar.name + (ar.name.endsWith("_") ? ar.index : ""), ar);
		}
	}

	public TilePackedTextureAtlas getAtlas() {
		return atlas;
	}

	public static AtlasRegion getRegion(String id) {
		return instance().regions.get(id);
	}

}
