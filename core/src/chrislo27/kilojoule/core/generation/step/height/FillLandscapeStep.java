package chrislo27.kilojoule.core.generation.step.height;

import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.generation.settings.BlockLayer;
import chrislo27.kilojoule.core.generation.step.Step;
import chrislo27.kilojoule.core.world.World;
import ionium.util.i18n.Localization;

public class FillLandscapeStep extends Step {

	int x = 0;
	int[] heights;

	public FillLandscapeStep(WorldGenerator gen) {
		super(gen);
	}

	@Override
	public void step(World world, WorldLoadingBuffer buffer) {

		int y = heights[x];
		Array<BlockLayer> layers = world.getBiome(x).generatorSettings.blockLayers;

		for (int layerindex = 0; layerindex < layers.size; layerindex++) {
			BlockLayer layer = layers.get(layerindex);
			int numLeft = y;

			for (int i = 0; i < (layerindex == layers.size - 1 ? numLeft
					: layer.amount); i++, y--) {
				world.setBlock(layer.block, x, y);
			}

			buffer.fillRect(layer.block.getMapColor(world.getBiome(x).foliageColor), x, 0, 1,
					y + layer.amount);
		}

		x++;
		setPercentage((x * 1f) / world.worldWidth);
	}

	@Override
	public String getMessageString() {
		return Localization.get("generating.fillingLandscape");
	}

	@Override
	public void onStart(Step lastStep, WorldLoadingBuffer buffer) {
		super.onStart(lastStep, buffer);

		buffer.clear(0, 0, 0);

		if (lastStep instanceof FineHeightmapStep) {
			heights = ((FineHeightmapStep) lastStep).actualHeights;
		}
	}

}
