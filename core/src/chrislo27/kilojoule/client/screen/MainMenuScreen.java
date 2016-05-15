package chrislo27.kilojoule.client.screen;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.client.Main;
import chrislo27.kilojoule.core.universe.Universe;
import chrislo27.kilojoule.core.universe.UniverseSavingLoading;
import ionium.registry.ScreenRegistry;
import ionium.screen.Updateable;
import ionium.stage.Group;
import ionium.stage.Stage;
import ionium.stage.ui.TextButton;
import ionium.stage.ui.skin.Palette;
import ionium.stage.ui.skin.Palettes;

public class MainMenuScreen extends Updateable<Main> {

	Stage stage;
	Group mainMenuGroup;

	public MainMenuScreen(Main m) {
		super(m);

		Palette p = Palettes.getIoniumDefault(main.font);
		stage = new Stage();

		final MainMenuScreen righthere = this;

		{
			mainMenuGroup = new Group(stage);

			mainMenuGroup.addActor(new TextButton(stage, p, "mainMenu.start") {

				@Override
				public void onClickRelease(float x, float y) {
					super.onClickRelease(x, y);

					FileHandle save = Gdx.files.local("saves/save1/");

					ScreenRegistry.get("world", WorldScreen.class).setUniverse(null);

					if (save.exists()) {
						final WorldScreen ws = ScreenRegistry.get("world", WorldScreen.class);

						try {
							ws.setUniverse(UniverseSavingLoading.load(save));
						} catch (IOException e) {
							e.printStackTrace();
						}

						Gdx.app.postRunnable(new Runnable() {

							@Override
							public void run() {
								main.setScreen(ws);
							}
						});
					} else {
						Gdx.app.postRunnable(new Runnable() {

							@Override
							public void run() {
								main.setScreen(new GenerationScreen(righthere.main,
										new Universe(System.nanoTime())));
							}
						});
					}
				}

			}).setPixelOffsetSize(256, 48).align(Align.center | Align.bottom)
					.setScreenOffset(0, 0.25f).setPixelOffset(0, 256);
			mainMenuGroup.addActor(new TextButton(stage, p, "mainMenu.settings") {

				@Override
				public TextButton setEnabled(boolean enabled) {
					super.setEnabled(false);

					return this;
				}

				@Override
				public void onClickRelease(float x, float y) {
					super.onClickRelease(x, y);
				}

			}).setPixelOffsetSize(256, 48).align(Align.center | Align.bottom)
					.setScreenOffset(0, 0.25f).setPixelOffset(0, 128).setEnabled(false);
			mainMenuGroup.addActor(new TextButton(stage, p, "mainMenu.exit") {

				@Override
				public void onClickRelease(float x, float y) {
					super.onClickRelease(x, y);

					Gdx.app.exit();
				}

			}).setPixelOffsetSize(256, 48).align(Align.center | Align.bottom)
					.setScreenOffset(0, 0.25f).setPixelOffset(0, 64);

			stage.addActor(mainMenuGroup);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();
		stage.render(main.batch);
		main.batch.end();

		main.batch.setProjectionMatrix(main.camera.combined);

	}

	@Override
	public void renderUpdate() {
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void getDebugStrings(Array<String> array) {
	}

	@Override
	public void resize(int width, int height) {
		stage.onResize(width, height);
	}

	@Override
	public void show() {
		if (Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
			stage.addSelfToInputMultiplexer((InputMultiplexer) Gdx.input.getInputProcessor());
		}
	}

	@Override
	public void hide() {
		if (Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
			stage.removeSelfFromInputMultiplexer((InputMultiplexer) Gdx.input.getInputProcessor());
		}
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
