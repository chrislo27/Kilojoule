package chrislo27.kilojoule.client.screen;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.client.Main;
import chrislo27.kilojoule.client.render.WorldRenderer;
import chrislo27.kilojoule.core.universe.Universe;
import chrislo27.kilojoule.core.universe.UniverseSavingLoading;
import chrislo27.kilojoule.core.world.World;
import ionium.screen.Updateable;
import ionium.util.DebugSetting;

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
		if (universe != null) universe.inputUpdate();

		if (DebugSetting.debug) {
			if (Gdx.input.isKeyJustPressed(Keys.V)) {
				try {
					UniverseSavingLoading.save(universe, new FileHandle("saves/test/"));
					Main.logger.info("Saved world");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (Gdx.input.isKeyJustPressed(Keys.C)) {
				try {
					universe = UniverseSavingLoading.load(new FileHandle("saves/test/"));
					Main.logger.info("Loaded world");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
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

		array.add("entities: " + universe.player.world.getActiveEntities().size + " / "
				+ universe.player.world.getAllEntities().size);

		array.add("player X: " + universe.player.physicsBody.bounds.x);
		array.add("player Y: " + universe.player.physicsBody.bounds.y);
		array.add("player veloX: " + universe.player.physicsBody.velocity.x);
		array.add("player veloY: " + universe.player.physicsBody.velocity.y);
		array.add("colliding: " + universe.player.collidingNormal);
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
