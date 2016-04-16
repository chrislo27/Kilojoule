package chrislo27.kilojoule.client.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.dimension.Dimension;
import chrislo27.kilojoule.core.world.World;
import ionium.templates.Main;

public class WorldRenderer {

	public static int extraMargin = 1;

	private final World world;
	public OrthographicCamera camera;

	public WorldRenderer(World world) {
		this.world = world;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 26.5f, 15);
	}

	public void render(Batch batch) {
		Dimension currentDim = world.getCurrentDim();

		camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth * 0.5f,
				currentDim.dimWidth - camera.viewportWidth * 0.5f);
		camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight * 0.5f,
				currentDim.dimHeight - camera.viewportHeight * 0.5f);
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		int minX = (int) MathUtils.clamp(
				camera.position.x - camera.viewportWidth * 0.5f - extraMargin, 0,
				currentDim.dimWidth);
		int minY = (int) MathUtils.clamp(
				camera.position.y - camera.viewportHeight * 0.5f - extraMargin, 0,
				currentDim.dimHeight);
		int maxX = (int) MathUtils.clamp(
				camera.position.x + camera.viewportWidth * 0.5f + extraMargin, 0,
				currentDim.dimWidth);
		int maxY = (int) MathUtils.clamp(
				camera.position.y + camera.viewportHeight * 0.5f + extraMargin, 0,
				currentDim.dimHeight);

		Block b;
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				b = currentDim.getBlock(x, y);

				if (b == null) continue;
				if (b.getRenderBlock() == null) continue;

				b.getRenderBlock().render(batch, currentDim, x, y);
			}
		}

		batch.end();
	}

}
