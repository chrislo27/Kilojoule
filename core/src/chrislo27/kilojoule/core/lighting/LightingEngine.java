package chrislo27.kilojoule.core.lighting;

import java.util.Stack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import chrislo27.kilojoule.core.block.Block;
import chrislo27.kilojoule.core.world.World;
import ionium.templates.Main;
import ionium.util.Coordinate;

public class LightingEngine {

	private static final Coordinate[] FOUR_DIRECTIONS = { new Coordinate(0, 1),
			new Coordinate(0, -1), new Coordinate(-1, 0), new Coordinate(1, 0) };

	private final Color tmp = new Color();
	private final Color tmp2 = new Color();

	public final World world;

	private Stack<LightData> traversalQueue = new Stack<>();
	private Pool<LightData> updatePool = new Pool<LightData>(64) {

		@Override
		protected LightData newObject() {
			return new LightData();
		}

	};

	public LightingEngine(World w) {
		world = w;
	}

	public void updateLighting(int startX, int startY, int width, int height) {
		// clear area
		for (int x = startX; x <= startX + width; x++) {
			for (int y = startY; y < startY + height; y++) {
				world.setLighting(LightUtils.TOTAL_BLACK, x, y);
			}

		}

		for (int x = startX; x <= startX + width; x++) {
			for (int y = world.worldHeight - 1; y >= 0; y--) {
				if (world.getBlock(x, y) != null) {
					break;
				} else {
					world.setLighting(LightUtils.rgblsToInt(MathUtils.random(0, 1),
							MathUtils.random(0, 1), MathUtils.random(0, 1), 0, 1), x, y);
				}
			}
		}

		for (int x = startX; x <= startX + width; x++) {
			for (int y = world.worldHeight - 1; y >= 0; y--) {
				if (world.getBlock(x, y) != null) {
					spreadLight(x, y + 1, world.getLighting(x, y + 1));

					break;
				}
			}
		}

	}

	public int mixLightingColors(int rgbls, int otherRgbls) {
		LightUtils.setColor(tmp, rgbls, 1);
		LightUtils.setColor(tmp2, otherRgbls, 1);

		tmp.lerp(tmp2, 0.5f);

		return LightUtils.rgblsToInt(tmp.r, tmp.g, tmp.b, LightUtils.getLighting(rgbls),
				LightUtils.getSky(rgbls));
	}

	private void spreadLightRecursive(final int sourceX, final int sourceY, final int rgbls) {

	}

	private void spreadLight(final int sourceX, final int sourceY, final int rgbls) {
		traversalQueue.clear();

		// add initial source
		traversalQueue.push(updatePool.obtain().set(sourceX, sourceY, rgbls));

		while (traversalQueue.size() > 0) {
			// origin
			LightData lu = traversalQueue.pop();
			int x = lu.x;
			int y = lu.y;
			int oldSpace = world.getLighting(x, y);
			float r = LightUtils.getR(lu.rgbls);
			float g = LightUtils.getG(lu.rgbls);
			float b = LightUtils.getB(lu.rgbls);
			float light = LightUtils.getLighting(lu.rgbls);
			float sky = LightUtils.getSky(lu.rgbls);

			// set the update space's lighting
			world.setLighting(LightUtils.rgblsToInt(r, g, b,
					Math.max(LightUtils.getLighting(oldSpace), light),
					Math.max(LightUtils.getSky(oldSpace), sky)), x, y);

			// calculate the decrease
			float decrease = world.getBlock(x, y) != null ? world.getBlock(x, y).lightDecreaseAmount
					: Block.LIGHT_DECREASE_AIR;
			int newRgbls = LightUtils.modify(lu.rgbls, 0, 0, 0,
					-decrease * LightUtils.INVERSE_LIGHT, -decrease * LightUtils.INVERSE_LIGHT);
			float newLight = LightUtils.getLighting(newRgbls);
			float newSky = LightUtils.getSky(newRgbls);

			// if the brightness is still high, attempt spread
			if (newLight > 0 || newSky > 0) {
				for (Coordinate c : FOUR_DIRECTIONS) {
					int cx = x + c.getX();
					int cy = y + c.getY();

					// out of bounds check
					if (cx < 0 || cx >= world.worldWidth || cy < 0 || cy >= world.worldHeight)
						continue;

					// light at new direction isn't lower, continue
					if (LightUtils.getLighting(world.getLighting(cx, cy)) >= newLight
							&& LightUtils.getSky(world.getLighting(cx, cy)) >= newSky)
						continue;

					// mix colour between origin and the new direction
					int mixedColorRgbls = mixLightingColors(newRgbls, world.getLighting(cx, cy));

					// add new update at new direction with new mixed colour
					traversalQueue.push(updatePool.obtain().set(cx, cy, mixedColorRgbls));
				}
			}

			// free update object
			updatePool.free(lu);
		}
	}

	private static class LightData implements Poolable {

		int x;
		int y;
		int rgbls;

		public LightData set(int x, int y, int rgbls) {
			this.x = x;
			this.y = y;
			this.rgbls = rgbls;

			return this;
		}

		@Override
		public void reset() {
		}

	}

}
