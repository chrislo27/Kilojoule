package chrislo27.kilojoule.client.screen;

import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.client.Main;
import chrislo27.kilojoule.client.render.WorldRenderer;
import chrislo27.kilojoule.core.universe.Universe;
import chrislo27.kilojoule.core.world.World;
import ionium.screen.Updateable;

public class WorldScreen extends Updateable<Main> {

	private Universe universe;
	private WorldRenderer renderer;

	public WorldScreen(Main m) {
		super(m);

		renderer = new WorldRenderer();
	}

	public void setUniverse(Universe uni) {
		universe = uni;
	}

	@Override
	public void render(float delta) {
		if (universe != null) {
			renderer.render(main, main.camera.combined, main.batch, universe.getCurrentWorld());
		}

		main.batch.setProjectionMatrix(main.camera.combined);
	}

	@Override
	public void renderUpdate() {
	}

	@Override
	public void tickUpdate() {
		if (universe != null) universe.tickUpdate();

	}

	@Override
	public void getDebugStrings(Array<String> array) {
		if (universe == null) {
			array.add("Universe is null");
			return;
		}

		if (universe.player == null) {
			array.add("Player doesn't exist");
			return;
		}

		array.add("player X: " + universe.player.physicsBody.bounds.x);
		array.add("player Y: " + universe.player.physicsBody.bounds.y);
		array.add("player veloX: " + universe.player.physicsBody.velocity.x);
		array.add("player veloY: " + universe.player.physicsBody.velocity.y);
	}

	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
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
		renderer.dispose();
	}

}
