package chrislo27.kilojoule.client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import chrislo27.kilojoule.client.Main;
import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.entity.Entity;
import chrislo27.kilojoule.core.entity.EntityPlayer;
import chrislo27.kilojoule.core.lighting.LightUtils;
import chrislo27.kilojoule.core.lighting.LightingRenderer;
import chrislo27.kilojoule.core.world.World;

public class WorldRenderer implements Disposable {

	public static int extraMargin = 1;

	public OrthographicCamera camera;
	private Vector3 tempVector = new Vector3();

	private FrameBuffer worldBuffer;
	private FrameBuffer lightingBuffer;
	private FrameBuffer bypassBuffer;

	public LightingRenderer lightingRenderer;

	private boolean isBypassing = false;

	public WorldRenderer() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 26.5f, 15);

		lightingRenderer = new LightingRenderer(this);

		createBuffers();
	}

	protected void updateCamera(World world) {
		EntityPlayer player = world.universe.player;

		if (player != null) {
			tempVector.set(player.renderer.lerpPosition.x + player.physicsBody.bounds.width * 0.5f,
					player.renderer.lerpPosition.y + player.physicsBody.bounds.height * 0.5f,
					camera.position.z);

			camera.position.lerp(tempVector, Gdx.graphics.getDeltaTime() * 16);
		}

		camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth * 0.5f,
				world.worldWidth - camera.viewportWidth * 0.5f);
		camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight * 0.5f,
				world.worldHeight - camera.viewportHeight * 0.5f);
		camera.update();
	}

	public void render(Main main, Matrix4 oldProjectionMatrix, Batch batch, World world) {
		updateCamera(world);

		batch.setProjectionMatrix(camera.combined);

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

		if (world.lightingEngine.needsUpdate()) {
			world.lightingEngine.updateLighting(minX, minY, maxX, maxY, false);
		}

		renderWorldToBuffer(batch, world, minX, minY, maxX, maxY);
		renderLightingToBuffer(batch, world, minX, minY, maxX, maxY);

		batch.setProjectionMatrix(oldProjectionMatrix);
		batch.begin();

		// draw world
		batch.draw(worldBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), 0, 0, worldBuffer.getColorBufferTexture().getWidth(),
				worldBuffer.getColorBufferTexture().getHeight(), false, true);

		// lighting buffer
		batch.setShader(main.maskshader);
		Main.useMask(worldBuffer.getColorBufferTexture());
		batch.draw(lightingBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), 0, 0, lightingBuffer.getColorBufferTexture().getWidth(),
				lightingBuffer.getColorBufferTexture().getHeight(), false, true);
		batch.setShader(Main.defaultShader);

		batch.draw(bypassBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), 0, 0, bypassBuffer.getColorBufferTexture().getWidth(),
				bypassBuffer.getColorBufferTexture().getHeight(), false, true);

		batch.end();
	}

	public void renderWorldToBuffer(Batch batch, World world, int minX, int minY, int maxX,
			int maxY) {
		bypassBuffer.begin();

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		bypassBuffer.end();

		worldBuffer.begin();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		Block b;
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				b = world.getBlock(x, y);

				if (b == null) continue;
				if (b.getRenderBlock() == null) continue;

				b.getRenderBlock().render(batch, world, x, y);
			}
		}

		batch.flush();

		Entity e;
		for (int i = 0; i < world.getActiveEntities().size; i++) {
			e = world.getActiveEntities().get(i);

			if (e.renderer != null) {
				e.renderer.updateLerpPosition();
				e.renderer.render(batch, world);
			}
		}

		batch.end();

		worldBuffer.end();
	}

	public void renderLightingToBuffer(Batch batch, World world, int minX, int minY, int maxX,
			int maxY) {
		lightingBuffer.begin();

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		lightingRenderer.render(batch, world, minX, minY, maxX, maxY);

		batch.end();

		lightingBuffer.end();
	}

	public void beginBypassing() {
		if (isBypassing) throw new IllegalStateException("Already bypassing!");

		isBypassing = true;

		worldBuffer.end();
		bypassBuffer.begin();
	}

	public void endBypassing() {
		if (!isBypassing) throw new IllegalStateException("Not bypassing!");

		isBypassing = false;

		bypassBuffer.end();
		worldBuffer.begin();
	}

	public void resize(int width, int height) {
		disposeBuffers();
		createBuffers();
	}

	private void createBuffers() {
		worldBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false);
		lightingBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false);
		bypassBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false);

		worldBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		lightingBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest,
				TextureFilter.Nearest);
		bypassBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest,
				TextureFilter.Nearest);
	}

	private void disposeBuffers() {
		worldBuffer.dispose();
		lightingBuffer.dispose();
		bypassBuffer.dispose();
	}

	@Override
	public void dispose() {
		disposeBuffers();
	}

}
