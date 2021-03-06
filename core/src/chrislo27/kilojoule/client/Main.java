package chrislo27.kilojoule.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import chrislo27.kilojoule.client.screen.AssetLoadingScreen;
import chrislo27.kilojoule.client.screen.GenerationScreen;
import chrislo27.kilojoule.client.screen.MainMenuScreen;
import chrislo27.kilojoule.client.screen.WorldScreen;
import chrislo27.kilojoule.core.universe.Universe;
import ionium.registry.AssetRegistry;
import ionium.registry.ScreenRegistry;
import ionium.stage.Actor;
import ionium.util.Logger;
import ionium.util.Utils;
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

	public TweenManager tweenManager = new TweenManager();

	public Main(Logger l) {
		super(l);
	}

	@Override
	public Screen getScreenToSwitchToAfterLoadingAssets() {
		return ScreenRegistry.get("mainMenu");
	}

	@Override
	public Screen getAssetLoadingScreenToUse() {
		return ScreenRegistry.get("assetloading");
	}

	@Override
	public void create() {
		resizeScreenFromSettings();
		Main.version = "v0.1.0-alpha";

		super.create();

		Gdx.graphics.setTitle(getTitle());

		Tween.registerAccessor(Actor.class, new ActorAccessor<Actor>());

		AssetRegistry.instance().addAssetLoader(new DefAssetLoader());
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

		reg.add("assetloading", new AssetLoadingScreen(this));
		reg.add("world", new WorldScreen(this));
		reg.add("mainMenu", new MainMenuScreen(this));
	}

	@Override
	protected void preRender() {
		super.preRender();

		tweenManager.update(Gdx.graphics.getDeltaTime());
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
		font.setFixedWidthGlyphs("0123456789");

		ttfParam.size *= 4;
		biggerFont = ttfGenerator.generateFont(ttfParam);
		biggerFont.getData().markupEnabled = true;
		biggerFont.setFixedWidthGlyphs("0123456789");

		ttfParam.borderWidth = 1.5f;
		ttfParam.size /= 4;

		fontBordered = ttfGenerator.generateFont(ttfParam);
		fontBordered.getData().markupEnabled = true;
		fontBordered.setFixedWidthGlyphs("0123456789");

		ttfParam.size *= 4;
		ttfParam.borderWidth *= 4;
		biggerFontBordered = ttfGenerator.generateFont(ttfParam);
		biggerFontBordered.getData().markupEnabled = true;
		biggerFontBordered.setFixedWidthGlyphs("0123456789");

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
