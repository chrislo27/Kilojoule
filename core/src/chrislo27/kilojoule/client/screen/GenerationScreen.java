package chrislo27.kilojoule.client.screen;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.client.Main;
import chrislo27.kilojoule.core.generation.WorldGenerator;
import chrislo27.kilojoule.core.generation.Step;
import chrislo27.kilojoule.core.world.World;
import ionium.screen.Updateable;

public class GenerationScreen extends Updateable<Main> {

	private final World world;

	private WorldGenerator generator;
	private FrameBuffer buffer;

	public GenerationScreen(Main m, World w) {
		super(m);

		world = w;

		generator = new WorldGenerator(world);

		buffer = new FrameBuffer(Format.RGBA8888, world.worldWidth, world.worldHeight, false);

	}

	@Override
	public void render(float delta) {

	}

	@Override
	public void renderUpdate() {
		if (generator.isFinished() == false) {
			generator.step(buffer);
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

}
