package chrislo27.kilojoule.client.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;

import chrislo27.kilojoule.client.Main;
import chrislo27.kilojoule.client.packer.TilePackedTextureAtlas;
import chrislo27.kilojoule.client.packer.TileTexturePacker;
import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.registry.BlockTextures;
import chrislo27.kilojoule.core.registry.Blocks;
import ionium.registry.AssetRegistry;

public class AssetLoadingScreen extends ionium.screen.AssetLoadingScreen {

	public AssetLoadingScreen(Main m) {
		super(m);
	}

	@Override
	public void onFinishLoading() {
		super.onFinishLoading();

		Array<Block> allBlocks = Blocks.instance().getAllBlocks();
		ObjectMap<String, Texture> tempMap = new ObjectMap<>();
		for (Block block : allBlocks) {
			block.getRequiredTextures(tempMap);
		}

		TileTexturePacker ttp = new TileTexturePacker(Block.TILE_SIZE);

		ttp.maxTextureSize = 2048;
		ttp.mustUsePowerOfTwo = true;

		Entries<String, Texture> iterator = tempMap.entries();
		Entry<String, Texture> entry;
		while (iterator.hasNext()) {
			entry = iterator.next();

			ttp.addTexture(entry.key, entry.value);
		}

		TilePackedTextureAtlas atlas = ttp.pack();

		BlockTextures.instance().setAtlas(atlas);
	}

}
