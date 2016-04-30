package chrislo27.kilojoule.core.lighting;

import java.util.Stack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
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

	private Stack<LightingUpdate> traversalStack = new Stack<>();
	private Pool<LightingUpdate> lightingUpdatePool = new Pool<LightingUpdate>(64) {

		@Override
		protected LightingUpdate newObject() {
			return new LightingUpdate();
		}

	};
	private Array<LightingUpdate> updatesToProcess = new Array<>();
	private boolean needsRefresh = false;

	public LightingEngine(World w) {
		world = w;
	}

	public void requestUpdate(int x, int y, int rgbls) {
		needsRefresh = true;
		if (LightUtils.getLighting(rgbls) > 0 || LightUtils.getSky(rgbls) > 0) {
			updatesToProcess.add(lightingUpdatePool.obtain().set(x, y, rgbls));
		}
	}

	public boolean needsUpdate() {
		return updatesToProcess.size > 0 || needsRefresh;
	}

	public void updateLighting(int startX, int startY, int width, int height, boolean force) {
		if (!force && !needsUpdate()) return;

		// clear area
		for (int x = startX; x <= startX + width; x++) {
			for (int y = startY; y < startY + height; y++) {
				world.setLighting(LightUtils.TOTAL_BLACK, x, y);
			}
		}

		// sky-cast
		for (int x = startX; x <= startX + width; x++) {
			for (int y = world.worldHeight - 1; y >= 0; y--) {
				if (world.getBlock(x, y) != null) {
					requestUpdate(x, y + 1, world.getLighting(x, y + 1));

					break;
				} else {
					world.setLighting(LightUtils.rgblsToInt(MathUtils.random(0, 1),
							MathUtils.random(0, 1), MathUtils.random(0, 1), 0, 1), x, y);
				}
			}
		}

		// process updates
		for (int i = updatesToProcess.size - 1; i >= 0; i--) {
			LightingUpdate lu = updatesToProcess.get(i);

			spreadLight(lu.x, lu.y, lu.rgbls);

			lightingUpdatePool.free(updatesToProcess.removeIndex(i));
		}

		needsRefresh = false;

	}

	public int mixLightingColors(int rgbls, int otherRgbls) {
		LightUtils.setColor(tmp, rgbls, 1);
		LightUtils.setColor(tmp2, otherRgbls, 1);

		tmp.lerp(tmp2, 0.5f);

		return LightUtils.rgblsToInt(tmp.r, tmp.g, tmp.b, LightUtils.getLighting(rgbls),
				LightUtils.getSky(rgbls));
	}

	private void spreadLight(final int sourceX, final int sourceY, final int rgbls) {
		traversalStack.clear();

		// add initial source
		traversalStack.push(lightingUpdatePool.obtain().set(sourceX, sourceY, rgbls));

		while (traversalStack.size() > 0) {
			// origin
			LightingUpdate lu = traversalStack.pop();
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
					traversalStack.push(lightingUpdatePool.obtain().set(cx, cy, mixedColorRgbls));
				}
			}

			// free update object
			lightingUpdatePool.free(lu);
		}
	}

	private static class LightingUpdate implements Poolable {

		int x;
		int y;
		int rgbls;

		public LightingUpdate set(int x, int y, int rgbls) {
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
