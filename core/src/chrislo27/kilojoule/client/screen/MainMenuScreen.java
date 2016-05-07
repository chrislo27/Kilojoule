package chrislo27.kilojoule.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import chrislo27.kilojoule.client.Main;
import ionium.screen.Updateable;

public class MainMenuScreen extends Updateable<Main> {

	private Stage stage;

	public MainMenuScreen(Main m) {
		super(m);

		stage = new Stage(new ScreenViewport());

		Table table = new Table(main.uiSkin);
		table.setFillParent(true);

		Table buttonTable = new Table(main.uiSkin);
		buttonTable.setFillParent(true);
		buttonTable.center().bottom().padBottom(196);
		buttonTable.add(new TextButton("test1", main.uiSkin)).width(256).pad(8).row();
		buttonTable.add(new TextButton("test2", main.uiSkin)).width(256).pad(8);

		table.addActor(buttonTable);

		stage.addActor(table);

		stage.setDebugAll(true);
	}

	@Override
	public void render(float delta) {
		stage.draw();
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
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		if (Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
			InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();

			im.addProcessor(stage);
		}
	}

	@Override
	public void hide() {
		if (Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
			InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();

			im.removeProcessor(stage);
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
		stage.dispose();
	}

}
