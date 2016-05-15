package chrislo27.kilojoule.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Quad;
import chrislo27.kilojoule.client.ActorAccessor;
import chrislo27.kilojoule.client.Main;
import ionium.screen.Updateable;
import ionium.stage.Group;
import ionium.stage.Stage;
import ionium.stage.ui.LocalizationStrategy;
import ionium.stage.ui.TextButton;
import ionium.stage.ui.skin.Palette;
import ionium.stage.ui.skin.Palettes;

public class MainMenuScreen extends Updateable<Main> {

	Stage stage;
	Group mainMenuGroup;
	Group saveSlotGroup;

	public MainMenuScreen(Main m) {
		super(m);

		Palette p = Palettes.getIoniumDefault(main.font);
		stage = new Stage();
		stage.debugMode = true;
		{
			mainMenuGroup = new Group(stage);

			mainMenuGroup.addActor(new TextButton(stage, p, "mainMenu.start") {

				@Override
				public void onClickRelease(float x, float y) {
					super.onClickRelease(x, y);

					mainMenuGroup.setEnabled(false);
					Tween.to(mainMenuGroup, ActorAccessor.SCREEN_X, 0.5f).target(-1)
							.start(main.tweenManager);
					Tween.to(saveSlotGroup, ActorAccessor.SCREEN_X, 0.5f).target(0)
							.setCallback(new TweenCallback() {

						@Override
						public void onEvent(int type, BaseTween<?> arg1) {
							if (type == COMPLETE) {
								saveSlotGroup.setEnabled(true);
							}
						}
					}).start(main.tweenManager);
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

			stage.addActor(mainMenuGroup).setScreenOffsetSize(0.5f, 0.5f);
		}

		{
			saveSlotGroup = new Group(stage);

			int numOfSlots = 8;
			float initialHeight = 8 + numOfSlots / 2 * 64;

			for (int i = 0; i < numOfSlots; i++) {
				final int saveNum = i + 1;

				saveSlotGroup.addActor(new TextButton(stage, p, "mainMenu.saveSlot") {

					@Override
					public String getText() {
						return getI10NStrategy().get(getLocalizationKey(), saveNum);
					}
				}).align(Align.center).setPixelOffset(0, initialHeight - i * 64, 256, 48);
			}

			saveSlotGroup.addActor(new TextButton(stage, p, "mainMenu.back") {

				@Override
				public void onClickRelease(float x, float y) {
					super.onClickRelease(x, y);

					saveSlotGroup.setEnabled(false);
					Tween.to(saveSlotGroup, ActorAccessor.SCREEN_X, 0.5f).target(1)
							.start(main.tweenManager);
					Tween.to(mainMenuGroup, ActorAccessor.SCREEN_X, 0.5f).target(0)
							.setCallback(new TweenCallback() {

						@Override
						public void onEvent(int type, BaseTween<?> target) {
							if (type == COMPLETE) {
								mainMenuGroup.setEnabled(true);

							}
						}
					}).start(main.tweenManager);
				}
			}).align(Align.bottomLeft).setPixelOffsetSize(128, 48).setScreenOffset(0.025f, 0.025f);

			//stage.addActor(saveSlotGroup).setScreenOffset(1, 0).setEnabled(false);
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
