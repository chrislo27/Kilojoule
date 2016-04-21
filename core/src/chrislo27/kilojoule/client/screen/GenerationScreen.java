package chrislo27.kilojoule.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.client.Main;
import chrislo27.kilojoule.core.generation.GeneratorSettings;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.universe.Universe;
import chrislo27.kilojoule.core.world.World;
import ionium.screen.Updateable;

public class GenerationScreen extends Updateable<Main> {

	private static final long AVG_FRAME_TIME = (long) ((1f / 60f) * 1_000_000_000);

	private final Universe universe;

	private WorldGenerator generator;
	private FrameBuffer buffer;
	private WorldLoadingBuffer worldBuffer;

	private Array<WorldGenerator> allGenerators;
	private int currentGen = 0;

	private long lastNanoRenderTime = AVG_FRAME_TIME;

	public GenerationScreen(Main m, Universe u) {
		super(m);

		universe = u;

		allGenerators = new Array<>();
		for (World w : universe.worlds.values()) {
			allGenerators.add(new WorldGenerator(w, new GeneratorSettings(w)));
		}

		updateCurrentGen();
	}

	private void updateCurrentGen() {
		generator = allGenerators.get(currentGen);

		buffer = new FrameBuffer(Format.RGBA8888, generator.world.worldWidth,
				generator.world.worldHeight, false);

		worldBuffer = new WorldLoadingBuffer(generator.world);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.setProjectionMatrix(main.camera.combined);
		main.batch.begin();

		main.batch.draw(buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());

		main.fontBordered.draw(main.batch,
				"Loading: " + String.format("%.3f", generator.getTotalPercentage() * 100) + "%",
				Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.75f, 1, Align.center,
				false);

		long time = System.nanoTime();

		main.batch.end();

		lastNanoRenderTime = Math.max((AVG_FRAME_TIME - (System.nanoTime() - time)), 1_000_000);
	}

	@Override
	public void renderUpdate() {
		long time = System.nanoTime();

		while (System.nanoTime() - time < lastNanoRenderTime && !generator.isFinished()) {
			generator.step(worldBuffer);
		}

		if (generator.isFinished() && currentGen < allGenerators.size - 1) {
			currentGen++;
			updateCurrentGen();
		}
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void getDebugStrings(Array<String> array) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		buffer.dispose();
	}

	public class WorldLoadingBuffer {

		private OrthographicCamera tempCam;

		public WorldLoadingBuffer(World world) {
			tempCam = new OrthographicCamera();
			tempCam.setToOrtho(false, world.worldWidth, world.worldHeight);
		}

		public void fillRect(float r, float g, float b, int x, int y, int w, int h) {
			buffer.begin();

			tempCam.update();
			main.batch.setProjectionMatrix(tempCam.combined);

			main.batch.setColor(r, g, b, 1);
			main.batch.begin();

			Main.fillRect(main.batch, x, y, w, h);

			main.batch.setColor(1, 1, 1, 1);
			main.batch.end();

			buffer.end();

			main.batch.setProjectionMatrix(main.camera.combined);
		}

		public void setPixel(float r, float g, float b, int x, int y) {
			fillRect(r, g, b, x, y, 1, 1);
		}

		public void clear(float r, float g, float b) {
			buffer.begin();

			tempCam.update();
			main.batch.setProjectionMatrix(tempCam.combined);

			main.batch.setColor(0, 0, 0, 1);
			main.batch.begin();

			Main.fillRect(main.batch, 0, 0, buffer.getWidth(), buffer.getHeight());

			main.batch.setColor(1, 1, 1, 1);
			main.batch.end();

			buffer.end();

			main.batch.setProjectionMatrix(main.camera.combined);
		}
	}

}
