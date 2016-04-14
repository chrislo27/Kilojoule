package chrislo27.kilojoule.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import ionium.templates.Main;

public class TileTexturePacker {

	public boolean mustUsePowerOfTwo = true;
	public int maxTextureSize = 2048;
	public String debugOutputDir = null;

	private int tileSize = 32;

	private Array<NamedTexture> texturesToPack = new Array<>();

	private TextureAtlas packedTex = null;

	public TileTexturePacker(int tileSize) {
		this.tileSize = tileSize;
	}

	public TextureAtlas pack() {
		if (packedTex != null) return getPackedTexture();
		if (texturesToPack.size == 0) throw new IllegalStateException("No textures to pack");

		int tilesWidth = 1;
		int tilesHeight = 1;

		while (tilesWidth * tilesHeight <= texturesToPack.size) {
			if (tilesWidth <= tilesHeight) {
				int n = 1;

				while (n <= tilesWidth) {
					n = n << 1;
				}

				tilesWidth = n;
			} else {
				int n = 1;

				while (n <= tilesHeight) {
					n = n << 1;
				}

				tilesHeight = n;
			}

			if (tilesWidth * tileSize > maxTextureSize || tilesHeight * tileSize > maxTextureSize) {
				throw new PackingException(
						"Exceeded texture size limit (" + maxTextureSize + ") while packing");
			}
		}

		Pixmap pixmap = new Pixmap(tilesWidth * tileSize, tilesHeight * tileSize, Format.RGBA8888);

		for (int y = 0; y < tilesHeight; y++) {
			for (int x = 0; x < tilesWidth; x++) {
				int id = y * tilesWidth + x;

				if (id >= texturesToPack.size) break;

				Texture t = texturesToPack.get(id).texture;
				if (!t.getTextureData().isPrepared()) t.getTextureData().prepare();
				Pixmap returned = t.getTextureData().consumePixmap();

				pixmap.drawPixmap(returned, x * tileSize, y * tileSize);

				if (t.getTextureData().disposePixmap()) returned.dispose();
			}
		}

		if (debugOutputDir != null) {
			PixmapIO.writePNG(Gdx.files.local(debugOutputDir), pixmap);
		}

		packedTex = new TextureAtlas();
		Texture newTex = new Texture(pixmap);
		pixmap.dispose();
		for (int y = 0; y < tilesHeight; y++) {
			for (int x = 0; x < tilesWidth; x++) {
				int id = y * tilesWidth + x;

				if (id >= texturesToPack.size) break;

				packedTex.addRegion(texturesToPack.get(id).name, newTex, x * tileSize, y * tileSize,
						tileSize, tileSize);
			}
		}

		return getPackedTexture();
	}

	public TileTexturePacker addTexture(String name, Texture tex) {
		if (tex.getWidth() != tileSize || tex.getHeight() != tileSize) {
			throw new IllegalArgumentException("Texture doesn't meet tile size (" + tileSize
					+ "^2), was " + tex.getWidth() + "x" + tex.getHeight());
		}

		texturesToPack.add(new NamedTexture(tex, name));

		return this;
	}

	public TextureAtlas getPackedTexture() {
		if (packedTex == null) throw new IllegalStateException("Texture was not packed yet");

		return packedTex;
	}

	public class PackingException extends RuntimeException {

		public PackingException(String reason) {
			super(reason);
		}
	}

	private class NamedTexture {

		protected final Texture texture;
		protected final String name;

		public NamedTexture(Texture t, String s) {
			texture = t;
			name = s;
		}
	}

}
