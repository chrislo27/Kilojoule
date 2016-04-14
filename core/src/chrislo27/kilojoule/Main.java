package chrislo27.kilojoule;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

import chrislo27.kilojoule.init.DefAssetLoader;
import ionium.registry.AssetRegistry;
import ionium.registry.ScreenRegistry;
import ionium.util.DebugSetting;
import ionium.util.Logger;
import ionium.util.Utils;
import ionium.util.i18n.Localization;
import ionium.util.resolution.AspectRatio;
import ionium.util.resolution.Resolution;

public class Main extends ionium.templates.Main {

	public static final AspectRatio[] allowedAspectRatios = { new AspectRatio(16, 9) };
	public static final Resolution[] allowedResolutions = { new Resolution(1280, 720),
			new Resolution(1360, 768), new Resolution(1366, 768), new Resolution(1600, 900),
			new Resolution(1768, 992), new Resolution(1920, 1080) };

	// 4x bigger
	public BitmapFont biggerFont;
	public BitmapFont biggerFontBordered;
	public BitmapFont font;
	public BitmapFont fontBordered;

	public Main(Logger l) {
		super(l);
	}

	@Override
	public String getScreenToSwitchToAfterLoadingAssets() {
		return "mainMenu";
	}

	@Override
	public void create() {
		resizeScreenFromSettings();
		Main.version = "v0.1.0-alpha";

		super.create();

		Gdx.graphics.setTitle(getTitle());

		AssetRegistry.instance().addAssetLoader(new DefAssetLoader());

		Utils.setCursorVisibility(false);
	}

	private void resizeScreenFromSettings() {
		Utils.resizeScreenFromSettings(Settings.instance().actualWidth,
				Settings.instance().actualHeight, Settings.instance().fullscreen,
				allowedAspectRatios);
	}

	@Override
	public void prepareStates() {
		super.prepareStates();

		ScreenRegistry reg = ScreenRegistry.instance();
	}

	@Override
	protected void preRender() {
		super.preRender();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	protected void postRender() {
		super.postRender();
	}

	@Override
	protected Array<String> getDebugStrings() {
		return super.getDebugStrings();
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();
	}

	@Override
	public void inputUpdate() {
		super.inputUpdate();

		if (Gdx.input.isKeyPressed(DebugSetting.DEBUG_KEY)) {
			if (Gdx.input.isKeyPressed(Keys.I) && Gdx.input.isKeyJustPressed(Keys.N)) {
				Localization.instance().reloadFromFile();
				Main.logger.debug("Reloaded I18N from files");
			}
		}

	}

	@Override
	public void loadFont() {
		super.loadFont();

		FreeTypeFontGenerator ttfGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/PTSans.ttf"));

		FreeTypeFontParameter ttfParam = new FreeTypeFontParameter();
		ttfParam.magFilter = TextureFilter.Nearest;
		ttfParam.minFilter = TextureFilter.Linear;
		ttfParam.genMipMaps = true;
		ttfParam.size = 24;

		font = ttfGenerator.generateFont(ttfParam);
		font.getData().markupEnabled = true;

		ttfParam.size *= 4;
		biggerFont = ttfGenerator.generateFont(ttfParam);
		biggerFont.getData().markupEnabled = true;

		ttfParam.borderWidth = 1.5f;
		ttfParam.size /= 4;

		fontBordered = ttfGenerator.generateFont(ttfParam);
		fontBordered.getData().markupEnabled = true;

		ttfParam.size *= 4;
		ttfParam.borderWidth *= 4;
		biggerFontBordered = ttfGenerator.generateFont(ttfParam);
		biggerFontBordered.getData().markupEnabled = true;

		ttfGenerator.dispose();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		if (!Utils.argumentsOverrode) {
			Settings.instance().actualWidth = Gdx.graphics.getWidth();
			Settings.instance().actualHeight = Gdx.graphics.getHeight();
			Settings.instance().fullscreen = Gdx.graphics.isFullscreen();
			Settings.instance().save();
		} else {
			Main.logger.info(
					"Screen was resized; settings not saved because the game was launched with arguments");
		}
	}

	@Override
	public void dispose() {
		super.dispose();

		biggerFont.dispose();
		biggerFontBordered.dispose();
		font.dispose();
		fontBordered.dispose();

		if (!Utils.argumentsOverrode) {
			Settings.instance().save();
		}
	}
}
