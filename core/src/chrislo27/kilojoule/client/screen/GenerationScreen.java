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
import ionium.util.i18n.Localization;

public class GenerationScreen extends Updateable<Main> {

	private static final long AVG_FRAME_TIME = (long) ((1f / 60f) * 1_000_000_000);

	private final Universe universe;

	private WorldGenerator generator;
	private FrameBuffer buffer;
	private WorldLoadingBuffer worldBuffer = new WorldLoadingBuffer();

	private Array<WorldGenerator> allGenerators = new Array<>();
	private int currentGen = 0;
	private float universalTotal = 0;

	private long lastNanoRenderTime = AVG_FRAME_TIME;

	public GenerationScreen(Main m, Universe u) {
		super(m);

		universe = u;

		for (World w : universe.worlds.values()) {
			allGenerators.add(new WorldGenerator(w, new GeneratorSettings(w)));
		}

		updateCurrentGen();
	}

	private void updateCurrentGen() {
		generator = allGenerators.get(currentGen);

		if (buffer == null || buffer.getWidth() != generator.world.worldWidth
				|| buffer.getHeight() != generator.world.worldHeight) {
			if (buffer != null) buffer.dispose();

			buffer = new FrameBuffer(Format.RGBA8888, generator.world.worldWidth,
					generator.world.worldHeight, false);
		}

		worldBuffer.setSize(generator.world.worldWidth, generator.world.worldHeight);

		worldBuffer.clear(0, 0, 0);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.setProjectionMatrix(main.camera.combined);
		main.batch.begin();

		main.batch.draw(buffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0,
				buffer.getColorBufferTexture().getWidth(),
				buffer.getColorBufferTexture().getHeight(), false, true);

		universalTotal = 0;
		for (WorldGenerator g : allGenerators) {
			universalTotal += g.getTotalPercentage();
		}
		universalTotal /= allGenerators.size;

		main.fontBordered.draw(main.batch,
				generator.getCurrentStep().getMessageString()
						+ getTruncatedPercentage(generator.getStepPercentage()) + "%\n"
						+ Localization.get("generating.worldTotal")
						+ getTruncatedPercentage(generator.getTotalPercentage()) + "%\n\n"
						+ Localization.get("generating.universeTotal")
						+ getTruncatedPercentage(universalTotal) + "%",
				Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.75f, 1, Align.center,
				false);

		long time = System.nanoTime();

		main.batch.end();

		lastNanoRenderTime = Math.max((AVG_FRAME_TIME - (System.nanoTime() - time)), 1_000_000);
	}

	private String getTruncatedPercentage(float percent) {
		return String.format("%.1f", percent * 100);
	}

	@Override
	public void renderUpdate() {
		if (generator.isFinished() && currentGen < allGenerators.size - 1) {
			currentGen++;
			updateCurrentGen();
		}

		long time = System.nanoTime();

		worldBuffer.tempCam.update();
		main.batch.setProjectionMatrix(worldBuffer.tempCam.combined);
		main.batch.begin();

		while (System.nanoTime() - time < lastNanoRenderTime && !generator.isFinished()) {
			generator.step(worldBuffer);
		}

		main.batch.end();

		main.batch.setProjectionMatrix(main.camera.combined);
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

		protected OrthographicCamera tempCam;

		private int sizex, sizey;

		public WorldLoadingBuffer() {
			tempCam = new OrthographicCamera();
		}

		public void setSize(int width, int height) {
			tempCam.setToOrtho(false, width, height);
		}

		public void fillRect(float r, float g, float b, int x, int y, int w, int h) {
			buffer.begin();

			main.batch.setColor(r, g, b, 1);

			Main.fillRect(main.batch, x, y, w, h);

			main.batch.setColor(1, 1, 1, 1);

			main.batch.flush();

			buffer.end();
		}

		public void setPixel(float r, float g, float b, int x, int y) {
			fillRect(r, g, b, x, y, 1, 1);
		}

		public void clear(float r, float g, float b) {
			buffer.begin();

			Gdx.gl.glClearColor(r, g, b, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			buffer.end();
		}
	}

}
