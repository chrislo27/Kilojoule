package chrislo27.kilojoule.client;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import ionium.animation.Animation;
import ionium.registry.handler.IAssetLoader;

public class DefAssetLoader implements IAssetLoader {

	@Override
	public void addManagedAssets(AssetManager manager) {
	}

	@Override
	public void addUnmanagedTextures(HashMap<String, Texture> textures) {
	}

	@Override
	public void addUnmanagedAnimations(HashMap<String, Animation> animations) {
	}

}