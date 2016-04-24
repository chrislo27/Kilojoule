package chrislo27.kilojoule.core.generation;

import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.core.generation.settings.BlockLayer;
import chrislo27.kilojoule.core.registry.Blocks;

public class BiomeGeneratorSettings {

	public float hillAmplification = 1;

	public Array<BlockLayer> blockLayers = new Array<>(new BlockLayer[] {
			new BlockLayer(Blocks.getBlock("grass"), 1), new BlockLayer(Blocks.getBlock("dirt"), 6),
			new BlockLayer(Blocks.getBlock("stone"), 1) });

	public BiomeGeneratorSettings() {

	}
}
