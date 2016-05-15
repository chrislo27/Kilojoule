package chrislo27.kilojoule.client.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;

import chrislo27.kilojoule.client.Main;
import chrislo27.kilojoule.client.packer.TileTexturePacker;
import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.registry.Blocks;

public class AssetLoadingScreen extends ionium.screen.AssetLoadingScreen {

	public AssetLoadingScreen(Main m) {
		super(m);
	}

	@Override
	public void onFinishLoading() {
		super.onFinishLoading();

		long nanoTime = System.nanoTime();

		Array<Block> allBlocks = Blocks.instance().getAllBlocks();
		ObjectMap<String, Texture> tempMap = new ObjectMap<>();
		for (Block block : allBlocks) {
			block.getRequiredTextures(tempMap);
		}

		TileTexturePacker ttp = new TileTexturePacker();

		ttp.maxTextureSize = 2048;
		ttp.mustUsePowerOfTwo = true;
		
		// causes crash due to PixmapIO stuff
		//ttp.debugOutputFile = "debug/blockTextures.png";

		Entries<String, Texture> iterator = tempMap.entries();
		Entry<String, Texture> entry;
		while (iterator.hasNext()) {
			entry = iterator.next();

			ttp.addTexture(entry.key, entry.value);
		}

		TextureAtlas atlas = ttp.pack();

		Blocks.instance().setAtlas(atlas);

		Main.logger.info("Packing block textures took " + ((System.nanoTime() - nanoTime) / 1_000_000f) + " ms");
	}

}
