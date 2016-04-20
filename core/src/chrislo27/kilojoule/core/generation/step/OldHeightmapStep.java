package chrislo27.kilojoule.core.generation.step;

import com.badlogic.gdx.math.MathUtils;

import chrislo27.kilojoule.client.screen.GenerationScreen.WorldLoadingBuffer;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.registry.Blocks;
import chrislo27.kilojoule.core.world.World;

public class OldHeightmapStep extends Step {

	int x = 0;

	public OldHeightmapStep(WorldGenerator gen) {
		super(gen);
	}

	@Override
	public void step(World world, WorldLoadingBuffer buffer) {

		long time = System.nanoTime();
		while (System.nanoTime() - time < 15_000_000 && !isFinished()) {
			double height = world.simplexNoise.eval(x * 0.0325f, 0);
			int actualHeight = (int) (generator.settings.seaLevel
					+ (height * generator.settings.hillHeight));

			for (int y = 0; y < actualHeight; y++) {
				world.setBlock(Blocks.getBlock("dirt"), x, actualHeight);
			}

			buffer.fillRect(132 / 255f, 84 / 255f, 50 / 255f, x, 0, 1, actualHeight);

			world.setBlock(Blocks.getBlock("grass"), x, actualHeight);
			buffer.setPixel(0.25f, 1f, 0.25f, x, actualHeight);
			
			setPercentage((x * 1f) / world.worldWidth);
			x++;
		}

	}

	@Override
	public String getMessageString() {
		return "Heightmapping: ";
	}

}
