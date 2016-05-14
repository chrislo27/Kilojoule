package chrislo27.kilojoule.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.client.Main;
import ionium.screen.Updateable;
import ionium.stage.Group;
import ionium.stage.Stage;
import ionium.stage.ui.Button;
import ionium.stage.ui.TextButton;
import ionium.stage.ui.skin.Palette;
import ionium.stage.ui.skin.Palettes;

public class MainMenuScreen extends Updateable<Main> {

	Stage stage;
	Group group;

	public MainMenuScreen(Main m) {
		super(m);

		stage = new Stage();
		group = new Group(stage);
		Palette p = Palettes.getIoniumDefault(main.font);

		group.addActor(new Button(stage, p)).align(Align.left | Align.center)
				.setScreenOffset(0.1f, 0, 0.25f, 0).setPixelOffsetSize(0, 48).setEnabled(false);

		group.addActor(new TextButton(stage, p, "generating.caves"))
				.align(Align.left | Align.center).setScreenOffset(0.5f, 0, 0.25f, 0)
				.setPixelOffsetSize(0, 48);

		stage.addActor(group);
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
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			group.setPixelOffset(group.getPixelOffsetX(),
					group.getPixelOffsetY() + Gdx.graphics.getDeltaTime() * 320);
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			group.setPixelOffset(group.getPixelOffsetX(),
					group.getPixelOffsetY() + -Gdx.graphics.getDeltaTime() * 320);
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			group.setPixelOffset(group.getPixelOffsetX() - Gdx.graphics.getDeltaTime() * 320,
					group.getPixelOffsetY());
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			group.setPixelOffset(group.getPixelOffsetX() + Gdx.graphics.getDeltaTime() * 320,
					group.getPixelOffsetY());
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
