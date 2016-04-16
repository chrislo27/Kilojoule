package chrislo27.kilojoule.client.screen;

import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.client.Main;
import chrislo27.kilojoule.client.render.WorldRenderer;
import chrislo27.kilojoule.core.world.World;
import ionium.screen.Updateable;

public class WorldScreen extends Updateable<Main> {

	World world;
	WorldRenderer renderer;

	public WorldScreen(Main m) {
		super(m);

		world = new World();
		renderer = new WorldRenderer(world);
	}

	@Override
	public void render(float delta) {
		renderer.render(main.batch);
		
		main.batch.setProjectionMatrix(main.camera.combined);
	}

	@Override
	public void renderUpdate() {
	}

	@Override
	public void tickUpdate() {
		world.getCurrentDim().tickUpdate();
		
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
	}

}
