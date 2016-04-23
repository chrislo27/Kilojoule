package chrislo27.kilojoule.core.generation.step;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.biome.Biome;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.world.World;
import ionium.util.i18n.Localization;

public class BiomeStep extends Step {

	public BiomeStep(WorldGenerator gen) {
		super(gen);
	}

	@Override
	public void step(World world, WorldLoadingBuffer buffer) {
	}

	@Override
	public String getMessageString() {
		return Localization.get("generating.assigningBiomes");
	}

	public static class BiomeRange {

		public Biome biome;
		public float range;

		public BiomeRange(Biome b, float range) {
			biome = b;
			this.range = range;
		}

	}

}
