package chrislo27.kilojoule.client.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.entity.EntityPlayer;
import chrislo27.kilojoule.core.world.World;

public class WorldRenderer {

	public static int extraMargin = 1;

	public OrthographicCamera camera;
	private Vector3 tempVector = new Vector3();

	public WorldRenderer() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 26.5f, 15);
	}

	protected void updateCamera(World world) {
		EntityPlayer player = world.universe.player;

		if (player != null) {
			tempVector.set(player.physicsBody.bounds.x, player.physicsBody.bounds.y,
					camera.position.z);

			camera.position.lerp(tempVector, 0.5f);
		}

		camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth * 0.5f,
				world.worldWidth - camera.viewportWidth * 0.5f);
		camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight * 0.5f,
				world.worldHeight - camera.viewportHeight * 0.5f);
		camera.update();
	}

	public void render(Batch batch, World world) {
		updateCamera(world);

		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		int minX = (int) MathUtils.clamp(
				camera.position.x - camera.viewportWidth * 0.5f - extraMargin, 0, world.worldWidth);
		int minY = (int) MathUtils.clamp(
				camera.position.y - camera.viewportHeight * 0.5f - extraMargin, 0,
				world.worldHeight);
		int maxX = (int) MathUtils.clamp(
				camera.position.x + camera.viewportWidth * 0.5f + extraMargin, 0, world.worldWidth);
		int maxY = (int) MathUtils.clamp(
				camera.position.y + camera.viewportHeight * 0.5f + extraMargin, 0,
				world.worldHeight);

		Block b;
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				b = world.getBlock(x, y);

				if (b == null) continue;
				if (b.getRenderBlock() == null) continue;

				b.getRenderBlock().render(batch, world, x, y);
			}
		}

		batch.end();
	}

}
