package chrislo27.kilojoule.core.generation.step;

import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.biome.Biome;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.world.World;
import ionium.util.i18n.Localization;

public class BiomeStep extends Step {

	private int current = 0;
	private int x = 0;
	private Array<BiomeRange> ranges = null;

	public BiomeStep(WorldGenerator gen) {
		super(gen);
	}

	@Override
	public void step(World world, WorldLoadingBuffer buffer) {
		if (ranges == null) {
			ranges = new Array<>();
			world.assignBiomes(ranges);
		}

		for (int i = x; i < ((int) (ranges.get(current).range * world.worldWidth)) + x; i++) {
			world.setBiome(ranges.get(current).biome, i);
		}

		x += (int) (ranges.get(current).range * world.worldWidth);
		current++;
		setPercentage(current * 1f / ranges.size);

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
